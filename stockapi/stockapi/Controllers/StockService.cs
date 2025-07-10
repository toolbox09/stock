using ClosedXML.Excel;
using DocumentFormat.OpenXml.Drawing;
using FluentFTP;
using Microsoft.Extensions.FileSystemGlobbing;
using NanoidDotNet;
using stockapi.Data;
using Swashbuckle.AspNetCore.SwaggerGen;
using System.Collections.Generic;
using System.IO;

namespace stockapi.Controllers;

public class StockService
{
  private FileContext _context;

  public StockService(FileContext context)
  {
    _context = context;
  }

  #region master

  public string masterPath = "master";

  public List<FileMetadata> GetMasterList()
  {
    return _context.client.GetListing($"/{masterPath}/")
      .Where(_ => _.Type == FluentFTP.FtpObjectType.File && System.IO.Path.GetExtension(_.FullName.ToLower()) == ".csv")
      .Select(_ => new FileMetadata(_.Name, _.Modified)).ToList();
  }


  public Dictionary<string, string> GetMaster(string masterUrl)
  {
    var items = _context.ReadCsv($"{masterPath}/{masterUrl}", MasterItem.Create);
    var master = new Dictionary<string, string>();
    foreach (var item in items)
    {
      if (item != null)
        master.TryAdd(item.barcode, item.name);
    }
    return master;
  }

  #endregion

  #region match

  public string matchPath = "match";

  public List<FileMetadata> GetMatchList()
  {
    return _context.client.GetListing($"/{matchPath}/")
      .Where(_ => _.Type == FluentFTP.FtpObjectType.File && System.IO.Path.GetExtension(_.FullName.ToLower()) == ".csv")
      .Select(_ => new FileMetadata(_.Name, _.Modified)).ToList();
  }

  public Dictionary<string, long> GetMatch(string matchUrl)
  {
    var items = _context.ReadCsv($"{matchPath}/{matchUrl}", MatchItem.Create);
    var match = new Dictionary<string, long>();
    foreach (var item in items)
    {
      if (item != null)
        match.TryAdd(item.barcode, item.count);
    }
    return match;
  }

  #endregion

  #region auth

  public string authPath = "auth";

  public Auth Login(string id, string password)
  {
    var auth = _context.ReadCsv($"{authPath}/users.csv", AuthItem.Create)
      .FirstOrDefault(_ => _.id == id && _.password == password);

    return auth == null ? null : new Auth(auth.id, auth.keyword);
  }


  #endregion

  #region project

  public string projectPath = "projects";

  public int CreateProject(CreateProjectReq req) 
  {
    var projectFullPath = $"{projectPath}/{req.name}";
    if (_context.client.DirectoryExists(projectFullPath))
      return -2; // 이미존재하는 경우

    var state = _context.client.CreateDirectory(projectFullPath);
    if (!state)
      return -1;

    var projectInfo = new ProjectMetadata(
      Nanoid.Generate(),
      req.name,
      KorTime.Now,
      req.masterUrl,
      req.matchUrl
    );

    return _context.WriteJson($"{projectFullPath}/.json", projectInfo) ? 1 : -1;
  }

  public ProjectMetadata GetProjectMetadata(string projectName)
    => _context.ReadJson<ProjectMetadata>($"{projectPath}/{projectName}/.json");

  public List<ProjectMetadata> GetProjects()
  {
    var result = new List<ProjectMetadata>();
    var projects = _context.client.GetListing($"{projectPath}/");
    foreach (var project in projects)
    {
      if (project.Type == FtpObjectType.Directory)
      {
        var info = GetProjectMetadata(project.Name);
        if (info != null)
          result.Add(info);
      }
    }
    return result;
  }

  public Project GetProject(string projectName)
  {
    var meta = GetProjectMetadata(projectName);
    if (meta == null)
      return null;

    var state = new Project
    {
      id = meta.id,
      name = meta.name,
      masterUrl = meta.masterUrl,
      matchUrl = meta.matchUrl,
      created = meta.created,
      works = new List<FileMetadata>(),
    };

    var targets = _context.client.GetListing($"{projectPath}/{projectName}/");
    foreach (var target in targets)
    {
      if (target.Type == FtpObjectType.File)
      {
        var file = new FileMetadata(target.Name, target.Modified);
        if (target.Name.Contains("workraws"))
        {
          state.works.Add(file);
        }
        else if (target.Name.Contains("취합"))
        {
          state.merge = file;
        }
        else if (target.Name.Contains("비교"))
        {
          state.match = file;
        }
      }
    }
    return state;
  }

  public ProjectForWork GetProjectForWork(string projectName) 
  {
    var projectMetadata = GetProjectMetadata(projectName);
    if (projectMetadata == null)
      return null;

    return new ProjectForWork(
      projectMetadata.id,
      projectName,
      projectMetadata.masterUrl == null ? null : GetMaster(projectMetadata.masterUrl)
    );

  }


  #endregion

  #region work


  private List<string> GetWorkrawsFiles(string projectName, string keyword) 
  {
    var targets = _context.client.GetListing($"{projectPath}/{projectName}/");
    var fileNames = new List<string>();
    foreach (var target in targets) 
    {
      if (target.Type != FtpObjectType.File)
        continue;

      if (target.Name.Contains($"workraws-{keyword}"))
        fileNames.Add(target.Name);
    }
    return fileNames;
  }

