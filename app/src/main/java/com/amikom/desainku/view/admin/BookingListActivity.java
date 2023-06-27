package com.amikom.desainku.view.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.adapter.AdapterBookingService;
import com.amikom.desainku.databinding.ActivityBookingListBinding;
import com.amikom.desainku.databinding.OptionDialogBinding;
import com.amikom.desainku.model.DesignBookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingListActivity extends AppCompatActivity {

    ActivityBookingListBinding binding;

    ArrayList<DesignBookingModel> designBookingModels;

    AdapterBookingService adapterBookingService;

    Dialog optionDialog;
    Dialog detailBooking;


    OptionDialogBinding optionDialogBinding;

    ProgressDialog progressDialog;

    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        designBookingModels = new ArrayList<>();

        Intent intent = getIntent();

        userType = intent.getStringExtra("userType");


        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(this));
        optionDialog = new Dialog(this, R.style.DialogStyle);
        optionDialogBinding = OptionDialogBinding.inflate(getLayoutInflater());
        optionDialog.setContentView(optionDialogBinding.getRoot());

        if(userType.equals("User")) {
            optionDialogBinding.tvDeleteReport.setVisibility(View.GONE);
        }
            optionDialogBinding.tvBook.setVisibility(View.GONE);



        progressDialog = new ProgressDialog(this);

        // TODO : CREATE DETAIL BOOKING

