package be.ititou.wescrabble;

import be.ititou.wescrabble.interfaces.ATWeScrabble;
import be.ititou.wescrabble.interfaces.TableObserver;
import be.ititou.wescrabble.ui.ScrabbleCell;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ScrabbleTableAdapter extends BaseAdapter implements TableObserver {
	private WeScrabble ws;
	
	public ScrabbleTableAdapter(WeScrabble backend){
		super();
		ws = backend;
		ws.getBackend().addObserver(this);
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
		if (convertView == null){
			return new ScrabbleCell(parent.getContext(), ws, row, col);
		} else {
			ScrabbleCell cell = (ScrabbleCell) convertView;
			cell.update();
			return cell;
		}
	}

	@Override
	public void update() {
		ws.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				notifyDataSetChanged();	
			}
		});
	}
}
