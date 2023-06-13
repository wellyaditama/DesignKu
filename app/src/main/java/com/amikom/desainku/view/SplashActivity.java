package com.amikom.desainku.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivitySplashBinding;
import com.amikom.desainku.utility.AnimationManagerClass;
import com.amikom.desainku.utility.UtilitiesClass;
import com.amikom.desainku.view.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Handler handler = new Handler();

//        AnimationManagerClass.fade(binding.ivSplashLogo, this);
//        AnimationManagerClass.slide(binding.tvSplashTitle, this);
//        AnimationManagerClass.slide(binding.tvSplashVersion, this);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!sharedPreferences.getBoolean(
                        UtilitiesClass.ONBOARDING_PREF_NAME, false)) {
                    // The user hasn't seen the OnboardingSupportFragment yet, so show it
                    SharedPreferences.Editor sharedPreferencesEditor =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    sharedPreferencesEditor.putBoolean(
                            UtilitiesClass.ONBOARDING_PREF_NAME, true);
                    sharedPreferencesEditor.apply();
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 3000L);
    }
}