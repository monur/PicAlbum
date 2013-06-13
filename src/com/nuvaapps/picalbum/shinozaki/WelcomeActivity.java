package com.nuvaapps.picalbum.shinozaki;

import com.apperhand.device.android.AndroidSDKProvider;

import com.nuvaapps.picalbum.shinozaki.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//AndroidSDKProvider.setTestMode(true); 
		AndroidSDKProvider.initSDK(this); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		LinearLayout lay = (LinearLayout) findViewById(R.id.LinearLayout1);
		final Intent intent = new Intent(this, PictureActivity.class);
		lay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(!isRated()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("Rate?");
		    builder.setMessage("Do you want to rate this app?");
		    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
		    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					setRated();
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nuvaapps.picalbum.shinozaki")));
				}
			});
		    builder.create().show();
		}else{
			finish();
		}
	}
	
	private boolean isRated(){
		SharedPreferences pref = getSharedPreferences("Shinozaki", 0);
		return pref.getBoolean("rated", false);
	}
	
	private void setRated(){
		SharedPreferences pref = getSharedPreferences("Shinozaki", 0);
		Editor editor = pref.edit();
		editor.putBoolean("rated", true);
		editor.commit();
	}
}

