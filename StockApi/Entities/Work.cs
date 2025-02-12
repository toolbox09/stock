namespace StockApi.Entities
{
    public class Work
    {
        public string Id { get; set; }
        public string ProjectName { get; set; }
        public Dictionary<string, int> Master { get; set; }
        // public Dictionary<string, int> Match { get; set; }
    }
}
