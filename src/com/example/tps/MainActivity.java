package com.example.ascii;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText textArea;
	private final int COLOR_REQUEST = 1;
	private final int LOAD_REQUEST = 2;
	private final int SAVE_SHARE = 3;
	private int currentPic = -1;
	private SharedPreferences asciiPrefs;
	private ImageDataHelper imgData;
	File bufDir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);

		textArea = (EditText) findViewById(R.id.ascii_text);

		imgData = ImageDataHelper.getInstance(this);

		Button loadBtn = (Button) findViewById(R.id.load_btn);
		loadBtn.setOnClickListener(this);

		Button saveASCIIBtn = (Button) findViewById(R.id.save_btn);
		saveASCIIBtn.setOnClickListener(this);

		Button newBtn = (Button) findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);

		Button deleteBtn = (Button) findViewById(R.id.delete_btn);
		deleteBtn.setOnClickListener(this);

		asciiPrefs = getSharedPreferences("AsciiPicPreferences", 0);
		String chosenColors = asciiPrefs.getString("colors", "");
		if (chosenColors.length() > 0) {
			String[] prefColors = chosenColors.split(" ");
			updateColors(prefColors[0], prefColors[1]);
		}

		Button saveImgBtn = (Button) findViewById(R.id.export_btn);
		saveImgBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.export_btn) {
			saveImg();
		} else if (v.getId() == R.id.load_btn) {
			Intent loadIntent = new Intent(this, PicChooser.class);
			this.startActivityForResult(loadIntent, LOAD_REQUEST);
		}// user has clicked new button
		else if (v.getId() == R.id.new_btn) {
			textArea.setText("");
			currentPic = -1;

		}
		// user has clicked save button
		else if (v.getId() == R.id.save_btn) {
			String enteredTxt = textArea.getText().toString();
			ContentValues picValues = new ContentValues();
			picValues.put(ImageDataHelper.ASCII_COL, enteredTxt);

			Date theDate = Calendar.getInstance().getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd_hh.mm.ss");
			String fileName = dateFormat.format(theDate);
			picValues.put(ImageDataHelper.CREATED_COL, fileName);

			SQLiteDatabase savedPicsDB = imgData.getWritableDatabase();

			if (currentPic < 0) {

				long insertNum = savedPicsDB.insert("pics", null, picValues);
				currentPic = (int) insertNum;

				if (insertNum >= 0)
					Toast.makeText(getApplicationContext(),
							"Image saved to database!", Toast.LENGTH_SHORT)
							.show();
			} else {
				int savedNum = savedPicsDB.update("pics", picValues,
						ImageDataHelper.ID_COL + "=?", new String[] { ""
								+ currentPic });
				if (savedNum > 0)
					Toast.makeText(getApplicationContext(),
							"Image saved to database!", Toast.LENGTH_SHORT)
							.show();
			}
			savedPicsDB.close();
			imgData.close();
		}
		// user has clicked delete button
		else if (v.getId() == R.id.delete_btn) {
			if (currentPic >= 0) {
				AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(
						this);
				confirmBuilder.setMessage("Delete the saved picture?");
				confirmBuilder.setCancelable(false);

				confirmBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								SQLiteDatabase savedPicsDB = imgData
										.getWritableDatabase();
								int deleteResult = savedPicsDB.delete("pics",
										ImageDataHelper.ID_COL + "=?",
										new String[] { "" + currentPic });

								if (deleteResult > 0)
									Toast.makeText(getApplicationContext(),
											"Picture deleted",
											Toast.LENGTH_SHORT).show();

								currentPic = -1;
								textArea.setText("");
								savedPicsDB.close();
								imgData.close();
							}
						});

				confirmBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert = confirmBuilder.create();
				alert.show();

			} else {
				// picture has not been loaded from database

				textArea.setText("");
			}
		}

	}

	private void saveImg() {
		Intent newShare = new Intent(this, ShareNsave.class);
		startActivityForResult(newShare, SAVE_SHARE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == COLOR_REQUEST) {
			if (resultCode == RESULT_OK) {
				String chosenTextColor = data.getStringExtra("textColor");
				String chosenBackColor = data.getStringExtra("backColor");
				updateColors(chosenTextColor, chosenBackColor);
				SharedPreferences.Editor prefsEd = asciiPrefs.edit();
				prefsEd.putString("colors", "" + chosenTextColor + " "
						+ chosenBackColor);
				prefsEd.commit();
			}
		} else if (requestCode == LOAD_REQUEST) {
			if (resultCode == RESULT_OK) {
				String pickedID = data.getStringExtra("pickedImg");
				currentPic = Integer.parseInt(pickedID);

				SQLiteDatabase savedPicsDB = imgData.getWritableDatabase();

				Cursor chosenCursor = savedPicsDB.query("pics",
						new String[] { ImageDataHelper.ASCII_COL },
						ImageDataHelper.ID_COL + "=?", new String[] { ""
								+ currentPic }, null, null, null);
				chosenCursor.moveToFirst();
				String savedChars = chosenCursor.getString(0);
				textArea.setText(savedChars);
				chosenCursor.close();
				savedPicsDB.close();
				imgData.close();
			}
		} else if (requestCode == SAVE_SHARE) {
			if (resultCode == RESULT_OK) {
				Boolean saveTrue, shareTrue, shareNsaveTrue = false;
				saveTrue = data.getBooleanExtra("saveTrue", false);
				shareTrue = data.getBooleanExtra("shareTrue", false);
				shareNsaveTrue = data.getBooleanExtra("shareNsaveTrue", false);

				if (saveTrue) {

					textArea.setCursorVisible(false);

					String state = Environment.getExternalStorageState();
					if (Environment.MEDIA_MOUNTED.equals(state)) {
						File picDir = new File(
								Environment.getExternalStorageDirectory()
										+ "/imagesTPS");

						if (!picDir.exists()) {
							picDir.mkdir();
						}
						textArea.setDrawingCacheEnabled(true);
						textArea.buildDrawingCache(true);
						Bitmap bitmap = textArea.getDrawingCache();
						textArea.setCursorVisible(true);
						Date date = new Date();
						String fileName = "img" + date.getTime() + ".png";
						File picFile = new File(picDir + "/" + fileName);
						try {

							picFile.createNewFile();
							FileOutputStream picOut = new FileOutputStream(
									picFile);
							boolean saved = bitmap.compress(CompressFormat.PNG,
									100, picOut);

							if (saved) {

								Toast.makeText(
										getApplicationContext(),
										"Image saved to  "
												+ "SDCard/imagesTPS Directory",
										Toast.LENGTH_LONG).show();

							} else {
								Toast.makeText(getApplicationContext(),
										"Whoops! File not saved.",
										Toast.LENGTH_SHORT).show();
							}
							picOut.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						textArea.destroyDrawingCache();
					} else {
						Toast.makeText(
								this.getApplicationContext(),
								"Sorry - you don't have an external"
										+ " storage directory available!",
								Toast.LENGTH_SHORT).show();
					}

				}

				if (shareTrue) {

					textArea.setCursorVisible(false);
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("image/*");

					String state1 = Environment.getExternalStorageState();
					if (Environment.MEDIA_MOUNTED.equals(state1)) {
						bufDir = new File(
								Environment.getExternalStorageDirectory()
										+ "/imagesTPS/buffered");

						if (!bufDir.exists()) {
							bufDir.mkdirs();
						}
						textArea.setDrawingCacheEnabled(true);
						textArea.buildDrawingCache(true);
						Bitmap bitmap = textArea.getDrawingCache();
						textArea.setCursorVisible(true);
						Date date = new Date();
						String fileName = "img" + date.getTime() + ".png";
						File picFile = new File(bufDir + "/" + fileName);
						try {

							picFile.createNewFile();
							FileOutputStream picOut = new FileOutputStream(
									picFile);
							boolean saved = bitmap.compress(CompressFormat.PNG,
									100, picOut);

							if (saved) {

								share.putExtra(
										Intent.EXTRA_STREAM,
										Uri.parse("file://"
												+ picFile.getAbsolutePath()));
								startActivity(Intent.createChooser(share,
										"Send picture using:"));
								

							} else {
								Toast.makeText(getApplicationContext(),
										"Whoops! File not saved.",
										Toast.LENGTH_SHORT).show();
							}
							picOut.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						textArea.destroyDrawingCache();
							
						
						
						
						
						
						
					} else {
						Toast.makeText(
								this.getApplicationContext(),
								"Sorry - you don't have an external"
										+ " storage directory available!",
								Toast.LENGTH_SHORT).show();
					}

				}

				if (shareNsaveTrue) {

					textArea.setCursorVisible(false);
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("image/*");

					String state1 = Environment.getExternalStorageState();
					if (Environment.MEDIA_MOUNTED.equals(state1)) {
						File picDir = new File(
								Environment.getExternalStorageDirectory()
										+ "/imagesTPS");

						if (!picDir.exists()) {
							picDir.mkdir();
						}
						textArea.setDrawingCacheEnabled(true);
						textArea.buildDrawingCache(true);
						Bitmap bitmap = textArea.getDrawingCache();
						textArea.setCursorVisible(true);
						Date date = new Date();
						String fileName = "img" + date.getTime() + ".png";
						File picFile = new File(picDir + "/" + fileName);
						try {

							picFile.createNewFile();
							FileOutputStream picOut = new FileOutputStream(
									picFile);
							boolean saved = bitmap.compress(CompressFormat.PNG,
									100, picOut);

							if (saved) {

								Toast.makeText(
										getApplicationContext(),
										"Image saved to  "
												+ "SDCard/imagesTPS directory!",
										Toast.LENGTH_LONG).show();

								share.putExtra(
										Intent.EXTRA_STREAM,
										Uri.parse("file://"
												+ picFile.getAbsolutePath()));
								startActivity(Intent.createChooser(share,
										"Send picture using:"));

							} else {
								Toast.makeText(getApplicationContext(),
										"Whoops! File not saved.",
										Toast.LENGTH_SHORT).show();
							}
							picOut.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						textArea.destroyDrawingCache();
					} else {
						Toast.makeText(
								this.getApplicationContext(),
								"Sorry - you don't have an external"
										+ " storage directory available!",
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		}
	}



	public boolean deleteFile(String uri)
	{
	     File currentFile = new File(uri);
	     File files[] = currentFile.listFiles();
	     for (int i = 0; i < files.length; i++)
	     {
	          if (files[i].isDirectory())
	          {
	              deleteFile(files[i].toString());
	          }
	          //no else, or you'll never get rid of this folder!
	          files[i].delete();
	     }
		return true;
	}
	private void updateColors(String tColor, String bColor) {
		// TODO Auto-generated method stub
		textArea.setTextColor(Color.parseColor(tColor));
		textArea.setBackgroundColor(Color.parseColor(bColor));
	}

	@Override
	public void onDestroy() {
		imgData.close();
		deleteFile(Environment.getExternalStorageDirectory()
				+ "/imagesTPS/buffered");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuItem item;
		item = menu.add("settings");

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		if (item.hasSubMenu() == false) {
			if (item.getTitle() == "settings") {
				Intent colorIntent = new Intent(this, ColorChooser.class);
				this.startActivityForResult(colorIntent, COLOR_REQUEST);
			}
		}

		return true;
	}

}
