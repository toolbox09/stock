using StockApi.Entities;

namespace StockApi.Dto
{
    public class UpdateWorkRequest
    {
        public string ProjectName { get; set; }
        public string FileName { get; set; }
        public List<BarcodeRaw> Raws { get; set; }
    }
}
