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
import android.util.AttributeSet;
import android.view.View;
import android.util.Log;

public class DayGridView extends View {
	
	private Calendar cr = new Calendar();
	
	//====================================
	//DayGridView的基本参数
	//====================================
	private int INDEX;
	private int END;	
	private int WIDTH;
	public int HEIGHT;
	private int edge;
	private DayGridViewAttrs attrs;
	public DayGridView(Context context,AttributeSet attrs)
	{
		super(context, attrs);
		INDEX = cr.getCalendarIndex();
		END = cr.getCalendarEnd();
		edge = 2;
	}
	
	
	public void setCalendarDate(Date date)
	{
		cr.setSolarDate(date);
		
		Date tempDate = new Date(date.getYear(),date.getMonth(),1);
        cr.setSolarDate(tempDate);
        INDEX = cr.getSolarDate().getWeekDay();
        cr.setSolarDate(date);
        END = cr.getDaysInSolarMonth();
	}
		
	private void drawDateTable(Canvas canvas)
	{
		canvas.drawColor(Color.argb(0, 0, 0, 0));
		int ROWS = attrs.getRows();
		int firstRowHeight = attrs.getFirstRowHeight();
		int deltaHeight = attrs.getDeltaHeight();
		int deltaWidth = attrs.getDeltaWidth();
		
		int tempDeltaSolar = deltaWidth/4;
		int tempDeltaLunar = deltaWidth/3;
		//绘制星期栏
		canvas.drawRect(new Rect(edge,edge,WIDTH-edge,firstRowHeight+edge), attrs.getWeekTitleBackgroundPaint());
		String []WEEKDAY = {"周日","周一","周二","周三","周四","周五","周六"};
		for(int i=0;i<7;i++)
		{
			canvas.drawText(WEEKDAY[i], tempDeltaSolar+i*deltaWidth, 3*firstRowHeight/4, attrs.getWeekTitlePaint());
		}
		
		//绘制网格
		canvas.drawLine(edge, edge, WIDTH-edge, edge, attrs.getGridPaint());
		for(int i=0;i<ROWS-1;i++)
		{
			canvas.drawLine(edge, edge+firstRowHeight+deltaHeight*i, WIDTH-edge, edge+firstRowHeight+deltaHeight*i, attrs.getGridPaint());
		}
		canvas.drawLine(edge, HEIGHT - edge, WIDTH-edge, HEIGHT - edge, attrs.getGridPaint());
		
		for(int j=0;j<8;j++)
		{
			canvas.drawLine(edge+deltaWidth*j, edge+firstRowHeight, edge+deltaWidth*j, HEIGHT - edge, attrs.getGridPaint());
		}
		
		
        //绘制日期
		int m = INDEX;
		int n = 0;
		int r = 0;
		for(int i=0;i<END;i++)
		{
			Date tempDate = new Date(cr.getSolarDate());
			cr.setSolarDate(new Date(tempDate.getYear(),tempDate.getMonth(),i+1));
			cr.Solar2Lunar();
			if(i>8)
			{
				if((m+n+1)%7 == 0)
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getWeekendPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					m = 0;
					n = 0;
					r++;
				}
				else if(n == 0 && m == 0)
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getWeekendPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					n++;
				}
				else
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getSolarDayPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					n++;
				}
			}
			else
			{
				if((m+n+1)%7 == 0)
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getWeekendPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					m = 0;
					n = 0;
					r++;
				}
				else if(n == 0 && m == 0)
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getWeekendPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					n++;
				}
				else
				{
					canvas.drawText(Integer.toString(i+1), tempDeltaSolar+(m+n)*deltaWidth, firstRowHeight+2*deltaHeight/3+r*deltaHeight, attrs.getSolarDayPaint());
					canvas.drawText(cr.getLunarDate().getDayInHanzi(), tempDeltaLunar+(m+n)*deltaWidth, firstRowHeight+deltaHeight+r*deltaHeight-edge, attrs.getLunarDayPaint());
					n++;
				}
			}
		}
		
	}
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.argb(255, 255, 255, 255));
		//获取绘制区域参数
		WIDTH =  this.getWidth();
		HEIGHT = this.getHeight();
		attrs = new DayGridViewAttrs();
		drawDateTable(canvas);
	}
	
	//=================================================================================
	//绘制日历视图时的参数设置
	//=================================================================================
	public class DayGridViewAttrs
	{
		//=============================
		//          画笔参数
		//=============================
		private Paint gridPaint;//网格画笔
		private Paint solarDayPaint;//阳历画笔
		private Paint lunarDayPaint;//阴历画笔
		private Paint weekendPaint;//阳历双休日画笔
		private Paint weekTitlePaint;//星期栏画笔
		private Paint weekTitleBackgroundPaint;
		
		//选中日或当日画笔
		private Paint selectedDayPaint;
		private Paint selectedDayBackgoundPaint;
		
		//=============================
		//         尺寸参数
		//=============================
		private int deltaWidth;//宽度步进
		private int deltaHeight;//高度步进
		private int rows;//日期所占的行数(除去星期栏)
		private int firstRowHeight;//星期栏的高度
		
		//=============================
		public DayGridViewAttrs()
		{			
			rows = cr.getCalendarRows();
			
			Log.i("rows", "rows="+Integer.toString(rows));
			
			deltaWidth = WIDTH/7;
			firstRowHeight = (HEIGHT)/(2*rows);
			deltaHeight = (HEIGHT - firstRowHeight)/(rows-1);
			//=============================
			//  初始化画笔
			//=============================
			gridPaint = new Paint();
			gridPaint.setColor(Color.argb(255, 204, 204, 204));
			
			solarDayPaint = new Paint();
			solarDayPaint.setColor(Color.argb(255, 51, 51, 51));
			solarDayPaint.setAntiAlias(true);
			solarDayPaint.setTextSize(deltaWidth/2);
			
			lunarDayPaint = new Paint();
			lunarDayPaint.setColor(Color.argb(255, 204, 204, 204));
			lunarDayPaint.setAntiAlias(true);
			lunarDayPaint.setTextSize(deltaHeight/5);
			
			weekendPaint = new Paint();
			weekendPaint.setColor(Color.argb(255, 153, 0, 153));
			weekendPaint.setAntiAlias(true);
			weekendPaint.setTextSize(deltaWidth/2);
			
			weekTitlePaint = new Paint();
			weekTitlePaint.setColor(Color.WHITE);
			weekTitlePaint.setAntiAlias(true);
			weekTitlePaint.setTextSize(firstRowHeight/2);
			
			weekTitleBackgroundPaint = new Paint();
			weekTitleBackgroundPaint.setColor(Color.argb(255, 255, 102, 102));
			weekTitleBackgroundPaint.setTextSize(firstRowHeight/2);
			
			selectedDayPaint = new Paint();
			selectedDayPaint.setColor(Color.WHITE);
			selectedDayPaint.setAntiAlias(true);
			selectedDayPaint.setTextSize(deltaWidth/2);
			
			selectedDayBackgoundPaint = new Paint();
			selectedDayBackgoundPaint.setColor(Color.BLACK);
			selectedDayBackgoundPaint.setAntiAlias(true);
		}

		public Paint getGridPaint() {
			return gridPaint;
		}

		public void setGridPaint(Paint gridPaint) {
			this.gridPaint = gridPaint;
		}

		public Paint getSolarDayPaint() {
			return solarDayPaint;
		}

		public void setSolarDayPaint(Paint solarDayPaint) {
			this.solarDayPaint = solarDayPaint;
		}

		public Paint getLunarDayPaint() {
			return lunarDayPaint;
		}

		public void setLunarDayPaint(Paint lunarDayPaint) {
			this.lunarDayPaint = lunarDayPaint;
		}

		public Paint getWeekendPaint() {
			return weekendPaint;
		}

		public void setWeekendPaint(Paint weekendPaint) {
			this.weekendPaint = weekendPaint;
		}

		public Paint getWeekTitlePaint() {
			return weekTitlePaint;
		}

		public void setWeekTitlePaint(Paint weekTitlePaint) {
			this.weekTitlePaint = weekTitlePaint;
		}

		public Paint getSelectedDayPaint() {
			return selectedDayPaint;
		}

		public void setSelectedDayPaint(Paint selectedDayPaint) {
			this.selectedDayPaint = selectedDayPaint;
		}

		public Paint getSelectedDayBackgoundPaint() {
			return selectedDayBackgoundPaint;
		}

		public void setSelectedDayBackgoundPaint(Paint selectedDayBackgoundPaint) {
			this.selectedDayBackgoundPaint = selectedDayBackgoundPaint;
		}

		public int getDeltaWidth() {
			return deltaWidth;
		}

		public int getDeltaHeight() {
			return deltaHeight;
		}

		public int getRows() {
			return rows;
		}

		public int getFirstRowHeight() {
			return firstRowHeight;
		}

		public Paint getWeekTitleBackgroundPaint() {
			return weekTitleBackgroundPaint;
		}

		public void setWeekTitleBackgroundPaint(Paint weekTitleBackgroundPaint) {
			this.weekTitleBackgroundPaint = weekTitleBackgroundPaint;
		}	
	}

}
