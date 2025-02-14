using FluentFTP;
using FluentFTP.Helpers;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace StockApi.Data
{
    public class FileContext : IDisposable
    {
        protected FtpClient _client { get; }

        public FileContext(IOptions<FtpSettings> settings) 
        {
            _client = new FtpClient(
                settings.Value.Host,
                settings.Value.Username,
                settings.Value.Password
            );

            _client.AutoConnect();
        }

        public void Dispose()
        {
            _client.Dispose();
        }


        #region path

        public string projectPath = "projects";
        public string masterPath = "master";
        public string authPath = "auth";
        public string matchPath = "match";

        public string GetProjectPath()
            => $"/{projectPath}/";

        public string GetProjectPath(string projectName)
            => $"/{projectPath}/{projectName}";

        public string GetProjectInfoPath(string projectName)
            => $"/{projectPath}/{projectName}/.json";

        public string GetMasterPath()
            => $"/{masterPath}/";

        public string GetMasterPath(string masterName)
            => $"/{masterPath}/{masterName}";

        public string GetUsersPath()
            => $"{authPath}/users.csv";

        public string GetMatchPath()
            => $"{matchPath}/";

        public string GetMatchPath(string fileName)
            => $"{matchPath}/{fileName}";


        #endregion

        #region

        public bool WriteJson<T>(string path, T data)
        {
            try
            {
                string json = JsonConvert.SerializeObject(data, Newtonsoft.Json.Formatting.Indented);
                using var stream = new MemoryStream(Encoding.UTF8.GetBytes(json));
                var state = _client.UploadStream(stream, path);
                return state.IsSuccess();
            }
            catch
            {
                return false;
            }
        }

        public T ReadJson<T>(string path)
            where T : class
        {
            try
            {
                using var stream = _client.OpenRead(path);
                using var reader = new StreamReader(stream, Encoding.UTF8);
                return JsonConvert.DeserializeObject<T>(reader.ReadToEnd());
            }
            catch
            {
                return null;
            }
        }

        public List<T> ReadCsv<T>(string path, Func<string,T> func)
            where T : class 
        {
            try
            {
                var result = new List<T>();
                if (_client.FileExists(path)) 
                {
                    using var stream = _client.OpenRead(path);
                    using var reader = new StreamReader(stream, Encoding.UTF8);
                    string line;
                    while ((line = reader.ReadLine()) != null)
                    {
                        var obj = func(line);
                            if (obj != null)
                            result.Add(obj);
                    }
                    return result;
                }
                return null;
            }
            catch 
            {
                return null;
            }
        }


        public bool WriteCsv<T>(string path, List<T> data, Func<T, string> func)
            where T : class
        {
            try
            {
                using var stream = new MemoryStream();
                using var writer = new StreamWriter(stream, Encoding.UTF8);
                foreach (var obj in data)
                {
                    var line = func(obj);
                    if (line != null)
                        writer.WriteLine(line);
                }
                writer.Flush();
                stream.Position = 0;
                var state = _client.UploadStream(stream, path);
                return state.IsSuccess();
            }
            catch
            {
                return false;
            }
        }

        #endregion


    }
}
