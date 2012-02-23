package foxtail.efinance.ecalendar;
//***************************************************
//自定义日期视图，以表格方式显示月视图
//***************************************************

import foxtail.util.Calendar;
import foxtail.util.Date;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
//import android.util.Log;

public class DayGridView extends View {
	
	private Calendar cr;
	private Date nowDate;
	
	//====================================
	//DayGridView的基本参数
	//====================================
	private int INDEX;
	private int END;	
	private int WIDTH;
	public int HEIGHT;//以上参数由日历获取
	
	private int edge;			//边界宽度
	private int deltaWidth;		//宽度步进
	private int deltaHeight;	//高度步进
	private int rows;			//日期所占的行数(除去星期栏)
	private int firstRowHeight;	//星期栏的高度
	private int deltaSolarWidth;//阳历日期文字偏移量
	private int deltaLunarWidth;//阴历日期文字偏移量
	
	private int solarDayFontSize;//deltaWidth/2
	private int lunarDayFontSize;//deltaHeight/5
	private int titleFontSize;   //firstRowHeight/2
	//===================================================
	//   画笔设置
	//===================================================
	private Paint calPaint;
	private void setGridPaint()
	{
		calPaint.setColor(Color.argb(255, 204, 204, 204));
		calPaint.setAntiAlias(false);
	}
	
	private void setSolarDayPaint()
	{
		calPaint.setColor(Color.argb(255, 51, 51, 51));
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(solarDayFontSize);
	}
	
	private void setLunarDayPaint()
	{
		calPaint.setColor(Color.argb(255, 204, 204, 204));
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(lunarDayFontSize);
	}
	
	private void setJieQiPaint()
	{
		calPaint.setColor(Color.argb(255, 255, 102, 102));
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(lunarDayFontSize);
	}
	
	private void setWeekendPaint()
	{
		calPaint.setColor(Color.argb(255, 255, 102, 102));
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(solarDayFontSize);
	}
	
	private void setWeekTitlePaint()
	{
		calPaint.setColor(Color.WHITE);
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(titleFontSize);
	}
	
	private void setWeekTitleBackgroundPaint()
	{
		calPaint.setColor(Color.argb(255, 102, 204, 204));
	}
	
	private void setSelectedDayPaint()
	{
		calPaint.setColor(Color.BLUE);
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(solarDayFontSize);
	}
	
	private void setSelectedDayBackgoundPaint()
	{
		calPaint.setColor(Color.BLACK);
	}
	
	private void setTodayPaint()
	{
		calPaint.setColor(Color.WHITE);
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(solarDayFontSize);
	}
	
	private void setTodayBackgoundPaint()
	{
		calPaint.setColor(Color.argb(255, 51, 102, 153));//102, 102, 153
	}
	//===================================================
	public DayGridView(Context context,AttributeSet attrs)
	{
		super(context, attrs);
		
		Time t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month+1;
        int day = t.monthDay;
        
		edge = 2;
		calPaint = new Paint();
		cr = new Calendar();
		nowDate = new Date();
		nowDate.setDate(year,month,day);
		INDEX = cr.getCalendarIndex();
        END = cr.getCalendarEnd();
	}
	
	
	public void setCalendarDate(Date date)
	{
		cr.setSolarDate(date);
        INDEX = cr.getCalendarIndex();
        END = cr.getCalendarEnd();
	}
			
