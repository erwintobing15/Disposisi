package com.erwintobing15.disposisi.model;

public class SelectSuratMasukModel extends MessageModel {

    private String id;
    private String no_agenda;
    private String no_surat;
    private String asal_surat;
    private String isi;
    private String kode;
    private String indeks;
    private String tgl_surat;
    private String tgl_diterima;
    private String file;
    private String keterangan;
    private String id_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo_agenda() {
        return no_agenda;
    }

    public void setNo_agenda(String no_agenda) {
        this.no_agenda = no_agenda;
    }

    public String getNo_surat() {
        return no_surat;
    }

    public void setNo_surat(String no_surat) {
        this.no_surat = no_surat;
    }

    public String getAsal_surat() {
        return asal_surat;
    }

    public void setAsal_surat(String asal_surat) {
        this.asal_surat = asal_surat;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public String getTgl_surat() {
        return tgl_surat;
    }

    public void setTgl_surat(String tgl_surat) {
        this.tgl_surat = tgl_surat;
    }

    public String getTgl_diterima() {
        return tgl_diterima;
    }

    public void setTgl_diterima(String tgl_diterima) {
        this.tgl_diterima = tgl_diterima;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
