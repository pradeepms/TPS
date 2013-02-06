package com.example.ascii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ColorChooser extends Activity {
	int COLOR_SENT = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_choice);
	}

	public void setColors(View view) {

		if (view.getId() == R.id.colors_3) {
			Intent newOne = new Intent(this, SpinnerClass.class);

			this.startActivityForResult(newOne, COLOR_SENT);
		} else {
			String tagInfo = (String) view.getTag();
			Toast.makeText(getApplicationContext(), tagInfo, Toast.LENGTH_SHORT)
					.show();
			String[] tagColors = tagInfo.split(" ");
			Toast.makeText(getApplicationContext(), tagColors[0],
					Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), tagColors[1],
					Toast.LENGTH_SHORT).show();
			Intent backIntent = new Intent();
			backIntent.putExtra("textColor", tagColors[0]);
			backIntent.putExtra("backColor", tagColors[1]);
			setResult(RESULT_OK, backIntent);
			finish();
		}
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == COLOR_SENT) {
			if (resultCode == RESULT_OK) {
				String tagInfo = data.getStringExtra("newColor");
				Toast.makeText(getApplicationContext(),
						"Inside onActivtyResult" + tagInfo, Toast.LENGTH_SHORT)
						.show();
				String[] tagColors = tagInfo.split(" ");
				Intent backIntent = new Intent();
				backIntent.putExtra("textColor", tagColors[0]);
				backIntent.putExtra("backColor", tagColors[1]);
				setResult(RESULT_OK, backIntent);
				finish();
			}
		}
	}

}
