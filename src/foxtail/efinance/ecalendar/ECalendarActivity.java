package foxtail.efinance.ecalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import foxtail.efinance.R;
import foxtail.util.Calendar;
import foxtail.util.Date;

public class ECalendarActivity extends Activity implements OnGestureListener{
	
	//获取当前日期
    Time t = new Time();
    
    
    final Date nowDate = new Date();
    final Date tempDate = new Date();
    
    
    
    class PreAfter
    {
    	int tag;//用于循环显示viewflipper避免日历翻滚时数据发生变化，主要日历刷新比翻页快的原因导致。
    	public PreAfter()
    	{
    		this.tag = 0;
    	}
    }
    
    final PreAfter pa = new PreAfter();
    
    Button nowDateButton;
    TextView inFo;
    TextView tv;
    DayGridView preDayTable;
    DayGridView afterDayTable;
    DayGridView DayTable[]; 
    ViewFlipper calFlipper;
    
    private GestureDetector mGestureDetector;
    
    Calendar cr = new Calendar();
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ecalendar);
	        
	        mGestureDetector = new GestureDetector(this);
	        
	        t.setToNow();
	        int year = t.year;
	        int month = t.month+1;
	        int day = t.monthDay;
	        nowDate.setDate(year, month, day);
	        tempDate.setDate(nowDate);
	        
	        cr.setSolarDate(nowDate);
	        cr.Solar2Lunar();
	        
	        nowDateButton = (Button)findViewById(R.id.today_id);
	        inFo = (TextView)findViewById(R.id.lunar_info_id);
	        tv = (TextView)findViewById(R.id.year_month_text_id);
	        preDayTable = (DayGridView)findViewById(R.id.pre_days_grid_id);
	        afterDayTable = (DayGridView)findViewById(R.id.after_days_grid_id); 
	        DayTable = new DayGridView[2];
	        DayTable[0] = preDayTable;
	        DayTable[1] = afterDayTable;
	        calFlipper = (ViewFlipper)findViewById(R.id.cal_flip_id);
	        
	        inFo.setTextColor(Color.BLACK);
	        inFo.setText(cr.getGanZhiYear()+"年  生肖【"+cr.getShengXiao()+"】 今天是 农历:"+cr.getLunarDate().getMonthInHanzi()+"月 "+cr.getLunarDate().getDayInHanzi());
	        
	        
	        tv.setTextColor(Color.BLACK);
	        tv.setText(Integer.toString(nowDate.getYear())+"年"+Integer.toString(nowDate.getMonth())+"月");
	        
	        
	        preDayTable.setCalendarDate(nowDate);
	        
	        
	        afterDayTable.setCalendarDate(nowDate);
	        
	        
	        Button dateDecButton = (Button)findViewById(R.id.month_dec_id);
	        Button dateIncButton = (Button)findViewById(R.id.month_inc_id);
	        
	        
	        nowDateButton.setVisibility(View.GONE);
	        
	        
	        
	        //==============================================================================
	        //各种监听事件啊
	        //==============================================================================
	        
	        class DecOnClickListener implements Button.OnClickListener
	        {
	        	private Context context;
	        	public DecOnClickListener(Context context)
	        	{
	        		this.context = context;
	        	}

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(tempDate.getMonth()-1 <=0)
					{
						tempDate.setYear(tempDate.getYear()-1);
						tempDate.setMonth(12);
						if(tempDate != nowDate)
						{
							nowDateButton.setVisibility(View.VISIBLE);
						}
						else
						{
							nowDateButton.setVisibility(View.GONE);
						}
					}
					else
					{
						tempDate.setMonth(tempDate.getMonth()-1);
						if(tempDate != nowDate)
						{
							nowDateButton.setVisibility(View.VISIBLE);
						}
						else
						{
							nowDateButton.setVisibility(View.GONE);
						}
					}
					
					tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
					
					if(pa.tag == 0)
					{
						pa.tag = 1;
					}
					else
					{
						pa.tag = 0;
					}
					DayTable[pa.tag].setCalendarDate(tempDate);
					calFlipper.setInAnimation(context,R.layout.ani_dec_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_dec_out);
					calFlipper.showPrevious();
					//preDayTable.invalidate();
					
				}
	        }
	        dateDecButton.setOnClickListener(new DecOnClickListener(this));
	        
	        
	        class IncOnClickListener implements Button.OnClickListener
	        {
	        	private Context context;
	        	public IncOnClickListener(Context context)
	        	{
	        		this.context = context;
	        	}
	        	
	        	public void onClick(View v) {
					// TODO Auto-generated method stub
					if(tempDate.getMonth() + 1 >=12 )
					{
						tempDate.setYear(tempDate.getYear()+1);
						tempDate.setMonth(1);
						if(tempDate != nowDate)
						{
							nowDateButton.setVisibility(View.VISIBLE);
						}
						else
						{
							nowDateButton.setVisibility(View.GONE);
						}
					}
					else
					{
						tempDate.setMonth(tempDate.getMonth()+1);
						if(tempDate != nowDate)
						{
							nowDateButton.setVisibility(View.VISIBLE);
						}
						else
						{
							nowDateButton.setVisibility(View.GONE);
						}
					}
					
					tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
					
					if(pa.tag == 0)
					{
						pa.tag = 1;
					}
					else
					{
						pa.tag = 0;
					}
					DayTable[pa.tag].setCalendarDate(tempDate);
					calFlipper.setInAnimation(context,R.layout.ani_inc_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_inc_out);
					calFlipper.showNext();
					//preDayTable.invalidate();
					
				}
	        	
	        }
	        dateIncButton.setOnClickListener(new IncOnClickListener(this));
	        
	        class NowDateOnClickListener implements Button.OnClickListener
	        {
	        	private Context context;
	        	public NowDateOnClickListener(Context context)
	        	{
	        		this.context = context;
	        	}
	        	public void onClick(View v) {
					// TODO Auto-generated method stub
					tempDate.setDate(nowDate);
					tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
					nowDateButton.setVisibility(View.GONE);
					if(pa.tag == 0)
					{
						pa.tag = 1;
					}
					else
					{
						pa.tag = 0;
					}
					DayTable[pa.tag].setCalendarDate(tempDate);					
					calFlipper.setInAnimation(context,R.layout.ani_today_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_today_out);
					calFlipper.showNext();
					//preDayTable.invalidate();
					
				}
	        	
	        }
	        nowDateButton.setOnClickListener(new NowDateOnClickListener(this));
	 }

	 //====================================================================================
	 //手势滑屏
	 //====================================================================================
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		
		if(arg0.getX()>arg1.getX())//向左移动
		{
			if(tempDate.getMonth() + 1 >=12 )
			{
				tempDate.setYear(tempDate.getYear()+1);
				tempDate.setMonth(1);
				if(tempDate != nowDate)
				{
					nowDateButton.setVisibility(View.VISIBLE);
				}
				else
				{
					nowDateButton.setVisibility(View.GONE);
				}
			}
			else
			{
				tempDate.setMonth(tempDate.getMonth()+1);
				if(tempDate != nowDate)
				{
					nowDateButton.setVisibility(View.VISIBLE);
				}
				else
				{
					nowDateButton.setVisibility(View.GONE);
				}
			}
			
			tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
			
			if(pa.tag == 0)
			{
				pa.tag = 1;
			}
			else
			{
				pa.tag = 0;
			}
			DayTable[pa.tag].setCalendarDate(tempDate);
			calFlipper.setInAnimation(this,R.layout.ani_inc_in);
	        calFlipper.setOutAnimation(this,R.layout.ani_inc_out);
			calFlipper.showNext();
			
		}
		else if(arg0.getX()<arg1.getX())
		{
			if(tempDate.getMonth()-1 <=0)
			{
				tempDate.setYear(tempDate.getYear()-1);
				tempDate.setMonth(12);
				if(tempDate != nowDate)
				{
					nowDateButton.setVisibility(View.VISIBLE);
				}
				else
				{
					nowDateButton.setVisibility(View.GONE);
				}
			}
			else
			{
				tempDate.setMonth(tempDate.getMonth()-1);
				if(tempDate != nowDate)
				{
					nowDateButton.setVisibility(View.VISIBLE);
				}
				else
				{
					nowDateButton.setVisibility(View.GONE);
				}
			}
			
			tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
			
			if(pa.tag == 0)
			{
				pa.tag = 1;
			}
			else
			{
				pa.tag = 0;
			}
			DayTable[pa.tag].setCalendarDate(tempDate);
			calFlipper.setInAnimation(this,R.layout.ani_dec_in);
	        calFlipper.setOutAnimation(this,R.layout.ani_dec_out);
			calFlipper.showPrevious();
		}
		else
		{
			return false;
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	} 
}
