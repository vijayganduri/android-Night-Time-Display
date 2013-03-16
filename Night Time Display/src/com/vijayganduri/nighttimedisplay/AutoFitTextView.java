package com.vijayganduri.nighttimedisplay;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;


public class AutoFitTextView extends DSDigitTextView {

    //Attributes
    private Paint mTestPaint;
    private static final String TAG = AutoFitTextView.class.getName();
    
    private boolean textSet = false;
    
    public AutoFitTextView(Context context) {
        super(context);
        initialise();
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        //max size defaults to the initially specified text size unless it is too small
    }

    /* Re size the font so the specified text fits in the text box
     * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth) 
    { 
    	
    	if(textSet)
    		return;
    	
        if (textWidth <= 0)
            return;
        
      //  Log.v(TAG,"textwidth : "+textWidth);
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        //float hi = this.getTextSize();
        //Log.v(TAG,"textsize: "+hi);
        float hi = 500;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            mTestPaint.setTextSize(size);
            if(mTestPaint.measureText(text) >= targetWidth) 
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
        textSet = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    /**
     * To reset the initialize size
     */
    public void resetSize(){
    	textSet = false;
    }
    
    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }

}