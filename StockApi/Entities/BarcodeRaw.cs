namespace StockApi.Entities
{
    public class BarcodeRaw
    {
        public string Barcode { get; set; }
        public int Count { get; set; }


        #region static method

        public static Dictionary<string, int> MergeMap(List<BarcodeRaw> raws) 
        {
            var merge = new Dictionary<string, int>();
            foreach (var law in raws)
            {
                if (merge.ContainsKey(law.Barcode))
                {
                    merge[law.Barcode] = merge[law.Barcode] + law.Count;
                }
                else
                {
                    merge[law.Barcode] = law.Count;
                }
            }
            return merge;
        }

        public static List<BarcodeRaw> MergeList(List<BarcodeRaw> raws) 
        {
            var map = MergeMap(raws);
            var list = new List<BarcodeRaw>();
            foreach (var pair in map)
            {
                list.Add(new BarcodeRaw { Barcode = pair.Key, Count = pair.Value });
            }
            return list;
        }

        #endregion
    }
}
