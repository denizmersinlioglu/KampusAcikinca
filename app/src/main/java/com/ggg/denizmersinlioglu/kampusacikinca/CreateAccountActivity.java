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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String USERNAME_CARRIER = "UserName_Carrier";
    private static final String EMAIL_CARRIER = "Email_Carrier";


    private FirebaseAuth mAuth;
    private Button signinButton;
    private Button loginButton;
    private Button verifyButton;
    private EditText editText_userName;
    private EditText editText_email;
    private EditText editText_password;
    private ImageView imageView;
    private ProgressBar progressBar;
    //<--------------------------------------------------->
    private String userName;
    private String email;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //<--------------  Initialization zone --------------->
        loginButton = (Button) findViewById(R.id.login_Button);
        signinButton = (Button) findViewById(R.id.signIn_Button);
        verifyButton = (Button) findViewById(R.id.verification_Button);
        editText_userName = (EditText) findViewById(R.id.userName_Input);
        editText_email = (EditText) findViewById(R.id.user_Email_Input);
        editText_password = (EditText) findViewById(R.id.password_Input);
        imageView = (ImageView) findViewById(R.id.applicaton_Icon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //<--------------------------------------------------->

        mAuth = FirebaseAuth.getInstance();

        //Set the title
        setTitle("Create an Account");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Initially progress bar is not visible
        progressBar.setVisibility(View.GONE);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        signinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if (validateForm(editText_email, editText_password)) {
                    email = editText_email.getText().toString();
                    password = editText_password.getText().toString();
                    userName = editText_userName.getText().toString();
                    createAccount(email, password);

                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navigateLoginPage();
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                sendEmailVerification();
            }
        });

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

        if (TextUtils.isEmpty(password)) {
            editText_userName.setError("Required.");
            valid = false;
        } else {
            editText_userName.setError(null);
        }
        return valid;
    }


    private void createAccount(String email, String password) {
            Log.d(TAG, "createAccount:" + email);

            progressBar.setVisibility(View.VISIBLE);

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                setDisplayName(userName);
                               // Toast.makeText(CreateAccountActivity.this, "Authentication Completed",
                               //         Toast.LENGTH_SHORT).show();
                                navigateLoginPage();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // [START_EXCLUDE]
                            progressBar.setVisibility(View.GONE);
                            // [END_EXCLUDE]
                        }
                    });
            // [END create_user_with_email]

        //Create user database in the Firebase
        createUserToUserdatabase(email,userName,password);
        }

    private void navigateLoginPage(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }

    private void navigateMainPage(){
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);

    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verification_Button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verification_Button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CreateAccountActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }



    private void setDisplayName(String _userName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(_userName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    private String arrangeEmailToBePath(String email) {
        String output = email.replace(".","-");
        return output;
    }

    private void createUserToUserdatabase(String _email, String _userName, String _password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("User");

        User newUser = new User(_email,_userName,_password);
        userRef.child(arrangeEmailToBePath(_email)).setValue(newUser);
    }
}
