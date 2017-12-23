package ahadoo.com.collect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ahadoo.com.collect.util.CollectHelpers;
import ahadoo.com.collect.util.LoginCredentials;
import ahadoo.com.collect.util.LoginResult;
import ahadoo.com.collect.util.UserPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    private View btnLogin, btnSignup;

    private EditText usernameEditable, passwordEditable;

    private ProgressDialog myProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);

        btnSignup = findViewById(R.id.link_signup);

        passwordEditable = (EditText) findViewById(R.id.login_password);

        usernameEditable = (EditText) findViewById(R.id.login_username);

        myProgressDialog = new ProgressDialog(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginCredentials loginCredentials = new LoginCredentials(

                        usernameEditable.getText().toString(),

                        passwordEditable.getText().toString()
                );

                if(loginCredentials.validate()){

                    CollectHelpers.showDialog(LoginActivity.this, "Please type your username and password properly");
                }

                else {

                    if(CollectHelpers.isNetworkAvailable(LoginActivity.this)){

                        myProgressDialog.setTitle(getString(R.string.logging_in));

                        myProgressDialog.show();

                        loginCredentials.login(LoginActivity.this, new LoginResult() {

                            @Override
                            public void onSuccess() {

                                myProgressDialog.cancel();

                                Toast.makeText(LoginActivity.this, getString(R.string.login_successful), Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);

                                finish();
                            }

                            @Override
                            public void onFailure(String errCredentials) {

                                myProgressDialog.cancel();

                                CollectHelpers.showDialog(LoginActivity.this, getString(R.string.error_connection));
                            }
                        });
                    }

                    else {

                        CollectHelpers.showDialog(LoginActivity.this, getString(R.string.no_connection_found));
                    }
                }
            }
        });

    }
}
