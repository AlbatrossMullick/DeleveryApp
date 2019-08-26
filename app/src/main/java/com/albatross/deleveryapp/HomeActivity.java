package com.albatross.deleveryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends Activity {
    private FirebaseAuth mAuth;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.homeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        List<ModelClass> modelClassList = new ArrayList<>();
        String img = "https://assets3.thrillist.com/v1/image/2797371/size/tmg-article_default_mobile.jpg";
        modelClassList.add(new ModelClass("123",img,"ggj","rkl","success"));
        modelClassList.add(new ModelClass("123",img,"ggj","rkl","success"));
        modelClassList.add(new ModelClass("123",img,"ggj","rkl","success"));
        modelClassList.add(new ModelClass("123",img,"ggj","rkl","success"));
        modelClassList.add(new ModelClass("123","https://yt3.ggpht.com/a/AGF-l7-9Xg5EGp1KDDIleuQqHTVxmnl6as7H47YH2A=s48-c-k-c0xffffffff-no-rj-mo","ggj","rkl","success"));

        HomeAdapter adapter = new HomeAdapter(modelClassList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
      //  updateUI(currentUser);
    }
    public void signOut(View v){
        Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
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
