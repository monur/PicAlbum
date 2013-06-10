package com.nuvaapps.picalbum.shinozaki;

import com.nuvaapps.picalbum.shinozaki.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
	
	@Override
	public void onBackPressed() {
		final CharSequence[] items = {"Exit", "Rate", "Cancel"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Sure?");
	    builder.setItems(items, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   switch (which) {
					case 0:
						finish();
						break;
					case 1:
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nuvaapps.picalbum")));
						break;
					default:
						break;
					}
	           }
	    });
	    builder.create().show();
	    
	}
	
}

