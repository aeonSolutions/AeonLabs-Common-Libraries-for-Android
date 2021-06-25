package aeonlabs.common.libraries.Libraries.Utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.UUID;

public class StringUtils {
    public static String createTransactionID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
