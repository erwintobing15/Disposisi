package com.erwintobing15.disposisi.ui.referensi;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.adapter.ReferensiAdapter;
import com.erwintobing15.disposisi.model.MessageModel;
import com.erwintobing15.disposisi.model.ReferensiModel;
import com.erwintobing15.disposisi.network.APIService;
import com.erwintobing15.disposisi.ui.beranda.LoginActivity;
import com.erwintobing15.disposisi.ui.beranda.MainActivity;
import com.erwintobing15.disposisi.ui.transaksi.TransaksiActivity;
import com.erwintobing15.disposisi.util.SessionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferensiActivity extends AppCompatActivity implements ReferensiAdapter.Listener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButtonAdd;
    private Toolbar toolbar;

    private RecyclerView recyclerViewReferensi;
    private ReferensiAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swLayout;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referensi);

        initViews();
        initToolbar(R.string.nav_referensi);

        initFloatingActionButton();
        initNavigation();

        initReferensiRecyclerView();
        loadAllReferensi();
        initRefresherLayout();
    }

    private void initViews() {
        floatingActionButtonAdd = findViewById(R.id.fab_referensi);
        drawerLayout = findViewById(R.id.drawerLayoutReferensi);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_menu_referensi);
        recyclerViewReferensi = findViewById(R.id.referensi_recyclerview);
        progressBar = findViewById(R.id.progressbar);
        swLayout = findViewById(R.id.refresh);
    }

    private void initToolbar(int titleName) {
        toolbar.setTitle(titleName);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, titleName, titleName);
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
                        Intent a = new Intent(ReferensiActivity.this, MainActivity.class);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        break;
                    case R.id.nav_transaksi:
                        Intent b = new Intent(ReferensiActivity.this , TransaksiActivity.class);
                        b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(b);
                        break;
//                  case R.id.nav_buku_agenda:
//                  Intent c = new Intent(ReferensiActivity.this , AgendaActivity.class);
//                  c.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                  startActivity(c);
//                  break;
//                    case R.id.nav_galeri_file:
//                        Intent d = new Intent(ReferensiActivity.this , GaleriActivity.class);
//                        d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(d);
//                        break;
                    case R.id.nav_referensi:
                        break;
                    case R.id.nav_logout:
                        SessionUtils.logout(ReferensiActivity.this);
                        Intent intent = new Intent(ReferensiActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * Load Views Data
     *
     */

    private void initReferensiRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter = new ReferensiAdapter(this, new ArrayList<ReferensiModel>(), (ReferensiAdapter.Listener) this);
        recyclerViewReferensi.setLayoutManager(linearLayoutManager);
        recyclerViewReferensi.setAdapter(adapter);
    }

    private void loadAllReferensi() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewReferensi.setVisibility(View.GONE);
        Call<ReferensiModel.ReferensiDataModel> call = APIService.Factory.create().allReferensi(SessionUtils.getLoggedUser(ReferensiActivity.this).getId());
        call.enqueue(new Callback<ReferensiModel.ReferensiDataModel>() {
            @Override
            public void onResponse(Call<ReferensiModel.ReferensiDataModel> call, Response<ReferensiModel.ReferensiDataModel> response) {
                progressBar.setVisibility(View.GONE);
                recyclerViewReferensi.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<ReferensiModel.ReferensiDataModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReferensiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Refresh view
     *
     */
    private void initRefresherLayout() {

        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);
                        loadAllReferensi();
                    }
                }, 2000);
            }
        });
    }

    /**
     * Insert data
     */

    private void initFloatingActionButton() {
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferensiActivity.this, InsertReferensiActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1001);
            }
        });
    }

    /**
     * Update and delete data
     *
     * @param id
     */
    @Override
    public void onReferensiClick(final String id) {

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
                Intent intent = new Intent(ReferensiActivity.this, UpdateReferensiActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Build an Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ReferensiActivity.this);
                builder.setTitle("Hapus Referensi");
                builder.setMessage("Apakah anda yakin ingin menghapus referensi?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        progressDialog = ProgressDialog.show(ReferensiActivity.this, "", "Loading....", true, false);

                        Call<MessageModel> call = APIService.Factory.create().postdeleteReferensi(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                progressDialog.dismiss();
                                loadAllReferensi();
                                Toast.makeText(ReferensiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.e("ERROR", t.getMessage());
                                Toast.makeText(ReferensiActivity.this, "Berhasil menghapus, silahkan refresh layar", Toast.LENGTH_SHORT).show();
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
    public void onReferensiLongClick(String id) {

    }
}