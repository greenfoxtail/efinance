package foxtail.efinance;

import foxtail.efinance.ecalendar.ECalendarActivity;
import foxtail.efinance.ecalendar.TestActivity;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class EFinanceActivity extends TabActivity implements OnCheckedChangeListener {
    /** Called when the activity is first created. */
	
	private TabHost mHost;
	private RadioGroup radioGroup;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottom_tab);
        
        mHost = this.getTabHost();
        
        mHost.addTab(mHost.newTabSpec("CALENDAR").setIndicator("CALENDAR").setContent(new Intent(this,ECalendarActivity.class)));
        mHost.addTab(mHost.newTabSpec("QUERY").setIndicator("QUERY").setContent(new Intent(this,ECalendarActivity.class)));
        mHost.addTab(mHost.newTabSpec("ANALYSIS").setIndicator("ANALYSIS").setContent(new Intent(this,ECalendarActivity.class)));
        mHost.addTab(mHost.newTabSpec("CALCULATOR").setIndicator("CALCULATOR").setContent(new Intent(this,ECalendarActivity.class)));
        mHost.addTab(mHost.newTabSpec("SETTING").setIndicator("SETTING").setContent(new Intent(this,TestActivity.class)));
                
        radioGroup = (RadioGroup)findViewById(R.id.bottom_tab_radio);
        radioGroup.setOnCheckedChangeListener(this);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId)
		{
		case R.id.tab_calendar:
			mHost.setCurrentTabByTag("CALENDAR");
			break;
		case R.id.tab_query:
			mHost.setCurrentTabByTag("QUERY");
			break;
		case R.id.tab_analysis:
			mHost.setCurrentTabByTag("ANALYSIS");
			break;
		case R.id.tab_calculator:
			mHost.setCurrentTabByTag("CALCULATOR");
			break;
		case R.id.tab_setting:
			mHost.setCurrentTabByTag("SETTING");
			break;
		}		
	}
}