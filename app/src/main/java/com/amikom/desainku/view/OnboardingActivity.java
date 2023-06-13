package com.amikom.desainku.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityOnboardingBinding;
import com.amikom.desainku.view.auth.LoginActivity;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    ActivityOnboardingBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();
        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForPaperOnboarding());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_onboarding, paperOnboardingFragment);
        fragmentTransaction.commit();

        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
            }
        });
    }

    private ArrayList<PaperOnboardingPage> getDataForPaperOnboarding() {

        PaperOnboardingPage page1 = new PaperOnboardingPage("Mudah Digunakan", "Aplikasi DesignKu mudah digunakan untuk semua kalangan", Color.parseColor("#AD7FFB"), R.drawable.ic_easy_to_use, R.drawable.ic_easy_to_use);
        PaperOnboardingPage page2 = new PaperOnboardingPage("Harga Murah", "Layanan yang tersedia di DesignKu murah", Color.parseColor("#AD7FFB"), R.drawable.ic_low_price, R.drawable.ic_low_price);
        PaperOnboardingPage page3 = new PaperOnboardingPage("Kualitas Bagus", "Pelayanan design di DesignKu memiliki kualitas yang baik", Color.parseColor("#AD7FFB"), R.drawable.ic_top_quality2, R.drawable.ic_top_quality2);
        PaperOnboardingPage page4 = new PaperOnboardingPage("Pengerjaan Cepat", "Pelayanan di Designku dikerjakan dengan cepat mulai dari 1 jam.", Color.parseColor("#AD7FFB"), R.drawable.ic_fast2, R.drawable.ic_fast2);


        ArrayList<PaperOnboardingPage> arrayList = new ArrayList<>();

        arrayList.add(page1);
        arrayList.add(page2);
        arrayList.add(page3);
        arrayList.add(page4);

        return arrayList;

    }
}