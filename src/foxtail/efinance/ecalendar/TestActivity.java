package foxtail.efinance.ecalendar;


import foxtail.efinance.R;
import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		final DayGridView dayTable = (DayGridView)findViewById(R.id.test_grid_id);
		}
}
