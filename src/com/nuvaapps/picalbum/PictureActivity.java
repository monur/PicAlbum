package com.nuvaapps.picalbum;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PictureActivity extends Activity {

	ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		imageView = (ImageView)findViewById(R.id.imageView1);
		Toast.makeText(this, "Tap right for next picture, left for previous", Toast.LENGTH_LONG ).show();
		loadPicture();
		LinearLayout layout = (LinearLayout)findViewById(R.id.LinearLayout1);
		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					System.out.println(event.getX() +"-" + event.getY());
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.picture, menu);
		return true;
	}
	
	private void loadPicture(){
		URL url;
		try {
			url = new URL("http://subi.milliyet.com.tr/MilliyetMizah/600xMax/2010/11/10/Sarki_soyleyen_at_f48a0.jpg");
			Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			imageView.setImageBitmap(bmp);
		} catch (MalformedURLException e) {
			Toast.makeText(this, "Connection error, check your internet connections",  Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "Connection error, check your internet connections",  Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

}
