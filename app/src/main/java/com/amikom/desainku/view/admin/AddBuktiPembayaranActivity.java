package com.amikom.desainku.view.admin;

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
import com.amikom.desainku.databinding.ActivityAddBuktiPembayaranBinding;
import com.amikom.desainku.model.BuktiPembayaranModel;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddBuktiPembayaranActivity extends AppCompatActivity {

    ActivityAddBuktiPembayaranBinding binding;

    String idBooking;

    Uri imageUri;
    String downloadUrl;
    StorageReference storageReference;

    ProgressDialog registerDialog;
    FirebaseFirestore db;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBuktiPembayaranBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnAddPhotoService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });



        Intent intent = getIntent();

        idBooking = intent.getStringExtra("ID_BOOKING");


        binding.tiIdBooking.setText(idBooking);
        binding.tiEmailPengguna.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keteranganPembayaran = binding.tiKeteranganJasa.getText().toString();
                String pembayaran = binding.tiHargaJasa.getText().toString();

                boolean isFormValid = true;

                if (imageUri == null) {
                    isFormValid = false;
                    Toast.makeText(AddBuktiPembayaranActivity.this, "Mohon pilih Foto Bukti Pembayaran!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(keteranganPembayaran.isEmpty()) {
                    isFormValid = false;
                    binding.tfKeteranganPembayaran.setError("Field ini harus diisi!");
                    return;
                }

                if(pembayaran.isEmpty()) {
                    isFormValid = false;
                    binding.tiHargaJasa.setError("Field ini harus diisi!");
                    return;
                }


                if(isFormValid) {
                    registerDialog.setMessage("Mengupload Bukti Pembayaran...");
                    registerDialog.setTitle("Mohon Bersabar.");
                    registerDialog.setCanceledOnTouchOutside(false);
                    registerDialog.show();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
                    Date now = new Date();
                    String filename = dateFormat.format(now);

                    storageReference = FirebaseStorage.getInstance().getReference("buktiPembayaran" + filename);

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

                                            sendDataToFirestore(keteranganPembayaran, pembayaran);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddBuktiPembayaranActivity.this, "File berhasil di upload namun tidak bisa mendapatkan link download!", Toast.LENGTH_SHORT).show();
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

        registerDialog.setMessage("Mengambil Data...");
        registerDialog.setTitle("Mohon Bersabar.");
        registerDialog.setCanceledOnTouchOutside(false);
        registerDialog.show();

        getUserData();

    }

    private void sendDataToFirestore(String keteranganPembayaran, String pembayaran) {
        String date = UtilitiesClass.getDateNow();

        String idPembayaran = UtilitiesClass.generateRandomString(8);

        BuktiPembayaranModel buktiPembayaranModel = new BuktiPembayaranModel(idBooking, idPembayaran, downloadUrl, pembayaran, keteranganPembayaran, FirebaseAuth.getInstance().getCurrentUser().getEmail(), date, "0");

        FirebaseFirestore.getInstance().collection("buktiPembayaran").document(idPembayaran).set(buktiPembayaranModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                registerDialog.dismiss();
                Log.d("FIRESTORE", "DocumentSnapshot added with document "+idPembayaran);
                Toast.makeText(AddBuktiPembayaranActivity.this, "Bukti Pembayaran Berhasil Dikirim!", Toast.LENGTH_SHORT).show();

                finish();
                callOwnerViaWhatsapp(buktiPembayaranModel);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerDialog.dismiss();
                Toast.makeText(AddBuktiPembayaranActivity.this, "Bukti Pembayaran Gagal Dikirim! Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
                Log.e("FIRESTORE", "onFailure: gagal upload data jasa ke cloud firestore",e );
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
            binding.ifvPhotoJasaDesign.setImageURI(imageUri);
        }
    }

    private void getUserData() {
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String phoneNumber = documentSnapshot.get("phoneNumber").toString();
                String email = documentSnapshot.get("email").toString();
                String photoUrl = documentSnapshot.get("photoUrl").toString();
                String dateCreated = documentSnapshot.get("dateCreated").toString();
                String userType = documentSnapshot.get("userType").toString();

                registerDialog.dismiss();

                userModel = new UserModel(name, phoneNumber, email, photoUrl, dateCreated, userType);
            }
        });
    }

    private void callOwnerViaWhatsapp(BuktiPembayaranModel buktiPembayaranModel) {
        String message = "Hi kak, saya mau Konfirmasi Pembayaran Jasa Dengan Detail Sebagai Berikut : " +
                "\n\nDetail Pemesan : " +
                "\n Nama : " + userModel.getName() + "" +
                "\n Email : " + userModel.getEmail() + "" +
                "\n No HP : " + userModel.getPhoneNumber() +"\n" +
                "\n\nDetail Bukti Pembayaran : " + "\n" +
                "\n ID Pembayaran : " + buktiPembayaranModel.getIdPembayaran() + "" +
                "\n ID Booking : " + buktiPembayaranModel.getIdBooking() + "" +
                "\n Jumlah Yang Dibayarkan : " + UtilitiesClass.formatRupiah(Integer.parseInt(buktiPembayaranModel.getPembayaran())) + "" +
                "\n Tanggal / Waktu Pembayaran : " + buktiPembayaranModel.getDateCreated()+  "" +
                "\n Keterangan Tambahan : " + buktiPembayaranModel.getKeteranganPembayaran() + "" +
                "\n\n Mohon segera dikonfirmasi pembayarannya terima kasih.";


        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "+6285942104143" +
                "&text=" + message));

        startActivity(i);
    }
}