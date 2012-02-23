package foxtail.util;

public class Date
{
	private int year;
	private int month;
	private int day;
	private int hour;
	private int weekday;
	private int leap;
	
	public Date(int year, int month, int day,int hour, int weekday, int leap)
	{
		this.year = year;
		this.month = month;
		this.day =  day;
		this.hour = hour;
		this.weekday = weekday;
		this.leap = leap;
	}
	
	public Date(int year, int month, int day)
	{
		this.year = year;
		this.month = month;
		this.day =  day;
	}
	
	public void setDate(int year, int month, int day)
	{
		this.year = year;
		this.month = month;
		this.day =  day;
	}
	
	public Date(Date d)
	{
		this.year = d.year;
		this.month = d.month;
		this.day =  d.day;
		this.hour = d.hour;
		this.weekday = d.weekday;
		this.leap = d.leap;
	}
	
	public void setDate(Date d)
	{
		this.year = d.year;
		this.month = d.month;
		this.day =  d.day;
		this.hour = d.hour;
		this.weekday = d.weekday;
		this.leap = d.leap;
	}
	
	public Date()
	{
		this.year = 1901;
		this.month = 1;
		this.day =  1;
		this.hour = 0;
		this.weekday = 0;
		this.leap = 0;
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}
	
	public void setMonth(int month)
	{
		this.month = month;
	}
	
	public void setDay(int day)
	{
		this.day = day;
	}
	
	public void setHour(int hour)
	{
		this.hour = hour;
	}
	
	public void setWeekDay(int weekday)
	{
		this.weekday = weekday;
	}
	
	public void setLeap(int leap)
	{
		this.leap = leap;
	}
	
	public int getYear()
	{
		return this.year;
	}
	
	public int getMonth()
	{
		return this.month;
	}
	
	public int getDay()
	{
		return this.day;
	}
	
	public int getHour()
	{
		return this.hour;
	}
	
	public int getWeekDay()
	{
		return this.weekday;
	}
	
	public int getLeap()
	{
		return this.leap;
	}
	
	final private String dayInHanzi[] = {"初一","初二","初三","初四","初五","初六","初七","初八","初九","初十",
			               "十一","十二","十三","十四","十五","十六","十七","十八","十九","二十",
			               "廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九","三十"};
	
	public String getDayInHanzi()
	{
		return dayInHanzi[this.getDay()-1];
	}
	
	final private String MonthInHanzi[] = {"正","二","三","四","五","六","七","八","九","十","十一","十二"};
	
	public String getMonthInHanzi()
	{
		return MonthInHanzi[this.getMonth()-1];
	}
	
}