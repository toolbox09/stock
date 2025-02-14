using System.Collections.Generic;

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

        public class Match 
        {
            public string Barcode { get; set; }
            public int TargetCount { get; set; }
            public int Count { get; set; }
        }

        public static List<Match> MatchList(List<BarcodeRaw> matchs, List<BarcodeRaw> merges)
        {
            var matchList = new List<Match>();
            foreach (var match in matchs) 
            {
                var merge = merges.FirstOrDefault(_ => _.Barcode == match.Barcode, null);
                var result = new Match
                {
                    Barcode = match.Barcode,
                    TargetCount = match.Count,
                    Count = merge != null ? merge.Count : 0,
                };
                matchList.Add(result);

                if (merge != null)
                {
                    merges.Remove(merge);
                }
            }

            foreach (var merge in merges) 
            {
                var result = new Match
                {
                    Barcode = merge.Barcode,
                    TargetCount = 0,
                    Count = merge.Count,
                };
                matchList.Add(result);
            }

            return matchList;

        }

        #endregion
    }
}
