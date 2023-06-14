package com.amikom.desainku.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityBookDesignBinding;
import com.amikom.desainku.model.DesignBookingModel;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.amikom.desainku.view.auth.LoginActivity;
import com.amikom.desainku.view.auth.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookDesignActivity extends AppCompatActivity {

    ActivityBookDesignBinding binding;

    FirebaseFirestore db;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DesignServiceModel designServiceModel;
    UserModel userModel;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDesignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();



        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        designServiceModel = (DesignServiceModel) intent.getSerializableExtra("DESIGN_SERVICE");

        binding.tvNamaDesign.setText(designServiceModel.getNamaJasa());
        binding.tvDescription.setText(designServiceModel.getKeterangan());
        binding.tvPrice.setText(UtilitiesClass.formatRupiah(designServiceModel.getHarga()));
        binding.tvLamaPengerjaan.setText(String.valueOf(designServiceModel.getLamapengerjaan()) + " Hari");
        
        progressDialog.setTitle("Mohon Bersabar");
        progressDialog.setMessage("Mengambil Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        getUserData();

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = binding.tiNamaJasa.getText().toString();
                String email = binding.tiEmailJasa.getText().toString();
                String nohp = binding.tiNoHp.getText().toString();
                String keterangan = binding.tiKeteranganJasa.getText().toString();

                boolean isFormValid = true;

                if (nama.isEmpty()) {
                    isFormValid = false;
                    binding.tiNamaJasa.setError("Field Nama Tidak Boleh Kosong!");
                    return;
                }

                if (email.isEmpty()) {
                    isFormValid = false;
                    binding.tiEmailJasa.setError("Field Email Tidak Boleh Kosong!");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    isFormValid = false;
                    binding.tiEmailJasa.setError("Format email tidak sesuai!");
                    return;
                }
                if (nohp.isEmpty()) {
                    isFormValid = false;
                    binding.tiNoHp.setError("Field No HP Tidak Boleh Kosong!");
                    return;
                }

                if (keterangan.isEmpty()) {
                    isFormValid = false;
                    binding.tiNoHp.setError("Field Keterangan Tambahan Tidak Boleh Kosong!");
                    return;
                }

                if(isFormValid) {

                    progressDialog.setTitle("Mohon Tunggu");
                    progressDialog.setMessage("Menyimpan Data Booking...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    String date = UtilitiesClass.getDateNow();
                    String idBooking = UtilitiesClass.generateRandomString(10);

                    DesignBookingModel designBookingModel = new DesignBookingModel(designServiceModel.getIdJasa(), idBooking, email, nama, nohp, keterangan, "1", "1", date );

                    FirebaseFirestore.getInstance().collection("booking").document(idBooking).set(designBookingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("FIRESTORE", "DocumentSnapshot added with document "+idBooking);
                            Toast.makeText(BookDesignActivity.this, "Booking anda berhasil terkirim!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            callOwnerViaWhatsapp(idBooking, keterangan);

                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(BookDesignActivity.this, "Gagal menyimpan data booking. Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
                            Log.e("FIRESTORE", "onFailure: gagal upload data register ke cloud firestore",e );
                        }
                    });
                }
            }
        });
        


    }

    private void getUserData() {
        db.collection("users").document(firebaseUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String phoneNumber = documentSnapshot.get("phoneNumber").toString();
                String email = documentSnapshot.get("email").toString();
                String photoUrl = documentSnapshot.get("photoUrl").toString();
                String dateCreated = documentSnapshot.get("dateCreated").toString();
                String userType = documentSnapshot.get("userType").toString();

                progressDialog.dismiss();

                userModel = new UserModel(name, phoneNumber, email, photoUrl, dateCreated, userType);

                binding.tiNamaJasa.setText(name);
                binding.tiEmailJasa.setText(email);
                binding.tiNoHp.setText(phoneNumber);
            }
        });
    }

    private void callOwnerViaWhatsapp(String idBooking, String keteranganTambahan) {
        String message = "Hi kak, saya mau pesan jasa Desain melalui Aplikasi DesignKu dengan detail sebagai berikut : " +
                "\n\nDetail Pemesan : " +
                "\n Nama : " + userModel.getName() + "" +
                "\n Email : " + userModel.getEmail() + "" +
                "\n No HP : " + userModel.getPhoneNumber() +"\n" +
                "\n\nDetail Booking Jasa Design : " + "\n" +
                "\n ID Jasa Design : " + designServiceModel.getIdJasa() + "" +
                "\n ID Booking : " + idBooking + "" +
                "\n Nama Layanan : " + designServiceModel.getNamaJasa() + "" +
                "\n Harga Jasa : " + UtilitiesClass.formatRupiah(designServiceModel.getHarga()) + "" +
                "\n Lama Pengerjaan : " + String.valueOf(designServiceModel.getLamapengerjaan()) + " Hari" + "" +
                "\n Keterangan Tambahan : " + keteranganTambahan + "" +
                "\n\n Mohon segera dikonfirmasi pemesanannya terima kasih.";


        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "+6285942104143" +
                "&text=" + message));

        startActivity(i);
    }
}