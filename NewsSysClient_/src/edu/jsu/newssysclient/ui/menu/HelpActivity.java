package edu.jsu.newssysclient.ui.menu;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import android.app.Activity;
import android.text.Html;
import android.widget.TextView;

/**
 * ∞Ô÷˙Activity
 * @author zuo
 *
 */
@EActivity(R.layout.help_layout)
public class HelpActivity extends MyBaseActivity {

	@ViewById
	TextView helpContent;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		helpContent.setText(Html.fromHtml("http://developer.android.com/"));
	}
}
