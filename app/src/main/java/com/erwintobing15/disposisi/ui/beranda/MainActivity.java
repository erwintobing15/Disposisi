package com.erwintobing15.disposisi.ui.beranda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.ReferensiModel;
import com.erwintobing15.disposisi.model.SuratPengantarKeluarModel;
import com.erwintobing15.disposisi.model.SuratMasukUndanganModel;
import com.erwintobing15.disposisi.model.SuratKeluarModel;
import com.erwintobing15.disposisi.model.SuratLainModel;
import com.erwintobing15.disposisi.model.SuratMasukModel;
import com.erwintobing15.disposisi.model.UserModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.ui.referensi.ReferensiActivity;
import com.erwintobing15.disposisi.ui.transaksi.TransaksiActivity;
import com.erwintobing15.disposisi.util.SessionUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    private TextView suratMasukCount;
    private TextView suratKeluarCount;
    private TextView suratPengantarKeluarCount;
    private TextView suratMasukUndanganCount;
    private TextView suratPerintahCount;
    private TextView suratKeputusanCount;
    private TextView nodinCount;
    private TextView perjanjianKerjasamaCount;
    private TextView agendaMouCount;
    private TextView sptjmCount;
    private TextView bastCount;
    private TextView suratKeteranganCount;
    private TextView jumlahKlasifikasiCount;
    private TextView userCount;

    private CardView cardViewSuratMasuk;
    private CardView cardViewSuratKeluar;
    private CardView cardViewSuratPengantarKeluar;
    private CardView cardViewSuratMasukUndangan;
    private CardView cardViewSuratPerintah;
    private CardView cardViewSuratKeputsan;
    private CardView cardViewNodin;
    private CardView cardViewPerjanjianKerjasama;
    private CardView cardViewAgendaMou;
    private CardView cardViewSptjm;
    private CardView cardViewBast;
    private CardView cardViewSuratKeterangan;
    private CardView cardViewJumlahKlasifikasi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Load Data.....", true, false);


        initViews();
        initToolbar();
        initNavigation();
        loadDashboard();
        initListeners();
    }

    /**
     * Initialize views, listeners, toolbar, navigation
     *
     *
     */

    private void initViews() {
        navigationView = findViewById(R.id.navigation_menu_beranda);
        suratMasukCount = findViewById(R.id.tv_surat_masuk);
        suratKeluarCount = findViewById(R.id.tv_surat_keluar);
        suratPengantarKeluarCount = findViewById(R.id.tv_surat_pengantar_keluar);
        suratMasukUndanganCount = findViewById(R.id.tv_surat_udangan_masuk);
        suratPerintahCount = findViewById(R.id.tv_surat_perintah);
        suratKeputusanCount = findViewById(R.id.tv_surat_keputusan);
        nodinCount = findViewById(R.id.tv_nodin);
        perjanjianKerjasamaCount = findViewById(R.id.tv_perjanjian_kerjasama);
        agendaMouCount = findViewById(R.id.tv_mou);
        sptjmCount = findViewById(R.id.tv_sptjm);
        bastCount = findViewById(R.id.tv_surat_bast);
        suratKeteranganCount = findViewById(R.id.tv_surat_keterangan);
        jumlahKlasifikasiCount = findViewById(R.id.tv_jumlah_klasifikasi);
        userCount = findViewById(R.id.tv_user_count);

        cardViewSuratMasuk = findViewById(R.id.cv_surat_masuk);
        cardViewSuratKeluar = findViewById(R.id.cv_surat_keluar);
        cardViewSuratPengantarKeluar = findViewById(R.id.cv_surat_pengantar_keluar);
        cardViewSuratMasukUndangan = findViewById(R.id.cv_surat_undanga_masuk);
        cardViewSuratPerintah = findViewById(R.id.cv_surat_perintah);
        cardViewSuratKeputsan = findViewById(R.id.cv_surat_keputusan);
        cardViewNodin = findViewById(R.id.cv_nodin);
        cardViewPerjanjianKerjasama = findViewById(R.id.cv_perjanjian_kerjasama);
        cardViewAgendaMou = findViewById(R.id.cv_mou);
        cardViewSptjm = findViewById(R.id.cv_sptjm);
        cardViewBast = findViewById(R.id.cv_surat_bast);
        cardViewSuratKeterangan = findViewById(R.id.cv_surat_keterangan);
        cardViewJumlahKlasifikasi = findViewById(R.id.cv_jumlah_klasifikasi);
    }

    private void initListeners() {
        cardViewSuratMasuk.setOnClickListener(this);
        cardViewSuratKeluar.setOnClickListener(this);
        cardViewSuratPengantarKeluar.setOnClickListener(this);
        cardViewSuratMasukUndangan.setOnClickListener(this);
        cardViewSuratPerintah.setOnClickListener(this);
        cardViewSuratKeputsan.setOnClickListener(this);
        cardViewNodin.setOnClickListener(this);
        cardViewPerjanjianKerjasama.setOnClickListener(this);
        cardViewAgendaMou.setOnClickListener(this);
        cardViewSptjm.setOnClickListener(this);
        cardViewBast.setOnClickListener(this);
        cardViewSuratKeterangan.setOnClickListener(this);
        cardViewJumlahKlasifikasi.setOnClickListener(this);
    }

    private void initToolbar() {
        drawerLayout = findViewById(R.id.drawerLayoutBeranda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_dashboard);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_dashboard, R.string.nav_dashboard);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }

    private void initNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_home:
                        break;
                    case R.id.nav_transaksi:
                        Intent a = new Intent(MainActivity.this , TransaksiActivity.class);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        break;
