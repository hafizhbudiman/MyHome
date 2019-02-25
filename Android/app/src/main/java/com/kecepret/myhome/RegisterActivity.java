package com.kecepret.myhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kecepret.myhome.model.Register;
import com.kecepret.myhome.model.ResponseBE;
import com.kecepret.myhome.model.UserSession;
import com.kecepret.myhome.network.APIClient;
import com.kecepret.myhome.network.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String PREFER_NAME = "Reg";

    private AutoCompleteTextView mFullName;
    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private AutoCompleteTextView mEmail;
    private AutoCompleteTextView mPhoneNumber;
    private AutoCompleteTextView mAddress;
    private AppCompatButton mSignUpButton;
    private String googleId = "False";

    // User Session Manager Class
    private UserSession session;
    private APIInterface apiInterface;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // User Session Manager
        session = new UserSession(getApplicationContext());

        mFullName = (AutoCompleteTextView) findViewById(R.id.fullname);
        mUsername = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmail = (AutoCompleteTextView) findViewById(R.id.email);
        mPhoneNumber = (AutoCompleteTextView) findViewById(R.id.phone);
        mAddress = (AutoCompleteTextView) findViewById(R.id.address);
        mSignUpButton = (AppCompatButton) findViewById(R.id.sign_up_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String fullname = extras.getString("fullname");
            googleId = extras.getString("googleId");

            mUsername.setVisibility(View.INVISIBLE);
            mPasswordView.setVisibility(View.INVISIBLE);
            mFullName.setText(fullname);
            mEmail.setText(email);
            mUsername.setText(email);
            mPasswordView.setText(email);

            mEmail.setFocusable(false);
            mFullName.setFocusable(false);
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    public void attemptRegister() {

        // Reset errors.
        mFullName.setError(null);
        mUsername.setError(null);
        mPasswordView.setError(null);
        mEmail.setError(null);
        mPhoneNumber.setError(null);
        mAddress.setError(null);

        // Store values at the time of the login attempt.
        String fullname = mFullName.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPasswordView.getText().toString();
        String email = mEmail.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String address = mAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid full name
        if (TextUtils.isEmpty(fullname)) {
            mFullName.setError(getString(R.string.error_empty_field));
            focusView = mFullName;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_empty_field));
            focusView = mUsername;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

        // Check for a valid phone number
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumber.setError(getString(R.string.error_empty_field));
            focusView = mPhoneNumber;
            cancel = true;
        }

        // Check for a valid address
        if (TextUtils.isEmpty(address)) {
            mAddress.setError(getString(R.string.error_empty_field));
            focusView = mAddress;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            mSignUpButton.setEnabled(false);

            Register(fullname, username, password, email, phoneNumber, address);
        }
    }

    public void Register(final String fullname, final String username, final String password, final String email, final String phoneNumber, final String address){

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Register register = new Register(username, email, password, fullname, address, googleId, phoneNumber);
        Call<ResponseBE> call = apiInterface.register(register);

        call.enqueue(new Callback<ResponseBE>() {

            @Override
            public void onResponse(Call<ResponseBE> call, Response<ResponseBE> response) {
                ResponseBE resource = response.body();
                Boolean success = resource.success;

                if (success) {
                    session.createUserLoginSession(username, password, fullname, email, phoneNumber, address);

                    Toast.makeText(getApplicationContext(), "Logged in as " + fullname,
                            Toast.LENGTH_LONG).show();

                    // Starting MainActivity
                    Intent i = new  Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    mSignUpButton.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Username is already taken",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBE> call, Throwable t) {
                call.cancel();

            }
        });
    }
}
