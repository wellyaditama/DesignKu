package com.amikom.desainku.utility;

import android.content.Intent;
import android.net.Uri;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class UtilitiesClass {

    public static String ONBOARDING_PREF_NAME = "ONBOARDING_PREF_NAME";

    public static String generateRandomString(int limit) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(limit)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public static String formatRupiah(Integer number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    public static String getDateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date now = new Date();
        String dateNow = dateFormat.format(now);

        return dateNow;
    }

    public static String getDateNowOnlyDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date();
        String dateNow = dateFormat.format(now);

        return dateNow;
    }

    public static String[] statusPembayaran = new String[] {"Belum Dibayar", "Sudah Dibayar"};
    public static String[] statusPengerjaan = new String[] {"Dalam Pengerjaan", "Revisi", "Selesai", "Dibatalkan"};


}
