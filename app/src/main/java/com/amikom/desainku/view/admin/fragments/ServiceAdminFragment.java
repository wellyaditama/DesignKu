package com.amikom.desainku.view.admin.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.adapter.AdapterDesignService;
import com.amikom.desainku.databinding.DetailServiceBinding;
import com.amikom.desainku.databinding.FragmentServiceAdminBinding;
import com.amikom.desainku.databinding.OptionDialogBinding;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.amikom.desainku.view.admin.AddDesignServiceActivity;
import com.amikom.desainku.view.admin.BookDesignActivity;
import com.amikom.desainku.view.admin.EditDesignServiceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ServiceAdminFragment extends Fragment {

    FragmentServiceAdminBinding binding;

    ArrayList<DesignServiceModel> designServiceModels;

    AdapterDesignService adapterDesignService;
    Dialog optionDialog;
    Dialog detailService;

    DetailServiceBinding detailServiceBinding;
    OptionDialogBinding optionDialogBinding;

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
        binding = FragmentServiceAdminBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        userType = bundle.getString("userType");


        return binding.getRoot();


    }

    private void getDataService() {
        showShimmer(true);

        designServiceModels.clear();

        FirebaseFirestore.getInstance().collection("jasaDesign").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> designServiceModelList = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot s : designServiceModelList) {
                        String idJasa = s.get("idJasa").toString();
                        String namaJasa = s.get("namaJasa").toString();
                        String keterangan = s.get("keterangan").toString();
                        int harga = Integer.parseInt(s.get("harga").toString());
                        int lamapengerjaan = Integer.parseInt(s.get("lamapengerjaan").toString());
                        String ketersediaan = s.get("ketersediaan").toString();
                        int jumlahPemesanan = Integer.parseInt(s.get("jumlahPemesanan").toString());
                        String dateCreated = s.get("dateCreated").toString();
                        String gambar = s.get("gambar").toString();

                        DesignServiceModel dsm = new DesignServiceModel(idJasa
                                , namaJasa
                                ,keterangan
                                ,harga
                                ,lamapengerjaan
                                ,ketersediaan
                                ,jumlahPemesanan
                                ,dateCreated
                                ,gambar);
                        designServiceModels.add(dsm);
                    }

                    showShimmer(false);

                    adapterDesignService = new AdapterDesignService(designServiceModels);
                    adapterDesignService.setOnClickServiceCallback(serviceModel -> onServiceClickCallback(serviceModel));
                    binding.rvMain.setAdapter(adapterDesignService);

                    binding.tiEmail.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            adapterDesignService.getFilter().filter(charSequence);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });

                    adapterDesignService.notifyDataSetChanged();
                } else {
                    showShimmer(false);
                    Toast.makeText(getContext(), "Data Service Kosong!", Toast.LENGTH_SHORT).show();
                    adapterDesignService = new AdapterDesignService(designServiceModels);
                    adapterDesignService.setOnClickServiceCallback(serviceModel -> onServiceClickCallback(serviceModel));
                    binding.rvMain.setAdapter(adapterDesignService);
                    adapterDesignService.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showShimmer(false);
                Toast.makeText(getContext(), "Gagal mendapatkan data Jasa Design! Cek koneksi Internet Anda!", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void onServiceClickCallback(DesignServiceModel serviceModel) {
        optionDialog.show();

        optionDialogBinding.tvDeleteReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Peringatan!");
                builder.setMessage("Apakah anda yakin ingin menghapus data " + serviceModel.getNamaJasa() + "?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.setMessage("Menghapus data Jasa Service");
                        progressDialog.show();

                        FirebaseFirestore.getInstance().collection("jasaDesign").document(serviceModel.getIdJasa()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();
                                Log.d("FIRESTORE", "onFailure : berhasil menghapus data Service");
                                getDataService();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Gagal Menghapus Data! Cek koneksi Internet anda!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                optionDialog.dismiss();
                                Log.e("FIRESTORE", "gagal : berhasil menghapus data Service", e);
                                getDataService();
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
                detailService.show();

                Picasso.get().load(serviceModel.getGambar()).into(detailServiceBinding.ivDetailServicePicture);

                detailServiceBinding.tvNamaJasa.setText(serviceModel.getNamaJasa());
                detailServiceBinding.tvDescription.setText(serviceModel.getKeterangan());
                detailServiceBinding.tvPrice.setText(UtilitiesClass.formatRupiah(serviceModel.getHarga()));
                detailServiceBinding.tvLamaPengerjaan.setText(serviceModel.getLamapengerjaan() + " Hari");
            }
        });

        optionDialogBinding.tvUpdateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditDesignServiceActivity.class);
                intent.putExtra("DESIGN_SERVICE", serviceModel);
                optionDialog.dismiss();

                startActivity(intent);

            }
        });

        optionDialogBinding.tvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookDesignActivity.class);
                intent.putExtra("DESIGN_SERVICE", serviceModel);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAdminServiceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddDesignServiceActivity.class));
            }
        });




        designServiceModels = new ArrayList<>();

        binding.rvMain.setHasFixedSize(true);
        binding.rvMain.setLayoutManager(new LinearLayoutManager(getContext()));

        optionDialog = new Dialog(getContext(), R.style.DialogStyle);
        optionDialogBinding = OptionDialogBinding.inflate(getLayoutInflater());
        optionDialog.setContentView(optionDialogBinding.getRoot());

        progressDialog = new ProgressDialog(getContext());

        detailService = new Dialog(getContext(), R.style.DialogStyle);
        detailServiceBinding =DetailServiceBinding.inflate(getLayoutInflater());
        detailService.setContentView(detailServiceBinding.getRoot());
        detailServiceBinding.ifvCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailService.dismiss();
            }
        });

        if(userType.equals("User")) {
            binding.btnAdminServiceAdd.setVisibility(View.GONE);

            optionDialogBinding.tvDeleteReport.setVisibility(View.GONE);
            optionDialogBinding.tvUpdateReport.setVisibility(View.GONE);
        }

        if(userType.equals("Admin")) {

            optionDialogBinding.tvBook.setVisibility(View.GONE);
        }

//        getDataService();



    }

    @Override
    public void onResume() {
        super.onResume();

        getDataService();
    }
}