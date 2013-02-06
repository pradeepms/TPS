package com.example.ascii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class ShareNsave extends Activity {
	CheckBox save;
	CheckBox share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_n_save);
		save = (CheckBox) findViewById(R.id.saveCB);
		share = (CheckBox) findViewById(R.id.shareCB);
		save.setChecked(false);
		share.setChecked(false);
	}
	public void goClicked(View view){
		Intent shareNsave = new Intent();
		if(!(save.isChecked()) && !(share.isChecked())){
			Toast.makeText(getApplicationContext(), "Select atleast one box", Toast.LENGTH_SHORT).show();
		}
		if(save.isChecked() && share.isChecked()){
			shareNsave.putExtra("shareNsaveTrue", true);
			setResult(RESULT_OK, shareNsave);
			finish();
			
		}
		else if(share.isChecked() ){
			shareNsave.putExtra("shareTrue", true);
			setResult(RESULT_OK, shareNsave);
			finish();
		}
		else if(save.isChecked() ){
				shareNsave.putExtra("saveTrue", true);
				setResult(RESULT_OK, shareNsave);
				finish();
		}
	}
}
