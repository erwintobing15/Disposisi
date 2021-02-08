package com.erwintobing15.disposisi.ui.transaksi.suratmasukundangan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.config.Constants;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.SelectSuratMasukUndanganModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.util.FileUtil;
import com.erwintobing15.disposisi.util.Imageutils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSuratMasukUndanganActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener {

    private EditText editTextNoAgenda;
    private EditText editTextAsal;
    private EditText editTextNoSurat;
    private EditText editTextIsi;
    private EditText editTextKode;
    private EditText editTextKet;
    private TextView textViewTanggal;

    private int mYear, mMonth, mDay;
    private ImageView imageViewPilihTanggal;

    private Button buttonPilihGambar;
    private Button buttonPilihPdf;
    private Button buttonPilihDocx;
    private Button buttonSimpan;
    private Button buttonBatal;

    private Toolbar toolbar;

    private Imageutils imageutils;
    private ImageView imageViewFoto;
    private TextView textViewFileName;

    private File fileImage;
    private File filePdf;
    private File fileDocx;

    private ProgressDialog progressDialog;

    private static final int PDF_STORAGE_PERMISSION_CODE = 101;
    private static final int DOCX_STORAGE_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sumasuk);
        progressDialog = ProgressDialog.show(UpdateSuratMasukUndanganActivity.this, "", "Load Data.....", true, false);

        initViews();
        initUtils();
        initToolbar();

        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");    // get id from intent extra

        loadViews(id);
        initListeners();
    }

    /**
     * Initialize views, toolbar, listener, utils
     *
     */

    private void initViews() {
        editTextNoAgenda = findViewById(R.id.et_update_nomor_agenda);
        editTextAsal = findViewById(R.id.et_update_tujuan_or_asal);
        editTextNoSurat = findViewById(R.id.et_update_nomor_surat);
        editTextIsi = findViewById(R.id.et_update_isi);
        editTextKode = findViewById(R.id.et_update_kode);
        editTextKet = findViewById(R.id.et_update_keterangan);
        textViewTanggal = findViewById(R.id.tv_update_tanggal_surat);
        imageViewPilihTanggal = findViewById(R.id.iv_update_calender);
        toolbar = findViewById(R.id.update_surat_undangan_masuk_toolbar);
        buttonPilihGambar = findViewById(R.id.btn_update_image);
        buttonPilihPdf = findViewById(R.id.btn_update_pdf);
        buttonPilihDocx = findViewById(R.id.btn_update_docx);
        imageViewFoto = findViewById(R.id.iv_surat_image);
        textViewFileName = findViewById(R.id.tv_surat_name);
        buttonSimpan = findViewById(R.id.btn_simpan);
        buttonBatal = findViewById(R.id.btn_batal);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ubah Surat Undangan Masuk");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListeners() {
        imageViewPilihTanggal.setOnClickListener(this);
        buttonPilihGambar.setOnClickListener(this);
        buttonSimpan.setOnClickListener(this);
        buttonBatal.setOnClickListener(this);
        buttonPilihPdf.setOnClickListener(this);
        buttonPilihDocx.setOnClickListener(this);
    }

    private void initUtils() {
        imageutils = new Imageutils(this);
    }

    /**
     * Load view of given id from intent extra
     *
     * @param id
     */

    private void loadViews(String id) {

        Call<SelectSuratMasukUndanganModel> call = APIService.Factory.create().oneSuratMasukUndangan(id);
        call.enqueue(new Callback<SelectSuratMasukUndanganModel>() {
            @Override
            public void onResponse(Call<SelectSuratMasukUndanganModel> call, Response<SelectSuratMasukUndanganModel> response) {
                progressDialog.dismiss();
                editTextNoAgenda.setText(response.body().getNo_agenda());
                editTextAsal.setText(response.body().getAsal_surat());
                editTextNoSurat.setText(response.body().getNo_surat());
                editTextIsi.setText(response.body().getIsi());
                editTextKode.setText(response.body().getKode());
                editTextKet.setText(response.body().getKeterangan());
                textViewTanggal.setText(response.body().getTgl_surat());
                textViewFileName.setText(response.body().getFile());

                // load images
                imageViewFoto.setVisibility(View.VISIBLE);
                Glide.with(UpdateSuratMasukUndanganActivity.this)
                        .load(Constants.IMAGES_URL+"surat_masuk_undangan/"+response.body().getFile())
                        .apply(new RequestOptions().error(R.drawable.doc))
                        .into(imageViewFoto);
            }

            @Override
            public void onFailure(Call<SelectSuratMasukUndanganModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Save surat masuk data on button clicked
     *
     */

    private void saveSuratPengantarKeluar() {
        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");

        String noAgenda = editTextNoAgenda.getText().toString();
        String asal = editTextAsal.getText().toString();
        String noSurat = editTextNoSurat.getText().toString();
        String isi = editTextIsi.getText().toString();
        String kode = editTextKode.getText().toString();
        String tglSurat = textViewTanggal.getText().toString();
        String ket = editTextKet.getText().toString();

        RequestBody requestBodyId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody requestBodyNoAgenda = RequestBody.create(MediaType.parse("text/plain"), noAgenda);
        RequestBody requestBodyAsal = RequestBody.create(MediaType.parse("text/plain"), asal);
        RequestBody requestBodyNoSurat = RequestBody.create(MediaType.parse("text/plain"), noSurat);
        RequestBody requestBodyIsi = RequestBody.create(MediaType.parse("text/plain"), isi);
        RequestBody requestBodyKode = RequestBody.create(MediaType.parse("text/plain"), kode);
        RequestBody requestBodyTanggalSurat = RequestBody.create(MediaType.parse("text/plain"), tglSurat);
        RequestBody requestBodyKeterangan = RequestBody.create(MediaType.parse("text/plain"), ket);

        if (noAgenda.isEmpty() || asal.isEmpty() || noSurat.isEmpty() || isi.isEmpty() || tglSurat.isEmpty() || ket.isEmpty()) {

            progressDialog.dismiss();
            Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            if (fileImage==null && filePdf==null && fileDocx==null) {

                Call<MessageModel> call = APIService.Factory.create().updateSuratMasukUndangan(requestBodyId, requestBodyNoAgenda, requestBodyAsal,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyTanggalSurat, requestBodyKeterangan,  null);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileImage  != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileImage.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().updateSuratMasukUndangan(requestBodyId, requestBodyNoAgenda, requestBodyAsal,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyTanggalSurat, requestBodyKeterangan,  multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (filePdf != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), filePdf);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", filePdf.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().updateSuratMasukUndangan(requestBodyId, requestBodyNoAgenda, requestBodyAsal,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyTanggalSurat, requestBodyKeterangan,  multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileDocx != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), fileDocx);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileDocx.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().updateSuratMasukUndangan(requestBodyId, requestBodyNoAgenda, requestBodyAsal,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyTanggalSurat, requestBodyKeterangan,  multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateSuratMasukUndanganActivity.this, "Berhasil mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

        }
    }

    /**
     * Pdf, docx and image file picker
     *
     */

    private void loadPdf()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select file"), PDF_STORAGE_PERMISSION_CODE);
    }

    private void loadDocx()
    {
        Intent intent = new Intent();
        intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select file"), DOCX_STORAGE_PERMISSION_CODE);
    }

    /**
     * Toolbar back button
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Checking permission
     *
     */

    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(UpdateSuratMasukUndanganActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(UpdateSuratMasukUndanganActivity.this,
                    new String[] { permission },
                    requestCode);
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Filepicker activity result
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // pdf file handler
        if (requestCode == PDF_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                filePdf = FileUtil.from(UpdateSuratMasukUndanganActivity.this, uri);
                Log.d("file", "File...:::: uti - "+filePdf .getPath()+" file -" + filePdf + " : " + filePdf .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewFileName.setText(filePdf.getName());
        }

        // docx file handler
        if (requestCode == DOCX_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                fileDocx = FileUtil.from(UpdateSuratMasukUndanganActivity.this, uri);
                Log.d("file", "File...:::: uti - "+fileDocx .getPath()+" file -" + fileDocx + " : " + fileDocx .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewFileName.setText(fileDocx.getName());
        }

        // image file handler
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Permission result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        imageutils.request_permission_result(requestCode, permissions, grantResults);

        if (requestCode == PDF_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UpdateSuratMasukUndanganActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(UpdateSuratMasukUndanganActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == DOCX_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UpdateSuratMasukUndanganActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(UpdateSuratMasukUndanganActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Display image if not empty
     *
     * @param from
     * @param filename
     * @param file
     * @param uri
     */

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        imageViewFoto.setVisibility(View.VISIBLE);
        imageViewFoto.setImageBitmap(file);

        String path = imageutils.getPath(uri);
        fileImage = new File(path);
    }

    /**
     * All this class onclick listener handler
     *
     * @param v
     */

    @Override
    public void onClick(View v) {

        if (v == imageViewPilihTanggal) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            textViewTanggal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == buttonPilihGambar) {
            imageutils.imagepicker(1);
        }

        if (v == buttonPilihPdf) {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PDF_STORAGE_PERMISSION_CODE)) {
                loadPdf();
            }
        }

        if (v == buttonPilihDocx) {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, DOCX_STORAGE_PERMISSION_CODE)) {
                loadDocx();
            }
        }

        if (v == buttonSimpan) {
            progressDialog = ProgressDialog.show(UpdateSuratMasukUndanganActivity.this, "", "Menyimpan.....", true, true);
            saveSuratPengantarKeluar();
        }

        if (v == buttonBatal) {
            finish();
        }

    }
}