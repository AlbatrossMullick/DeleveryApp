package com.albatross.deleveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Order extends AppCompatActivity {
    TextView orderidtv;
    TextView nametv;
    TextView toAddresstv;
    TextView fromAddresstv;
    TextView statustv;
    TextView amounttv;
    final String ORDERCOLLECTION = "Orders";
    String orderid;
    private FirebaseFirestore db;
    Map orderdetails;
    ProgressBar mProgressView;
    ConstraintLayout mOrderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderidtv = findViewById(R.id.order_orderid);
        nametv = findViewById(R.id.order_name);
        toAddresstv = findViewById(R.id.order_toAddress);
        fromAddresstv = findViewById(R.id.order_fromAddress);
        statustv = findViewById(R.id.order_status);
        amounttv = findViewById(R.id.order_amount);
        mProgressView = findViewById(R.id.order_progressBar);
        mOrderView = findViewById(R.id.order_home);
        db = FirebaseFirestore.getInstance();
        showProgress(true);
        Intent intent = getIntent();
        orderid =  intent.getStringExtra("orderid");
        getOrderDetails(orderid);


    }

    private void setOrder() {
        orderidtv.setText(orderdetails.get("orderid").toString());
        toAddresstv.setText(orderdetails.get("ToAddress").toString());
        fromAddresstv.setText(orderdetails.get("FromAddress").toString());
        amounttv.setText(orderdetails.get("BillAmount").toString());
        statustv.setText(orderdetails.get("status").toString());
        nametv.setText(orderdetails.get("userName").toString());
        showProgress(false);
    }

    private void getOrderDetails(String orderid) {

        final DocumentReference docRef = db.collection(ORDERCOLLECTION).document(orderid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("data fond",document.getData().toString());
                       orderdetails = document.getData();
                       orderdetails.put("orderid",document.getId());
                       String userID=document.getData().get("User_ID").toString();
                       db.collection("LastUser").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()) {
                                   DocumentSnapshot document = task.getResult();
                                   if (document.exists()) {
                                       orderdetails.put("userName",document.getData().get("userName"));
                                       setOrder();

                                   }else{
                                       orderdetails.put("userName","Something went Wrong");
                                       Log.d("data not found", "No such document user");
                                       showProgress(false);

                                   }
                                   }else{
                                   orderdetails.put("userName","Something went Wrong");
                                   Log.d("data got wrong ", "No user");
                                   showProgress(false);

                               }

                           }
                       });


                    } else {
                        Log.d("data", "No such document");
                        showProgress(false);

                    }
                } else {
                    Log.d("exceptions", "get failed with ", task.getException());
                    showProgress(false);

                }

            }
        });


    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mOrderView.setVisibility(show ? View.GONE : View.VISIBLE);
            mOrderView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOrderView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mOrderView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
