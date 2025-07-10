namespace stockapi;

public class KorTime
{
  public static DateTime Now
  {
    get
    {
      return DateTime.UtcNow.AddHours(9);
    }
  }
}

