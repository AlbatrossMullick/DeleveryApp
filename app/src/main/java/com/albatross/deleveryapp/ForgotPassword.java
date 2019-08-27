package com.albatross.deleveryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class ForgotPassword extends Activity {
    EditText emailbox;
    CardView forgetPasswordButton ;
    FirebaseAuth auth;
    ImageView backReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailbox = (EditText)findViewById(R.id.forgetPsswordEmail);
        forgetPasswordButton = (CardView)findViewById(R.id.forgetPasswordButton);
        auth = FirebaseAuth.getInstance();
        backReset = findViewById(R.id.resetBackButton);

        backReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailbox.getText().toString();
                emailbox.clearFocus();
                forgetPasswordButton.requestFocus();
                showProgress(true);
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(emailbox.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(findViewById(R.id.paswordResetLayout), "Email sent", Snackbar.LENGTH_SHORT).show();

                                }
                                else{
                                    Log.d("errorid", task.getException().toString());
                                    Snackbar.make(findViewById(R.id.paswordResetLayout), task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                                showProgress(false);
                            }
                        });
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.forgetPsswordprogressBar).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void showProgress(boolean show){
        if(show){
            findViewById(R.id.forgetPsswordprogressBar).setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        }
        else{
         //   findViewById(R.id.paswordResetLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.forgetPsswordprogressBar).setVisibility(View.GONE);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }

}
