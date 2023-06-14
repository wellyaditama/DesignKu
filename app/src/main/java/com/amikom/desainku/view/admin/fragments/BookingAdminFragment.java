package com.amikom.desainku.view.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.FragmentBookingAdminBinding;
import com.amikom.desainku.view.admin.BookingListActivity;


public class BookingAdminFragment extends Fragment {

    FragmentBookingAdminBinding binding;

    String userType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingAdminBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        userType = bundle.getString("userType");
        return binding.getRoot();



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cvBookingSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookingListActivity.class);
                intent.putExtra("userType", userType);
                startActivity(intent);
            }
        });

        if(userType.equals("User")) {
            binding.tvBookingAll.setText("Daftar Booking Anda");
        }
    }
}