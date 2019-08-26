package com.albatross.deleveryapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;



import android.os.Build;
import android.os.Bundle;


import android.text.InputType;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.inputmethod.InputMethodManager;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private FirebaseAuth mAuth;
    SignInButton googleSignIn;
    CardView mEmailSignInButton;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    Context mycontext;

    int RC_SIGN_IN = 0;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox showPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private Switch userSwitch;
    private String userType;
    private TextView forgetPassword;
   private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mycontext = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.username);
        googleSignIn=(SignInButton)findViewById(R.id.google_sign_in_button);
        forgetPassword = (TextView) findViewById(R.id.forgetPssword);
        mPasswordView = (EditText) findViewById(R.id.txtpassword);
        showPassword = (CheckBox)findViewById(R.id.showcheckBox);
         mEmailSignInButton = (CardView) findViewById(R.id.loginButton);
         userSwitch=(Switch)findViewById(R.id.switch1);
        if(userSwitch.isChecked()) {
            userType="MED";
        }else{
            userType="DEL";
        }
         userSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked){
                    userSwitch.setText("Medical Shop");
                     userType="MED";
                 }else{
                     userSwitch.setText("Delevery Boy");
                     userType="DEL";
                 }
             }
         });

        forgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();


            }
        });
        showPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showPassword.isChecked()){
                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.loginhome);
        mProgressView = findViewById(R.id.mprogressbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
       showProgress(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                showProgress(false);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginhome), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);

                        }


                        // ...
                    }
                });
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
//                                Toast.makeText(getApplicationContext(), "welcome "+user.getDisplayName(),
//                                        Toast.LENGTH_SHORT).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                //  Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(mycontext, "login Failed",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            showProgress(false);
                            // ...
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

        private void updateUI(final FirebaseUser user){
        if(user!=null){



            final DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Map<String, Object> userdata=document.getData();

                            if(userType.equals(userdata.get("userType").toString())){
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                User.setUserName(userdata.get("userName").toString());
                                User.setUserType(userdata.get("userType").toString());
                                User.setEmail(userdata.get("email").toString());

                                User.setUid(userdata.get("uid").toString());
                                User.setToken(user.getIdToken(true).toString());

                                Toast.makeText(mycontext, "welcome "+User.getUserName(),
                                        Toast.LENGTH_SHORT).show();
                                Intent mIntent = new Intent(mycontext,HomeActivity.class);
                                finishAffinity();
                                startActivity(mIntent);
                            }else{
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(mycontext, "Register yourself first",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(mycontext, "Register yourself first",Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                    showProgress(false);
                }
            });





        }
        else{
            showProgress(false);
        }


    }


}

