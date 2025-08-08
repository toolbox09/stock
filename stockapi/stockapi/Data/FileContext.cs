using CsvHelper;
using CsvHelper.Configuration;
using FluentFTP;
using FluentFTP.Helpers;
using Newtonsoft.Json;
using stockapi.Controllers;
using System.Globalization;
using System.Text;

namespace stockapi.Data;

public class FileContext : IDisposable
{

  Encoding euckr = Encoding.GetEncoding("euc-kr");

  public FtpClient client { get; }

  public FileContext() 
  {
    // client = new FtpClient("121.133.57.68","counter","1234");
    client = new FtpClient("site31613.siteasp.net", "site31613", "y%9W5B?dCa4!");
    
    client.AutoConnect();
  }
  public void Dispose()
  {
    client.Dispose();
  }

  public bool WriteJson<T>(string path, T data)
  {
    try
    {
      string json = JsonConvert.SerializeObject(data, Formatting.Indented);
      using var stream = new MemoryStream(Encoding.UTF8.GetBytes(json));
      var state = client.UploadStream(stream, path);
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
      using var stream = client.OpenRead(path);
      using var reader = new StreamReader(stream, Encoding.UTF8);
      return JsonConvert.DeserializeObject<T>(reader.ReadToEnd());
    }
    catch
    {
      return null;
    }
  }

  public List<T> ReadCsv<T>(string path, Func<string, T> func)
    where T : class
  {

    try
    {
      var result = new List<T>();
      if (client.FileExists(path))
      {
        using var stream = client.OpenRead(path);
        using var reader = new StreamReader(stream, euckr);


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
      using var writer = new StreamWriter(stream, euckr);
      foreach (var obj in data)
      {
        var line = func(obj);
        if (line != null)
          writer.WriteLine(line);
      }
      writer.Flush();
      stream.Position = 0;
      var state = client.UploadStream(stream, path);
      return state.IsSuccess();
    }
    catch
    {
      return false;
    }
  }

  public long WriteRowItem(StreamWriter writer, RowItem item)
  {
    try
    {
      var rowText = $"{item.division},{item.locationName},{item.barcode},{item.master ?? ""},{item.count},{item.date},{item.time}";
      writer.WriteLine(rowText);
      return item.id;
    }
    catch
    {
      return -1;
    }
  }

  public List<long> AppendRowItems(string path, List<RowItem> data)
  {
    var resultIds = new List<long>();
    using (var stream = client.OpenAppend(path))
    using (var writer = new StreamWriter(stream, euckr))
    {
      foreach (var item in data)
      {
        var id = WriteRowItem(writer, item);
        if (id > 0)
          resultIds.Add(id);
      }
    }
    return resultIds;
  }


  public bool AppendCsv<T>(string path, List<T> data)
    where T : class
  {
    try 
    {
      var config = new CsvConfiguration(CultureInfo.InvariantCulture)
      {
        HasHeaderRecord = false
      };
      using (var stream = client.OpenAppend(path))
      using (var writer = new StreamWriter(stream, euckr))
      using (var csv = new CsvWriter(writer, config))
      {
        csv.WriteRecords(data);
      }
      return true;
    }
    catch { return false; }

  }
}
