package com.erwintobing15.disposisi.ui.transaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.adapter.TagendamouAdapter;
import com.erwintobing15.disposisi.adapter.TbastAdapter;
import com.erwintobing15.disposisi.adapter.TnodinAdapter;
import com.erwintobing15.disposisi.adapter.TperjanjiankerjasamaAdapter;
import com.erwintobing15.disposisi.adapter.TsptjmAdapter;
import com.erwintobing15.disposisi.adapter.TsuratkeputusanAdapter;
import com.erwintobing15.disposisi.adapter.TsuratketeranganAdapter;
import com.erwintobing15.disposisi.adapter.TsuratpengantarkeluarAdapter;
import com.erwintobing15.disposisi.adapter.TsuratmasukundanganAdapter;
import com.erwintobing15.disposisi.adapter.TsuratkeluarAdapter;
import com.erwintobing15.disposisi.adapter.TsuratmasukAdapter;
import com.erwintobing15.disposisi.adapter.TsuratperintahAdapter;

import com.erwintobing15.disposisi.model.AgendaMouModel;
import com.erwintobing15.disposisi.model.BastModel;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.NodinModel;
import com.erwintobing15.disposisi.model.PerjanjianKerjasamaModel;
import com.erwintobing15.disposisi.model.SptjmModel;
import com.erwintobing15.disposisi.model.SuratKeputusanModel;
import com.erwintobing15.disposisi.model.SuratKeteranganModel;
import com.erwintobing15.disposisi.model.SuratPengantarKeluarModel;
import com.erwintobing15.disposisi.model.SuratMasukUndanganModel;
import com.erwintobing15.disposisi.model.SuratKeluarModel;
import com.erwintobing15.disposisi.model.SuratMasukModel;
import com.erwintobing15.disposisi.model.SuratPerintahModel;

