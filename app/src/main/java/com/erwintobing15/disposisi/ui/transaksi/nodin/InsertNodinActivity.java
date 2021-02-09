package com.erwintobing15.disposisi.ui.transaksi.nodin;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class InsertNodinActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener {

    private EditText editTextNoAgenda;
    private EditText editTextTujuan;
    private EditText editTextNoSurat;
    private EditText editTextIsi;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_surat_lain);

        initViews();
        initUtils();
        initToolbar();
        initListeners();
    }

    /**
     * Initialize views, toolbar, listener
     *
     */

    private void initViews() {
        editTextNoAgenda = findViewById(R.id.et_insert_nomor_agenda);
        editTextTujuan = findViewById(R.id.et_insert_tujuan);
        editTextNoSurat = findViewById(R.id.et_insert_nomor_surat);
        editTextIsi = findViewById(R.id.et_insert_isi);
        editTextKet = findViewById(R.id.et_insert_keterangan);
        textViewTanggal = findViewById(R.id.tv_insert_tanggal_surat);
        imageViewPilihTanggal = findViewById(R.id.iv_insert_calender);
        toolbar = findViewById(R.id.insert_surat_lain_toolbar);
        buttonPilihGambar = findViewById(R.id.btn_insert_image);
        buttonPilihPdf = findViewById(R.id.btn_insert_pdf);
        buttonPilihDocx = findViewById(R.id.btn_insert_docx);
        imageViewFoto = findViewById(R.id.iv_surat_image);
        textViewPfd = findViewById(R.id.tv_file_name);
        buttonSimpan = findViewById(R.id.btn_simpan);
        buttonBatal = findViewById(R.id.btn_batal);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Nodin");
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
     * Save surat pengantar masuk data on button clicked
     *
     */

    private void saveNodin() {

        String noAgenda = editTextNoAgenda.getText().toString();
        String tujuan = editTextTujuan.getText().toString();
        String noSurat = editTextNoSurat.getText().toString();
        String isi = editTextIsi.getText().toString();
        String tglSurat = textViewTanggal.getText().toString();
        String ket = editTextKet.getText().toString();

        final String idUser = SessionUtils.getLoggedUser(InsertNodinActivity.this).getId();

        RequestBody requestBodyNoAgenda = RequestBody.create(MediaType.parse("text/plain"), noAgenda);
        RequestBody requestBodyTujuan = RequestBody.create(MediaType.parse("text/plain"), tujuan);
        RequestBody requestBodyNoSurat = RequestBody.create(MediaType.parse("text/plain"), noSurat);
        RequestBody requestBodyIsi = RequestBody.create(MediaType.parse("text/plain"), isi);
        RequestBody requestBodyTanggalSurat = RequestBody.create(MediaType.parse("text/plain"), tglSurat);
        RequestBody requestBodyKeterangan = RequestBody.create(MediaType.parse("text/plain"), ket);
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("text/plain"), idUser);

        if (noAgenda.isEmpty() || tujuan.isEmpty() || noSurat.isEmpty() || isi.isEmpty() || tglSurat.isEmpty() || ket.isEmpty()) {

            progressDialog.dismiss();
            Toast.makeText(InsertNodinActivity.this, "Silahkan lengkapi data", Toast.LENGTH_SHORT).show();

        } else {

            if (fileImage==null && filePdf==null && fileDocx==null) {

                Call<MessageModel> call = APIService.Factory.create().postInsertNodin(requestBodyNoAgenda, requestBodyTujuan,
                        requestBodyNoSurat, requestBodyIsi, requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, null);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileImage  != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileImage.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertNodin(requestBodyNoAgenda, requestBodyTujuan,
                        requestBodyNoSurat, requestBodyIsi, requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (filePdf != null) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/pdf"), filePdf);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", filePdf.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertNodin(requestBodyNoAgenda, requestBodyTujuan,
                        requestBodyNoSurat, requestBodyIsi, requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

            else if (fileDocx != null) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), fileDocx);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", fileDocx.getName(), requestBody);

                Call<MessageModel> call = APIService.Factory.create().postInsertNodin(requestBodyNoAgenda, requestBodyTujuan,
                        requestBodyNoSurat, requestBodyIsi, requestBodyTanggalSurat, requestBodyKeterangan, requestBodyIdUser, multipartBody);

                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(InsertNodinActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
            }

        }
    }

    /**
     * Images, pdf, and docs picker
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
     * Back from insert surat pengantar keluar view
     *
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

    public boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(InsertNodinActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(InsertNodinActivity.this,
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
                filePdf = FileUtil.from(InsertNodinActivity.this, uri);
                Log.d("file", "File...:::: uti - "+filePdf .getPath()+" file -" + filePdf + " : " + filePdf .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewPfd.setText(filePdf.getName());
        }

        // docx file handler
        if (requestCode == DOCX_STORAGE_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                fileDocx = FileUtil.from(InsertNodinActivity.this, uri);
                Log.d("file", "File...:::: uti - "+fileDocx .getPath()+" file -" + fileDocx + " : " + fileDocx .exists());

            } catch (IOException e) {
                e.printStackTrace();
            }

            textViewPfd.setText(fileDocx.getName());
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
                Toast.makeText(InsertNodinActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(InsertNodinActivity.this,
                        "Akses penyimpanan ditolak",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == DOCX_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(InsertNodinActivity.this,
                        "Akses diberikan, silahkan pilih lagi",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(InsertNodinActivity.this,
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
     * All onclick handler
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
            progressDialog = ProgressDialog.show(InsertNodinActivity.this, "", "Menyimpan.....", true, true);
            saveNodin();
        }

        if (v == buttonBatal) {
            finish();
        }

    }
}
