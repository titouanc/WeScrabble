package be.ititou.wescrabble.ui;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class ScrabbleCell extends TextView {
	private Boolean selected = false;
	private int x, y;
	private ATWeScrabble ws;

	private class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View target) {
			selected = ! selected;
			update();
		}
	}
	
	public ScrabbleCell(Context context, ATWeScrabble backend, int row, int col) {
		super(context);
		x = col;
		y = row;
		ws = backend;
		update();
		setOnClickListener(new ClickListener());
		setGravity(Gravity.CENTER_HORIZONTAL);
	}
	
	private int getColor(){
		if (selected){
			return Color.rgb(0xf6, 0xcc, 0x9e);
		}
		
		// Medians
		if (x == 7 || y == 7) {
			// Center
			if (x == y){
				return Color.rgb(0xff, 0x7e, 0x7e);
			} else {
				return Color.rgb(0xc5, 0xf6, 0x9e);
			}
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
