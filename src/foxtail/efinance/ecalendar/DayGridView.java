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
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

public class DayGridView extends View implements OnGestureListener{
	
	private GestureDetector dgvGestureDetector;
	private Calendar cr;
	private Date nowDate;
	private Date selectedDate;
	//----------------��������
	private int touchX;
	private int touchY;
	private int touchRow;
	private int touchCol;
	
	//====================================
	//DayGridView�Ļ�������
	//====================================
	private int INDEX;
	private int END;	
	private int WIDTH;
	public int HEIGHT;//���ϲ�����������ȡ
	
	//private int edge;			//�߽���
	private final int edge = 4;
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
		calPaint.setColor(Color.WHITE);
		calPaint.setAntiAlias(true);
		calPaint.setTextSize(solarDayFontSize);
	}
	
	private void setSelectedDayBackgoundPaint()
	{
		calPaint.setColor(Color.argb(255, 205, 102, 102));
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
        
		//edge = 2;
		calPaint = new Paint();
		cr = new Calendar();
		nowDate = new Date();
		selectedDate = new Date();
		nowDate.setDate(year,month,day);
		selectedDate.setDate(nowDate);
		INDEX = cr.getCalendarIndex();
        END = cr.getCalendarEnd();      
        this.dgvGestureDetector = new GestureDetector(this);
	}
	
	
	public void setCalendarDate(Date date)
	{
		cr.setSolarDate(date);
        INDEX = cr.getCalendarIndex();
        END = cr.getCalendarEnd();
        touchRow = 0;
        touchCol = INDEX;
        this.selectedDate.setDate(date);
	}
			
	private void drawDateTable(Canvas canvas)
	{
		Log.i("order", "E");
		//canvas.drawColor(Color.argb(255, 255, 255, 255));
		//����������
		//this.setWeekTitleBackgroundPaint();
		//canvas.drawRect(new Rect(edge,edge,WIDTH-edge,firstRowHeight+edge), calPaint);
		String []WEEKDAY = {"����","��һ","�ܶ�","����","����","����","����"};
		this.setWeekTitlePaint();
		calPaint.setColor(Color.argb(255, 102, 51, 102));
		canvas.drawRect(new Rect(0,0,deltaWidth+1,firstRowHeight), calPaint);
		calPaint.setColor(Color.WHITE);
		canvas.drawText(WEEKDAY[0], deltaSolarWidth/2, 4*firstRowHeight/5, calPaint);
		
		calPaint.setColor(Color.argb(255, 153, 102, 153));
		canvas.drawRect(new Rect(deltaWidth+1,0,WIDTH-deltaWidth+2,firstRowHeight), calPaint);
		this.setWeekTitlePaint();
		for(int i=1;i<6;i++)
		{
			canvas.drawText(WEEKDAY[i], deltaSolarWidth/2+i*deltaWidth, 4*firstRowHeight/5, calPaint);
		}
		calPaint.setColor(Color.argb(255, 102, 51, 102));
		canvas.drawRect(new Rect(6*deltaWidth,0,WIDTH,firstRowHeight), calPaint);
		calPaint.setColor(Color.WHITE);
		canvas.drawText(WEEKDAY[6], deltaSolarWidth/2+6*deltaWidth, 4*firstRowHeight/5, calPaint);
		
		//��������
		this.setGridPaint();
		canvas.drawLine(0, 0, WIDTH, 0, calPaint);
		for(int i=0;i<rows-1;i++)
		{
			canvas.drawLine(0, firstRowHeight+deltaHeight*i, WIDTH, firstRowHeight+deltaHeight*i, calPaint);
		}
		canvas.drawLine(0, HEIGHT, WIDTH, HEIGHT, calPaint);
		
		for(int j=0;j<8;j++)
		{
			canvas.drawLine(deltaWidth*j, firstRowHeight, deltaWidth*j, HEIGHT, calPaint);
		}//edge+deltaWidth*j    WIDTH-edge-deltaWidth*j
		
		boolean isToday = false;
		boolean isJieQi = false;
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
				//Log.i("true:nowDate",Integer.toString(nowDate.getDay()) );
			}
			else
			{
				isToday = false;
				//Log.i("false:nowDate",Integer.toString(nowDate.getDay()) );
			}
			//���ý���
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
					canvas.drawRect(new Rect((m+n)*deltaWidth+1,firstRowHeight+r*deltaHeight+1,(m+n+1)*deltaWidth,firstRowHeight+(r+1)*deltaHeight), calPaint);
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
			else if(n == 0 && m == 0)//������
			{
				if(isToday)
				{
					this.setTodayBackgoundPaint();
					canvas.drawRect(new Rect((m+n)*deltaWidth+1,firstRowHeight+r*deltaHeight+1,(m+n+1)*deltaWidth,firstRowHeight+(r+1)*deltaHeight), calPaint);
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
			else//������
			{
				if(isToday)
				{
					this.setTodayBackgoundPaint();
					canvas.drawRect(new Rect((m+n)*deltaWidth+1,firstRowHeight+r*deltaHeight+1,(m+n+1)*deltaWidth,firstRowHeight+(r+1)*deltaHeight), calPaint);
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
		
		//----------------����ѡ����
		if(getSelectedDay()>0 && getSelectedDay()<=END)
		{
			//----------------
			cr.getSolarDate().setDay(getSelectedDay());
			cr.Solar2Lunar();
			if(cr.getJieQi()!= "noInfo")
			{
				isJieQi = true;
			}
			else
			{
				isJieQi = false;
			}
			this.setSelectedDayBackgoundPaint();
			canvas.drawRect(new Rect(touchCol*deltaWidth+1,firstRowHeight+touchRow*deltaHeight+1,(touchCol+1)*deltaWidth,firstRowHeight+(touchRow+1)*deltaHeight), calPaint);
			this.setSelectedDayPaint();
			canvas.drawText(Integer.toString(getSelectedDay()), deltaSolarWidth+touchCol*deltaWidth, firstRowHeight+2*deltaHeight/3+touchRow*deltaHeight, calPaint);
			if(isJieQi)
			{
				this.setJieQiPaint();
				calPaint.setColor(Color.WHITE);
				canvas.drawText(cr.getJieQi(), deltaLunarWidth+touchCol*deltaWidth, firstRowHeight+deltaHeight+touchRow*deltaHeight-edge, calPaint);
			}
			else
			{
				this.setLunarDayPaint();
				calPaint.setColor(Color.WHITE);
				canvas.drawText(cr.getLunarDate().getDayInHanzi(), deltaLunarWidth+touchCol*deltaWidth, firstRowHeight+deltaHeight+touchRow*deltaHeight-edge, calPaint);
			}
		}	
	}
	
	private int getSelectedDay()
	{
		selectedDate.setYear(cr.getSolarDate().getYear());
		selectedDate.setMonth(cr.getSolarDate().getMonth());
		selectedDate.setDay(touchRow*7-INDEX+touchCol+1);
		return selectedDate.getDay();
	}
	
	public Date getSelectedDate()
	{
		return selectedDate;
	}
	
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
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
	
	/*
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		 Log.i("ECalendarActivity", "dispatchTouchEvent");
		 this.onTouchEvent(event);
		 return true;
	}*/
	
	 public boolean onTouchEvent (MotionEvent event)
	{
		 Log.i("order", "F");
		 Log.i("onTouchEvent", "in GRIDVIEW X="+Integer.toString((int)event.getX()));
		 //super.onTouchEvent(event);
		 return this.dgvGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		Log.i("order", "G");
		Log.i("onTouchEvent", "in GRIDVIEW onDown X="+Integer.toString((int)event.getX()));
		// TODO Auto-generated method stub
		this.touchX = (int)event.getX();
		this.touchY = (int)event.getY();
		 
		touchCol = touchX/deltaWidth;
		touchRow = (touchY - firstRowHeight)/deltaHeight;
		this.getSelectedDay();
		this.invalidate();
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
