package com.erwintobing15.disposisi.model;

import java.util.List;

public class SUMasukModel {

    private String id;
    private String no_agenda;
    private String asal_surat;
    private String no_surat;
    private String isi;
    private String kode;
    private String tgl_surat;
    private String tgl_catat;
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

    public String getAsal_surat() {
        return asal_surat;
    }

    public void setAsal_surat(String asal_surat) {
        this.asal_surat = asal_surat;
    }

    public String getNo_surat() {
        return no_surat;
    }

    public void setNo_surat(String no_surat) {
        this.no_surat = no_surat;
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

    public String getTgl_surat() {
        return tgl_surat;
    }

    public void setTgl_surat(String tgl_surat) {
        this.tgl_surat = tgl_surat;
    }

    public String getTgl_catat() {
        return tgl_catat;
    }

    public void setTgl_catat(String tgl_catat) {
        this.tgl_catat = tgl_catat;
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

    public class SUMasukDataModel extends MessageModel {
        private List<SUMasukModel> results;

        public List<SUMasukModel> getResults() {
            return results;
        }

        public void setResults(List<SUMasukModel> results) {
            this.results = results;
        }
    }
}
