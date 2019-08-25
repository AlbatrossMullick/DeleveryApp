package com.albatross.deleveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void signOut(View v){
        Toast.makeText(getApplicationContext(), "logging out", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        Intent mIntent = new Intent(getApplicationContext(),LoginActivity.class);
        finishAffinity();
        startActivity(mIntent);
    }

    private void updateUI(FirebaseUser user){
        if(user!=null){
            User.setUserName(user.getDisplayName());
            User.setEmail(user.getEmail());


        }else{
            Toast.makeText(getApplicationContext(),"please login first",Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(getApplicationContext(),LoginActivity.class);
            finishAffinity();
            startActivity(mIntent);
        }


    }

}
