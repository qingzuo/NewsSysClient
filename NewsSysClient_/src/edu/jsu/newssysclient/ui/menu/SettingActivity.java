package edu.jsu.newssysclient.ui.menu;

import org.androidannotations.annotations.EActivity;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 设置Activity
 * @author zuo
 *
 */
public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener{
	EditTextPreference serverIp;
	String serverIpKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		
		serverIpKey = StringUtil.getString(R.string.prekey_serverip);
		serverIp = (EditTextPreference) findPreference(serverIpKey);
		
		serverIp.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		AppLogger.i("onPreferenceChange");
		AppLogger.i(preference.getKey());
		if (preference.getKey().equals(serverIpKey)) {
			Toast.makeText(this, "请重新启动应用~", Toast.LENGTH_LONG).show();
//			IntentUtil.restartApp(this);
//			finish();
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
}
