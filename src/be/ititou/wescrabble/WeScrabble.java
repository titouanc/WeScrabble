package be.ititou.wescrabble;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.interfaces.WeScrabbleUI;
import edu.vub.at.IAT;
import edu.vub.at.android.util.IATAndroid;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;


public class WeScrabble extends Activity implements WeScrabbleUI {
	private static final int _ASSET_INSTALLER_ = 0;
	private static final String AT_LAUNCHER = "import /.wescrabble.wescrabble.start();";
	
	private String myName;
	private IAT iat;
	private ATWeScrabble aws;
	private GridView table;
	
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
    			new StartIATTask().execute((Void) null);
    		}
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.we_scrabble, menu);
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

	@Override
	public String getMyName() {
		return myName;
	}

	@Override
	public void setMessage(final String title) {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				TextView view = (TextView) findViewById(R.id.title);
				view.setText(title.toCharArray(), 0, title.length());
			}
		});
	}
	
	@Override
	public void setBackend(ATWeScrabble backend){
		aws = backend;
		final Activity owner = this;
		this.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				table = (GridView) findViewById(R.id.table);
		        table.setAdapter(new ScrabbleAdapter(aws, owner));
			}
		});
	}

	@Override
	public void setAppTitle(final String title) {
		this.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				setTitle(title);
			}
		});
	}
}
