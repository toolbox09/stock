using Newtonsoft.Json.Linq;
using System.Diagnostics.Metrics;

namespace stockapi.Controllers;

public record class CreateProjectReq(
  string name, 
  string masterUrl, 
  string matchUrl
);

public record class FileMetadata(
  string name,
  DateTime modified
);

public record class MasterItem(
  string barcode, 
  string name
)
{
  public static MasterItem Create(string value)
  {
    try
    {
      var values = value.Split(",");
      return new MasterItem(values[0].Trim(), values[1].Trim());
    }
    catch
    {
      return null;
    }
  }
}

public record class MatchItem(
  string barcode, 
  long count
  )
{
  public static MatchItem Create(string value)
  {
    try
    {
      var values = value.Split(",");
      return new MatchItem(values[0].Trim(), Convert.ToInt64(values[1].Trim()));
    }
    catch
    {
      return null;
    }
  }
}

public record class AuthItem(
  string id,
  string password,
  string keyword
)
{
  public static AuthItem Create(string value)
  {
    try
    {
      var values = value.Split(",");
      return new AuthItem(values[0].Trim(), values[1].Trim(), values[2].Trim());
    }
    catch
    {
      return null;
    }
  }
}

public record class Auth(
  string id,
  string keyword
);

public record class ProjectMetadata(
  string id,
  string name,
  DateTime created,
  string masterUrl = null,
  string matchUrl = null
);

public class Project
{
  public string id { get; set; }
  public string name { get; set; }
  public DateTime created { get; set; }
  public string masterUrl { get; set; }
  public string matchUrl { get; set; }
  public FileMetadata match { get; set; }
  public FileMetadata merge { get; set; }
  public List<FileMetadata> works { get; set; }
}


public record class ProjectForWork(
  string id,
  string projectName,
  Dictionary<string, string> master = null
);


public record ScanItem(
  long id,
  string locationDivision,
  string locationName,
  string barcode,
  string masterName,
  int count,
  bool isMatch,
  bool isUpload,
  string date,
  string time
);

public record class AppendWorkReq(
  string projectName,
  string fileName,
  List<ScanItem> scanItems
);

public record class RowItem(
  long id,
  string division,
  string locationName,
  string barcode,
  string master,
  int count,
  string date,
  string time
)
{
  public static RowItem Create(ScanItem scanItem) 
  {
    return new RowItem(
      scanItem.id,
      scanItem.locationDivision,
      scanItem.locationName,
      scanItem.barcode,
      scanItem.masterName,
      scanItem.count,
      scanItem.date,
      scanItem.time
    );
  }

  public static RowItem Create(string value) 
  {
    try
    {
      var values = value.Split(",");

      var locationDivision = values[0].Trim();
      var locationName = values[1].Trim();
      var barcode = values[2].Trim();
      var masterName = values[3].Trim();
      var count = Convert.ToInt32(values[4].Trim());
      var date = values[5].Trim();
      var time = values[6].Trim();

      return new RowItem(
        0,
        locationDivision,
        locationName,
        barcode,
        masterName,
        count,
        date,
        time
      );
    }
    catch
    {
      return null;
    }
  }

  public static Dictionary<string, int> MergeMap(List<RowItem> raws)
  {
    var merge = new Dictionary<string, int>();
    foreach (var law in raws)
    {
      if (merge.ContainsKey(law.barcode))
      {
        merge[law.barcode] = merge[law.barcode] + law.count;
      }
      else
      {
        merge[law.barcode] = law.count;
      }
    }
    return merge;
  }
}

public record class MergeRow(
  string barcode,
  int count
);

public record class MatchRaw(
  string barcode,
  int count
)
{
  public static MatchRaw Create(string value) 
  {
    try
    {
      var values = value.Split(",");

      var barcode = values[0].Trim();
      var count = Convert.ToInt32(values[1].Trim());
      return new MatchRaw(barcode, count);
    }
    catch 
    {
      return null;
    }
  }

  public static Dictionary<string, int> MergeMap(List<MatchRaw> raws)
  {
    var merge = new Dictionary<string, int>();
    foreach (var law in raws)
    {
      if (merge.ContainsKey(law.barcode))
      {
        merge[law.barcode] = merge[law.barcode] + law.count;
      }
      else
      {
        merge[law.barcode] = law.count;
      }
    }
    return merge;
  }
}

public class MatchResultItem
{
  public string 품번코드 { get; set; }
  public int 전산 { get; set; }
  public int 실사 { get; set; }
  public int 차이 { get => 전산 - 실사; }
  public string 비고 { get; set; }
}




public class CollectRes 
{
  public long totalCount { get; set; }
}
