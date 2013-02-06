package com.example.ascii;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PicChooser extends ListActivity {

	private ImageDataHelper picDataHelp;
	private SQLiteDatabase savedPictures;
	private Cursor picCursor;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
		
		picDataHelp=ImageDataHelper.getInstance(this);
		savedPictures=picDataHelp.getReadableDatabase();
		
		picCursor = savedPictures.query("pics", null, null, null, null, null, null);
		
		String[] columns = {ImageDataHelper.ID_COL, ImageDataHelper.CREATED_COL};
		int[] views = {R.id.picID, R.id.picName};
		
		SimpleCursorAdapter picAdapter = new SimpleCursorAdapter(this, R.layout.pic_item, picCursor, columns,
			    views, SimpleCursorAdapter.FLAG_AUTO_REQUERY);
		
		setListAdapter(picAdapter);
	}
	
	public void picChosen(View view){
		TextView pickedView = (TextView)view.findViewById(R.id.picID);
		String chosenID = (String)pickedView.getText();
		
		picDataHelp.close();
		savedPictures.close();
		picCursor.close();
		
		Intent backIntent = new Intent();
		backIntent.putExtra("pickedImg", chosenID);
		setResult(RESULT_OK, backIntent);
		finish();
	}
	
	@Override
	public void onDestroy() {
	    picCursor.close();
	    picDataHelp.close();
	    savedPictures.close();
	    super.onDestroy();
	
	}
}
