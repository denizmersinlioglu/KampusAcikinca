package com.ggg.denizmersinlioglu.kampusacikinca;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    public static final String EMAIL_CARRIER = "Email_Carrier";
    public static final String PASSWORD_CARRIER = "Password_Carrier";
    private static final int SIGN_IN_REQUEST_CODE = 123;
    //<--------------  Decleration zone --------------->
    private Button loginButton;
    private Button signinButton;
    private EditText editText_email;
    private EditText editText_password;
    private ImageView imageView;
    private ProgressBar progressBar;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    //<------------------------------------------------>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //<--------------  Initialization zone --------------->
        loginButton = (Button) findViewById(R.id.login_Button);
        signinButton = (Button) findViewById(R.id.signIn_Button);
        editText_email = (EditText) findViewById(R.id.user_Email_Input);
        editText_password = (EditText) findViewById(R.id.password_Input);
        imageView = (ImageView) findViewById(R.id.applicaton_Icon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //<--------------------------------------------------->

        mAuth = FirebaseAuth.getInstance();

        //Set the title
        setTitle("Login with E-mail");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Initially progress bar is not visible
        progressBar.setVisibility(View.GONE);

        //Set the listener to login button.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the username and password from the user.
                email = editText_email.getText().toString();
                password = editText_password.getText().toString();

                if (validateForm(editText_email,editText_password)) {
                //Action that user enters a valid e_mail.
                    logIn(email,password);
                }
                //Action that user enters a valid e_mail.

            }
        });
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateSignUpPage();
            }
        });
    }

    private void logIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);


        progressBar.setVisibility(View.VISIBLE);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication Successed.",
                                    Toast.LENGTH_SHORT).show();
                            navigateMainPage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        progressBar.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]


    }

    private boolean validateForm(TextView _email, TextView _password) {
        boolean valid = true;

        String email = _email.getText().toString();

        if (TextUtils.isEmpty(email)) {
            editText_email.setError("Required.");
            valid = false;
        } else {
            if(! editText_email.getText().toString().contains("@"))
            {
                editText_email.setError("It needs to be in Email Format.");
                valid = false;
            }
            else {
                editText_email.setError(null);
            }
        }

        String password = _password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editText_password.setError("Required.");
            valid = false;
        } else {
            editText_password.setError(null);
        }

        return valid;
    }

    private void navigateSignUpPage(){
        Intent myIntent = new Intent(this, CreateAccountActivity.class);
        startActivity(myIntent);
    }

    private void navigateMainPage(){
        Intent myIntent = new Intent(this,MainActivity.class);
        email = editText_email.getText().toString();
        myIntent.putExtra(EMAIL_CARRIER,email);
        myIntent.putExtra(PASSWORD_CARRIER,password);
        startActivity(myIntent);
    }

}
