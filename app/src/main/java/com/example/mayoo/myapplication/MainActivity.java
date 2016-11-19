package com.example.mayoo.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Register(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    public void login(View view) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.setContentView(R.layout.login);
        dialog.show();
    }
}