import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.ui.beranda.LoginActivity;
import com.erwintobing15.disposisi.ui.beranda.MainActivity;
import com.erwintobing15.disposisi.ui.referensi.ReferensiActivity;
import com.erwintobing15.disposisi.ui.transaksi.agendamou.InsertAgendaMouActivity;
import com.erwintobing15.disposisi.ui.transaksi.agendamou.UpdateAgendaMouActivity;
import com.erwintobing15.disposisi.ui.transaksi.bast.InsertBastActivity;
import com.erwintobing15.disposisi.ui.transaksi.bast.UpdateBastActivity;
import com.erwintobing15.disposisi.ui.transaksi.nodin.InsertNodinActivity;
import com.erwintobing15.disposisi.ui.transaksi.nodin.UpdateNodinActivity;
import com.erwintobing15.disposisi.ui.transaksi.perjanjiankerjasama.InsertPerjanjianKerjasamaActivity;
import com.erwintobing15.disposisi.ui.transaksi.perjanjiankerjasama.UpdatePerjanjianKerjasamaActivity;
import com.erwintobing15.disposisi.ui.transaksi.sptjm.InsertSptjmActivity;
import com.erwintobing15.disposisi.ui.transaksi.sptjm.UpdateSptjmActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratkeluar.InsertSuratKeluarActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratkeluar.UpdateSuratKeluarActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratkeputusan.InsertSuratKeputusanActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratkeputusan.UpdateSuratKeputusanActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratketerangan.InsertSuratKeteranganActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratketerangan.UpdateSuratKeteranganActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratmasuk.InsertSuratMasukActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratmasuk.UpdateSuratMasukActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratpengantarkeluar.InsertSuratPengantarkeluarActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratpengantarkeluar.UpdateSuratPengantarkeluarActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratperintah.InsertSuratPerintahActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratperintah.UpdateSuratPerintahActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratmasukundangan.InsertSuratMasukUndanganActivity;
import com.erwintobing15.disposisi.ui.transaksi.suratmasukundangan.UpdateSuratMasukUndanganActivity;
import com.erwintobing15.disposisi.util.SessionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiActivity extends AppCompatActivity implements TsuratmasukAdapter.Listener,
                                                                    TsuratkeluarAdapter.Listener,
                                                                    TsuratpengantarkeluarAdapter.Listener,
                                                                    TsuratmasukundanganAdapter.Listener,
                                                                    TsuratperintahAdapter.Listener,
                                                                    TsuratkeputusanAdapter.Listener,
                                                                    TnodinAdapter.Listener,
                                                                    TperjanjiankerjasamaAdapter.Listener,
                                                                    TagendamouAdapter.Listener,
                                                                    TsptjmAdapter.Listener,
                                                                    TbastAdapter.Listener,
                                                                    TsuratketeranganAdapter.Listener,
                                                                    AdapterView.OnItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Spinner spinner;
    private RecyclerView recyclerViewTransaksi;

    private TsuratmasukAdapter adapter1;
    private TsuratkeluarAdapter adapter2;
    private TsuratpengantarkeluarAdapter adapter3;
    private TsuratmasukundanganAdapter adapter4;
    private TsuratperintahAdapter adapter5;
    private TsuratkeputusanAdapter adapter6;
    private TnodinAdapter adapter7;
    private TperjanjiankerjasamaAdapter adapter8;
    private TagendamouAdapter adapter9;
    private TsptjmAdapter adapter10;
    private TbastAdapter adapter11;
    private TsuratketeranganAdapter adapter12;

    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButtonAdd;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swLayout;
    public String spinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        initViews();
        initToolbar();
        initSpinner();
        initListeners();
        initFloatingActionButton();
        initNavigation();
        initRefreserLayout();

        /**
         * Try get bundle as spinner position
         * if bundle is not empty load view based on spinner position
         * if bundle is empty load view on spinner position 0
         */

        int pos;
        try{
            Bundle b = getIntent().getExtras();
            pos = b.getInt("position");
            spinner.setSelection(pos);
        } catch(Exception ex){
            spinner.setSelection(0);
        }
    }

    /**
     * Initialize views, listener, refresher layout, spinner, toolbar
     * and navigation
     *
     */

    private void initViews() {
        recyclerViewTransaksi = findViewById(R.id.transaksi_recyclerview);
        progressBar = findViewById(R.id.progressbar);
        spinner = findViewById(R.id.spinner);
        floatingActionButtonAdd = findViewById(R.id.fab_transaksi);
        navigationView = findViewById(R.id.navigation_menu_transaksi);
        swLayout = findViewById(R.id.refresh);
    }

    private void initListeners() {
        spinner.setOnItemSelectedListener(this);
    }

    private void initRefreserLayout() {

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);

                        spinnerItem = spinner.getSelectedItem().toString();

                        switch (spinnerItem) {
                            case "Surat Masuk":
                                loadAllTsuratmasuk();
                                break;
                            case "Surat Keluar":
                                loadAllTsuratkeluar();
                                break;
                            case "Surat Pengantar Keluar":
                                loadAllTsuratpengantarkeluar();
                                break;
                            case "Surat Undangan Masuk":
                                loadAllTsuratmasukundangan();
                                break;
                            case "Surat Perintah":
                                loadAllTsuratperintah();
                                break;
                            case "Surat Keputusan":
                                loadAllTsuratkeputusan();
                                break;
                            case "Nodin":
                                loadAllTnodin();
                                break;
                            case "Perjanjian Kerjasama":
                                loadAllTperjanjiankerjasama();
                                break;
                            case "Agenda MOU":
                                loadAllTagendamou();
                                break;
                            case "SPTJM":
                                loadAllTsptjm();
                                break;
                            case "BAST":
                                loadAllTbast();
                                break;
                            case "Surat Keterangan":
                                loadAllTsuratketerangan();
                                break;
                        }
                    }
                }, 2000);
            }
        });
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_surat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initToolbar() {
        drawerLayout = findViewById(R.id.drawerLayoutTransaksi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_transaksi);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_transaksi, R.string.nav_transaksi);
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
                        Intent a = new Intent(TransaksiActivity.this, MainActivity.class);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        break;
                    case R.id.nav_transaksi:
                        break;
