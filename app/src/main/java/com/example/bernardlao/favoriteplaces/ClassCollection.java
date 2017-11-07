package com.example.bernardlao.favoriteplaces;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Bernard Lao on 10/20/2017.
 */

public class ClassCollection {
    Context context;
    public ClassCollection(Context c){
        this.context = c;
    }
    public InputFilter getAlphaNumericFilter(){
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        return filter;
    }
}
