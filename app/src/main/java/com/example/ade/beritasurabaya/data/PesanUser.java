package com.example.ade.beritasurabaya.data;

/**
 * Created by widianta on 11/1/2016.
 */

public class PesanUser {
    private String id;
    private String judul;
    private String pesan;

    public PesanUser(String judul1) {
        judul = judul1;
    }
    public PesanUser(String id1, String judul1, String pesan1) {
        id = id1;
        judul = judul1;
        pesan = pesan1;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getPesan() {
        return pesan;
    }
}
