package aeonlabs.common.libraries.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditTextTimesNewRoman extends androidx.appcompat.widget.AppCompatEditText {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "times_new_roman.ttf";

    public EditTextTimesNewRoman(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextTimesNewRoman(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextTimesNewRoman(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), FONTAWESOME);
        setTypeface(tf);
    }

}