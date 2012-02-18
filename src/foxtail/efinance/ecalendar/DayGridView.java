package foxtail.efinance.ecalendar;
//***************************************************
//�Զ���������ͼ���Ա��ʽ��ʾ����ͼ
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
import android.util.Log;

public class DayGridView extends View {
	
	private Calendar cr;
	private Date nowDate;
	
	//====================================
	//DayGridView�Ļ�������
	//====================================
	private int INDEX;
	private int END;	
	private int WIDTH;
	public int HEIGHT;//���ϲ�����������ȡ
	
	private int edge;			//�߽���
	private int deltaWidth;		//��Ȳ���
	private int deltaHeight;	//�߶Ȳ���
	private int rows;			//������ռ������(��ȥ������)
	private int firstRowHeight;	//�������ĸ߶�
	private int deltaSolarWidth;//������������ƫ����
	private int deltaLunarWidth;//������������ƫ����
	
	private int solarDayFontSize;//deltaWidth/2
	private int lunarDayFontSize;//deltaHeight/5
	private int titleFontSize;   //firstRowHeight/2
	//===================================================
	//   ��������
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
		calPaint.setColor(Color.argb(255, 51, 153, 51));
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
		canvas.drawColor(Color.argb(0, 0, 0, 0));
		//����������
		//this.setWeekTitleBackgroundPaint();
		//canvas.drawRect(new Rect(edge,edge,WIDTH-edge,firstRowHeight+edge), calPaint);
		String []WEEKDAY = {"����","��һ","�ܶ�","����","����","����","����"};
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
		
		//��������
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
        //��������
		int m = INDEX;
		int n = 0;
		int r = 0;
		for(int i=0;i<END;i++)
		{
			Date tempDate = new Date(cr.getSolarDate());
			cr.setSolarDate(new Date(tempDate.getYear(),tempDate.getMonth(),i+1));
			cr.Solar2Lunar();
			//���õ���
			if(cr.getSolarDate().getYear()== nowDate.getYear() &&
					cr.getSolarDate().getMonth() == nowDate.getMonth() &&
					nowDate.getDay() == i+1)
			{
				isToday = true;
				Log.i("true:nowDate",Integer.toString(nowDate.getDay()) );
			}
			else
			{
				isToday = false;
				Log.i("false:nowDate",Integer.toString(nowDate.getDay()) );
			}
			//���ý���
			boolean isJieQi = false;
			if(cr.getJieQi()!= "noInfo")
			{
				isJieQi = true;
			}
			else
			{
				isJieQi = false;
			}
		
			if((m+n+1)%7 == 0)//������
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
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				m = 0;
				n = 0;
				r++;
			}
			else if(n == 0 && m == 0)//������
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
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				n++;
			}
			else//������
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
					canvas.drawText(cr.getJieQi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				else
				{
					this.setLunarDayPaint();
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, calPaint);
				}
				n++;
			}
		}	
	}
	
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.argb(255, 255, 255, 255));
		//===============================================
		//��ȡ�����������
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