	private void drawDateTable(Canvas canvas)
	{
		//canvas.drawColor(Color.argb(255, 255, 255, 255));
		//绘制星期栏
		//this.setWeekTitleBackgroundPaint();
		//canvas.drawRect(new Rect(edge,edge,WIDTH-edge,firstRowHeight+edge), calPaint);
		String []WEEKDAY = {"周日","周一","周二","周三","周四","周五","周六"};
		this.setWeekTitlePaint();
		calPaint.setColor(Color.argb(255, 102, 51, 102));
		canvas.drawRect(new Rect(edge,edge,edge+deltaWidth+1,firstRowHeight+edge), calPaint);
		calPaint.setColor(Color.WHITE);
		canvas.drawText(WEEKDAY[0], deltaSolarWidth, 4*firstRowHeight/5, calPaint);
		
		calPaint.setColor(Color.argb(255, 153, 102, 153));
		canvas.drawRect(new Rect(edge+deltaWidth+1,edge,WIDTH-2*edge-deltaWidth+2,firstRowHeight+edge), calPaint);
		this.setWeekTitlePaint();
		for(int i=1;i<6;i++)
		{
			canvas.drawText(WEEKDAY[i], deltaSolarWidth+i*deltaWidth, 4*firstRowHeight/5, calPaint);
		}
		calPaint.setColor(Color.argb(255, 102, 51, 102));
		canvas.drawRect(new Rect(edge+6*deltaWidth,edge,edge+WIDTH-2*edge,firstRowHeight+edge), calPaint);
		calPaint.setColor(Color.WHITE);
		canvas.drawText(WEEKDAY[6], deltaSolarWidth+6*deltaWidth, 4*firstRowHeight/5, calPaint);
		
		//绘制网格
		this.setGridPaint();
		canvas.drawLine(edge, edge, WIDTH-edge, edge, calPaint);
		for(int i=0;i<rows-1;i++)
		{
			canvas.drawLine(edge, edge+firstRowHeight+deltaHeight*i, WIDTH-edge, edge+firstRowHeight+deltaHeight*i, calPaint);
		}
		canvas.drawLine(edge, HEIGHT - edge, WIDTH-edge, HEIGHT - edge, calPaint);
		
		for(int j=0;j<8;j++)
		{
			canvas.drawLine(edge+deltaWidth*j, edge+firstRowHeight, edge+deltaWidth*j, HEIGHT - edge, calPaint);
		}//edge+deltaWidth*j    WIDTH-edge-deltaWidth*j
		
		boolean isToday = false;
        //绘制日期
		int m = INDEX;
		int n = 0;
		int r = 0;
		for(int i=0;i<END;i++)
		{
			Date tempDate = new Date(cr.getSolarDate());
			cr.setSolarDate(new Date(tempDate.getYear(),tempDate.getMonth(),i+1));
			cr.Solar2Lunar();
			//设置当日
			if(cr.getSolarDate().getYear()== nowDate.getYear() &&
					cr.getSolarDate().getMonth() == nowDate.getMonth() &&
					nowDate.getDay() == i+1)
			{
				isToday = true;
				//Log.i("true:nowDate",Integer.toString(nowDate.getDay()) );
			}
			else
			{
				isToday = false;
				//Log.i("false:nowDate",Integer.toString(nowDate.getDay()) );
			}
			//设置节气
			boolean isJieQi = false;
			if(cr.getJieQi()!= "noInfo")
			{
				isJieQi = true;
			}
			else
			{
				isJieQi = false;
			}
		
			if((m+n+1)%7 == 0)//星期六
			{
				if(isToday)
				{
					this.setTodayBackgoundPaint();
					canvas.drawRect(new Rect(edge+(m+n)*deltaWidth+1,firstRowHeight+edge+r*deltaHeight+1,edge+(m+n+1)*deltaWidth,firstRowHeight+edge+(r+1)*deltaHeight), calPaint);
					this.setTodayPaint();
				}
				else
				{
					this.setWeekendPaint();
				}
				canvas.drawText(Integer.toString(i+1), deltaSolarWidth+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, calPaint);
				if(isJieQi)
				{
					this.setJieQiPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				m = 0;
				n = 0;
				r++;
			}
			else if(n == 0 && m == 0)//星期天
			{
				if(isToday)
				{
					this.setTodayBackgoundPaint();
					canvas.drawRect(new Rect(edge+(m+n)*deltaWidth+1,firstRowHeight+edge+r*deltaHeight+1,edge+(m+n+1)*deltaWidth,firstRowHeight+edge+(r+1)*deltaHeight), calPaint);
					this.setTodayPaint();
				}
				else
				{
					this.setWeekendPaint();
				}
				canvas.drawText(Integer.toString(i+1), deltaSolarWidth+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, calPaint);
				if(isJieQi)
				{
					this.setJieQiPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				n++;
			}
			else//工作日
			{
				if(isToday)
				{
					this.setTodayBackgoundPaint();
					canvas.drawRect(new Rect(edge+(m+n)*deltaWidth+1,firstRowHeight+edge+r*deltaHeight+1,edge+(m+n+1)*deltaWidth,firstRowHeight+edge+(r+1)*deltaHeight), calPaint);
					this.setTodayPaint();
				}
				else
				{
					this.setSolarDayPaint();
				}
				canvas.drawText(Integer.toString(i+1), deltaSolarWidth+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, calPaint);
				if(isJieQi)
				{
					this.setJieQiPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					if(isToday)
					{
						calPaint.setColor(Color.WHITE);
					}
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				n++;
			}
		}	
	}
	
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//===============================================
		//获取绘制区域参数
		WIDTH =  this.getWidth();
		HEIGHT = this.getHeight();
		deltaWidth = WIDTH/7;
		rows = cr.getCalendarRows();
		firstRowHeight = (HEIGHT)/(2*rows);
		deltaHeight = (HEIGHT - firstRowHeight)/(rows-1);
		deltaSolarWidth = deltaWidth/4;
		deltaLunarWidth = deltaWidth/3;
		solarDayFontSize = deltaWidth/2;
		lunarDayFontSize = deltaHeight/5;
		titleFontSize = 2*firstRowHeight/3;
		//================================================
			
		drawDateTable(canvas);
	}
}
