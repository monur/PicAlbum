package com.nuvaapps.picalbum;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
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
						pictureId++;
					else
						pictureId--;
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
		try {
			URL url = new URL("http://nuvaapps.zz.mu/pictures/getPicture.php?pictureId=" + pictureId);
			currentImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			imageView.setImageBitmap(currentImage);
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Connection error, check your internet connections",  Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Connection error, check your internet connections",  Toast.LENGTH_LONG).show();
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
		default:
			break;
		}
		return true;
	}

}