//        getDataBooking();




    }

    private void getDataBooking() {

        showShimmer(true);

        designBookingModels.clear();

        Log.d("TAG", "Email Pemesan : " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if(userType.equals("User")) {
            FirebaseFirestore.getInstance().collection("booking").whereEqualTo("emailPemesan", FirebaseAuth.getInstance().getCurrentUser().getEmail()).orderBy("dateCreated", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            FirebaseFirestore.getInstance().collection("booking").whereEqualTo("emailPemesan", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<DocumentSnapshot> bookingList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot s : bookingList) {
                            String idJasa = s.get("idJasa").toString();
                            String idBooking = s.get("idBooking").toString();
                            String emailPemesan = s.get("emailPemesan").toString();
                            String namaPemesan = s.get("namaPemesan").toString();
                            String noHpPemesan = s.get("noHpPemesan").toString();
                            String keterangan = s.get("keterangan").toString();
                            String statusPembayaran = s.get("statusPembayaran").toString();
                            String statusPengerjaan = s.get("statusPengerjaan").toString();
                            String dateCreated = s.get("dateCreated").toString();
                            String harga = s.get("harga").toString();
                            String dibayarkan = s.get("dibayarkan").toString();

                            DesignBookingModel dsm = new DesignBookingModel(
                                    idJasa,
                                    idBooking,
                                    emailPemesan,
                                    namaPemesan,
                                    noHpPemesan,
                                    keterangan,
                                    statusPembayaran,
                                    statusPengerjaan,
                                    dateCreated,
                                    harga, dibayarkan);

                            designBookingModels.add(dsm);
                        }

                        showShimmer(false);

                        adapterBookingService = new AdapterBookingService(designBookingModels);
                        adapterBookingService.setOnClickBookingCallback(bookingModel -> onServiceClickCallback(bookingModel));
                        binding.rvMain.setAdapter(adapterBookingService);

                        binding.tiEmail.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapterBookingService.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        adapterBookingService.notifyDataSetChanged();
                    } else {
                        showShimmer(false);
                        Toast.makeText(BookingListActivity.this, "Data Booking Kosong!", Toast.LENGTH_SHORT).show();
                        adapterBookingService = new AdapterBookingService(designBookingModels);
                        adapterBookingService.setOnClickBookingCallback(bookingModel -> onServiceClickCallback(bookingModel));
                        binding.rvMain.setAdapter(adapterBookingService);
                        adapterBookingService.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showShimmer(false);
                    Toast.makeText(BookingListActivity.this, "Gagal mendapatkan data Booking Design! Cek koneksi Internet Anda!", Toast.LENGTH_LONG).show();

                }
            });
        } else {

            FirebaseFirestore.getInstance().collection("booking").orderBy("dateCreated", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<DocumentSnapshot> bookingList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot s : bookingList) {
                            String idJasa = s.get("idJasa").toString();
                            String idBooking = s.get("idBooking").toString();
                            String emailPemesan = s.get("emailPemesan").toString();
                            String namaPemesan = s.get("namaPemesan").toString();
                            String noHpPemesan = s.get("noHpPemesan").toString();
                            String keterangan = s.get("keterangan").toString();
                            String statusPembayaran = s.get("statusPembayaran").toString();
                            String statusPengerjaan = s.get("statusPengerjaan").toString();
                            String dateCreated = s.get("dateCreated").toString();
                            String harga = s.get("harga").toString();
                            String dibayarkan = s.get("dibayarkan").toString();

                            DesignBookingModel dsm = new DesignBookingModel(
                                    idJasa,
                                    idBooking,
                                    emailPemesan,
                                    namaPemesan,
                                    noHpPemesan,
                                    keterangan,
                                    statusPembayaran,
                                    statusPengerjaan,
                                    dateCreated,
                                    harga,
                                    dibayarkan);

                            designBookingModels.add(dsm);
                        }

                        showShimmer(false);

                        adapterBookingService = new AdapterBookingService(designBookingModels);
                        adapterBookingService.setOnClickBookingCallback(bookingModel -> onServiceClickCallback(bookingModel));
                        binding.rvMain.setAdapter(adapterBookingService);

                        binding.tiEmail.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                adapterBookingService.getFilter().filter(charSequence);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });


                        adapterBookingService.notifyDataSetChanged();
                    } else {
                        showShimmer(false);
                        Toast.makeText(BookingListActivity.this, "Data Booking Kosong!", Toast.LENGTH_SHORT).show();
                        adapterBookingService = new AdapterBookingService(designBookingModels);
                        adapterBookingService.setOnClickBookingCallback(bookingModel -> onServiceClickCallback(bookingModel));
                        binding.rvMain.setAdapter(adapterBookingService);
                        adapterBookingService.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showShimmer(false);
                    Toast.makeText(BookingListActivity.this, "Gagal mendapatkan data Booking Design! Cek koneksi Internet Anda!", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void onServiceClickCallback(DesignBookingModel bookingModel) {
        optionDialog.show();

        optionDialogBinding.tvUpdateReport.setVisibility(View.GONE);

        optionDialogBinding.tvDeleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingListActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Peringatan!");
                builder.setMessage("Apakah anda yakin ingin menghapus data booking " + bookingModel.getNamaPemesan() + "?");

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setTitle("Mohon Tunggu");
                        progressDialog.setMessage("Menghapus data booking...");
                        progressDialog.show();

                        FirebaseFirestore.getInstance().collection("booking").document(bookingModel.getIdBooking()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(BookingListActivity.this, "Data Berhasil di Hapus!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();

                                Log.d("FIRESTORE", "onFailure : berhasil menghapus data Booking dengan ID" + bookingModel.getIdBooking());
                                getDataBooking();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BookingListActivity.this, "Gagal menghapus data booking. Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();
                                Log.e("FIRESTORE", "gagal : Gagal menghapus data booking", e);
                                getDataBooking();

                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        optionDialogBinding.tvViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingListActivity.this, DetailBookingActivity.class);

                intent.putExtra("BOOKING_MODEL", bookingModel);
                intent.putExtra("userType", userType);
                optionDialog.dismiss();
                startActivity(intent);
            }
        });
    }

    private void showShimmer(boolean b) {
        if (b) {
            binding.rvMain.setVisibility(View.GONE);
            binding.shimmerList.setVisibility(View.VISIBLE);
        } else {
            binding.rvMain.setVisibility(View.VISIBLE);
            binding.shimmerList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataBooking();
    }
}