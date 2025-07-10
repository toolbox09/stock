using System.ComponentModel;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace stockapi;

public static class Middleware
{
  public static WebApplicationBuilder AddDateTimeJson(this WebApplicationBuilder builder, string format)
  {
    builder.Services.AddControllers()
        .AddJsonOptions(options =>
        {
          options.JsonSerializerOptions.Converters.Add(new DateTimeConverter("yyyy/MM/dd HH:mm:ss"));
        });
    return builder;
  }

  public static WebApplicationBuilder AddAllowCors(this WebApplicationBuilder builder)
  {
    builder.Services.AddCors(options =>
    {
      options.AddDefaultPolicy(builder =>
          builder.SetIsOriginAllowed(_ => true)
          .AllowAnyMethod()
          .AllowAnyHeader()
          .AllowCredentials());
    });
    return builder;
  }
}

public class DateTimeConverter : JsonConverter<DateTime>
{
  private readonly string _format;

  public DateTimeConverter(string format)
  {
    _format = format;
  }

  public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
  {
    return DateTime.ParseExact(reader.GetString(), _format, null);
  }

  public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
  {
    writer.WriteStringValue(value.ToString(_format));
  }
}