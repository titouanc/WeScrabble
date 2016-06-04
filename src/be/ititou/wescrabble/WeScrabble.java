package be.ititou.wescrabble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.interfaces.WeScrabbleUI;
import be.ititou.wescrabble.ui.SwapLetterDialog;
import edu.vub.at.IAT;
import edu.vub.at.android.util.IATAndroid;
import edu.vub.at.objects.natives.NATText;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.WindowManager;
import android.widget.*;


public class WeScrabble extends Activity implements WeScrabbleUI {
	private static final int _ASSET_INSTALLER_ = 0;
	private static final String AT_LAUNCHER = "import /.wescrabble.wescrabble.start();";
	public static final int _ADD_WORD_ = 42;
	public static final int _SWAP_LETTER_ = 131;
	
	private String myName;
	private int myTeam;
	private IAT iat;
	private ATWeScrabble aws;
	private GridView table;
	private List<String> myLetters = new ArrayList<String>();
	private Map<String, List<String>> racks = new HashMap<String, List<String>>();
	private LooperThread lt;
	
	public static class Suggestion {
		public String word;
		public int col, row;
		public boolean horizontally;
		
		public Suggestion(String a, int b, int c, boolean d){
			word = a;
			row = b;
			col = c;
			horizontally = d;
		}
	}
	
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
	
	class LooperThread extends Thread {
		public Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (aws == null)
					return;
				switch (msg.what){
				case _ADD_WORD_:
					Suggestion s = (Suggestion) msg.obj;
					aws.addWord(s.word, s.row, s.col, s.horizontally);
					break;
				case _SWAP_LETTER_:
					String args[] = (String[]) msg.obj;
					break;
				}
			}
		};

		public void run() {
			Looper.prepare();
			Looper.loop();
		}
	}
	
	private StartIATTask iatRunner;
	
	/* == Activity == */
	@Override
	protected void onStop(){
		super.onStop();
		if (iat != null){
			iat.evalAndPrint("system.exit()");
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
		myTeam = intent.getIntExtra("myTeam", 0);
		this.setContentView(R.layout.activity_we_scrabble);
		TextView text = (TextView) findViewById(R.id.teamLabel);
		switch (myTeam){
			case WeScrabbleUI.TeamA:
				text.setText("Red Team");
				text.setTextColor(Color.RED);
				break;
			case WeScrabbleUI.TeamB:
				text.setText("Blue Team");
				text.setTextColor(Color.BLUE);
				break;
		}

		lt = new LooperThread();
		lt.start();
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
    	synchronized (racks){
    		menu.clear();
	        menu.add("My team racks...");
	        final WeScrabble ctx = this;
	        
	        for (final String player : racks.keySet()){
	        	final List<String> hisLetters = racks.get(player);
	        	String letters = join(", ", hisLetters);
	        	MenuItem item = menu.add(player + ": " + letters);
	        	item.setOnMenuItemClickListener(new OnMenuItemClickListener(){
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						final Dialog dialog = new SwapLetterDialog(ctx, myLetters, player, hisLetters); 
					    dialog.show();
						return true;
					}
	        	});
	        }
    	}
        return super.onPrepareOptionsMenu(menu);
    }
    
    public Handler getHandler(){
    	return lt.mHandler;
    }
    
    public ATWeScrabble getBackend(){
    	return aws;
    }

    /* == WeScrabbleUI == */
	@Override
	public String getMyName() {
		return myName;
	}
	
	@Override
	public int getMyTeam(){
		return myTeam;
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
		final WeScrabble me = this;
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				table = (GridView) findViewById(R.id.table);
		        table.setAdapter(new ScrabbleTableAdapter(me));
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
				
			}
		});
	}

	@Override
	public void setPlayerRack(String playerName, List<NATText> letters) {
		synchronized (racks) {
			racks.put(playerName, convertATTexts(letters));
		}
	}

	@Override
	public void removePlayerRack(String playerName) {
		synchronized (racks){
			racks.remove(playerName);
		}
	}

	@Override
	public void showMyLetters(final List<NATText> letters) {
		myLetters = convertATTexts(letters);
		final String finalText = join(", ", myLetters);
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
	
	private String join(String inBetween, Iterable<String> iter){
		int i = 0;
		String res = "";
		for (String s : iter){
			if (i > 0){
				res += inBetween;
			}
			res += s;
			i++;
		}
		return res;
	}
	
	private List<String> convertATTexts(List<NATText> texts){
		List<String> res = new ArrayList<String>();
		for (NATText nat : texts){
			String s = nat.toString().replaceAll("\"", "").toUpperCase();
			res.add(s);
		}
		return res;
	}
}
