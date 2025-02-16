using Microsoft.Extensions.Options;
using StockApi.Data;
using StockApi.Middleware;
using StockContext = StockApi.Data.StockContext;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddHttpsRedirection(options =>
{
    options.HttpsPort = 8081;
});

builder.AddDateTimeJson("yyyy/MM/dd HH:mm:ss");
builder.AddAllowCors();

builder.Services.Configure<FtpSettings>(builder.Configuration.GetSection("FtpSettings"));
builder.Services.AddScoped<StockContext>();

var app = builder.Build();

// Configure the HTTP request pipeline.
// if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.UseCors();

app.MapControllers();

app.Run();
