package com.amikom.desainku.model;

import java.io.Serializable;

public class DesignServiceModel implements Serializable {

    private String idJasa;
    private String namaJasa;
    private String keterangan;
    private int harga;
    private int lamapengerjaan;
    private String ketersediaan;
    private int jumlahPemesanan;
    private String dateCreated;
    private String gambar;

    public DesignServiceModel(String idJasa, String namaJasa, String keterangan, int harga, int lamapengerjaan, String ketersediaan, int jumlahPemesanan, String dateCreated, String gambar) {
        this.idJasa = idJasa;
        this.namaJasa = namaJasa;
        this.keterangan = keterangan;
        this.harga = harga;
        this.lamapengerjaan = lamapengerjaan;
        this.ketersediaan = ketersediaan;
        this.jumlahPemesanan = jumlahPemesanan;
        this.dateCreated = dateCreated;
        this.gambar = gambar;
    }


    public String getIdJasa() {
        return idJasa;
    }

    public void setIdJasa(String idJasa) {
        this.idJasa = idJasa;
    }

    public String getNamaJasa() {
        return namaJasa;
    }

    public void setNamaJasa(String namaJasa) {
        this.namaJasa = namaJasa;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getLamapengerjaan() {
        return lamapengerjaan;
    }

    public void setLamapengerjaan(int lamapengerjaan) {
        this.lamapengerjaan = lamapengerjaan;
    }

    public String getKetersediaan() {
        return ketersediaan;
    }

    public void setKetersediaan(String ketersediaan) {
        this.ketersediaan = ketersediaan;
    }

    public int getJumlahPemesanan() {
        return jumlahPemesanan;
    }

    public void setJumlahPemesanan(int jumlahPemesanan) {
        this.jumlahPemesanan = jumlahPemesanan;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

}
