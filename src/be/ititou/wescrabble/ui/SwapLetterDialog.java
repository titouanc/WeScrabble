package be.ititou.wescrabble.ui;

import java.util.List;

import be.ititou.wescrabble.R;
import be.ititou.wescrabble.WeScrabble;
import be.ititou.wescrabble.interfaces.ATWeScrabble;
import android.app.Dialog;
import android.view.View;
import android.widget.*;

public class SwapLetterDialog extends Dialog {
	public SwapLetterDialog(WeScrabble context, List<String> myLetters, final String player, List<String> hisLetters) {
		super(context);
		setContentView(R.layout.dialog_swap_letter);
		setTitle("Swap a letter with " + player);
		
		final Spinner myMenu = (Spinner) findViewById(R.id.myLetterSwap);
		myMenu.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, myLetters));
		
		final Spinner hisMenu = (Spinner) findViewById(R.id.otherLetterSwap);
		hisMenu.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, hisLetters));
		
		final ATWeScrabble backend = context.getBackend();
		
		Button but = (Button) findViewById(R.id.validateSwap);
		but.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String myLetter = (String) ((TextView) myMenu.getSelectedView()).getText();
				String hisLetter = (String) ((TextView) hisMenu.getSelectedView()).getText();
				backend.swapLetter(myLetter, player, hisLetter);
				dismiss();
			}
		});
	}
}
