package com.erwintobing15.disposisi.model.user;

import com.erwintobing15.disposisi.model.message.MessageModel;

import java.util.List;

public class UserModel {

    private String id;
    private String username;
    private String password;
    private String nama;
    private String nip;
    private String admin;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public class UserDataModel extends MessageModel {
        private List<UserModel> results;

        public List<UserModel> getResults() {
            return results;
        }

        public void setResults(List<UserModel> results) {
            this.results = results;
        }
    }
}
