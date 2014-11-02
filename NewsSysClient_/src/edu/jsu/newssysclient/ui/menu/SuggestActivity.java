package edu.jsu.newssysclient.ui.menu;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 建议Activity
 * @author zuo
 *
 */
@EActivity(R.layout.suggest_layout)
public class SuggestActivity extends MyBaseActivity {

	@ViewById
	Button suggestSubmit;
	@ViewById
	EditText suggestContent;
	
	@Click
	public void suggestSubmit() {
		suggestContent.setText("");
		Toast.makeText(mContext, "您的建议已经提交，感谢您对我们的支持~", 0).show();
	}
}
