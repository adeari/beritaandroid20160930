package com.example.ade.beritasurabaya.data;

/**
 * Created by widianta on 11/3/2016.
 */

public class Alamat {
    private String kota;
    private String alamat;
    private String email;
    private String telepon;
    public Alamat(String kota1, String alamat1, String telepon1, String email1) {
        kota = kota1;
        alamat = alamat1;
        email = email1;
        telepon = telepon1;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }

    public String getKota() {
        return kota;
    }
    public String getTelepon() {
        return telepon;
    }
}