//                    case R.id.nav_buku_agenda:
//                        Intent b = new Intent(TransaksiActivity.this , AgendaActivity.class);
//                        b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(b);
//                        break;
//                    case R.id.nav_galeri_file:
//                        Intent c = new Intent(TransaksiActivity.this , GaleriActivity.class);
//                        c.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(c);
//                        break;
                    case R.id.nav_referensi:
                        Intent d = new Intent(TransaksiActivity.this , ReferensiActivity.class);
                        d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(d);
                        break;
                    case R.id.nav_logout:
                        SessionUtils.logout(TransaksiActivity.this);
                        Intent intent = new Intent(TransaksiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * load views based on spinner selected
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text) {
            case "Surat Masuk":
                initTsuratmasukRecyclerView();
                loadAllTsuratmasuk();
                break;
            case "Surat Keluar":
                initTsuratkeluarRecyclerView();
                loadAllTsuratkeluar();
                break;
            case "Surat Pengantar Keluar":
                initTsuratpengantarkeluarRecyclerView();
                loadAllTsuratpengantarkeluar();
                break;
            case "Surat Masuk Undangan":
                initTsuratmasukundanganRecyclerView();
                loadAllTsuratmasukundangan();
                break;
            case "Surat Perintah":
                initTsuratperintahRecyclerView();
                loadAllTsuratperintah();
                break;
            case "Surat Keputusan":
                initTsuratkeputusanRecyclerView();
                loadAllTsuratkeputusan();
                break;
            case "Nodin":
                initTnodinRecyclerView();
                loadAllTnodin();
                break;
            case "Perjanjian Kerjasama":
                initTperjanjiankerjasamaRecyclerView();
                loadAllTperjanjiankerjasama();
                break;
            case "Agenda MOU":
                initTagendamouRecyclerView();
                loadAllTagendamou();
                break;
            case "SPTJM":
                initTsptjmRecyclerView();
                loadAllTsptjm();
                break;
            case "BAST":
                initTbastRecyclerView();
                loadAllTbast();
                break;
            case "Surat Keterangan":
                initTsuratketeranganRecyclerView();
                loadAllTsuratketerangan();
                break;
        }
    }

    /**
     * Insert new data floating action button
     * start new activity to insert new data based on spinner item selected
     *
     */

    private void initFloatingActionButton() {
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
                String text = mySpinner.getSelectedItem().toString();

                switch (text) {
                    case "Surat Masuk":
                        Intent intent1 = new Intent(TransaksiActivity.this, InsertSuratMasukActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent1, 1001);
                        break;
                    case "Surat Keluar":
                        Intent intent2 = new Intent(TransaksiActivity.this, InsertSuratKeluarActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 1002);
                        break;
                    case "Surat Pengantar Keluar":
                        Intent intent3 = new Intent(TransaksiActivity.this, InsertSuratPengantarkeluarActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent3, 1003);
                        break;
                    case "Surat Undangan Masuk":
                        Intent intent4 = new Intent(TransaksiActivity.this, InsertSuratMasukUndanganActivity.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent4, 1004);
                        break;
                    case "Surat Perintah":
                        Intent intent5 = new Intent(TransaksiActivity.this, InsertSuratPerintahActivity.class);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent5, 1005);
                        break;
                    case "Surat Keputusan":
                        Intent intent6 = new Intent(TransaksiActivity.this, InsertSuratKeputusanActivity.class);
                        intent6.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent6, 1006);
                        break;
                    case "Nodin":
                        Intent intent7 = new Intent(TransaksiActivity.this, InsertNodinActivity.class);
                        intent7.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent7, 1007);
                        break;
                    case "Perjanjian Kerjasama":
                        Intent intent8 = new Intent(TransaksiActivity.this, InsertPerjanjianKerjasamaActivity.class);
                        intent8.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent8, 1008);
                        break;
                    case "Agenda MOU":
                        Intent intent9 = new Intent(TransaksiActivity.this, InsertAgendaMouActivity.class);
                        intent9.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent9, 1009);
                        break;
                    case "SPTJM":
                        Intent intent10 = new Intent(TransaksiActivity.this, InsertSptjmActivity.class);
                        intent10.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent10, 1010);
                        break;
                    case "BAST":
                        Intent intent11 = new Intent(TransaksiActivity.this, InsertBastActivity.class);
                        intent11.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent11, 1011);
                        break;
                    case "Surat Keterangan":
                        Intent intent12 = new Intent(TransaksiActivity.this, InsertSuratKeteranganActivity.class);
                        intent12.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent12, 1012);
                        break;
                }
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Initialize and load surat masuk adapter UI
     *
     */

    private void initTsuratmasukRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter1 = new TsuratmasukAdapter(this, new ArrayList<SuratMasukModel>(), (TsuratmasukAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter1);
    }

    private void loadAllTsuratmasuk() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratMasukModel.SuratMasukDataModel> call = APIService.Factory.create().allSuratMasuk(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratMasukModel.SuratMasukDataModel>() {
            @Override
            public void onResponse(Call<SuratMasukModel.SuratMasukDataModel> call, Response<SuratMasukModel.SuratMasukDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter1.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratMasukModel.SuratMasukDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Surat masuk onclick listener handler
     * insert, update, and delete data
     */

    @Override
    public void onTsuratmasukClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);
//        TextView textViewDownload = dialog.findViewById(R.id.dialog_download);


//        textViewDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });


        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratMasukActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postDeleteSuratMasuk(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratmasuk();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratmasukLongClick(final String id) {
        // empty action
    }

    /**
     * Initialize and load surat keluar adapter UI
     *
     */
    private void initTsuratkeluarRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter2 = new TsuratkeluarAdapter(this, new ArrayList<SuratKeluarModel>(), (TsuratkeluarAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter2);
    }

    private void loadAllTsuratkeluar() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratKeluarModel.SuratKeluarDataModel> call = APIService.Factory.create().allSuratKeluar(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratKeluarModel.SuratKeluarDataModel>() {
            @Override
            public void onResponse(Call<SuratKeluarModel.SuratKeluarDataModel> call, Response<SuratKeluarModel.SuratKeluarDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter2.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratKeluarModel.SuratKeluarDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Surat keluar onclick listener handler
     * insert, update, and delete data
     */

    @Override
    public void onTsuratkeluarClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);


        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratKeluarActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Keluar");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postDeleteSuratKeluar(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratkeluar();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratkeluarLongClick(String id) {

    }

    /**
     * Initialize and load surat pengantar keluar (suratpengantarkeluar) adapter UI
     *
     */

    private void initTsuratpengantarkeluarRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter3 = new TsuratpengantarkeluarAdapter(this, new ArrayList<SuratPengantarKeluarModel>(), (TsuratpengantarkeluarAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter3);
    }

    private void loadAllTsuratpengantarkeluar() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> call = APIService.Factory.create().allSuratPengantarKeluar(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel>() {
            @Override
            public void onResponse(Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> call, Response<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter3.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Surat pengantar keluar onclick listener handler
     * insert, update, and delete data
     */

    @Override
    public void onTsuratpengantarkeluarClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);


        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratPengantarkeluarActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Pengantar Keluar");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postDeleteSuratPengantarKeluar(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratpengantarkeluar();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratpengantarkeluarLongClick(String id) {

    }

    /**
     * Initialize and load surat masuk undangan adapter UI
     *
     */

    private void initTsuratmasukundanganRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter4 = new TsuratmasukundanganAdapter(this, new ArrayList<SuratMasukUndanganModel>(), (TsuratmasukundanganAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter4);
    }

    private void loadAllTsuratmasukundangan() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> call = APIService.Factory.create().allSuratMasukUndangan(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratMasukUndanganModel.SuratMasukUndanganDataModel>() {
            @Override
            public void onResponse(Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> call, Response<SuratMasukUndanganModel.SuratMasukUndanganDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter4.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Surat masuk undangan onclick listener handler
     * insert, update, and delete data
     */

    @Override
    public void onTsuratmasukundanganClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);


        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratMasukUndanganActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Undangan Masuk");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSuratMasukUndangan(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratmasukundangan();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratmasukundanganLongClick(String id) {

    }


    /**
     * Initialize and load adapter UI view for surat perintah
     * Surat perintah onClick handler
     */

    private void initTsuratperintahRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter5 = new TsuratperintahAdapter(this, new ArrayList<SuratPerintahModel>(), (TsuratperintahAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter5);
    }

    private void loadAllTsuratperintah() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratPerintahModel.SuratPerintahDataModel> call = APIService.Factory.create().allSuratPerintah(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratPerintahModel.SuratPerintahDataModel>() {
            @Override
            public void onResponse(Call<SuratPerintahModel.SuratPerintahDataModel> call, Response<SuratPerintahModel.SuratPerintahDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratPerintahModel.SuratPerintahDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTsuratperintahClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratPerintahActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Perintah");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSuratPerintah(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratperintah();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratperintahLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for surat keputusan
     * Surat keputusan onClick handler
     */

    private void initTsuratkeputusanRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter6 = new TsuratkeputusanAdapter(this, new ArrayList<SuratKeputusanModel>(), (TsuratkeputusanAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter6);
    }

    private void loadAllTsuratkeputusan() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratKeputusanModel.SuratKeputusanDataModel> call = APIService.Factory.create().allSuratKeputusan(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratKeputusanModel.SuratKeputusanDataModel>() {
            @Override
            public void onResponse(Call<SuratKeputusanModel.SuratKeputusanDataModel> call, Response<SuratKeputusanModel.SuratKeputusanDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter6.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratKeputusanModel.SuratKeputusanDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTsuratkeputusanClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratKeputusanActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Keputusan");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSuratKeputusan(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratkeputusan();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTsuratkeputusanLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for surat nodin
     * Surat nodin onClick handler
     */

    private void initTnodinRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter7 = new TnodinAdapter(this, new ArrayList<NodinModel>(), (TnodinAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter7);
    }

    private void loadAllTnodin() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<NodinModel.NodinDataModel> call = APIService.Factory.create().allNodin(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<NodinModel.NodinDataModel>() {
            @Override
            public void onResponse(Call<NodinModel.NodinDataModel> call, Response<NodinModel.NodinDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter7.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<NodinModel.NodinDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTnodinClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateNodinActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Nodin");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteNodin(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTnodin();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();
    }

    @Override
    public void onTnodinLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for surat perjanjian kerjasama
     * Surat perjanjian kerjasama onClick handler
     */

    private void initTperjanjiankerjasamaRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter8 = new TperjanjiankerjasamaAdapter(this, new ArrayList<PerjanjianKerjasamaModel>(), (TperjanjiankerjasamaAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter8);
    }

    private void loadAllTperjanjiankerjasama() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel> call = APIService.Factory.create().allPerjanjianKerjasama(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel>() {
            @Override
            public void onResponse(Call<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel> call, Response<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter8.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTperjanjiankerjasamaClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdatePerjanjianKerjasamaActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Perjanjian Kerjasama");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeletePerjanjianKerjasama(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTperjanjiankerjasama();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();

    }

    @Override
    public void onTperjanjiankerjasamaLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for agenda mou
     * agenda mou onClick handler
     */

    private void initTagendamouRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter9 = new TagendamouAdapter(this, new ArrayList<AgendaMouModel>(), (TagendamouAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter9);
    }

    private void loadAllTagendamou() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<AgendaMouModel.AgendaMouDataModel> call = APIService.Factory.create().allAgendaMou(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<AgendaMouModel.AgendaMouDataModel>() {
            @Override
            public void onResponse(Call<AgendaMouModel.AgendaMouDataModel> call, Response<AgendaMouModel.AgendaMouDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter9.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<AgendaMouModel.AgendaMouDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTagendamouClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateAgendaMouActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Agenda MOU");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteAgendaMou(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTagendamou();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();

    }

    @Override
    public void onTagendamouLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for SPTJM
     * SPTJM onClick handler
     */

    private void initTsptjmRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter10 = new TsptjmAdapter(this, new ArrayList<SptjmModel>(), (TsptjmAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter10);
    }

    private void loadAllTsptjm() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SptjmModel.SptjmDataModel> call = APIService.Factory.create().allSptjm(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SptjmModel.SptjmDataModel>() {
            @Override
            public void onResponse(Call<SptjmModel.SptjmDataModel> call, Response<SptjmModel.SptjmDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter10.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SptjmModel.SptjmDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTsptjmClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSptjmActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus SPTJM");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSptjm(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsptjm();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();

    }

    @Override
    public void onTsptjmLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for BAST
     * BAST onClick handler
     */

    private void initTbastRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter11 = new TbastAdapter(this, new ArrayList<BastModel>(), (TbastAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter11);
    }

    private void loadAllTbast() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<BastModel.BastDataModel> call = APIService.Factory.create().allBast(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<BastModel.BastDataModel>() {
            @Override
            public void onResponse(Call<BastModel.BastDataModel> call, Response<BastModel.BastDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter11.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<BastModel.BastDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTbastClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateBastActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus BAST");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteBast(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTbast();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();

    }

    @Override
    public void onTbastLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for surat keterangan
     * surat keterangan onClick handler
     */

    private void initTsuratketeranganRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter12 = new TsuratketeranganAdapter(this, new ArrayList<SuratKeteranganModel>(), (TsuratketeranganAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter12);
    }

    private void loadAllTsuratketerangan() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratKeteranganModel.SuratKeteranganDataModel> call = APIService.Factory.create().allSuratKeterangan(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratKeteranganModel.SuratKeteranganDataModel>() {
            @Override
            public void onResponse(Call<SuratKeteranganModel.SuratKeteranganDataModel> call, Response<SuratKeteranganModel.SuratKeteranganDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter12.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratKeteranganModel.SuratKeteranganDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTsuratketeranganClick(final String id) {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransaksiActivity.this, UpdateSuratKeteranganActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
                builder.setTitle("Hapus Surat Keterangan");
                builder.setMessage("Apakah anda yakin ingin menghapus surat?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Delete file when user clicked the Yes button
                        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSuratKeterangan(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsuratketerangan();
                                Toast.makeText(TransaksiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(TransaksiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "Tidak jadi menghapus",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dialog.show();

    }

    @Override
    public void onTsuratketeranganLongClick(String id) {

    }
}