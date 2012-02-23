package foxtail.efinance.ecalendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import foxtail.efinance.R;
import foxtail.util.Calendar;
import foxtail.util.Date;

public class ECalendarActivity extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.ecalendar);
	        
	        //获取当前日期
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
	        inFo.setText(cr.getGanZhiYear()+"年  生肖【"+cr.getShengXiao()+"】 今天是 农历:"+cr.getLunarDate().getMonthInHanzi()+"月 "+cr.getLunarDate().getDayInHanzi());
	        
	        final TextView tv = (TextView)findViewById(R.id.year_month_text_id);
	        tv.setTextColor(Color.BLACK);
	        tv.setText(Integer.toString(nowDate.getYear())+"年"+Integer.toString(nowDate.getMonth())+"月");
	        
	        final DayGridView dayTable = (DayGridView)findViewById(R.id.days_grid_id);
	        dayTable.setCalendarDate(nowDate);
	        
	        Button dateDecButton = (Button)findViewById(R.id.month_dec_id);
	        Button dateIncButton = (Button)findViewById(R.id.month_inc_id);
	        
	        final Button nowDateButton = (Button)findViewById(R.id.today_id);
	        nowDateButton.setVisibility(View.GONE);
	        
	        dateDecButton.setOnClickListener(new Button.OnClickListener()
	        {
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
					
					dayTable.setCalendarDate(tempDate);
					dayTable.invalidate();
					
				}
	        });
	        
	        dateIncButton.setOnClickListener(new Button.OnClickListener()
	        {
				@Override
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
					
					dayTable.setCalendarDate(tempDate);
					dayTable.invalidate();
					
				}
	        });
	        
	        nowDateButton.setOnClickListener(new Button.OnClickListener()
	        {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tempDate.setDate(nowDate);
					tv.setText(Integer.toString(tempDate.getYear())+"年"+Integer.toString(tempDate.getMonth())+"月");
					nowDateButton.setVisibility(View.GONE);
					dayTable.setCalendarDate(tempDate);
					dayTable.invalidate();
					
				}
	        });
	 } 
}
