package com.aadu_maadu_kozhi.gregantech.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Anand.S on 12/5/2017.
 */

public class KeyboardUtil {
    public static void forceShowSoftKeypad(View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }
}
