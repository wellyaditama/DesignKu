package com.amikom.desainku.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdminActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityHomeAdminBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    Bundle mBundle;

    private InterstitialAd mInterstitialAd;


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


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d("TAG", "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d("TAG", "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e("TAG", "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d("TAG", "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("TAG", "Ad showed fullscreen content.");
                            }
                        });

                        mInterstitialAd.show(HomeAdminActivity.this);
                        Log.d("TAG", "The interstitial ad successfully displayed.");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                        mInterstitialAd = null;
                    }
                });


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