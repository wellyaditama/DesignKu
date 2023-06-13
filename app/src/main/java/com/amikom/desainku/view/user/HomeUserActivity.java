package com.amikom.desainku.view.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityUserHomeBinding;
import com.amikom.desainku.view.admin.HomeAdminActivity;
import com.amikom.desainku.view.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeUserActivity extends AppCompatActivity {

    ActivityUserHomeBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                startActivity(new Intent(HomeUserActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}