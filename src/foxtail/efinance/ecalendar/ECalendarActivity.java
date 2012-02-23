package foxtail.efinance.ecalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;
import foxtail.efinance.R;
import foxtail.util.Calendar;
import foxtail.util.Date;

public class ECalendarActivity extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ecalendar);
	        
	        //��ȡ��ǰ����
	        Time t = new Time();
	        t.setToNow();
	        int year = t.year;
	        int month = t.month+1;
	        int day = t.monthDay;
	        
	        final Date nowDate = new Date(year, month, day);
	        final Date tempDate = new Date(nowDate);
	        
	        Calendar cr = new Calendar();
	        cr.setSolarDate(nowDate);
	        cr.Solar2Lunar();
	        
	        final TextView inFo = (TextView)findViewById(R.id.lunar_info_id);
	        inFo.setTextColor(Color.BLACK);
	        inFo.setText(cr.getGanZhiYear()+"��  ��Ф��"+cr.getShengXiao()+"�� ������ ũ��:"+cr.getLunarDate().getMonthInHanzi()+"�� "+cr.getLunarDate().getDayInHanzi());
	        
	        final TextView tv = (TextView)findViewById(R.id.year_month_text_id);
	        tv.setTextColor(Color.BLACK);
	        tv.setText(Integer.toString(nowDate.getYear())+"��"+Integer.toString(nowDate.getMonth())+"��");
	        
	        final DayGridView preDayTable = (DayGridView)findViewById(R.id.pre_days_grid_id);
	        preDayTable.setCalendarDate(nowDate);
	        
	        final DayGridView afterDayTable = (DayGridView)findViewById(R.id.after_days_grid_id);
	        afterDayTable.setCalendarDate(nowDate);
	        
	        final DayGridView DayTable[] = {preDayTable, afterDayTable};
	        
	        final ViewFlipper calFlipper = (ViewFlipper)findViewById(R.id.cal_flip_id);
	        //calFlipper.setInAnimation(this,R.layout.in);
	        //calFlipper.setOutAnimation(this,R.layout.out);
	        
	        Button dateDecButton = (Button)findViewById(R.id.month_dec_id);
	        Button dateIncButton = (Button)findViewById(R.id.month_inc_id);
	        
	        final Button nowDateButton = (Button)findViewById(R.id.today_id);
	        nowDateButton.setVisibility(View.GONE);
	        
	        class PreAfter
	        {
	        	int tag;//����ѭ����ʾviewflipper������������ʱ���ݷ����仯����Ҫ����ˢ�±ȷ�ҳ���ԭ���¡�
	        	public PreAfter()
	        	{
	        		this.tag = 0;
	        	}
	        }
	        
	        final PreAfter pa = new PreAfter();
	        
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
					calFlipper.setInAnimation(context,R.layout.ani_today_in);
			        calFlipper.setOutAnimation(context,R.layout.ani_today_out);
					calFlipper.showNext();
					//preDayTable.invalidate();
					
				}
	        	
	        }
	        nowDateButton.setOnClickListener(new NowDateOnClickListener(this));
	 } 
}
