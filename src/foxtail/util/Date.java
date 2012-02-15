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
	
	final private String dayInHanzi[] = {"��һ","����","����","����","����","����","����","����","����","��ʮ",
			               "ʮһ","ʮ��","ʮ��","ʮ��","ʮ��","ʮ��","ʮ��","ʮ��","ʮ��","��ʮ",
			               "إһ","إ��","إ��","إ��","إ��","إ��","إ��","إ��","إ��","��ʮ"};
	
	public String getDayInHanzi()
	{
		return dayInHanzi[this.getDay()-1];
	}
	
}