package be.ititou.wescrabble;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
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
		TextView text;
		if (convertView == null){
			text = new TextView(parent.getContext());
		} else {
			text = (TextView) convertView;
		}
		final int row = position/15;
		final int col = position%15;
		text.setText(ws.getLetterAt(row, col));
		text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ws.setLetterAt(row, col);
				notifyDataSetChanged();
			}
		});
		
		return text;
	}
}
