package com.vijayganduri.nighttimedisplay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * Main Screen for displaying the time
 * @author Vijay Ganduri
 *
 */
@EActivity
public class DisplayActivity extends Activity{

	@ViewById(R.id.time_text_background) TextView timeBackgroundTextView;
	@ViewById(R.id.time_text) TextView timeTextView;	
	@ViewById(R.id.menuOptions) LinearLayout menuOptions;

	
	private final SimpleDateFormat otherFormat    = new SimpleDateFormat("HH:mm:ss");	
	private final SimpleDateFormat otherHourMinFormat    = new SimpleDateFormat("HH:mm");
	private final SimpleDateFormat otherBlinkHourMinFormat    = new SimpleDateFormat("HH mm");
	
	private final SimpleDateFormat otherSmallFormat    = new SimpleDateFormat("hh:mm:ss");	
	private final SimpleDateFormat otherSmallHourMinFormat    = new SimpleDateFormat("hh:mm");
	private final SimpleDateFormat otherBlinkSmallHourMinFormat    = new SimpleDateFormat("hh mm");
	
	private static final int FAST = 200;
	private static final int SLOW = 500;
	
	private String theme;
	private String timeMode;
	
	private boolean hideSecs;
	private boolean blinkSeparator;
	
	private boolean blinkStatus;
	private boolean stop;
	
	private String TAG = DisplayActivity.class.getName();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		checkPreferences();
		
		setAppTheme(theme);
		setContentView(R.layout.activity_main);
		
		if(hideSecs){
			timeBackgroundTextView.setText("88:88");
			timeTextView.setText("00:00");
		}
		
