package com.xw.mediaselector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       getSupportFragmentManager()
               .beginTransaction()
               .replace(R.id.frameLayout,new MainFragment())
               .commitAllowingStateLoss();
    }
}