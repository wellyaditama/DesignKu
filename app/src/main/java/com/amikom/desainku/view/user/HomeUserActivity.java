package com.amikom.desainku.view.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityUserHomeBinding;
import com.amikom.desainku.view.admin.HomeAdminActivity;
import com.amikom.desainku.view.admin.fragments.BookingAdminFragment;
import com.amikom.desainku.view.admin.fragments.ServiceAdminFragment;
import com.amikom.desainku.view.admin.fragments.SettingsAdminFragment;
import com.amikom.desainku.view.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeUserActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    ActivityUserHomeBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    BookingAdminFragment bookingAdminFragment = new BookingAdminFragment();
    ServiceAdminFragment serviceAdminFragment = new ServiceAdminFragment();
    SettingsAdminFragment settingsAdminFragment = new SettingsAdminFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getWindow().setNavigationBarColor(getResources().getColor(R.color.primary3));

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigationView.setSelectedItemId(R.id.service);

        Bundle mBundle = new Bundle();
        mBundle.putString("userType", "User");

        serviceAdminFragment.setArguments(mBundle);
        bookingAdminFragment.setArguments(mBundle);
        settingsAdminFragment.setArguments(mBundle);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {


            case R.id.service:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, serviceAdminFragment)
                        .commit();
                return true;

            case R.id.booking:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, bookingAdminFragment)
                        .commit();
                return true;

            case R.id.settings:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, settingsAdminFragment)
                        .commit();
                return true;
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}