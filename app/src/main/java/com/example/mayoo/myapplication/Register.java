package com.example.mayoo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by mayoo on 11/14/2016.
 */

public class Register extends AppCompatActivity {

    UserSessionManager session;

    EditText userName_editText, password_editText, confirm_editText;
    TextView username_validation, password_validation, confirm_pw_validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        session = new UserSessionManager(this);


        userName_editText = (EditText) findViewById(R.id.username_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);
        confirm_editText = (EditText) findViewById(R.id.confirm_editText);

        username_validation = (TextView) findViewById(R.id.username_validation);
        password_validation = (TextView) findViewById(R.id.password_validation);
        confirm_pw_validation = (TextView) findViewById(R.id.confirm_pw_validation);


        userName_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (userName_editText.getText().toString().length() > 0) {

                        if (userName_editText.getText().toString().length() < 3) {
                            username_validation.setVisibility(View.VISIBLE);
                            username_validation.setText("Username must be 3 or more letters");
                        } else {
                            username_validation.setVisibility(View.GONE);

                        }
                    }
                }
            }

        });

        userName_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (userName_editText.getText().toString().length() > 0) {

                    if (userName_editText.getText().toString().length() <= 2) {

                        username_validation.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (password_editText.getText().toString().length() > 0) {
                        if (password_editText.getText().toString().length() < 6) {
                            password_validation.setVisibility(View.VISIBLE);
                        } else {
                            password_validation.setVisibility(View.GONE);

                        }
                    }
                }
            }
        });

        password_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (password_editText.getText().toString().length() > 0) {

                    if (password_editText.getText().toString().length() <= 5) {

                        password_validation.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (confirm_editText.getText().toString().length() > 0) {
                        if (!password_editText.getText().toString().equals(confirm_editText.getText().toString())) {
                            confirm_pw_validation.setVisibility(View.VISIBLE);
                        } else {
                            confirm_pw_validation.setVisibility(View.GONE);

                        }
                    }
                }
            }
        });

        confirm_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (confirm_editText.getText().toString().length() > 0) {

                    if (password_editText.getText().toString().equals(confirm_editText.getText().toString())) {

                        confirm_pw_validation.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    public void register(View view) {
        final String userName_str = userName_editText.getText().toString();

        final String password_str = password_editText.getText().toString();

        final String confirm_str = confirm_editText.getText().toString();

        if (!userName_str.isEmpty() && !password_str.isEmpty() && !confirm_str.isEmpty()) {
            if (username_validation.getVisibility() == View.GONE && password_validation.getVisibility() == View.GONE &&
                    confirm_pw_validation.getVisibility() == View.GONE) {


                Helper h = new Helper(Register.this);
                h.open();


                if (h.userNameChecking(userName_str)) {
                    if (password_str.equals(confirm_str)) {
                        session.createUserLoginSession(userName_str);

                        h.insertEntry(userName_str, password_str, "-", "-", "-");
                        h.close();
                        Intent i = new Intent(Register.this, Home.class);
                        i.putExtra("username", userName_str); // (Key, Value)
                        finish();
                        startActivity(i);


                    }


                } else {
                    username_validation.setVisibility(View.VISIBLE);
                    username_validation.setText("Username already exists");                }

            }


        }
    }
}