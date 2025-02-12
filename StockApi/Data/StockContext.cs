using Microsoft.Extensions.Options;
using StockApi.Entities;
using NanoidDotNet;
using StockApi.Utils;
using FluentFTP;
using FileInfo = StockApi.Entities.FileInfo;
using System.Diagnostics.Metrics;
using System.Text;

namespace StockApi.Data
{
    public class StockContext : FileContext
    {
        public StockContext(IOptions<FtpSettings> settings) : base(settings)
        {
        }

        #region convert

        public string ToBarcode(string value)
        {
            try
            {
                var values = value.Split(",");
                return values[0].Trim();
            }
            catch
            {
                return null;
            }
        }

        public BarcodeRaw ToBarcodLaw(string value)
        {
            try
            {
                var values = value.Split(",");
                var barcode = values[0].Trim();
                var count = Convert.ToInt32(values[1]);
                return new BarcodeRaw { Barcode = barcode, Count = count };
            }
            catch
            {
                return null;
            }
        }

        public Auth ToAuth(string value)
        {
            try
            {
                var values = value.Split(",");
                var id = values[0].Trim();
                var password = values[1].Trim();
                var keyword = values[2].Trim();
                return new Auth { Id = id, Password = password, Keyword = keyword };
            }
            catch
            {
                return null;
            }
        }

        #endregion

        #region project

        public bool CreateProject(string projectName, string masterUrl, string matchUrl) 
        {
            var path = GetProjectPath(projectName);
            var state =_client.CreateDirectory(path);
            if (!state)
                return false;

            var projectInfo = new ProjectInfo
            {
                Id = Nanoid.Generate(),
                Name = projectName,
                MasterUrl = masterUrl,
                MatchUrl = matchUrl,
                Created = KorTime.Now,
            };

            return WriteJson(GetProjectInfoPath(projectName), projectInfo);
        }

        public ProjectInfo GetProjectInfo(string projectName) 
        {
            return ReadJson<ProjectInfo>(GetProjectInfoPath(projectName));
        }

        public List<ProjectInfo> GetProjectInfoList() 
        {
            var result = new List<ProjectInfo>();
            var projects = _client.GetListing(GetProjectPath());
            foreach (var project in projects)
            {
                if (project.Type == FtpObjectType.Directory)
                {
                    var info = GetProjectInfo(project.Name);
                    if (info != null)
                        result.Add(info);
                }
            }
            return result;
        }

        public ProjectState GetProjectState(string projectName) 
        {
            var info = GetProjectInfo(projectName);
            if (info == null)
                return null;

            var state = new ProjectState
            {
                Id = info.Id,
                Name = info.Name,
                MasterUrl = info.MasterUrl,
                Created = info.Created,
                WorkFiles = new List<FileInfo>(),
            };

            var targets = _client.GetListing(GetProjectPath(projectName));
            foreach (var target in targets)
            {
                if (target.Type == FtpObjectType.File)
                {
                    var file = new FileInfo { Name = target.Name, Modified = target.Modified };
                    if (target.Name.Contains("workraws"))
                    {
                        state.WorkFiles.Add(file);
                    }
                    else if (target.Name.Contains("origin"))
                    {
                        state.OriginFile = file;
                    }
                    else if (target.Name.Contains("merge"))
                    {
                        state.MergeFile = file;
                    }
                }
            }
            return state;
        }

        public List<ProjectState> GetProjectStateList() 
        {
            var list = new List<ProjectState>();
            var targets = _client.GetListing(GetProjectPath());
            foreach (var target in targets)
            {
                if (target.Type == FtpObjectType.Directory)
                {
                    var info = GetProjectState(target.Name);
                    if (info != null)
                        list.Add(info);
                }
            }
            return list;
        }

        #endregion

        #region master

        public List<FileInfo> GetMasterFileList() 
        {
            var list = new List<FileInfo>();
            var targets = _client.GetListing(GetMasterPath());
            foreach(var target in targets) 
            {
                if (target.Type == FtpObjectType.File) 
                {
                    list.Add(new FileInfo { Name = target.Name, Modified = target.Modified });
                }
            }
            return list;
        }

        public Dictionary<string, int> GetMaster(string masterName) 
        {
            var barcodes = ReadCsv(GetMasterPath(masterName), ToBarcode);
            var maps = new Dictionary<string, int>();
            foreach (var barcode in barcodes)
            {
                maps.TryAdd(barcode, 1);
            }
            return maps;
        }

        public List<FileInfo> GetMatchFileList()
        {
            var list = new List<FileInfo>();
            var targets = _client.GetListing(GetMatchPath());
            foreach (var target in targets)
            {
                if (target.Type == FtpObjectType.File)
                {
                    list.Add(new FileInfo { Name = target.Name, Modified = target.Modified });
                }
            }
            return list;
        }

        #endregion

        #region collect

        public bool UpdateWork(string projectName, string fileName, List<BarcodeRaw> raws) 
        {
            var path = $"{GetProjectPath(projectName)}/workraws-{fileName}.csv";
            return WriteCsv(path, raws, (obj) =>$"{obj.Barcode},{obj.Count}");
        }

        public bool Collect(string projectName) 
        {
            try 
            {
                var raws = new List<BarcodeRaw>();
                var projectPath = GetProjectPath(projectName);
                var workFiles = _client.GetListing(projectPath);
                foreach (var workFile in workFiles)
                {
                    if (workFile.Type == FtpObjectType.File && workFile.Name.Contains("workraws"))
                    {
                        var workRaws = ReadCsv(workFile.FullName, ToBarcodLaw);
                        if (workRaws != null)
                            raws.AddRange(workRaws);
                    }
                }

                var merge = BarcodeRaw.MergeList(raws);
                WriteCsv($"{projectPath}/origin.csv", raws, (obj) => $"{obj.Barcode},{obj.Count}");
                WriteCsv($"{projectPath}/merge.csv", merge, (obj) => $"{obj.Barcode},{obj.Count}");
                return true;
            }
            catch 
            {
                return false;
            }
        }

        #endregion

        #region auth
        public List<Auth> ReadAuths()
        {
            try
            {
                return ReadCsv(GetUsersPath(), ToAuth);
            }
            catch
            {
                return null;
            }
        }

        #endregion

    }
}
