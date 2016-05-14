package be.ititou.wescrabble.ui;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ScrabbleCell extends TextView {
	private int x, y;
	private ATWeScrabble ws;
	
	private class AddWordDialog extends Dialog {
		private class OnClickListener implements View.OnClickListener {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				Boolean r = true;
				
				switch (id){
				case R.id.confirmHorizontalButton:
					r = ws.addWord(getWord(), y, x, true);
					break;
				case R.id.confirmVerticalButton:
					r = ws.addWord(getWord(), y, x, false);
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
			findViewById(R.id.confirmHorizontalButton).setOnClickListener(listener);
			findViewById(R.id.confirmVerticalButton).setOnClickListener(listener);
			findViewById(R.id.cancelButton).setOnClickListener(listener);
		}
		
		private String getWord(){
			EditText text = (EditText) findViewById(R.id.wordProposition);
			return text.getText().toString();
		}
	}
	
	public ScrabbleCell(Context context, ATWeScrabble backend, int row, int col) {
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
		// Medians
		if (x == 7 || y == 7) {
			// Center
			if (x == y){
				return Color.rgb(0xff, 0x7e, 0x7e);
			}
			return Color.rgb(0xc5, 0xf6, 0x9e);
		} 
		// Diagonals
		if (x == y || x == 14-y){
			return Color.rgb(0x9e, 0xce, 0xf6);
		}
		return Color.WHITE;
	}
	
	public void update(){
		setText(ws.getLetterAt(y, x));
		setBackgroundColor(this.getColor());
	}
}
