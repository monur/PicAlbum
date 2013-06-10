package com.nuvaapps.picalbum.shinozaki;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
import android.widget.LinearLayout;
import android.widget.Toast;

public class PictureActivity extends Activity {
	private static final int PICTURE_COUNT = 632;
	ImageView imageView;
	SharedPreferences prefs;
	Bitmap currentImage;
	int pictureId = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		imageView = (ImageView)findViewById(R.id.imageView1);
		Toast.makeText(this, "Tap right for next picture, left for previous", Toast.LENGTH_LONG ).show();
		prefs = getSharedPreferences("PicAlbum", 0);
		pictureId = prefs.getInt("pictureId", 1);
		loadPicture();
		LinearLayout layout = (LinearLayout)findViewById(R.id.LinearLayout1);
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
			url = new URL("http://nuvaapps.zz.mu/pictures/getPicture.php?pictureId=" + pictureId);
			Bitmap currentImage = new DownloadPictureTask().execute(url).get();
			if(currentImage != null){
				imageView.setImageBitmap(currentImage);
				Toast.makeText(this, String.valueOf(pictureId),  Toast.LENGTH_SHORT).show();
			}
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Cannot download image",  Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (InterruptedException e) {
			Toast.makeText(this, "Cannot download image",  Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast.makeText(this, "Cannot download image",  Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			String result = MediaStore.Images.Media.insertImage(getContentResolver(), currentImage, "ai_shinozaki" + pictureId , "Ai Shinozaki " + pictureId);
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
			//TODO app adý ne olacak la?
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nuvaapps.picalbum")));
			break;
		case R.id.cancel:
		default:
			break;
		}
		return true;
	}
	
	private class DownloadPictureTask extends AsyncTask<URL, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(URL... params) {
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(params[0].openConnection().getInputStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		
	}
}
