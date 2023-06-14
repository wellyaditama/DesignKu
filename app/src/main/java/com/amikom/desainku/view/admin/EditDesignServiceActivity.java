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
import android.widget.RadioButton;
import android.widget.Toast;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ActivityEditDesignServiceBinding;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDesignServiceActivity extends AppCompatActivity {

    ActivityEditDesignServiceBinding binding;

    ProgressDialog progressDialog;

    Uri imageUri;
    String downloadUrl;
    StorageReference storageReference;

    String ketersediaan;
    ProgressDialog registerDialog;
    FirebaseFirestore db;

    DesignServiceModel designServiceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDesignServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ketersediaan = "Tersedia";
        registerDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        binding.btnAddPhotoService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        designServiceModel = (DesignServiceModel) intent.getSerializableExtra("DESIGN_SERVICE");

        Picasso.get().load(designServiceModel.getGambar()).into(binding.ifvPhotoJasaDesign);
//
        binding.tiNamaJasa.setText(designServiceModel.getNamaJasa());
        binding.tiHargaJasa.setText(String.valueOf(designServiceModel.getHarga()));
        binding.tiLamaPengerjaan.setText(String.valueOf(designServiceModel.getLamapengerjaan()));
        binding.tiKeteranganJasa.setText(designServiceModel.getKeterangan());


        if(designServiceModel.getKetersediaan().equals("Tersedia")) {
            binding.rbKetersediaanTersedia.setChecked(true);
            ketersediaan = "Tersedia";
        } else {
            binding.rbKetersediaanTidakTersedia.setChecked(true);
            ketersediaan = "Tidak Tersedia";
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idJasa;
                String namaJasa = binding.tiNamaJasa.getText().toString();
                String keterangan = binding.tiKeteranganJasa.getText().toString();
                String harga = binding.tiHargaJasa.getText().toString();
                String lamapengerjaan = binding.tiLamaPengerjaan.getText().toString();
                int jumlahPemesanan = 0;
                String dateCreated;

                boolean isFormValid = true;

                if (imageUri == null) {
                    isFormValid = false;
                    Toast.makeText(EditDesignServiceActivity.this, "Mohon pilih Foto Jasa Design Anda!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(namaJasa.isEmpty()) {
                    isFormValid = false;
                    binding.tiNamaJasa.setError("Field ini harus diisi!");
                    return;
                }

                if(keterangan.isEmpty()) {
                    isFormValid = false;
                    binding.tiKeteranganJasa.setError("Field ini harus diisi!");
                    return;
                }

                if(harga.isEmpty()) {
                    isFormValid = false;
                    binding.tiHargaJasa.setError("Field ini harus diisi!");
                    return;
                }

                if(lamapengerjaan.isEmpty()) {
                    isFormValid = false;
                    binding.tiLamaPengerjaan.setError("Field ini harus diisi!");
                    return;
                }

                if(isFormValid) {
                    registerDialog.setMessage("Mengubah Jasa Design...");
                    registerDialog.setTitle("Mohon Bersabar.");
                    registerDialog.setCanceledOnTouchOutside(false);
                    registerDialog.show();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
                    Date now = new Date();
                    String filename = dateFormat.format(now);

                    storageReference = FirebaseStorage.getInstance().getReference("fotoJasaDesign" + filename);

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

                                            sendDataToFirestore(namaJasa, keterangan, harga, lamapengerjaan, jumlahPemesanan, designServiceModel.getIdJasa());

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditDesignServiceActivity.this, "File berhasil di upload namun tidak bisa mendapatkan link download!", Toast.LENGTH_SHORT).show();
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



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void sendDataToFirestore(String namaJasa, String keterangan, String harga, String lamapengerjaan, int jumlahPemesanan, String idJasa) {
        String date = UtilitiesClass.getDateNow();



        DesignServiceModel dsm = new DesignServiceModel(idJasa, namaJasa, keterangan, Integer.parseInt(harga), Integer.parseInt(lamapengerjaan), ketersediaan, jumlahPemesanan, date, downloadUrl);

        FirebaseFirestore.getInstance().collection("jasaDesign").document(idJasa).set(dsm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                registerDialog.dismiss();
                Log.d("FIRESTORE", "DocumentSnapshot added with document "+idJasa);
                Toast.makeText(EditDesignServiceActivity.this, "Jasa Desain baru berhasil dibuat!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerDialog.dismiss();
                Toast.makeText(EditDesignServiceActivity.this, "Jasa Desain gagal dibuat! Cek koneksi Internet Anda!", Toast.LENGTH_SHORT).show();
                Log.e("FIRESTORE", "onFailure: gagal upload data jasa ke cloud firestore",e );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.ifvPhotoJasaDesign.setImageURI(imageUri);
        }
    }

    public void onRadioKetersediaanClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_ketersediaan_tersedia:
                if (checked)
                    // Pirates are the best
                    ketersediaan = "Tersedia";

                break;
            case R.id.rb_ketersediaan_tidak_tersedia:
                if (checked)
                    // Ninjas rule
                    ketersediaan = "Tidak Tersedia";
                break;
        }

    }
}