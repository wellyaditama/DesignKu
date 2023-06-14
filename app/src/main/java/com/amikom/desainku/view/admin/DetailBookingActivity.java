package com.amikom.desainku.view.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityDetailBookingBinding;
import com.amikom.desainku.databinding.UpdateStatusPembayaranBinding;
import com.amikom.desainku.databinding.UpdateStatusPengerjaanBinding;
import com.amikom.desainku.model.DesignBookingModel;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.model.UserModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DetailBookingActivity extends AppCompatActivity {

    ActivityDetailBookingBinding binding;

    ProgressDialog registerDialog;
    FirebaseFirestore db;

    UserModel userModel;

    DesignBookingModel designBookingModel;
    DesignServiceModel designServiceModel;

    Dialog dialogUpdatePembayaran;
    Dialog dialogUpdatePengerjaan;

    UpdateStatusPembayaranBinding updateStatusPembayaranBinding;
    UpdateStatusPengerjaanBinding updateStatusPengerjaanBinding;

    String statusPembayaran;
    String statusPengerjaan;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ifvCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        statusPembayaran = "1";
        statusPengerjaan = "1";

        registerDialog = new ProgressDialog(this);
        registerDialog.setTitle("Mohon Tunggu");
        registerDialog.setMessage("Mengambil Data...");
        registerDialog.setCanceledOnTouchOutside(false);
        registerDialog.show();


        designBookingModel = (DesignBookingModel) intent.getSerializableExtra("BOOKING_MODEL");
        userType = intent.getStringExtra("userType");


        dialogUpdatePembayaran = new Dialog(this, R.style.DialogStyle);
        updateStatusPembayaranBinding = UpdateStatusPembayaranBinding.inflate(getLayoutInflater());
        dialogUpdatePembayaran.setContentView(updateStatusPembayaranBinding.getRoot());

        dialogUpdatePengerjaan = new Dialog(this, R.style.DialogStyle);
        updateStatusPengerjaanBinding = UpdateStatusPengerjaanBinding.inflate(getLayoutInflater());
        dialogUpdatePengerjaan.setContentView(updateStatusPengerjaanBinding.getRoot());

        if(userType.equals("User")) {
            binding.btnChangeStatus.setVisibility(View.GONE);
            binding.btnChangeStatusPembayaran.setVisibility(View.GONE);
        }


        binding.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdatePembayaran.show();

                updateStatusPembayaranBinding.ifvCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogUpdatePembayaran.dismiss();
                    }
                });

                if(designBookingModel.getStatusPembayaran().equals("1")) {
                    updateStatusPembayaranBinding.rbBelumDibayar.setChecked(true);
                    statusPembayaran = "1";
                } else if(designBookingModel.getStatusPembayaran().equals("2")) {
                    updateStatusPembayaranBinding.rbSudahDibayar.setChecked(true);
                    statusPembayaran = "2";
                }

                updateStatusPembayaranBinding.btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        registerDialog.setTitle("Mohon Tunggu");
                        registerDialog.setMessage("Mengubah Status Pembayaran");
                        registerDialog.setCanceledOnTouchOutside(false);
                        registerDialog.show();

                        db.collection("booking").document(designBookingModel.getIdBooking()).update("statusPembayaran", statusPembayaran).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                registerDialog.dismiss();
                                dialogUpdatePembayaran.dismiss();
                                Toast.makeText(DetailBookingActivity.this, "Status Pembayaran Berhasil Diubah!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });

            }
        });

        binding.btnChangeStatusPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdatePengerjaan.show();

                updateStatusPengerjaanBinding.ifvCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogUpdatePengerjaan.dismiss();
                    }
                });

                if(designBookingModel.getStatusPengerjaan().equals("1")) {
                    updateStatusPengerjaanBinding.rbDalamPengerjaan.setChecked(true);
                    statusPengerjaan = "1";
                } else if(designBookingModel.getStatusPengerjaan().equals("2")) {
                    updateStatusPengerjaanBinding.rbRevisi.setChecked(true);
                    statusPengerjaan = "2";
                } else if(designBookingModel.getStatusPengerjaan().equals("3")) {
                    updateStatusPengerjaanBinding.rbSelesai.setChecked(true);
                    statusPengerjaan = "3";
                } else if(designBookingModel.getStatusPembayaran().equals("4")) {
                    updateStatusPengerjaanBinding.rbDibatalkan.setChecked(true);
                    statusPengerjaan = "4";
                }

                updateStatusPengerjaanBinding.btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        registerDialog.setTitle("Mohon Tunggu");
                        registerDialog.setMessage("Mengubah Status Pembayaran");
                        registerDialog.setCanceledOnTouchOutside(false);
                        registerDialog.show();

                        db.collection("booking").document(designBookingModel.getIdBooking()).update("statusPengerjaan", statusPengerjaan).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                registerDialog.dismiss();
                                dialogUpdatePengerjaan.dismiss();
                                Toast.makeText(DetailBookingActivity.this, "Status Pengerjaan Berhasil Diubah!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });


    }

    private void getDataJasa(String idJasa) {
        db.collection("jasaDesign").document(idJasa).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String idJasa = documentSnapshot.get("idJasa").toString();
                String namaJasa = documentSnapshot.get("namaJasa").toString();
                String keterangan = documentSnapshot.get("keterangan").toString();
                int harga = Integer.valueOf(documentSnapshot.get("harga").toString());
                int lamapengerjaan = Integer.valueOf(documentSnapshot.get("lamapengerjaan").toString());
                String ketersediaan = documentSnapshot.get("ketersediaan").toString();
                int jumlahPemesanan = Integer.valueOf(documentSnapshot.get("jumlahPemesanan").toString());
                String dateCreated = documentSnapshot.get("dateCreated").toString();
                String gambar = documentSnapshot.get("gambar").toString();

                designServiceModel = new DesignServiceModel(
                        idJasa,
                        namaJasa,
                        keterangan,
                        harga,
                        lamapengerjaan,
                        ketersediaan,
                        jumlahPemesanan,
                        dateCreated,
                        gambar
                );

                Picasso.get().load(gambar).into(binding.ivDetailServicePicture);

                binding.tvNamaJasa.setText(namaJasa);
                binding.tvDescription.setText(keterangan);
                binding.tvPrice.setText(UtilitiesClass.formatRupiah(harga));
                binding.tvLamaPengerjaan.setText(String.valueOf(lamapengerjaan) + " Hari");

                binding.tvTanggalPemesanan.setText(designBookingModel.getDateCreated());

                getDataUser(designBookingModel.getEmailPemesan());
            }
        });
    }

    private void getDataUser(String emailPemesan) {
        db.collection("users").document(emailPemesan).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.get("name").toString();
                String phoneNumber = documentSnapshot.get("phoneNumber").toString();
                String email = documentSnapshot.get("email").toString();
                String photoUrl = documentSnapshot.get("photoUrl").toString();
                String dateCreated = documentSnapshot.get("dateCreated").toString();
                String userType = documentSnapshot.get("userType").toString();

                Picasso.get().load(photoUrl).into(binding.ivUserProfile);
                userModel = new UserModel(name, phoneNumber, email, photoUrl, dateCreated, userType);

                binding.tvNamaUser.setText(name);
                binding.tvEmailUser.setText(email);
                binding.tvNomorTeleponUser.setText(phoneNumber);

                binding.tvDescriptionBooking.setText(designBookingModel.getKeterangan());

                if(designBookingModel.getStatusPembayaran().equals("1")) {
                    binding.tvStatusPembayaranValue.setText(UtilitiesClass.statusPembayaran[0]);
                } else {
                    binding.tvStatusPembayaranValue.setText(UtilitiesClass.statusPembayaran[1]);
                }

                if(designBookingModel.getStatusPengerjaan().equals("1")) {
                    binding.tvStatusPengerjaanValue.setText(UtilitiesClass.statusPengerjaan[0]);
                } else if(designBookingModel.getStatusPengerjaan().equals("2")) {
                    binding.tvStatusPengerjaanValue.setText(UtilitiesClass.statusPengerjaan[1]);
                } else if(designBookingModel.getStatusPengerjaan().equals("3")) {
                    binding.tvStatusPengerjaanValue.setText(UtilitiesClass.statusPengerjaan[2]);
                } else {
                    binding.tvStatusPengerjaanValue.setText(UtilitiesClass.statusPengerjaan[3]);
                }

                registerDialog.dismiss();
            }
        });
    }


    public void onRadioPengerjaanClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_dalam_pengerjaan:
                if (checked)
                    // Pirates are the best
                    statusPengerjaan = "1";

                break;
            case R.id.rb_revisi:
                if (checked)
                    // Ninjas rule
                    statusPengerjaan = "2";
                break;

            case R.id.rb_selesai:
                if (checked)
                    // Ninjas rule
                    statusPengerjaan = "3";
                break;

            case R.id.rb_dibatalkan:
                if (checked)
                    // Ninjas rule
                    statusPengerjaan = "4";
                break;

            case R.id.rb_belum_dibayar:
                if (checked)
                    // Pirates are the best
                    statusPembayaran = "1";

                break;
            case R.id.rb_sudah_dibayar:
                if (checked)
                    // Ninjas rule
                    statusPembayaran = "2";
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataJasa(designBookingModel.getIdJasa());
    }
}