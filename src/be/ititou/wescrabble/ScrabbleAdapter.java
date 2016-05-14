package be.ititou.wescrabble;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.ui.ScrabbleCell;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScrabbleAdapter extends BaseAdapter {
	private ATWeScrabble ws;
	
	public ScrabbleAdapter(ATWeScrabble backend){
		super();
		ws = backend;
	}
	
	@Override
	public int getCount() {
		return 15*15;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int row = position/15;
		int col = position%15;
		return new ScrabbleCell(parent.getContext(), ws, row, col);
	}
}
