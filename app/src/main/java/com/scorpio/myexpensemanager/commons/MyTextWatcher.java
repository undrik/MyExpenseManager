package com.scorpio.myexpensemanager.commons;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.scorpio.myexpensemanager.R;

import java.util.List;

/**
 * Created by User on 07-03-2018.
 */

public class MyTextWatcher implements TextWatcher {
    private View view;
    private List<String> strList;

    public MyTextWatcher(@NonNull View view) {
        this.view = view;
    }

    public MyTextWatcher(@NonNull View view, @NonNull List<String> strList) {
        this.view = view;
        this.strList = strList;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (view.getId()) {
            case R.id.inputName:
                validateName();
                break;
            case R.id.inputEmail:
                validateEmail();
                break;
        }
    }

    private void validateName() {

    }

    private void validateEmail() {

    }

}
