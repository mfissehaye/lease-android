package ahadoo.com.collect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import ahadoo.com.collect.util.CollectHelpers;

public class RegisterActivity extends AppCompatActivity {

    private View bgnSignup, bgnLogin;

    private EditText usernameEditable;

    private EditText firstNameEditable;

    private EditText lastNameEditable;

    private EditText emailEditable;

    private EditText passwordEditable;

    private EditText confirmPasswordEditable;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        bgnSignup = findViewById(R.id.btn_signup);

        bgnLogin = findViewById(R.id.link_login);

        usernameEditable = (EditText) findViewById(R.id.register_username);

        firstNameEditable = (EditText) findViewById(R.id.regoster_firstname);

        lastNameEditable = (EditText) findViewById(R.id.register_lastname);

        emailEditable = (EditText) findViewById(R.id.register_email);

        passwordEditable = (EditText) findViewById(R.id.register_password);

        confirmPasswordEditable = (EditText) findViewById(R.id.register_password_confirm);

        progressDialog = new ProgressDialog(this);

        bgnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!passwordEditable.getText().toString().equals(confirmPasswordEditable.getText().toString())) {

                    CollectHelpers.showDialog(RegisterActivity.this, "Password Don't Match");

                    return;
                }

                if (CollectHelpers.isNetworkAvailable(RegisterActivity.this)) {

                    progressDialog.setTitle("Registering ....");

                    progressDialog.show();

                        /*SendRequestService service = new SendRequestService();

                        service.setAPI_URL(RequestServices.MainAPI + RequestServices.REGISTRATION_REQUEST.API_URL.toString());

                        List<Pair<String, User>> sendParam = new ArrayList<Pair<String, User>>();

                        User user = new User();

                        user.setEmail(emailEditable.getText().toString());

                        user.setLast_name(lastNameEditable.getText().toString());

                        user.setUser_name(usernameEditable.getText().toString());

                        user.setPassword(passwordEditable.getText().toString());

                        user.setConfirm_password(confirmPasswordEditable.toString());

                        JSONObject userRequestObject = new JSONObject();

                        JSONObject userObject = new JSONObject();

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_NAME.toString(), usernameEditable.getText().toString());

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_FIRST_NAME.toString(), firstNameEditable.getText().toString());

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_LAST_NAME.toString(), lastNameEditable.getText().toString());

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_EMAIL.toString(), emailEditable.getText().toString());

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_PASSWORD.toString(), passwordEditable.getText().toString());

                        userObject.put(RequestServices.REGISTRATION_REQUEST.USER_PASSWORD_CONFIRM.toString(), confirmPasswordEditable.getText().toString());

                        userRequestObject.putOpt("user", userObject);

                        userRequestObject.putOpt("is_company", true);

                        service.processRegisterRequest(userRequestObject.toString(), Register.this);*/

                } else {

                    CollectHelpers.showDialog(RegisterActivity.this, getString(R.string.no_connection_found));
                }
            }

        });

        bgnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();

            }
        });


    }


    /*@Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void successCallback(String string) {
        progressDialog.cancel();
        Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_LONG).getFragment();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void failedCallback(String string) {
        progressDialog.cancel();
        Utils.showDialog(getString(R.string.error_general), this);
    }*/


}