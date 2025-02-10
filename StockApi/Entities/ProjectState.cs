namespace StockApi.Entities
{
    public class ProjectState : ProjectInfo
    {
        public FileInfo OriginFile { get; set; }
        public FileInfo MergeFile { get; set; }
        public List<FileInfo> WorkFiles { get; set; }
    }
}
