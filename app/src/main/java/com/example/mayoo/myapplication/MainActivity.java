package com.example.mayoo.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow();
        dialog.setContentView(R.layout.login);
        dialog.show();
        Button loginbtn = (Button) dialog.findViewById(R.id.login_button);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username_txt = (EditText) dialog.findViewById(R.id.username_editText);
                EditText password_txt = (EditText) dialog.findViewById(R.id.pw_editText);
                Helper h = new Helper(MainActivity.this);
                h.open();

                String savedpw = h.getSingleEntry(username_txt.getText().toString());

                if(savedpw.equals(password_txt.getText().toString())){
                    dialog.dismiss();
                    Intent i = new Intent(MainActivity.this, Home.class);

                    i.putExtra("username", username_txt.getText().toString()); // (Key, Value)
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();


                }


            }
        });
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_button);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }



}

