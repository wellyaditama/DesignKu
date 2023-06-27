package com.amikom.desainku.model;

public class BuktiPembayaranModel {

    private String idBooking;
    private String idPembayaran;
    private String image;
    private String pembayaran;
    private String keteranganPembayaran;
    private String emailPembayar;
    private String dateCreated;
    private String isValid;

    public BuktiPembayaranModel(String idBooking, String idPembayaran, String image, String pembayaran, String keteranganPembayaran, String emailPembayar, String dateCreated, String isValid) {
        this.idBooking = idBooking;
        this.idPembayaran = idPembayaran;
        this.image = image;
        this.pembayaran = pembayaran;
        this.keteranganPembayaran = keteranganPembayaran;
        this.emailPembayar = emailPembayar;
        this.dateCreated = dateCreated;
        this.isValid = isValid;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public String getKeteranganPembayaran() {
        return keteranganPembayaran;
    }

    public String getEmailPembayar() {
        return emailPembayar;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getIdPembayaran() {
        return idPembayaran;
    }

    public String getImage() {
        return image;
    }

    public String getIsValid() {
        return isValid;
    }
}
