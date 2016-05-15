package be.ititou.wescrabble;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {
	private String myName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loadSavedPreferences();
		EditText text = (EditText) findViewById(R.id.myNameInput);
		text.setText(myName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToGame(View sender){
		EditText text = (EditText) findViewById(R.id.myNameInput);
		myName = text.getText().toString();
		savePreferences();
		
		Intent intent = new Intent(this, WeScrabble.class);
		intent.putExtra("myName", myName);
		startActivity(intent);
	}
	
	private void loadSavedPreferences(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		myName = prefs.getString("myName", "");
		System.out.println("Load myName=" + myName);
	}
	
	private void savePreferences(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor ed = prefs.edit();
		ed.putString("myName", myName);
		System.out.println("Save myName=" + myName);
		ed.commit();
	}
}
