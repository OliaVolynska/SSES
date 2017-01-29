package com.example.olia.test.Controller;

import android.text.Editable;
import android.text.TextWatcher;

interface Function{
    void CheckFields();
}

public class TextWatcherHelper {

    public TextWatcher textWatcher(final Function checkFields) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {checkFields.CheckFields();
            }
        };
    }
}
