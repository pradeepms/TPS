package com.example.ascii;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class ImageDataHelper extends SQLiteOpenHelper {

	private ImageDataHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub

	}

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "asciipics.db";
	public static final String ID_COL = BaseColumns._ID;
	public static final String TABLE_NAME = "pics";
	public static final String ASCII_COL = "ascii_text";
	public static final String CREATED_COL = "pic_creation";

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ " (" + ID_COL + " INTEGER " + "PRIMARY KEY AUTOINCREMENT, "
			+ ASCII_COL + " TEXT, " + CREATED_COL + " TEXT);";

	private static ImageDataHelper dbInstance;
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS pics");
	    db.execSQL("VACUUM");
	    onCreate(db);
	}
	
	public static ImageDataHelper getInstance(Context context) {
	    if (dbInstance == null)
	        dbInstance = new ImageDataHelper(context.getApplicationContext());
	        return dbInstance;
	}

}
