package edu.jsu.newssysclient.ui.main;

import edu.jsu.newssysclient.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * ¶©ÔÄ½çÃæ
 * @author zuo
 *
 */
public class SubscriptionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_subscription); 
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
