package com.vijayganduri.nighttimedisplay;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class DSDigitTextView extends TextView {
    private static final String DS_DIGIT = "DS-DIGIT.ttf";
	private static final String TAG = DSDigitTextView.class.getName();

	public DSDigitTextView(Context context) {
        super(context);

    	setCustomFont(context, DS_DIGIT);
    }

    public DSDigitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
    	setCustomFont(context, DS_DIGIT);

    }

    public DSDigitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
    	setCustomFont(context, DS_DIGIT);
   
    }
    
    public boolean setCustomFont(Context ctx, String fontFile) {

    	try {
        	setTypeface(Typeface.createFromAsset(ctx.getAssets(), fontFile));  		
        	return true;
        	
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }
    }
}
