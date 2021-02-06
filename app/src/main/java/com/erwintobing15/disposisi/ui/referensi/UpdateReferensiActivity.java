package com.erwintobing15.disposisi.ui.referensi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.SelectReferensiModel;
import com.erwintobing15.disposisi.network.APIService;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateReferensiActivity extends AppCompatActivity {

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
        progressDialog = ProgressDialog.show(UpdateReferensiActivity.this, "", "Load Data.....", true, false);

        initViews();
        initToolbar();
        initListenter();
        loadView();
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
     * update data and cancel button action
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
                progressDialog = ProgressDialog.show(UpdateReferensiActivity.this, "", "Menyimpan.....", true, true);
                updateReferensi();
            }
        });
    }

    /**
     * Load views data
     */

    private void loadView() {

        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");

        Call<SelectReferensiModel> call = APIService.Factory.create().oneReferensi(id);
        call.enqueue(new Callback<SelectReferensiModel>() {
            @Override
            public void onResponse(Call<SelectReferensiModel> call, Response<SelectReferensiModel> response) {
                progressDialog.dismiss();
                editTextKode.setText(response.body().getKode());
                editTextNama.setText(response.body().getNama());
                editTextUraian.setText(response.body().getUraian());
            }

            @Override
            public void onFailure(Call<SelectReferensiModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateReferensiActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Method to update data
     */

    private void updateReferensi() {

        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");

        String kode = editTextKode.getText().toString();
        String nama = editTextNama.getText().toString();
        String uraian = editTextUraian.getText().toString();

        RequestBody requestBodyId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody requestBodyKode = RequestBody.create(MediaType.parse("text/plain"), kode);
        RequestBody requestBodyNama = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody requestBodyUraian = RequestBody.create(MediaType.parse("text/plain"), uraian);

        if (kode.isEmpty() || nama.isEmpty() || uraian.isEmpty() ) {

            progressDialog.dismiss();
            Toast.makeText(UpdateReferensiActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            Call<MessageModel> call = APIService.Factory.create().updateReferensi(requestBodyId, requestBodyKode, requestBodyNama,
                    requestBodyUraian);

            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    Toast.makeText(UpdateReferensiActivity.this, "Berhasil mengubah, refresh layar", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Toast.makeText(UpdateReferensiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
        }
    }
}
