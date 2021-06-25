package aeonlabs.common.libraries.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class FontAwesomeBrands extends androidx.appcompat.widget.AppCompatTextView {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "Font-Awesome-5-Brands-Regular-400.otf";

    public FontAwesomeBrands(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontAwesomeBrands(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesomeBrands(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), FONTAWESOME);
        setTypeface(tf);
    }

}