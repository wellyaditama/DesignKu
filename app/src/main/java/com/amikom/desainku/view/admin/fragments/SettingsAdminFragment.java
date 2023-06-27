package com.amikom.desainku.view.admin.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.FragmentSettingsAdminBinding;
import com.amikom.desainku.databinding.UpdateProfilBinding;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.amikom.desainku.view.auth.LoginActivity;
import com.amikom.desainku.view.auth.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SettingsAdminFragment extends Fragment {

    FragmentSettingsAdminBinding binding;

    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String email;

    UserModel userModel;

    ProgressDialog progressDialog;
    String userType;

    Dialog editProfileDialog;
    UpdateProfilBinding updateProfilBinding;

    Uri imageUri;

    StorageReference storageReference;

    String downloadUrl;

    String emailPattern;

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

        editProfileDialog = new Dialog(getContext(), R.style.DialogStyle);
        updateProfilBinding = UpdateProfilBinding.inflate(getLayoutInflater());
        editProfileDialog.setContentView(updateProfilBinding.getRoot());

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        getDataUser();



        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileDialog.show();

                Picasso.get().load(userModel.getPhotoUrl()).into(updateProfilBinding.ivProfilePicture);

                updateProfilBinding.tiNama.setText(userModel.getName());
                updateProfilBinding.tiEmail.setText(userModel.getEmail());
                updateProfilBinding.tiPhone.setText(userModel.getPhoneNumber());


                updateProfilBinding.btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseImage();
                    }
                });

                updateProfilBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nama = updateProfilBinding.tiNama.getText().toString();
                        String email = updateProfilBinding.tiEmail.getText().toString();
                        String phone = updateProfilBinding.tiPhone.getText().toString();


                        boolean isFormValid = true;
                        boolean isUserChangePhoto = false;

                        if (imageUri == null) {

                            isUserChangePhoto = true;

                            downloadUrl = userModel.getPhotoUrl();

                            return;
                        }

                        if (nama.isEmpty()) {
                            isFormValid = false;
                            updateProfilBinding.tiNama.setError("Field Nama Tidak Boleh Kosong!");
                            return;
                        }

                        if (email.isEmpty()) {
                            isFormValid = false;
                            updateProfilBinding.tiEmail.setError("Field Email Tidak Boleh Kosong!");
                            return;
                        }
                        if (!email.matches(emailPattern)) {
                            isFormValid = false;
                            updateProfilBinding.tiEmail.setError("Format email tidak sesuai!");
                            return;
                        }
                        if (phone.isEmpty()) {
                            isFormValid = false;
                            updateProfilBinding.tiPhone.setError("Field No HP Tidak Boleh Kosong!");
                            return;
                        }


                        if(isFormValid) {
                            progressDialog.setMessage("Mengubah detail akun...");
                            progressDialog.setTitle("Mohon Bersabar.");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
                            Date now = new Date();
                            String filename = dateFormat.format(now);
                            storageReference = FirebaseStorage.getInstance().getReference("profilePictureUser" + filename);

                            if(isUserChangePhoto) {

                            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if(taskSnapshot.getMetadata()!= null) {
                                        if(taskSnapshot.getMetadata().getReference()!=null) {
                                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    Log.d("CLOUD FIRESTORE", "onComplete: " + task.getResult().toString());

                                                    String downloadUrlImage = task.getResult().toString();
                                                    downloadUrl = downloadUrlImage;

                                                    createuserRegisterFirestore(email, phone, nama, downloadUrl);



                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "File berhasil di upload namun tidak bisa mendapatkan link download!", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Log.e("FIRESTORE", "onFailure: Gagal mengupload foto profil ke cloud firestore", e);
                                    Snackbar.make(view, "Akun anda gagal dibuat", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                            } else {
                                createuserRegisterFirestore(email, phone, nama, downloadUrl);
                            }
                        }



                    }
                });


            }
        });
    }

    private void createuserRegisterFirestore(String email, String phone, String nama, String downloadUrl) {


        UserModel sm = new UserModel(nama, phone, email, downloadUrl, userModel.getDateCreated(), userModel.getUserType());

        FirebaseFirestore.getInstance().collection("users").document(email).set(sm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FIRESTORE", "DocumentSnapshot added with document "+email);
                Toast.makeText(getContext(), "Akun anda berhasil dibuat!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    editProfileDialog.dismiss();
                    getDataUser();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.e("FIRESTORE", "onFailure: gagal ubah data akun ke cloud firestore",e );
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            updateProfilBinding.ivProfilePicture.setImageURI(imageUri);
        }
    }
}