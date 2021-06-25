package aeonlabs.common.libraries.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class ButtonTimesNewRoman extends androidx.appcompat.widget.AppCompatButton {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "times_new_roman.ttf";

    public ButtonTimesNewRoman(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonTimesNewRoman(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonTimesNewRoman(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), FONTAWESOME);
        setTypeface(tf);
    }

}