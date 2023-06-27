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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.adapter.AdapterBuktiPembayaran;
import com.amikom.desainku.adapter.AdapterDesignService;
import com.amikom.desainku.databinding.ActivityDaftarBuktiPembayaranBinding;
import com.amikom.desainku.databinding.DetailBuktiPembayaranBinding;
import com.amikom.desainku.databinding.OptionDialogBinding;
import com.amikom.desainku.model.BuktiPembayaranModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class DaftarBuktiPembayaranActivity extends AppCompatActivity {

    ActivityDaftarBuktiPembayaranBinding binding;

    ArrayList<BuktiPembayaranModel> buktiPembayaranModelArrayList;

    AdapterBuktiPembayaran adapterBuktiPembayaran;

    Dialog optionDialog;

    Dialog detailBuktiPembayaran;

    OptionDialogBinding optionDialogBinding;

    DetailBuktiPembayaranBinding detailBuktiPembayaranBinding;

    ProgressDialog progressDialog;

    String idBooking;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaftarBuktiPembayaranBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();

        idBooking = intent.getStringExtra("ID_BOOKING");
        role = intent.getStringExtra("ROLE");

        if(!role.equals("User")) {
            binding.btnUploadBuktiPembayaran.setVisibility(View.GONE);
        }

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnUploadBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaftarBuktiPembayaranActivity.this, AddBuktiPembayaranActivity.class);
                intent.putExtra("ID_BOOKING", idBooking);
                startActivity(intent);
            }
        });

        buktiPembayaranModelArrayList = new ArrayList<>();

        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(this));

        optionDialog = new Dialog(this, R.style.DialogStyle);
        optionDialogBinding = OptionDialogBinding.inflate(getLayoutInflater());
        optionDialog.setContentView(optionDialogBinding.getRoot());

        progressDialog = new ProgressDialog(this);

        //detail service

        detailBuktiPembayaran = new Dialog(this, R.style.DialogStyle);
        detailBuktiPembayaranBinding = DetailBuktiPembayaranBinding.inflate(getLayoutInflater());
        detailBuktiPembayaran.setContentView(detailBuktiPembayaranBinding.getRoot());
        detailBuktiPembayaranBinding.ifvCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailBuktiPembayaran.dismiss();
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
        getDataBuktiPembayaran();

    }

    private void getDataBuktiPembayaran() {
        showShimmer(true);

        buktiPembayaranModelArrayList.clear();

        FirebaseFirestore.getInstance().collection("buktiPembayaran").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> daftarBuktiPembayaranList = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot s : daftarBuktiPembayaranList) {
                        String idBooking = s.get("idBooking").toString();
                        String idPembayaran = s.get("idPembayaran").toString();
                        String image = s.get("image").toString();
                        String pembayaran = s.get("pembayaran").toString();
                        String keteranganPembayaran = s.get("keteranganPembayaran").toString();
                        String emailPembayar = s.get("emailPembayar").toString();
                        String dateCreated = s.get("dateCreated").toString();
                        String isValid = s.get("isValid").toString();

                        BuktiPembayaranModel bpm = new BuktiPembayaranModel(
                                idBooking,
                                idPembayaran,
                                image,
                                pembayaran,
                                keteranganPembayaran,
                                emailPembayar,
                                dateCreated,
                                isValid
                        );

                        buktiPembayaranModelArrayList.add(bpm);
                    }
                    showShimmer(false);
                    adapterBuktiPembayaran = new AdapterBuktiPembayaran(buktiPembayaranModelArrayList);
                    adapterBuktiPembayaran.setOnClickBuktiPembayaranCallback(serviceModel -> onServiceClickCallback(serviceModel));
                    binding.rvMain.setAdapter(adapterBuktiPembayaran);

                    adapterBuktiPembayaran.notifyDataSetChanged();
                } else {
                    showShimmer(false);
                    Toast.makeText(DaftarBuktiPembayaranActivity.this, "Data Bukti Pembayaran Kosong!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(DaftarBuktiPembayaranActivity.this, "Upload Bukti Pembayaran anda!", Toast.LENGTH_SHORT).show();
                    adapterBuktiPembayaran = new AdapterBuktiPembayaran(buktiPembayaranModelArrayList);
                    adapterBuktiPembayaran.setOnClickBuktiPembayaranCallback(serviceModel -> onServiceClickCallback(serviceModel));
                    binding.rvMain.setAdapter(adapterBuktiPembayaran);
                    adapterBuktiPembayaran.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showShimmer(false);
                Toast.makeText(DaftarBuktiPembayaranActivity.this, "Gagal mendapatkan data, cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onServiceClickCallback(BuktiPembayaranModel serviceModel) {

        optionDialog.show();

        optionDialogBinding.tvBook.setVisibility(View.GONE);
        optionDialogBinding.tvUpdateReport.setVisibility(View.GONE);

        if(role.equals("User")) {
            optionDialogBinding.tvDeleteReport.setVisibility(View.GONE);
        }

        optionDialogBinding.tvDeleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DaftarBuktiPembayaranActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Peringatan!");
                builder.setMessage("Apakah anda yakin ingin menghapus data pembayaran milik " + serviceModel.getEmailPembayar() + " dengan ID Pembayaran "+serviceModel.getIdPembayaran()+"?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore.getInstance().collection("buktiPembayaran").document(serviceModel.getIdPembayaran()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DaftarBuktiPembayaranActivity.this, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();
                                Log.d("FIRESTORE", "onFailure : berhasil menghapus data Service");
                                getDataBuktiPembayaran();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DaftarBuktiPembayaranActivity.this, "Gagal Menghapus Data! Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();
                                Log.e("FIRESTORE", "gagal : berhasil menghapus data Service", e);
                                getDataBuktiPembayaran();
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
                detailBuktiPembayaran.show();

                Picasso.get().load(serviceModel.getImage()).into(detailBuktiPembayaranBinding.ivDetailServicePicture);

                detailBuktiPembayaranBinding.tvNamaJasa.setText(serviceModel.getEmailPembayar() + "\nID Pembayaran : " + serviceModel.getIdPembayaran());
                detailBuktiPembayaranBinding.tvPrice.setText(UtilitiesClass.formatRupiah(Integer.parseInt(serviceModel.getPembayaran())));
                detailBuktiPembayaranBinding.tvLamaPengerjaan.setText(serviceModel.getDateCreated());
                detailBuktiPembayaranBinding.tvDescription.setText(serviceModel.getKeteranganPembayaran());
                if(serviceModel.getIsValid().equals("0")) {
                    detailBuktiPembayaranBinding.tvDescription2.setText("Status Pembayaran : \nMenunggu Konfirmasi");
                } else{
                    detailBuktiPembayaranBinding.tvDescription2.setText("Status Pembayaran : \nDisetujui");
                }

                if(!role.equals("User")) {
                    if(serviceModel.getIsValid().equals("0")) {
                        detailBuktiPembayaranBinding.btnKonfirmasiPembayaran.setVisibility(View.VISIBLE);
                    } else {
                        detailBuktiPembayaranBinding.btnKonfirmasiPembayaran.setVisibility(View.INVISIBLE);
                    }

                }



                detailBuktiPembayaranBinding.btnKonfirmasiPembayaran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DaftarBuktiPembayaranActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Peringatan!");
                        builder.setMessage("Apakah anda yakin ingin mengkonfirmasi data pembayaran milik " + serviceModel.getEmailPembayar() + " dengan ID Pembayaran "+serviceModel.getIdPembayaran()+"?");
                        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore.getInstance().collection("buktiPembayaran").document(serviceModel.getIdPembayaran()).update("isValid", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(DaftarBuktiPembayaranActivity.this, "Bukti Pembayaran Berhasil Dikonfirmasi", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                        detailBuktiPembayaran.dismiss();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DaftarBuktiPembayaranActivity.this, "Bukti Pembayaran Gagal Dikonfirmasi! Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog dialog22 = builder.create();
                        dialog22.show();


                    }
                });

            }
        });




    }
}