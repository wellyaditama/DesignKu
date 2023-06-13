package com.amikom.desainku.view.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.FragmentBookingAdminBinding;


public class BookingAdminFragment extends Fragment {

    FragmentBookingAdminBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}