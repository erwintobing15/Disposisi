package com.erwintobing15.disposisi.ui.referensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.message.MessageModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.util.SessionUtils;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertReferensiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button buttonSimpan;
    private Button buttonBatal;
    private ProgressDialog progressDialog;

    private EditText editTextKode;
    private EditText editTextNama;
    private EditText editTextUraian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_referensi);

        initViews();
        initToolbar();
        initListenter();
    }

    private void initViews() {
        toolbar = findViewById(R.id.insert_referensi_toolbar);
        buttonBatal = findViewById(R.id.insert_surat_masuk_button_batal);
        buttonSimpan = findViewById(R.id.insert_surat_masuk_button__simpan);
        editTextKode = findViewById(R.id.et_insert_kode_referensi);
        editTextNama = findViewById(R.id.et_insert_nama_referensi);
        editTextUraian = findViewById(R.id.et_insert_uraian);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Referensi");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Back button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * save and cancel button action
     */

    private void initListenter() {
        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(InsertReferensiActivity.this, "", "Menyimpan.....", true, true);
                insertReferensi();
            }
        });
    }

    /**
     * Method to insert new data
     */

    private void insertReferensi() {

        String kode = editTextKode.getText().toString();
        String nama = editTextNama.getText().toString();
        String uraian = editTextUraian.getText().toString();

        final String idUser = SessionUtils.getLoggedUser(InsertReferensiActivity.this).getId();

        RequestBody requestBodyKode = RequestBody.create(MediaType.parse("text/plain"), kode);
        RequestBody requestBodyNama = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody requestBodyUraian = RequestBody.create(MediaType.parse("text/plain"), uraian);
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("text/plain"), idUser);

        if (kode.isEmpty() || nama.isEmpty() || uraian.isEmpty() ) {

            progressDialog.dismiss();
            Toast.makeText(InsertReferensiActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            Call<MessageModel> call = APIService.Factory.create().postInsertReferensi(requestBodyKode, requestBodyNama,
                    requestBodyUraian, requestBodyIdUser);

            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    Toast.makeText(InsertReferensiActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Toast.makeText(InsertReferensiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
        }
    }
}