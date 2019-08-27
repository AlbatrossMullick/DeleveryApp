package com.albatross.deleveryapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Welcome extends Activity {
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();



    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user){

        if(user!=null){
            User.setUserName(user.getDisplayName());
            User.setEmail(user.getEmail());
            Toast.makeText(getApplicationContext(),"welcome "+User.getUserName(),Toast.LENGTH_SHORT).show();
            changeActivity(HomeActivity.class);


        }else{
            Toast.makeText(getApplicationContext(),"please login first",Toast.LENGTH_SHORT).show();
            changeActivity(LoginActivity.class);
        }


    }
    private void changeActivity(final Class c) {
        Thread closeActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent mIntent = new Intent(getApplicationContext(),c);
                    finishAffinity();
                    startActivity(mIntent);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.start();


    }
}