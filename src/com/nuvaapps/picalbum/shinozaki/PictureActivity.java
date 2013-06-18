package com.nuvaapps.picalbum.shinozaki;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.nuvaapps.picalbum.shinozaki.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PictureActivity extends Activity {
	private int PICTURE_COUNT;
	ImageView imageView;
	SharedPreferences prefs;
	Bitmap currentImage;
	int pictureId = 1;
	String pictureURL = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		PICTURE_COUNT = Integer.parseInt(getResources().getString(R.string.pictureCount));
		pictureURL = getResources().getString(R.string.pictureURL);
		imageView = (ImageView)findViewById(R.id.imageView1);
		Toast.makeText(this, R.string.toastPictureStart, Toast.LENGTH_LONG ).show();
		prefs = getSharedPreferences(getResources().getString(R.string.appCode), 0);
		pictureId = prefs.getInt("pictureId", 1);
		loadPicture();
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.RelativeLayout1);
		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(event.getX() < v.getWidth() /2)
						pictureId--;
					else
						pictureId++;
					if(pictureId < 1) pictureId = PICTURE_COUNT;
					if(pictureId > PICTURE_COUNT) pictureId = 1;
					savePictureId();
					loadPicture();
					//Toast.makeText(v.getContext(), String.valueOf(pictureId),  Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
	}
	
	private void savePictureId(){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("pictureId", pictureId);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.picture, menu);
		return true;
	}
	
	private void loadPicture(){
		URL url;
		try {
			url = new URL(pictureURL + "?pictureId=" + pictureId);
			new DownloadPictureTask().execute(url);
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Cannot download image",  Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			String result = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, getResources().getString(R.string.pictureSaveName) + pictureId , getResources().getString(R.string.pictureSaveName2) + pictureId);
			if(result == null)
				Toast.makeText(this, "Cannot save image",  Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Image saved to gallery",  Toast.LENGTH_LONG).show();
			break;
		case R.id.exit:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.rate:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.appMarketURL))));
			break;
		case R.id.cancel:
		default:
			break;
		}
		return true;
	}
	
	private class DownloadPictureTask extends AsyncTask<URL, Void, Bitmap>{

		private ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
        @Override
        protected void onPreExecute() {
        	bar.setVisibility(View.VISIBLE);
        }
		@Override
		protected Bitmap doInBackground(URL... params) {
			final Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						imageView.setImageBitmap(bitmap);
					}
				});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			bar.setVisibility(View.INVISIBLE);
		}
	}
}
