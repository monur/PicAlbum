package com.nuvaapps.picalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		RelativeLayout lay = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		final Intent intent = new Intent(this, PictureActivity.class);
		lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(intent);
			}
		});
	}
	
	
}

