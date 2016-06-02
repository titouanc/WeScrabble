package be.ititou.wescrabble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.interfaces.WeScrabbleUI;
import edu.vub.at.IAT;
import edu.vub.at.android.util.IATAndroid;
import edu.vub.at.objects.natives.NATText;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.WindowManager;
import android.widget.*;


public class WeScrabble extends Activity implements WeScrabbleUI {
	private static final int _ASSET_INSTALLER_ = 0;
	private static final String AT_LAUNCHER = "import /.wescrabble.wescrabble.start();";
	
	private String myName;
	private IAT iat;
	private ATWeScrabble aws;
	private GridView table;
	private Map<String, String> racks = new HashMap<String, String>();
	
	public class StartIATTask extends AsyncTask<Void, String, Void> {
		private ProgressDialog pd;

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			pd.setMessage(values[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(WeScrabble.this, "WeScrabble", "Starting AmbientTalk");
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pd.dismiss();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				iat = IATAndroid.create(WeScrabble.this);
				this.publishProgress("Loading weScrabble code");
				iat.evalAndPrint(AT_LAUNCHER, System.err);
			} catch (Exception e) {
				Log.e("AmbientTalk", "Could not start IAT", e);
			}
			return null;
		}
	}
	
	private StartIATTask iatRunner;
	
	/* == Activity == */
	@Override
	protected void onStop(){
		super.onStop();
		if (iatRunner != null){
			iatRunner.cancel(true);
		}
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (iat == null) {
			Intent i = new Intent(this, WeScrabbleAssetInstaller.class);
			startActivityForResult(i, _ASSET_INSTALLER_);
		}
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_we_scrabble);
    }
	
	@Override
	protected void onStart(){
		super.onStart();
		Intent intent = getIntent();
		myName = intent.getStringExtra("myName");
		this.setContentView(R.layout.activity_we_scrabble);
	}
	
	@Override
	protected void onActivityResult(int rq, int res, Intent data) {
    	switch (rq){
    	case _ASSET_INSTALLER_:
    		if (res == Activity.RESULT_OK){
    			iatRunner = new StartIATTask();
    			iatRunner.execute((Void) null);
    		}
    	}
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add("My team racks...");
        for (String player : racks.keySet()){
        	String letters = racks.get(player);
        	MenuItem item = menu.add(player + ": " + letters);
        	item.setOnMenuItemClickListener(new OnMenuItemClickListener(){
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					showMessage("Selected " + item.toString());
					return true;
				}
        	});
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /* == WeScrabbleUI == */
	@Override
	public String getMyName() {
		return myName;
	}

	@Override
	public void showMessage(final String title) {
		final Context ctx = this;
		this.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				Toast toast = Toast.makeText(ctx, title, Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}
	
	@Override
	public void setBackend(ATWeScrabble backend){
		aws = backend;
		final Activity owner = this;
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				table = (GridView) findViewById(R.id.table);
		        table.setAdapter(new ScrabbleTableAdapter(aws, owner));
			}
		});
	}

	@Override
	public void setAppTitle(final String title) {
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				setTitle(title);
			}
		});
	}

	@Override
	public void setTeam(final int team) {
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				TextView text = (TextView) findViewById(R.id.teamLabel);
				switch (team){
				case WeScrabbleUI.TeamA:
					text.setText("Team A");
					text.setTextColor(Color.RED);
					break;
				case WeScrabbleUI.TeamB:
					text.setText("Team B");
					text.setTextColor(Color.BLUE);
					break;
				}
			}
		});
	}
	
	private static String joinLetters(List<NATText> letters){
		String text = "";
		int i = 0;
		for (NATText nat : letters){
			if (i > 0){
				text += ", ";
			}
			text += nat.toString().replaceAll("\"", "").toUpperCase();
			i++;
		}
		return text;
	}

	@Override
	public void showMyLetters(final List<NATText> letters) {
		final String finalText = joinLetters(letters);
		final TextView myLetters = (TextView) findViewById(R.id.myLetters);
		if (myLetters != null){
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					myLetters.setText(finalText);
					myLetters.invalidate();
				}
			});
		}
	}

	@Override
	public void setPlayerRack(String playerName, List<NATText> letters) {
		synchronized (racks) {
			racks.put(playerName, joinLetters(letters));
		}
	}

	@Override
	public void removePlayerRack(String playerName) {
		synchronized (racks){
			racks.remove(playerName);
		}
	}
}
