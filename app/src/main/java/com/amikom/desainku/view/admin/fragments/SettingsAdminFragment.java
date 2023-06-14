package com.amikom.desainku.view.admin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.FragmentSettingsAdminBinding;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.view.auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class SettingsAdminFragment extends Fragment {

    FragmentSettingsAdminBinding binding;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String email;

    UserModel userModel;

    ProgressDialog progressDialog;
    String userType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSettingsAdminBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        userType = bundle.getString("userType");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email = firebaseUser.getEmail();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setMessage("Memuat Data...");
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        getDataUser();



        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void getDataUser() {
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String phoneNumber = documentSnapshot.get("phoneNumber").toString();
                String email = documentSnapshot.get("email").toString();
                String photoUrl = documentSnapshot.get("photoUrl").toString();
                String dateCreated = documentSnapshot.get("dateCreated").toString();

                userModel = new UserModel(
                        name,
                        phoneNumber,
                        email,
                        photoUrl,
                        dateCreated,
                        userType
                );

                progressDialog.dismiss();

                Picasso.get().load(photoUrl).into(binding.cvUserProfile);

                binding.tvUserNama.setText(name);
                binding.tvUserEmail.setText(email);
                binding.tvUserPhone.setText(phoneNumber);
                binding.tvUserType.setText(userType);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(getContext(), "Gagal memuat data! Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}