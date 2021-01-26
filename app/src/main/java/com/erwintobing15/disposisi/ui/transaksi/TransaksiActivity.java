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
import com.erwintobing15.disposisi.adapter.TspkeluarAdapter;
import com.erwintobing15.disposisi.adapter.TsumasukAdapter;
import com.erwintobing15.disposisi.adapter.TsuratkeluarAdapter;
import com.erwintobing15.disposisi.adapter.TsuratlainAdapter;
import com.erwintobing15.disposisi.adapter.TsuratmasukAdapter;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.SPKeluarModel;
import com.erwintobing15.disposisi.model.SUMasukModel;
import com.erwintobing15.disposisi.model.SuratKeluarModel;
import com.erwintobing15.disposisi.model.SuratLainModel;
import com.erwintobing15.disposisi.model.SuratMasukModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.ui.galeri.GaleriActivity;
import com.erwintobing15.disposisi.ui.LoginActivity;
import com.erwintobing15.disposisi.ui.MainActivity;
import com.erwintobing15.disposisi.ui.referensi.ReferensiActivity;
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
                                                                    TspkeluarAdapter.Listener,
                                                                    TsumasukAdapter.Listener,
                                                                    TsuratlainAdapter.Listener,
                                                                    AdapterView.OnItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Spinner spinner;
    private RecyclerView recyclerViewTransaksi;
    private TsuratmasukAdapter adapter1;
    private TsuratkeluarAdapter adapter2;
    private TspkeluarAdapter adapter3;
    private TsumasukAdapter adapter4;
    private TsuratlainAdapter adapter5;
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
        navigationView = (NavigationView) findViewById(R.id.navigation_menu_transaksi);
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
                                loadAllTspkeluar();
                                break;
                            case "Surat Undangan Masuk":
                                loadAllTsumasuk();
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));
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
     * load views on spinner selected
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
                initTspkeluarRecyclerView();
                loadAllTspkeluar();
                break;
            case "Surat Undangan Masuk":
                initTsumasukRecyclerView();
                loadAllTsumasuk();
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
                initTsuratperintahRecyclerView();
                loadAllTnodin();
                break;
            case "Perjanjian Kerjasama":
                initTsuratperintahRecyclerView();
                loadAllTperjanjiankerjasama();
                break;
            case "Agenda MOU":
                initTsuratperintahRecyclerView();
                loadAllTagendamou();
                break;
            case "SPTJM":
                initTsuratperintahRecyclerView();
                loadAllTsptjm();
                break;
            case "BAST":
                initTsuratperintahRecyclerView();
                loadAllTbast();
                break;
            case "Surat Keterangan":
                initTsuratperintahRecyclerView();
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
                        Intent intent3 = new Intent(TransaksiActivity.this, InsertSpkeluarActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent3, 1003);
                        break;
                    case "Surat Undangan Masuk":
                        Intent intent4 = new Intent(TransaksiActivity.this, InsertSumasukActivity.class);
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
     * Initialize and load surat pengantar keluar (spkeluar) adapter UI
     *
     */

    private void initTspkeluarRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter3 = new TspkeluarAdapter(this, new ArrayList<SPKeluarModel>(), (TspkeluarAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter3);
    }

    private void loadAllTspkeluar() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SPKeluarModel.SPKeluarDataModel> call = APIService.Factory.create().allSPKeluar(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SPKeluarModel.SPKeluarDataModel>() {
            @Override
            public void onResponse(Call<SPKeluarModel.SPKeluarDataModel> call, Response<SPKeluarModel.SPKeluarDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter3.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SPKeluarModel.SPKeluarDataModel> call, Throwable t) {
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
    public void onTspkeluarClick(final String id) {
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
                Intent intent = new Intent(TransaksiActivity.this, UpdateSpkeluarActivity.class);
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

                        Call<MessageModel> call = APIService.Factory.create().postDeleteSPKeluar(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTspkeluar();
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
    public void onTspkeluarLongClick(String id) {

    }

    /**
     * Initialize and load surat undangan masuk (sumasuk) adapter UI
     *
     */

    private void initTsumasukRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter4 = new TsumasukAdapter(this, new ArrayList<SUMasukModel>(), (TsumasukAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter4);
    }

    private void loadAllTsumasuk() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SUMasukModel.SUMasukDataModel> call = APIService.Factory.create().allSUMasuk(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SUMasukModel.SUMasukDataModel>() {
            @Override
            public void onResponse(Call<SUMasukModel.SUMasukDataModel> call, Response<SUMasukModel.SUMasukDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter4.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SUMasukModel.SUMasukDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Surat undangan masuk onclick listener handler
     * insert, update, and delete data
     */

    @Override
    public void onTsumasukClick(final String id) {

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
                Intent intent = new Intent(TransaksiActivity.this, UpdateSumasukActivity.class);
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

                        Call<MessageModel> call = APIService.Factory.create().postdeleteSUMasuk(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllTsumasuk();
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
    public void onTsumasukLongClick(String id) {

    }

    /**
     * Initialize and load adapter UI view for surat perintah
     *
     */

    private void initTsuratperintahRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter5 = new TsuratlainAdapter(this, new ArrayList<SuratLainModel>(), (TsuratlainAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter5);
    }

    private void loadAllTsuratperintah() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allSuratPerintah(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for surat keputusan
     *
     */

    private void initTsuratkeputusanRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter5 = new TsuratlainAdapter(this, new ArrayList<SuratLainModel>(), (TsuratlainAdapter.Listener) this);
        recyclerViewTransaksi.setLayoutManager(linearLayoutManager);
        recyclerViewTransaksi.setAdapter(adapter5);
    }

    private void loadAllTsuratkeputusan() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allSuratKeputusan(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for nodin
     *
     */

    private void loadAllTnodin() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allNodin(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for perjanjian kerjasama
     *
     */

    private void loadAllTperjanjiankerjasama() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allPerjanjianKerjasama(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for Agenda MOU
     *
     */

    private void loadAllTagendamou() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allAgendaMou(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for SPTJM
     *
     */

    private void loadAllTsptjm() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allSptjm(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for BAST
     *
     */

    private void loadAllTbast() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allBast(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize and load adapter UI view for Surat Keterangan
     *
     */

    private void loadAllTsuratketerangan() {
        progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Load data.....", true, false);
        recyclerViewTransaksi.setVisibility(View.GONE);
        Call<SuratLainModel.SuratLainDataModel> call = APIService.Factory.create().allSuratKeterangan(SessionUtils.getLoggedUser(TransaksiActivity.this).getId());
        call.enqueue(new Callback<SuratLainModel.SuratLainDataModel>() {
            @Override
            public void onResponse(Call<SuratLainModel.SuratLainDataModel> call, Response<SuratLainModel.SuratLainDataModel> response) {
                progressDialog.dismiss();
                recyclerViewTransaksi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter5.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<SuratLainModel.SuratLainDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransaksiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Onclick listener handler for surat perintah, surat keputusan, nodin, agenda MOU
     * SPTJM, BAST, and surat keterangan data
     * insert, update, and delete data
     */

    @Override
    public void onTsuratlainClick(final String id) {

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
                spinnerItem = spinner.getSelectedItem().toString();

                switch (spinnerItem) {
                    case "Surat Perintah":
                        Intent intent1 = new Intent(TransaksiActivity.this, UpdateSuratPerintahActivity.class);
                        intent1.putExtra("id", id);
                        startActivity(intent1);
                        break;
                    case "Surat Keputusan":
                        Intent intent2 = new Intent(TransaksiActivity.this, UpdateSuratKeputusanActivity.class);
                        intent2.putExtra("id", id);
                        startActivity(intent2);
                        break;
                    case "Nodin":
                        Intent intent3 = new Intent(TransaksiActivity.this, UpdateNodinActivity.class);
                        intent3.putExtra("id", id);
                        startActivity(intent3);
                        break;
                    case "Perjanjian Kerjasama":
                        Intent intent4 = new Intent(TransaksiActivity.this, UpdatePerjanjianKerjasamaActivity.class);
                        intent4.putExtra("id", id);
                        startActivity(intent4);
                        break;
                    case "Agenda MOU":
                        Intent intent5 = new Intent(TransaksiActivity.this, UpdateAgendaMouActivity.class);
                        intent5.putExtra("id", id);
                        startActivity(intent5);
                        break;
                    case "SPTJM":
                        Intent intent6 = new Intent(TransaksiActivity.this, UpdateSptjmActivity.class);
                        intent6.putExtra("id", id);
                        startActivity(intent6);
                        break;
                    case "BAST":
                        Intent intent7 = new Intent(TransaksiActivity.this, UpdateBastActivity.class);
                        intent7.putExtra("id", id);
                        startActivity(intent7);
                        break;
                    case "Surat Keterangan":
                        Intent intent8 = new Intent(TransaksiActivity.this, UpdateSuratKeteranganActivity.class);
                        intent8.putExtra("id", id);
                        startActivity(intent8);
                        break;
                }
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerItem = spinner.getSelectedItem().toString();

                // delete data based on spinner selected and view id clicked
                switch (spinnerItem) {
                    case "Surat Perintah":
                        dialog.dismiss();
                        Call<MessageModel> call1 = APIService.Factory.create().postdeleteSuratPerintah(id);
                        deleteSuratPerintah(call1);
                        break;
                    case "Surat Keputusan":
                        dialog.dismiss();
                        Call<MessageModel> call2 = APIService.Factory.create().postdeleteSuratKeputusan(id);
                        deleteSuratKeputusan(call2);
                        break;
                    case "Nodin":
                        dialog.dismiss();
                        Call<MessageModel> call3 = APIService.Factory.create().postdeleteNodin(id);
                        deleteNodin(call3);
                        break;
                    case "Perjanjian Kerjasama":
                        dialog.dismiss();
                        Call<MessageModel> call4 = APIService.Factory.create().postdeletePerjanjianKerjasama(id);
                        deletePerjanjianKerjasama(call4);
                        break;
                    case "Agenda MOU":
                        dialog.dismiss();
                        Call<MessageModel> call5 = APIService.Factory.create().postdeleteAgendaMou(id);
                        deleteAgendaMou(call5);
                        break;
                    case "SPTJM":
                        dialog.dismiss();
                        Call<MessageModel> call6 = APIService.Factory.create().postdeleteSptjm(id);
                        deleteSptjm(call6);
                        break;
                    case "BAST":
                        dialog.dismiss();
                        Call<MessageModel> call7 = APIService.Factory.create().postdeleteBast(id);
                        deleteBast(call7);
                        break;
                    case "Surat Keterangan":
                        dialog.dismiss();
                        Call<MessageModel> call8 = APIService.Factory.create().postdeleteSuratKeterangan(id);
                        deleteSuratKeterangan(call8);
                        break;
                }

            }
        });

        dialog.show();
    }

    /**
     * Method to delete surat perintah
     *
     * @param call
     */

    private void deleteSuratPerintah(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Surat Perintah");
        builder.setMessage("Apakah anda yakin ingin menghapus surat?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete surat keputusan
     *
     * @param call
     */

    private void deleteSuratKeputusan(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Surat Keputusan");
        builder.setMessage("Apakah anda yakin ingin menghapus surat?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete nodin
     *
     * @param call
     */

    private void deleteNodin(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Nodin");
        builder.setMessage("Apakah anda yakin ingin menghapus nodin?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete perjanjian kerjasama data
     *
     * @param call
     */

    private void deletePerjanjianKerjasama(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Perjanjian Kerjasama");
        builder.setMessage("Apakah anda yakin ingin menghapus surat perjanjian kerjasama?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete Agenda MOU data
     *
     * @param call
     */

    private void deleteAgendaMou(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Agenda MOU");
        builder.setMessage("Apakah anda yakin ingin menghapus Agenda MOU?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete SPTJM data
     *
     * @param call
     */

    private void deleteSptjm(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus Agenda MOU");
        builder.setMessage("Apakah anda yakin ingin menghapus Agenda MOU?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete BAST data
     *
     * @param call
     */

    private void deleteBast(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus surat BAST");
        builder.setMessage("Apakah anda yakin ingin menghapus surat BAST?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    /**
     * Method to delete Surat keterangan data
     *
     * @param call
     */

    private void deleteSuratKeterangan(final Call<MessageModel> call) {

        // Build an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TransaksiActivity.this);
        builder.setTitle("Hapus surat keterangan");
        builder.setMessage("Apakah anda yakin ingin menghapus surat keterangan?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Delete file when user clicked the Yes button
                progressDialog = ProgressDialog.show(TransaksiActivity.this, "", "Loading....", true, false);

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

    @Override
    public void onTsuratlainLongClick(String id) {

    }
}