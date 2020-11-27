package com.example.firebaseintro;

import android.widget.EditText;

public class Validations {

    public static void  checkEmptiness(String data, EditText et, String message){
        if (data.isEmpty()){
            et.setError(message);
            et.requestFocus();
        }
    }
}
