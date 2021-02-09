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

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.message.MessageModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.util.FileUtil;
import com.erwintobing15.disposisi.util.Imageutils;
import com.erwintobing15.disposisi.util.SessionUtils;

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

public class InsertSuratMasukActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener{

    private EditText editTextNoAgenda;
    private EditText editTextAsal;
    private EditText editTextNoSurat;
    private EditText editTextIsi;
    private EditText editTextKode;
    private EditText editTextIndeks;
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
    private TextView textViewPfd;
    private File fileImage;
    private File filePdf;
    private File fileDocx;

    private ProgressDialog progressDialog;

    private static final int PDF_STORAGE_PERMISSION_CODE = 101;
    private static final int DOCX_STORAGE_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_surat_masuk);

        initViews();
        initUtils();
        initToolbar();
        initListeners();
    }

    private void initViews() {
        editTextNoAgenda = findViewById(R.id.et_insert_nomor_agenda);
        editTextAsal = findViewById(R.id.et_insert_asal_surat);
        editTextNoSurat = findViewById(R.id.et_insert_nomor_surat);
        editTextIsi = findViewById(R.id.et_insert_isi);
        editTextKode = findViewById(R.id.et_insert_kode);
        editTextIndeks = findViewById(R.id.et_insert_indeks);
        editTextKet = findViewById(R.id.et_insert_keterangan);
        textViewTanggal = findViewById(R.id.tv_insert_tanggal_surat);
        imageViewPilihTanggal = findViewById(R.id.iv_insert_suratmasuk_calender);
        toolbar = findViewById(R.id.insert_surat_masuk_toolbar);
        buttonSimpan = findViewById(R.id.btn_insert_surat_masuk_simpan);
        buttonBatal = findViewById(R.id.btn_insert_surat_masuk_batal);
        buttonPilihGambar = findViewById(R.id.btn_insert_surat_masuk_image);
        buttonPilihPdf = findViewById(R.id.btn_insert_surat_masuk_pdf);
        buttonPilihDocx = findViewById(R.id.btn_insert_surat_masuk_docx);
        imageViewFoto = findViewById(R.id.iv_insert_surat_masuk_image);
        textViewPfd = findViewById(R.id.tv_surat_masuk_pdf);
    }

    private void initUtils() {
        imageutils = new Imageutils(this);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Surat Masuk");
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

    // save button event handler
    private void saveSuratMasuk() {

        String noAgenda = editTextNoAgenda.getText().toString();
        String asalSurat = editTextAsal.getText().toString();
        String noSurat = editTextNoSurat.getText().toString();
        String isi = editTextIsi.getText().toString();
        String kode = editTextKode.getText().toString();
        String indeks = editTextIndeks.getText().toString();
        String tglSurat = textViewTanggal.getText().toString();
        String ket = editTextKet.getText().toString();

        final String idUser = SessionUtils.getLoggedUser(InsertSuratMasukActivity.this).getId();

        RequestBody requestBodyNoAgenda = RequestBody.create(MediaType.parse("text/plain"), noAgenda);
        RequestBody requestBodyAsalSurat = RequestBody.create(MediaType.parse("text/plain"), asalSurat);
        RequestBody requestBodyNoSurat = RequestBody.create(MediaType.parse("text/plain"), noSurat);
        RequestBody requestBodyIsi = RequestBody.create(MediaType.parse("text/plain"), isi);
        RequestBody requestBodyKode = RequestBody.create(MediaType.parse("text/plain"), kode);
        RequestBody requestBodyIndeks = RequestBody.create(MediaType.parse("text/plain"), indeks);
        RequestBody requestBodyTanggalSurat = RequestBody.create(MediaType.parse("text/plain"), tglSurat);
        RequestBody requestBodyKeterangan = RequestBody.create(MediaType.parse("text/plain"), ket);
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("text/plain"), idUser);

        if (noAgenda.isEmpty() || asalSurat.isEmpty() || noSurat.isEmpty() || isi.isEmpty() ||
                kode.isEmpty() || indeks.isEmpty() || tglSurat.isEmpty() || ket.isEmpty()) {

            progressDialog.dismiss();
            Toast.makeText(InsertSuratMasukActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            if (fileImage==null && filePdf==null && fileDocx==null) {

                Call<MessageModel> call = APIService.Factory.create().postInsertSuratMasuk(requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, null);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileImage  != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileImage.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertSuratMasuk(requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (filePdf != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), filePdf);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", filePdf.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertSuratMasuk(requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileDocx != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), fileDocx);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileDocx.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertSuratMasuk(requestBodyNoAgenda, requestBodyAsalSurat,
                        requestBodyNoSurat, requestBodyIsi, requestBodyKode, requestBodyIndeks,
                        requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertSuratMasukActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

        }
    }

    private void loadPdf()
    {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/pdf");
////        i.setType("application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document");
////        intent.setType("application/pdf");
//        startActivityForResult(Intent.createChooser(intent,"select file"), STORAGE_PERMISSION_CODE);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Function to check and request permission.
    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(InsertSuratMasukActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(InsertSuratMasukActivity.this,
                    new String[] { permission },
                    requestCode);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pdf file picker
        if (requestCode == PDF_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                filePdf = FileUtil.from(InsertSuratMasukActivity.this, uri);
                Log.d("file", "File...:::: uti - "+filePdf .getPath()+" file -" + filePdf + " : " + filePdf .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewPfd.setText(filePdf.getName());
        }

        // docx file picker
        if (requestCode == DOCX_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                fileDocx = FileUtil.from(InsertSuratMasukActivity.this, uri);
                Log.d("file", "File...:::: uti - "+fileDocx .getPath()+" file -" + fileDocx + " : " + fileDocx .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewPfd.setText(fileDocx.getName());
        }

        // image file picker
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);

        if (requestCode == PDF_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(InsertSuratMasukActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(InsertSuratMasukActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == DOCX_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(InsertSuratMasukActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(InsertSuratMasukActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        imageViewFoto.setVisibility(View.VISIBLE);
        imageViewFoto.setImageBitmap(file);

        String path = imageutils.getPath(uri);
        fileImage = new File(path);
    }

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
//            loadPdf();
        }

        if (v == buttonPilihDocx) {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, DOCX_STORAGE_PERMISSION_CODE)) {
                loadDocx();
            }
        }

        if (v == buttonSimpan) {
            progressDialog = ProgressDialog.show(InsertSuratMasukActivity.this, "", "Menyimpan.....", true, true);
            saveSuratMasuk();
        }

        if (v == buttonBatal) {
            finish();
        }
    }

}