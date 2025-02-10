namespace StockApi.Middleware
{
    public static class WebApplicationBuilderEx
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
}