//                    case R.id.nav_buku_agenda:
//                        Intent b = new Intent(MainActivity.this , AgendaActivity.class);
//                        b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(b);
//                        break;
//                    case R.id.nav_galeri_file:
//                        Intent c = new Intent(MainActivity.this , GaleriActivity.class);
//                        c.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(c);
//                        break;
                    case R.id.nav_referensi:
                        Intent d = new Intent(MainActivity.this , ReferensiActivity.class);
                        d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(d);
                        break;
                    case R.id.nav_logout:
                        SessionUtils.logout(MainActivity.this);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * load data count on dasboard cardview
     *
     */

    private void loadDashboard() {

        Call<SuratMasukModel.SuratMasukDataModel> callSrtMasuk = APIService.Factory.create().allSuratMasuk(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSrtMasuk.enqueue(new Callback<SuratMasukModel.SuratMasukDataModel>() {
            @Override
            public void onResponse(Call<SuratMasukModel.SuratMasukDataModel> call, Response<SuratMasukModel.SuratMasukDataModel> response) {
                List<SuratMasukModel> list = response.body().getResults();
                suratMasukCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratMasukModel.SuratMasukDataModel> call, Throwable t) {
                suratMasukCount.setText("0");
            }
        });

        Call<SuratKeluarModel.SuratKeluarDataModel> callSrtKeluar = APIService.Factory.create().allSuratKeluar(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSrtKeluar.enqueue(new Callback<SuratKeluarModel.SuratKeluarDataModel>() {
            @Override
            public void onResponse(Call<SuratKeluarModel.SuratKeluarDataModel> call, Response<SuratKeluarModel.SuratKeluarDataModel> response) {
                List<SuratKeluarModel> list = response.body().getResults();
                suratKeluarCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratKeluarModel.SuratKeluarDataModel> call, Throwable t) {
                suratKeluarCount.setText("0");
            }
        });

        Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> callSuratPengantarKeluar = APIService.Factory.create().allSuratPengantarKeluar(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSuratPengantarKeluar.enqueue(new Callback<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel>() {
            @Override
            public void onResponse(Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> call, Response<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> response) {
                List<SuratPengantarKeluarModel> list = response.body().getResults();
                suratPengantarKeluarCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> call, Throwable t) {
                suratPengantarKeluarCount.setText("0");
            }
        });

        Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> callSuratMasukUndangan = APIService.Factory.create().allSuratMasukUndangan(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSuratMasukUndangan.enqueue(new Callback<SuratMasukUndanganModel.SuratMasukUndanganDataModel>() {
            @Override
            public void onResponse(Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> call, Response<SuratMasukUndanganModel.SuratMasukUndanganDataModel> response) {
                List<SuratMasukUndanganModel> list = response.body().getResults();
                suratMasukUndanganCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> call, Throwable t) {
                suratMasukUndanganCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callSrtPerintah = APIService.Factory.create().allSuratPerintah(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSrtPerintah.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                suratPerintahCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                suratPerintahCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callSrtKeputusan = APIService.Factory.create().allSuratKeputusan(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSrtKeputusan.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                suratKeputusanCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                suratKeputusanCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callNodin = APIService.Factory.create().allNodin(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callNodin.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                nodinCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                nodinCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callPerjanjianKerjasama = APIService.Factory.create().allPerjanjianKerjasama(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callPerjanjianKerjasama.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                perjanjianKerjasamaCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                perjanjianKerjasamaCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callAgendaMou = APIService.Factory.create().allAgendaMou(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callAgendaMou.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                agendaMouCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                agendaMouCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callSptjm = APIService.Factory.create().allSptjm(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSptjm.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                sptjmCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                sptjmCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callBast = APIService.Factory.create().allBast(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callBast.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                bastCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                bastCount.setText("0");
            }
        });

        Call<SuratLainModel.SuratLainDataModel> callSuratKeterangan = APIService.Factory.create().allSuratKeterangan(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callSuratKeterangan.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                List<SuratLainModel> list = response.body().getResults();
                suratKeteranganCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                suratKeteranganCount.setText("0");
            }
        });

        Call<ReferensiModel.ReferensiDataModel> callReferensi = APIService.Factory.create().allReferensi(SessionUtils.getLoggedUser(MainActivity.this).getId());
        callReferensi.enqueue(new Callback<ReferensiModel.ReferensiDataModel>() {
            @Override
            public void onResponse(Call<ReferensiModel.ReferensiDataModel> call, Response<ReferensiModel.ReferensiDataModel> response) {
                List<ReferensiModel> list = response.body().getResults();
                jumlahKlasifikasiCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<ReferensiModel.ReferensiDataModel> call, Throwable t) {
                jumlahKlasifikasiCount.setText("0");
            }
        });

        Call<UserModel.UserDataModel> callUser = APIService.Factory.create().allUser("0");
        callUser.enqueue(new Callback<UserModel.UserDataModel>() {
            @Override
            public void onResponse(Call<UserModel.UserDataModel> call, Response<UserModel.UserDataModel> response) {
                List<UserModel> list = response.body().getResults();
                userCount.setText(String.valueOf(list.size()));
            }
            @Override
            public void onFailure(Call<UserModel.UserDataModel> call, Throwable t) {
            }
        });

        progressDialog.dismiss();
    }

    /**
     * handler for cardview on clicked
     *
     * @param v
     */

    @Override
    public void onClick(View v) {

        if (v == cardViewSuratMasuk)
        {
            Intent intent1 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent1.putExtra("position", 0);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
        }

        if (v == cardViewSuratKeluar)
        {
            Intent intent2 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent2.putExtra("position", 1);
            intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent2);
        }

        if (v == cardViewSuratPengantarKeluar)
        {
            Intent intent3 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent3.putExtra("position", 2);
            intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent3);
        }

        if (v == cardViewSuratMasukUndangan)
        {
            Intent intent4 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent4.putExtra("position", 3);
            intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent4);
        }

        if (v == cardViewSuratPerintah)
        {
            Intent intent5 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent5.putExtra("position", 4);
            intent5.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent5);
        }

        if (v == cardViewSuratKeputsan)
        {
            Intent intent6 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent6.putExtra("position", 5);
            intent6.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent6);
        }

        if (v == cardViewNodin)
        {
            Intent intent7 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent7.putExtra("position", 6);
            intent7.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent7);
        }

        if (v == cardViewPerjanjianKerjasama)
        {
            Intent intent8 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent8.putExtra("position", 7);
            intent8.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent8);
        }

        if (v == cardViewAgendaMou)
        {
            Intent intent9 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent9.putExtra("position", 8);
            intent9.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent9);
        }

        if (v == cardViewSptjm)
        {
            Intent intent10 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent10.putExtra("position", 9);
            intent10.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent10);
        }

        if (v == cardViewBast)
        {
            Intent intent11 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent11.putExtra("position", 10);
            intent11.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent11);
        }

        if (v == cardViewSuratKeterangan)
        {
            Intent intent12 = new Intent(MainActivity.this, TransaksiActivity.class);
            intent12.putExtra("position", 11);
            intent12.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent12);
        }

        if (v == cardViewJumlahKlasifikasi)
        {
            Intent intent13 = new Intent(MainActivity.this, ReferensiActivity.class);
            intent13.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent13);
        }
    }

}