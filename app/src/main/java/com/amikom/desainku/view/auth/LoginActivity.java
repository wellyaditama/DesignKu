package com.amikom.desainku.view.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityLoginBinding;
import com.amikom.desainku.databinding.DialogForgotPasswordBinding;
import com.amikom.desainku.view.admin.HomeAdminActivity;
import com.amikom.desainku.view.user.HomeUserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    ProgressDialog registerDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String emailPattern;

    DialogForgotPasswordBinding dialogForgotPasswordBinding;
    Dialog dialogForgotPassword;

    TextView tvLupaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        dialogForgotPasswordBinding = DialogForgotPasswordBinding.inflate(getLayoutInflater());
        dialogForgotPassword = new Dialog(this, R.style.DialogStyle);
        dialogForgotPassword.setContentView(dialogForgotPasswordBinding.getRoot());

        registerDialog = new ProgressDialog(this);

        tvLupaPassword = (TextView) findViewById(R.id.tv_lupa_passwordsss);

        tvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogForgotPassword.show();

                dialogForgotPasswordBinding.ifvCloseShowroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogForgotPassword.dismiss();
                    }
                });

                dialogForgotPasswordBinding.btnDialogSendForgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String forgotEmail = dialogForgotPasswordBinding.tiForgotEmail.getText().toString();

                        boolean isValid = true;

                        if (forgotEmail.isEmpty()) {
                            isValid = false;
                            dialogForgotPasswordBinding.tiForgotEmail.setError("Field ini harus diisi!");
                        }

                        if (!forgotEmail.matches(emailPattern)) {
                            isValid = false;
                            dialogForgotPasswordBinding.tiForgotEmail.setError("Isi dengan email yang valid!");
                        }

                        if (isValid) {
                            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                            progressDialog.setTitle("Mohon tunggu");
                            progressDialog.setMessage("Mengirim Email Reset Password...");
                            progressDialog.setCanceledOnTouchOutside(false);

                            progressDialog.show();

                            FirebaseAuth.getInstance().sendPasswordResetEmail(forgotEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    dialogForgotPassword.dismiss();

                                    Toast.makeText(LoginActivity.this, "Berhasil mengirimkan form lupa password ke email anda!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    dialogForgotPassword.dismiss();

                                    Toast.makeText(LoginActivity.this, "Gagal mengirimkan form lupa password, cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });


        if (mUser != null) {
            registerDialog.setMessage("Mohon tunggu...");
            registerDialog.setTitle("Login ke Akun Anda.");
            registerDialog.setCanceledOnTouchOutside(false);
            registerDialog.show();
            String email = mUser.getEmail();
//            if (!mUser.isEmailVerified()) {
//                Toast.makeText(this, "Email anda belum terverifikasi", Toast.LENGTH_SHORT).show();
//                registerDialog.dismiss();
//            } else {
            db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> user = documentSnapshot.getData();

                    String userType = user.get("userType").toString();
                    registerDialog.dismiss();
                    sendUserToHomeActivity(userType);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("FIRESTOREAUTH", "onFailure: Gagal mengambil data login dengan document email " + email, e);
                    registerDialog.dismiss();
                }
            });
//            }
        }

        binding.btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDialog.setTitle("Mohon Tunggu");
                registerDialog.setMessage("Login ke Akun Anda");
                registerDialog.show();

                String email;
                String password;

                email = binding.tiEmail.getText().toString();
                password = binding.tiPassword.getText().toString();

                if (email.isEmpty()) {
                    binding.tiEmail.setError("Email harus Diisi!");
                    return;
                }
                if (password.isEmpty()) {
                    binding.tiPassword.setError("Password harus Diisi!");
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
//                        if (authResult.getUser().isEmailVerified()) {
                        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> user = documentSnapshot.getData();

                                String userType = user.get("userType").toString();
                                registerDialog.dismiss();
                                sendUserToHomeActivity(userType);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIRESTOREAUTH", "onFailure: Gagal mengambil data login dengan document email " + email, e);
                                registerDialog.dismiss();
                            }
                        });
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Akun anda belum diverifikasi! Cek inbox!", Toast.LENGTH_SHORT).show();
//                            registerDialog.dismiss();
//                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Username / Password salah", Toast.LENGTH_SHORT).show();
                        registerDialog.dismiss();
                    }
                });

            }
        });
    }

    private void sendUserToHomeActivity(String userType) {
        Intent intentAdmin = new Intent(LoginActivity.this, HomeAdminActivity.class);
        Intent intentUser = new Intent(LoginActivity.this, HomeUserActivity.class);

        if (userType.equals("User")) {
            startActivity(intentUser);
            finish();
        } else {
            startActivity(intentAdmin);
            finish();
        }


    }
}