  private int GetNextWorkrawsFileCount(string projectName, string keyword)
  {
    var files = GetWorkrawsFiles(projectName, keyword);
    if (files.Count == 0)
      return 0;

    var ft = $"workraws-{keyword}";
    var names = files.Select(_ => _.Replace(ft, "").Replace("(", "").Replace(").csv", "").Trim());

    var nums = new List<int>();
    foreach (var name in names) 
    {
      try
      {
        var num = Convert.ToInt32(name);
        nums.Add(num);
      }
      catch { }
    }
    return nums.Count == 0 ? 1 : nums.Max() + 1;
  }

  private string GetWorkrawsPathName(string projectName, string keyword) 
  {
    var num = GetNextWorkrawsFileCount(projectName, keyword);
    return num == 0 ?
      $"{projectPath}/{projectName}/workraws-{keyword}.csv" :
      $"{projectPath}/{projectName}/workraws-{keyword} ({num}).csv";
  }

  public List<long> AppendWork(AppendWorkReq req) 
  {
    var path = GetWorkrawsPathName(req.projectName, req.fileName);
    // var result = _context.AppendCsv(path, req.scanItems.Select(RowItem.Create).ToList());
    return _context.AppendRowItems(path, req.scanItems.Select(RowItem.Create).ToList());
    // return result ? req.scanItems.Select( _ => _.id ).ToList() : null;
  }


  public CollectRes Collect(string projectName) 
  {
    if (projectName == null)
      return null;

    var path = $"{projectPath}/{projectName}/";
    var workFiles = _context.client.GetListing(path);
    var meta = GetProjectMetadata(projectName);

    var raws = new List<RowItem>();
    foreach (var workFile in workFiles)
    {
      if (workFile.Type == FtpObjectType.File && workFile.Name.Contains("workraws"))
      {
        var workRaws = _context.ReadCsv(workFile.FullName, RowItem.Create).ToList();
        if (workRaws != null)
          raws.AddRange(workRaws);
      }
    }

    var merge = RowItem.MergeMap(raws);
    var mergeRows = merge.Select(_ => new MergeRow(_.Key, Convert.ToInt32(_.Value))).ToList();

    _context.WriteCsv($"{path}merge.csv", mergeRows, (o) => $"{o.barcode},{o.count}");
    WriteMerge($"{path}취합.xlsx", raws, mergeRows);


    if (meta.matchUrl != null) 
    {
      var matchData = GetMatch(meta.matchUrl);
      var matchResult = Matching(matchData, merge);
      WriteMatch($"{path}비교.xlsx", matchResult);
    }
    return new CollectRes
    {
      totalCount = mergeRows.Sum(_ => _.count)
    };
  }

  public List<MatchResultItem> Matching(Dictionary<string, long> matchs, Dictionary<string, int> merges) 
  {
    var normal = new List<MatchResultItem>();
    foreach (var match in matchs)
    {
      var item = new MatchResultItem
      {
        품번코드 = match.Key,
        전산 = (int)match.Value,
        실사 = 0,
        비고 = ""
      };

      if (merges.ContainsKey(match.Key)) 
      {
        item.실사 = merges[match.Key];
      }
      normal.Add(item);
    }

    var over = new List<MatchResultItem>();
    foreach (var merge in merges) 
    {
      if (matchs.ContainsKey(merge.Key) == false) 
      {
        over.Add(new MatchResultItem
        {
          품번코드 = merge.Key,
          전산 = 0,
          실사 = merge.Value,
          비고 = "전산없음",
        });
      }
    }

    normal.AddRange(over);
    return normal;
  }


  public void WriteMerge(string filepath, List<RowItem> rows, List<MergeRow> mergeRows) 
  {
    try
    {
      using var stream = new MemoryStream();
      // using var writer = new StreamWriter(stream, Encoding.UTF8);

      using var wb = new XLWorkbook();
      var ws = wb.AddWorksheet("취합");

      var table1 = ws.Cell("A1").InsertTable(rows);
      table1.SetShowRowStripes(false);


      var table2 = ws.Cell("I1").InsertTable(mergeRows, "MergeCount", true);

      // table2.ShowTotalsRow = true;
      table2.SetShowRowStripes(false);
      int lastRow = table2.LastRowUsed().RowNumber();
      ws.Cell(lastRow + 1, 9).Value = "합"; // 첫 번째 열에 '합계' 텍스트 입력
      ws.Cell(lastRow + 1, 10).FormulaA1 = "SUM(MergeCount[count])"; // '나이' 컬럼의



      wb.SaveAs(stream);

      //writer.Flush();
      stream.Position = 0;
      var state = _context.client.UploadStream(stream, filepath);
    }
    catch
    {
    }
  }

  public void WriteMatch(string filepath, List<MatchResultItem> items)
  {
    try
    {
      using var stream = new MemoryStream();
      // using var writer = new StreamWriter(stream, Encoding.UTF8);

      using var wb = new XLWorkbook();
      var ws = wb.AddWorksheet("비교");

      var table1 = ws.Cell("A1").InsertTable(items, "MatchCount", true);
      table1.SetShowRowStripes(false);
      int lastRow = table1.LastRowUsed().RowNumber();
      ws.Cell(lastRow + 1, 1).Value = "합계"; // 첫 번째 열에 '합계' 텍스트 입력
      ws.Cell(lastRow + 1, 2).FormulaA1 = "SUM(MatchCount[전산])";
      ws.Cell(lastRow + 1, 2).FormulaA1 = "SUM(MatchCount[실사])";
      ws.Cell(lastRow + 1, 2).FormulaA1 = "SUM(MatchCount[차이])";


      wb.SaveAs(stream);

      //writer.Flush();
      stream.Position = 0;
      var state = _context.client.UploadStream(stream, filepath);
    }
    catch
    {
    }
  }




  #endregion
}