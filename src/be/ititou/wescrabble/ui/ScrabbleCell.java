package be.ititou.wescrabble.ui;

import be.ititou.wescrabble.R;
import be.ititou.wescrabble.WeScrabble;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ScrabbleCell extends TextView {
	private int x, y;
	private WeScrabble ws;
		
	private class AddWordDialog extends Dialog {
		void addWord(String word, Boolean horizontally){
			WeScrabble.Suggestion sugg = new WeScrabble.Suggestion(word, y, x, horizontally);
			Handler h = ws.getHandler();
			h.sendMessage(Message.obtain(h, WeScrabble._ADD_WORD_, sugg));
		}
		
		private class OnClickListener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				
				switch (id){
				case R.id.RedTeam:
					addWord(getWord(), true);
					break;
				case R.id.confirmVerticalButton:
					addWord(getWord(), false);
					break;
				}
				
				dismiss();
			}
		}
		
		public AddWordDialog(Context context) {
			super(context);
			setTitle("Add a word...");
			setContentView(R.layout.dialog_add_word);
			
			OnClickListener listener = new OnClickListener();
			findViewById(R.id.RedTeam).setOnClickListener(listener);
			findViewById(R.id.confirmVerticalButton).setOnClickListener(listener);
			findViewById(R.id.cancelButton).setOnClickListener(listener);
		}
		
		private String getWord(){
			EditText text = (EditText) findViewById(R.id.wordProposition);
			return text.getText().toString();
		}
	}
	
	public ScrabbleCell(Context context, WeScrabble backend, int row, int col) {
		super(context);
		x = col;
		y = row;
		ws = backend;
		update();
		setGravity(Gravity.CENTER_HORIZONTAL);
		setTypeface(null, Typeface.BOLD);
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View target) {
				final Dialog dialog = new AddWordDialog(target.getContext()); 
			    dialog.show();
			}
		});
	}
	
	private int getColor(){
		// Center
		if (x == 7 && y == 7) {
			return Color.rgb(0xff, 0x7e, 0x7e);
		}
		// Diagonals
		if (x == y || x == 14-y){
			return Color.rgb(0x9e, 0xce, 0xf6);
		}
		return Color.WHITE;
	}
	
	public void update(){
		setText(ws.getBackend().getLetterAt(y, x).toUpperCase());
		setBackgroundColor(this.getColor());
	}
}
