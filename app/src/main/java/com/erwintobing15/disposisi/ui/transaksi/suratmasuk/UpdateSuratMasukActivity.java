package com.erwintobing15.disposisi.ui.transaksi.suratmasuk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.config.Constants;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.SelectSuratMasukModel;
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

public class UpdateSuratMasukActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener {

    private EditText editTextNoAgenda;
    private EditText editTextAsal;
    private EditText editTextNoSurat;
    private EditText editTextIsi;
    private EditText editTextKode;
    private EditText editTextIndeks;
    private EditText editTextKet;
    private TextView textViewTanggal;

    private Imageutils imageutils;
    private File fileImage;
    private ImageView imageViewFoto;
    private TextView textViewDoc;
    private File filePdf;
    private File fileDocx;

    private int mYear, mMonth, mDay;
    private ImageView imageViewPilihTanggal;

    private Button buttonPilihGambar;
    private Button buttonPilihPdf;
    private Button buttonPilihDocx;
    private Button buttonSimpan;
    private Button buttonBatal;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    private static final int PDF_STORAGE_PERMISSION_CODE = 101;
    private static final int DOCX_STORAGE_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_surat_masuk);
        progressDialog = ProgressDialog.show(UpdateSuratMasukActivity.this, "", "Load data.....", true, false);

        initViews();
        initUtils();
        initToolbar();

        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");    // get id from intent extra

        loadViews(id);
        initListener();
    }

    /**
     * Initialize views, utils, toolbar, listener
     *
     */

    private void initViews() {
        editTextNoAgenda = findViewById(R.id.et_update_nomor_agenda);
        editTextAsal = findViewById(R.id.et_update_asal_surat);
        editTextNoSurat = findViewById(R.id.et_update_nomor_surat);
        editTextIsi = findViewById(R.id.et_update_isi);
        editTextKode = findViewById(R.id.et_update_kode);
        editTextIndeks = findViewById(R.id.et_update_indeks);
        editTextKet = findViewById(R.id.et_update_keterangan);
        textViewTanggal = findViewById(R.id.tv_update_tanggal_surat);
        toolbar = findViewById(R.id.update_surat_masuk_toolbar);
        imageViewFoto = findViewById(R.id.iv_update_surat_masuk_image);
        imageViewPilihTanggal = findViewById(R.id.iv_update_suratmasuk_calender);
        buttonSimpan = findViewById(R.id.btn_update_surat_masuk_simpan);
        buttonBatal = findViewById(R.id.btn_update_surat_masuk_batal);
        buttonPilihGambar = findViewById(R.id.btn_update_surat_masuk_image);
        buttonPilihPdf = findViewById(R.id.btn_update_surat_masuk_pdf);
        buttonPilihDocx = findViewById(R.id.btn_update_surat_masuk_docx);
        textViewDoc = findViewById(R.id.tv_update_surat_masuk_doc);
    }

    private void initUtils() {
        imageutils = new Imageutils(this);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ubah Surat Masuk");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListener() {
        imageViewPilihTanggal.setOnClickListener(this);
        buttonPilihGambar.setOnClickListener(this);
        buttonPilihPdf.setOnClickListener(this);
        buttonPilihDocx.setOnClickListener(this);
        buttonSimpan.setOnClickListener(this);
        buttonBatal.setOnClickListener(this);
    }

    /**
     * Load view of given id from intent extra
     *
     * @param id
     */

    private void loadViews(String id) {

        Call<SelectSuratMasukModel> call = APIService.Factory.create().oneSuratMasuk(id);
        call.enqueue(new Callback<SelectSuratMasukModel>() {
            @Override
            public void onResponse(Call<SelectSuratMasukModel> call, Response<SelectSuratMasukModel> response) {
                progressDialog.dismiss();
                editTextNoAgenda.setText(response.body().getNo_agenda());
                editTextAsal.setText(response.body().getAsal_surat());
                editTextNoSurat.setText(response.body().getNo_surat());
                editTextIsi.setText(response.body().getIsi());
                editTextKode.setText(response.body().getKode());
                editTextIndeks.setText(response.body().getIndeks());
                editTextKet.setText(response.body().getKeterangan());
                textViewTanggal.setText(response.body().getTgl_surat());
                textViewDoc.setText(response.body().getFile());

                // load images
                imageViewFoto.setVisibility(View.VISIBLE);
                Glide.with(UpdateSuratMasukActivity.this)
                        .load(Constants.IMAGES_URL+"surat_masuk/"+response.body().getFile())
                        .apply(new RequestOptions().error(R.drawable.doc))
                        .into(imageViewFoto);
            }

            @Override
            public void onFailure(Call<SelectSuratMasukModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateSuratMasukActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Save surat masuk data on button clicked
     *
     */

    private void saveSuratMasuk() {
        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");

        String noAgenda = editTextNoAgenda.getText().toString();
        String asalSurat = editTextAsal.getText().toString();
        String noSurat = editTextNoSurat.getText().toString();
        String isi = editTextIsi.getText().toString();
        String kode = editTextKode.getText().toString();
        String indeks = editTextIndeks.getText().toString();
        String tglSurat = textViewTanggal.getText().toString();
        String ket = editTextKet.getText().toString();

        RequestBody requestBodyId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody requestBodyNoAgenda = RequestBody.create(MediaType.parse("text/plain"), noAgenda);
        RequestBody requestBodyAsalSurat = RequestBody.create(MediaType.parse("text/plain"), asalSurat);
        RequestBody requestBodyNoSurat = RequestBody.create(MediaType.parse("text/plain"), noSurat);
        RequestBody requestBodyIsi = RequestBody.create(MediaType.parse("text/plain"), isi);
        RequestBody requestBodyKode = RequestBody.create(MediaType.parse("text/plain"), kode);
        RequestBody requestBodyIndeks = RequestBody.create(MediaType.parse("text/plain"), indeks);
        RequestBody requestBodyTanggalSurat = RequestBody.create(MediaType.parse("text/plain"), tglSurat);
        RequestBody requestBodyKeterangan = RequestBody.create(MediaType.parse("text/plain"), ket);

        if (noAgenda.isEmpty() || asalSurat.isEmpty() || noSurat.isEmpty() || isi.isEmpty() ||
                kode.isEmpty() || indeks.isEmpty() || tglSurat.isEmpty() || ket.isEmpty()) {

            progressDialog.dismiss();
            Toast.makeText(UpdateSuratMasukActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            if (fileImage==null && filePdf==null && fileDocx==null) {

                Call<MessageModel> call = APIService.Factory.create().postUpdateSuratMasuk(requestBodyId, requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, null);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });

            }

            else if (fileImage != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileImage.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postUpdateSuratMasuk(requestBodyId, requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (filePdf != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), filePdf);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", filePdf.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postUpdateSuratMasuk(requestBodyId, requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileDocx != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), fileDocx);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileDocx.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postUpdateSuratMasuk(requestBodyId, requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(UpdateSuratMasukActivity.this, "Berhasil Mengubah", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

        }
    }

    /**
     * Checking permission
     *
     */

    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(UpdateSuratMasukActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(UpdateSuratMasukActivity.this,
                    new String[] { permission },
                    requestCode);
            return false;
        }
        else {
            return true;
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
     * Filepicker activity result
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pdf file handler
        if (requestCode == PDF_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                filePdf = FileUtil.from(UpdateSuratMasukActivity.this, uri);
                Log.d("file", "File...:::: uti - "+filePdf .getPath()+" file -" + filePdf + " : " + filePdf .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewDoc.setText(filePdf.getName());
        }

        // docx file handler
        if (requestCode == DOCX_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                fileDocx = FileUtil.from(UpdateSuratMasukActivity.this, uri);
                Log.d("file", "File...:::: uti - "+fileDocx .getPath()+" file -" + fileDocx + " : " + fileDocx .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewDoc.setText(fileDocx.getName());
        }

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
        imageutils.request_permission_result(requestCode, permissions, grantResults);

        if (requestCode == PDF_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UpdateSuratMasukActivity.this,
                        "Akses diberikan, silahkan pilih file",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(UpdateSuratMasukActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == DOCX_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UpdateSuratMasukActivity.this,
                        "Akses diberikan, silahkan pilih file",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(UpdateSuratMasukActivity.this,
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
            progressDialog = ProgressDialog.show(UpdateSuratMasukActivity.this, "", "Menyimpan.....", true, true);
            saveSuratMasuk();
        }

        if (v == buttonBatal) {
            finish();
        }
    }
}