package com.example.ascii;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SpinnerClass extends Activity implements OnItemSelectedListener {

	Spinner spinner1;
	Spinner spinner2;
	String fontColor = "";
	String backGroundColor = "";
	String colorString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinnerforcolor);

		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
		        this, R.array.spinner_array_font, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
		        this, R.array.spinner_array_backGround, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner1.setAdapter(adapter1);
		spinner1.setSelected(false);
		
		spinner2.setAdapter(adapter2);
		spinner2.setSelected(false);

		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		switch (arg0.getId()) {
		case R.id.spinner1:
			int position = spinner1.getSelectedItemPosition();
			switch (position) {
			case 1:
				fontColor = "#ffffff";
				Toast.makeText(getApplicationContext(), "white",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				fontColor = "#0000FF";
				Toast.makeText(getApplicationContext(), "blue",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				fontColor = "#000000";
				Toast.makeText(getApplicationContext(), "black",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				fontColor = "#f80e19";
				Toast.makeText(getApplicationContext(), "red",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				fontColor = "#35bc10";
				Toast.makeText(getApplicationContext(), "green",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				fontColor = "#e24211";
				Toast.makeText(getApplicationContext(), "orange",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:
				fontColor = "#e5e809";
				Toast.makeText(getApplicationContext(), "yellow",
						Toast.LENGTH_SHORT).show();
				break;
			case 8:
				fontColor = "#9B30FF";
				Toast.makeText(getApplicationContext(), "purple",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				fontColor = "#DC143C";
				Toast.makeText(getApplicationContext(), "crimson",
						Toast.LENGTH_SHORT).show();
				break;
			case 10:
				fontColor = "#87CEFF";
				Toast.makeText(getApplicationContext(), "skyblue",
						Toast.LENGTH_SHORT).show();
				break;
			}

			break;
		case R.id.spinner2:
			int position1 = spinner2.getSelectedItemPosition();
			switch (position1) {
			case 1:
				backGroundColor = "#f80e19";
				Toast.makeText(getApplicationContext(), "red",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				backGroundColor = "#0000FF";
				Toast.makeText(getApplicationContext(), "blue",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				backGroundColor = "#000000";
				Toast.makeText(getApplicationContext(), "black",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				backGroundColor = "#ffffff";
				Toast.makeText(getApplicationContext(), "white",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				backGroundColor = "#35bc10";
				Toast.makeText(getApplicationContext(), "green",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				backGroundColor = "#e24211";
				Toast.makeText(getApplicationContext(), "orange",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:
				backGroundColor = "#e5e809";
				Toast.makeText(getApplicationContext(), "yellow",
						Toast.LENGTH_SHORT).show();
				break;
			case 8:
				backGroundColor = "#9B30FF";
				Toast.makeText(getApplicationContext(), "purple",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				backGroundColor = "#DC143C";
				Toast.makeText(getApplicationContext(), "crimson",
						Toast.LENGTH_SHORT).show();
				break;
			case 10:
				backGroundColor = "#87CEFF";
				Toast.makeText(getApplicationContext(), "skyblue",
						Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		}

	}

	public void setColor(View view) {
		if(backGroundColor=="" || fontColor==""){
			Toast.makeText(getApplicationContext(), "Choose both the Colors", Toast.LENGTH_LONG).show();
			return;
		} if(fontColor.equals(backGroundColor)){
			showDialog(this);
			return;
		}
		colorString = fontColor+" "+backGroundColor;
		Intent colorValueSent = new Intent();
		colorValueSent.putExtra("newColor", colorString);
		setResult(RESULT_OK, colorValueSent);
		finish();
	}
	
	public static void showDialog(Context context) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setTitle("Choose different color");
	    builder.setMessage("It's good to differentiate Background color from Font color");
	    builder.setPositiveButton("ok", new OnClickListener() {
	            public void onClick(DialogInterface dialog, int arg1) {
	                dialog.dismiss();
	            }});
	    builder.setCancelable(true);
	    builder.create().show();
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
