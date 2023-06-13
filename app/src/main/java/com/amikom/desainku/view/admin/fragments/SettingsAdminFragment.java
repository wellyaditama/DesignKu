package com.amikom.desainku.view.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.FragmentSettingsAdminBinding;


public class SettingsAdminFragment extends Fragment {

    FragmentSettingsAdminBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSettingsAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}