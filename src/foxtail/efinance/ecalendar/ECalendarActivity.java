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
	
    private Time t;    
    private Date nowDate;
    private Date tempDate;
      
    class PreAfter
    {
    	int tag;//����ѭ����ʾviewflipper������������ʱ���ݷ����仯����Ҫ����ˢ�±ȷ�ҳ���ԭ���¡�
    	public PreAfter()
    	{
    		this.tag = 0;
    	}
    }
    
    private PreAfter pa;
    
    private Button nowDateButton;
    private Button dateDecButton;
    private Button dateIncButton;
    
    private TextView inFo;
    private TextView tv;
    private TextView selectedInfo;
    
    private DayGridView preDayTable;
    private DayGridView afterDayTable;
    private DayGridView DayTable[];
    private ViewFlipper calFlipper;
    
    private GestureDetector mGestureDetector;
    
    Calendar cr;
    
    private void initialAll()
    {
    	cr = new Calendar();
    	mGestureDetector = new GestureDetector(this);//����
        pa = new PreAfter();
        //----------------------------��ȡ��ǰʱ���ʼ��
        nowDate = new Date();
        tempDate = new Date();
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month+1;
        int day = t.monthDay;
        nowDate.setDate(year, month, day);
        tempDate.setDate(nowDate);
        //----------------------------ת����ũ��
        cr.setSolarDate(nowDate);
        cr.Solar2Lunar();
        //----------------------------���°�ť�����¸��°�ť��ʼ��
        nowDateButton = (Button)findViewById(R.id.today_id);
        dateDecButton = (Button)findViewById(R.id.month_dec_id);
        dateIncButton = (Button)findViewById(R.id.month_inc_id);
        //----------------------------ũ����ϸ��Ϣ���������������·���Ϣ�ı��ؼ��ĳ�ʼ��
        inFo = (TextView)findViewById(R.id.lunar_info_id);
        tv = (TextView)findViewById(R.id.year_month_text_id);
        selectedInfo = (TextView)findViewById(R.id.selected_info_id);
        //----------------------------����Ч�����������DayGridView�ؼ���ViewFlipper
        preDayTable = (DayGridView)findViewById(R.id.pre_days_grid_id);
        afterDayTable = (DayGridView)findViewById(R.id.after_days_grid_id); 
        DayTable = new DayGridView[2];
        DayTable[0] = preDayTable;
        DayTable[1] = afterDayTable;
        calFlipper = (ViewFlipper)findViewById(R.id.cal_flip_id);
    }
    
    private boolean isToday()
    {
    	if((tempDate.getYear() == nowDate.getYear()) && (tempDate.getMonth() == nowDate.getMonth()))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ecalendar);      
	        this.initialAll();
	         
	        //inFo.setTextColor(Color.BLACK);
	        inFo.setText(cr.getGanZhiYear()+"��  ��Ф��"+cr.getShengXiao()+"�� ������ ũ��:"+cr.getLunarDate().getMonthInHanzi()+"�� "+cr.getLunarDate().getDayInHanzi());
	        //tv.setTextColor(Color.BLACK);
	        tv.setText(Integer.toString(nowDate.getYear())+"��"+Integer.toString(nowDate.getMonth())+"��");
	        tempDate.setDay(1);
	        cr.setSolarDate(tempDate);
	        cr.Solar2Lunar();
	        selectedInfo.setText("ũ��:"+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi());
	               
	        preDayTable.setCalendarDate(nowDate);      
	        afterDayTable.setCalendarDate(nowDate);
	        nowDateButton.setVisibility(View.GONE);
	                 
	        //==============================================================================
	        //���ּ����¼���
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
					
					tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
					
					if(pa.tag == 0)
					{
						pa.tag = 1;
					}
					else
					{
						pa.tag = 0;
					}
					DayTable[pa.tag].setCalendarDate(tempDate);
					tempDate.setDay(1);
					cr.setSolarDate(tempDate);
			    	cr.Solar2Lunar();
			    	selectedInfo.setText("ũ��:"+(cr.getLunarDate().getLeap()!=0?"��":"")+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi()+(cr.getJieQi()=="noInfo" ? "":cr.getJieQi()));
					calFlipper.setInAnimation(context,R.layout.ani_dec_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_dec_out);
					calFlipper.showPrevious();
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
					
					tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
					
					if(pa.tag == 0)
					{
						pa.tag = 1;
					}
					else
					{
						pa.tag = 0;
					}
					DayTable[pa.tag].setCalendarDate(tempDate);
					tempDate.setDay(1);
					cr.setSolarDate(tempDate);
			    	cr.Solar2Lunar();
			    	selectedInfo.setText("ũ��:"+(cr.getLunarDate().getLeap()!=0?"��":"")+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi()+(cr.getJieQi()=="noInfo" ? "":cr.getJieQi()));
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
					tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
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
					tempDate.setDay(1);
					cr.setSolarDate(tempDate);
			    	cr.Solar2Lunar();
			    	selectedInfo.setText("ũ��:"+(cr.getLunarDate().getLeap()!=0?"��":"")+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi()+(cr.getJieQi()=="noInfo" ? "":cr.getJieQi()));
					calFlipper.setInAnimation(context,R.layout.ani_today_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_today_out);
					calFlipper.showNext();
					//preDayTable.invalidate();
					
				}
	        	
	        }
	        nowDateButton.setOnClickListener(new NowDateOnClickListener(this));
	 }
	 

	 //====================================================================================
	 //���ƻ���
	 //====================================================================================
	 public boolean onTouchEvent (MotionEvent event)
	{	
		 cr.setSolarDate(DayTable[pa.tag].getSelectedDate());
		 cr.Solar2Lunar();
		 selectedInfo.setText("ũ��:"+(cr.getLunarDate().getLeap()!=0?"��":"")+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi()+(cr.getJieQi()=="noInfo" ? "":cr.getJieQi()));
		return this.mGestureDetector.onTouchEvent(event);
	}
	 
	/* public boolean onInterceptTouchEvent(MotionEvent event) 
	{  
		 Log.i("outView", "OUT");
		 this.mGestureDetector.onTouchEvent(event);
		 this.preDayTable.onTouchEvent(event);
		 this.afterDayTable.onTouchEvent(event);
         return false;
	} */
	
	
	 public boolean dispatchTouchEvent(MotionEvent event)
	 {
		 this.onTouchEvent(event);
		 return super.dispatchTouchEvent(event);
	 }
	 
	@Override
	public boolean onDown(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	
	private final int flipSize = 90;
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		if(arg0.getX()-arg1.getX()>flipSize)//�����ƶ�
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
			
			tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
			
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
		else if(arg0.getX()-arg1.getX()<-flipSize)
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
			
			tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
			
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
		else if((arg0.getX()-arg1.getX()<flipSize) &&
				(arg0.getX()-arg1.getX()>-flipSize)&&
				(arg0.getY()-arg1.getY()>2*flipSize)&& !this.isToday()
				)
		{
			tempDate.setDate(nowDate);
			tv.setText(Integer.toString(tempDate.getYear())+"��"+Integer.toString(tempDate.getMonth())+"��");
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
			calFlipper.setInAnimation(this,R.layout.ani_today_in);
	        calFlipper.setOutAnimation(this,R.layout.ani_today_out);
			calFlipper.showNext();
		}
		else
		{
			return false;
		}
		tempDate.setDay(1);
		cr.setSolarDate(tempDate);
    	cr.Solar2Lunar();
    	selectedInfo.setText("ũ��:"+(cr.getLunarDate().getLeap()!=0?"��":"")+cr.getLunarDate().getMonthInHanzi()+"��"+cr.getLunarDate().getDayInHanzi()+(cr.getJieQi()=="noInfo" ? "":cr.getJieQi()));
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