		calculateTime();
		showorHideMenu();
		
	}
	
	
	/**
	 * Fetches all the latest preferences and updates the fields
	 */
	public void checkPreferences(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		theme = sharedPrefs.getString("example_list", "-1");
		timeMode = sharedPrefs.getString("example_list_time_format", "-1");
		
		hideSecs = sharedPrefs.getBoolean("example_checkbox", false);
		blinkSeparator = sharedPrefs.getBoolean("example_checkbox_blink", true);
		
		Log.d(TAG,"theme :"+theme+", timemode : "+timeMode+", hidesecs : "+hideSecs+", blinksepartor : "+blinkSeparator);
		
	}
	
	
	@Background
	public void calculateTime(){
		
		while(!stop){
			updateTime(formatTime(new Date().getTime()));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}
	}
	
	
	@UiThread
	public void updateTime(String time){
		
		timeTextView.setText(time);
	}
	
	
	/**
	 *  Settings screen
	 */
	@Click(R.id.settings_button)
	public void settingsClicked(){
				
		Intent settingsActivity = new Intent(this, SettingsActivity.class);
		startActivity(settingsActivity);
		menuOptions.setVisibility(View.GONE);
		
	}
	
	
	/**
	 *  About screen
	 */
	@Click(R.id.about_button)
	public void aboutClicked(){

		Intent aboutActivity = new Intent(this, AboutActivity_.class);
		startActivity(aboutActivity);
		menuOptions.setVisibility(View.GONE);
		
	}
	
	
	@Click(R.id.displayBackground)
	public void backGroundClicked(){
				
		showorHideMenu(FAST);
		
	}
	
	
	/**
	 * Default speed is SLOW
	 */
	public void showorHideMenu(){
		
		showorHideMenu(SLOW);
		
	}
	
	
	public void showorHideMenu(int speed){
		
		if(menuOptions.isShown()){			
			setLayoutAnim_slidedownfromtop(speed, menuOptions, this);
			menuOptions.setVisibility(View.GONE);
		}else{
			setLayoutAnim_slideupfrombottom(speed, menuOptions, this);
			menuOptions.setVisibility(View.VISIBLE);
		}		
		
	}
	

	@Override
	protected void onResume() {

		super.onResume();		
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);		
		String newTheme = sharedPrefs.getString("example_list", "-1");
		
		if(!theme.equals(newTheme)){
			Log.d(TAG,"Theme changed");
			theme = newTheme;
			setAppTheme(theme);
			setContentView(R.layout.activity_main);			
		}
		
		checkPreferences();
		
		((AutoFitTextView)timeBackgroundTextView).resetSize();
		((AutoFitTextView)timeTextView).resetSize();
		
		if(hideSecs){
			timeBackgroundTextView.setText("88:88");
			timeTextView.setText("00:00");
		}else{
			timeBackgroundTextView.setText("88:88:88");
			timeTextView.setText("00:00:00");
		}
		
		if(stop){			
			stop = false;
			calculateTime();			
		}
		
	}
	
	
	@Override
	protected void onPause() {		
		super.onPause();
		stop = true;		
	}
	
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		stop = true;
	}	
	
	
	public void setAppTheme(){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);		
		theme = sharedPrefs.getString("example_list", "-1");
		setAppTheme(theme);
	}
	
	/**
	 * Sets the app theme based on preference value
	 * @param theme
	 */
	public void setAppTheme(String theme){
		
		if(theme.equals("-1")){			
			setTheme(R.style.GreenLedTheme);
		}else if(theme.equals("1")){			
			setTheme(R.style.RedLedTheme);
		}else if(theme.equals("2")){			
			setTheme(R.style.BlueLedTheme);
		}else{
			setTheme(R.style.SoftBlueLedTheme);
		}
		
	}
	

	private String formatTime(long time) {
		
		Calendar startTime    = Calendar.getInstance();
		startTime.setTimeZone(TimeZone.getDefault());
		startTime.setTimeInMillis(time);
		
		String timeFormatted;
		
		if(timeMode.equals("-1")){
			//24 hour mode
			if(hideSecs){//22:45
				if(blinkSeparator && blinkStatus){
					timeFormatted = otherBlinkHourMinFormat.format(startTime.getTime());
					blinkStatus = false;
				}else{
					timeFormatted = otherHourMinFormat.format(startTime.getTime());
					blinkStatus = true;
				}
			}else//22:45:59
				timeFormatted = otherFormat.format(startTime.getTime());
		}else{
			//12 hour mode
			if(hideSecs){//10:45
				if(blinkSeparator && blinkStatus){
					timeFormatted = otherBlinkSmallHourMinFormat.format(startTime.getTime());
					blinkStatus = false;
				}else{
					timeFormatted = otherSmallHourMinFormat.format(startTime.getTime());
					blinkStatus = true;
				}
			}else//10:45:59
				timeFormatted = otherSmallFormat.format(startTime.getTime());	
		}
		
		return timeFormatted;		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    
		if ( keyCode == KeyEvent.KEYCODE_MENU ) {
			
			showorHideMenu(FAST);
			
	        return true;
	    }
	    
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	public static void setLayoutAnim_slideupfrombottom(int speed, ViewGroup panel, Context ctx) {

		  AnimationSet set = new AnimationSet(true);

		  Animation animation = new AlphaAnimation(0.0f, 1.0f);
		  animation.setDuration(100);
		  set.addAnimation(animation);

		  animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f
		  );
		  animation.setDuration(speed);
		  set.addAnimation(animation);

		  LayoutAnimationController controller =
		      new LayoutAnimationController(set, 0.25f);
		  panel.setLayoutAnimation(controller);

		  panel.startAnimation(animation);
		  
	}
	
	
	public static void setLayoutAnim_slidedownfromtop(int speed, ViewGroup panel, Context ctx) {

		  AnimationSet set = new AnimationSet(true);

		  Animation animation = new AlphaAnimation(1.0f, 1.0f);//(1,0) works well too..
		  animation.setDuration(speed);
		  set.addAnimation(animation);

		  animation = new TranslateAnimation(
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		      Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
		  );
		  animation.setDuration(speed);
		  set.addAnimation(animation);

		  LayoutAnimationController controller =
		      new LayoutAnimationController(set, 0.25f);
		  panel.setLayoutAnimation(controller);
		  
		  panel.startAnimation(animation);
		  		  
	}		
	

}
