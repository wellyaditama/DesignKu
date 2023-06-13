package com.amikom.desainku.view.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityRegisterBinding;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    Uri imageUri;

    ProgressDialog registerDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    String downloadUrl;
    StorageReference storageReference;

    String tipeAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        registerDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = binding.tiNama.getText().toString();
                String email = binding.tiEmail.getText().toString();
                String noHp = binding.tiPhone.getText().toString();
                String password = binding.tiPassword.getText().toString();
                String confirmPassword = binding.tiConfirmPassword.getText().toString();
                tipeAkun = "User";

                boolean isFormValid = true;

                if (imageUri == null) {
                    isFormValid = false;
                    Toast.makeText(RegisterActivity.this, "Mohon pilih Foto Profil Anda!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nama.isEmpty()) {
                    isFormValid = false;
                    binding.tiNama.setError("Field Nama Tidak Boleh Kosong!");
                    return;
                }

                if (email.isEmpty()) {
                    isFormValid = false;
                    binding.tiEmail.setError("Field Email Tidak Boleh Kosong!");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    isFormValid = false;
                    binding.tiEmail.setError("Format email tidak sesuai!");
                    return;
                }
                if (noHp.isEmpty()) {
                    isFormValid = false;
                    binding.tiPhone.setError("Field No HP Tidak Boleh Kosong!");
                    return;
                }
                if (password.isEmpty()) {
                    isFormValid = false;
                    binding.tiPassword.setError("Field Password Tidak Boleh Kosong!");
                    return;
                }

                if (password.length() < 8) {
                    isFormValid = false;
                    binding.tiPassword.setError("Minimal password 8 karakter!");
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    isFormValid = false;
                    binding.tiConfirmPassword.setError("Field ini Tidak Boleh Kosong!");
                    return;
                }

                if (password.length() < 8) {
                    isFormValid = false;
                    binding.tiConfirmPassword.setError("Minimal konfirmasi password 8 karakter!");
                    return;
                }

                if(!password.equals(confirmPassword)) {
                    isFormValid = false;
                    binding.tiConfirmPassword.setError("Konfirmasi password tidak sama dengan password!");
                    return;
                }

                if(isFormValid) {
                    registerDialog.setMessage("Membuat akun anda...");
                    registerDialog.setTitle("Mohon Bersabar.");
                    registerDialog.setCanceledOnTouchOutside(false);
                    registerDialog.show();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
                    Date now = new Date();
                    String filename = dateFormat.format(now);
                    storageReference = FirebaseStorage.getInstance().getReference("profilePictureUser" + filename);

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

                                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        createuserRegisterFirestore(email, noHp, nama, tipeAkun, downloadUrl);
                                                        Log.d("FIRESTORE", "onComplete: successfully upload image");
                                                    } else {
                                                        registerDialog.dismiss();
                                                        Snackbar.make(view, "Akun anda gagal dibuat!", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    registerDialog.dismiss();
                                                    Log.e("FIREBASEAUTH", "onFailure: Gagal mendaftarkan akun dengan email " + email, e);
                                                    Snackbar.make(view, "Akun anda gagal dibuat!", Snackbar.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "File berhasil di upload namun tidak bisa mendapatkan link download!", Toast.LENGTH_SHORT).show();
                                            registerDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            registerDialog.dismiss();
                            Log.e("FIRESTORE", "onFailure: Gagal mengupload foto profil ke cloud firestore", e);
                            Snackbar.make(view, "Akun anda gagal dibuat", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    private void createuserRegisterFirestore(String email, String noHp, String nama, String tipeAkun, String downloadUrl) {

        String date = UtilitiesClass.getDateNow();

        UserModel sm = new UserModel(nama, noHp, email, downloadUrl, date, tipeAkun);

        FirebaseFirestore.getInstance().collection("users").document(email).set(sm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FIRESTORE", "DocumentSnapshot added with document "+email);
                Toast.makeText(RegisterActivity.this, "Akun anda berhasil dibuat!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        registerDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerDialog.dismiss();
                Log.e("FIRESTORE", "onFailure: gagal upload data register ke cloud firestore",e );
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
            binding.ivProfilePicture.setImageURI(imageUri);
        }
    }
}