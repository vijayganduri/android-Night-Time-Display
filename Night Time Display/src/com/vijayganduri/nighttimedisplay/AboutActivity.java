package com.vijayganduri.nighttimedisplay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * About screen
 * @author Vijay Ganduri
 *
 */
@EActivity
public class AboutActivity extends Activity{
	
	@ViewById(R.id.app_title_background) TextView appTitle;
	
	private String TAG = AboutActivity.class.getName();
	private boolean stop;

	int colors[] = new int[4];
	int i=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	
		colors[0] = getResources().getColor(R.color.led_green);
		colors[1] = getResources().getColor(R.color.led_blue);
		colors[2] = getResources().getColor(R.color.led_red);
		colors[3] = getResources().getColor(R.color.led_soft_blue);
		
		changeColors();
	}

	/**
	 * For flickering neon effect
	 */
	@Background
	public void changeColors(){
		
		while(!stop){
			
			updateTextColor();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
	}
	
	
	@UiThread
	public void updateTextColor(){
		
		if(i>=4)
			i=0;
		
		appTitle.setTextColor(colors[i]);
		i++;
	}
	

	@Override
	protected void onResume() {
		super.onResume();		
		stop = false;
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stop = true;
	}

}
