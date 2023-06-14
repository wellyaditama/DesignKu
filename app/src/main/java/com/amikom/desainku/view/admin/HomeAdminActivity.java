package com.amikom.desainku.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityHomeAdminBinding;
import com.amikom.desainku.view.admin.fragments.BookingAdminFragment;
import com.amikom.desainku.view.admin.fragments.HomeAdminFragment;
import com.amikom.desainku.view.admin.fragments.ServiceAdminFragment;
import com.amikom.desainku.view.admin.fragments.SettingsAdminFragment;
import com.amikom.desainku.view.auth.LoginActivity;
import com.amikom.desainku.view.user.HomeUserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityHomeAdminBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getWindow().setNavigationBarColor(getResources().getColor(R.color.primary3));

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigationView.setSelectedItemId(R.id.service);

        mBundle = new Bundle();
        mBundle.putString("userType", "Admin");

        serviceAdminFragment.setArguments(mBundle);
        bookingAdminFragment.setArguments(mBundle);
        settingsAdminFragment.setArguments(mBundle);



    }

    HomeAdminFragment homeAdminFragment = new HomeAdminFragment();
    BookingAdminFragment bookingAdminFragment = new BookingAdminFragment();
    ServiceAdminFragment serviceAdminFragment = new ServiceAdminFragment();
    SettingsAdminFragment settingsAdminFragment = new SettingsAdminFragment();

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