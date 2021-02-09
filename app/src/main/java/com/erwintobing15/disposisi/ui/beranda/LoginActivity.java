package com.erwintobing15.disposisi.ui.beranda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.user.UserModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.util.SessionUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_username, txt_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initListeners();

        if (SessionUtils.isLoggedIn(this)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        btn_login = findViewById(R.id.btn_login);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
    }

    private void initListeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                // mengecek kolom yang kosong
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    checkLogin(username, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext() ,"Ada field yang kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();

        Call<UserModel.UserDataModel> api = APIService.Factory.create().postLogin(username, password);
        api.enqueue(new Callback<UserModel.UserDataModel>() {
            @Override
            public void onResponse(Call<UserModel.UserDataModel> call, retrofit2.Response<UserModel.UserDataModel> response) {
                hideDialog();
                if (response.isSuccessful()){
                    if (response.body().getMessage().equalsIgnoreCase("Berhasil")){
                        if (SessionUtils.login(LoginActivity.this, response.body().getResults().get(0))){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel.UserDataModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
