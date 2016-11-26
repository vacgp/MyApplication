package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mayoo on 11/14/2016.
 */

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void register(View view) {
        EditText userName_editText = (EditText) findViewById(R.id.username_editText);
        final String userName_str = userName_editText.getText().toString();

        EditText password_editText = (EditText) findViewById(R.id.password_editText);
        final String password_str = password_editText.getText().toString();

        EditText confirm_editText = (EditText) findViewById(R.id.confirm_editText);
        final String confirm_str = confirm_editText.getText().toString();


        Helper h = new Helper(Register.this);
        h.open();


        if (h.userNameChecking(userName_str)) {
            if (password_str.equals(confirm_str)) {
                h.insertEntry(userName_str, password_str, "-", "-", "-");
                h.close();
                Intent i = new Intent(Register.this, Home.class);
                i.putExtra("username", userName_str); // (Key, Value)
                startActivity(i);



            } else {
                Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(Register.this, "Already exists", Toast.LENGTH_SHORT).show();
        }




    }
}