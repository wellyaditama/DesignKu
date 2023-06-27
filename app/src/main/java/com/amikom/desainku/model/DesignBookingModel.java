package com.amikom.desainku.model;

import java.io.Serializable;

public class DesignBookingModel implements Serializable {

    private String idJasa;
    private String idBooking;
    private String emailPemesan;
    private String namaPemesan;
    private String noHpPemesan;
    private String keterangan;
    private String statusPembayaran;
    private String statusPengerjaan;
    private String dateCreated;

    private String harga;

    private String dibayarkan;

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public void setStatusPembayaran(String statusPembayaran) {
        this.statusPembayaran = statusPembayaran;
    }

    public String getStatusPengerjaan() {
        return statusPengerjaan;
    }

    public void setStatusPengerjaan(String statusPengerjaan) {
        this.statusPengerjaan = statusPengerjaan;
    }

    public DesignBookingModel(String idJasa, String idBooking, String emailPemesan, String namaPemesan, String noHpPemesan, String keterangan, String statusPembayaran, String statusPengerjaan, String dateCreated, String harga, String dibayarkan) {
        this.idJasa = idJasa;
        this.idBooking = idBooking;
        this.emailPemesan = emailPemesan;
        this.namaPemesan = namaPemesan;
        this.noHpPemesan = noHpPemesan;
        this.keterangan = keterangan;
        this.statusPembayaran = statusPembayaran;
        this.statusPengerjaan = statusPengerjaan;
        this.dateCreated = dateCreated;
        this.harga = harga;
        this.dibayarkan = dibayarkan;
    }

    public String getIdJasa() {
        return idJasa;
    }

    public void setIdJasa(String idJasa) {
        this.idJasa = idJasa;
    }

    public String getEmailPemesan() {
        return emailPemesan;
    }

    public void setEmailPemesan(String emailPemesan) {
        this.emailPemesan = emailPemesan;
    }

    public String getNamaPemesan() {
        return namaPemesan;
    }

    public void setNamaPemesan(String namaPemesan) {
        this.namaPemesan = namaPemesan;
    }

    public String getNoHpPemesan() {
        return noHpPemesan;
    }

    public void setNoHpPemesan(String noHpPemesan) {
        this.noHpPemesan = noHpPemesan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDibayarkan() {
        return dibayarkan;
    }

    public void setDibayarkan(String dibayarkan) {
        this.dibayarkan = dibayarkan;
    }
}
