package com.erwintobing15.disposisi.ui.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.ui.beranda.LoginActivity;
import com.erwintobing15.disposisi.ui.beranda.MainActivity;
import com.erwintobing15.disposisi.ui.referensi.ReferensiActivity;
import com.erwintobing15.disposisi.ui.transaksi.TransaksiActivity;
import com.erwintobing15.disposisi.util.SessionUtils;
import com.google.android.material.navigation.NavigationView;

public class AgendaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        initViews();
        initToolbar(R.string.nav_agenda);

        initSpinner();
        initNavigation();
    }

    private void initViews() {
        spinner = findViewById(R.id.spinner);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu_agenda);
    }

    private void initToolbar(int titleName) {
        drawerLayout = findViewById(R.id.drawerLayoutAgenda);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(titleName);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, titleName, titleName);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));
        actionBarDrawerToggle.syncState();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_surat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    // spinner event handler
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initNavigation() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId())
                {
                    case  R.id.nav_home:
                        Intent a = new Intent(AgendaActivity.this, MainActivity.class);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        break;
                    case R.id.nav_transaksi:
                        Intent b = new Intent(AgendaActivity.this , TransaksiActivity.class);
                        b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(b);
                        break;
//                    case R.id.nav_buku_agenda:
//                        break;
//                    case R.id.nav_galeri_file:
//                        Intent c = new Intent(AgendaActivity.this , GaleriActivity.class);
//                        c.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(c);
//                        break;
                    case R.id.nav_referensi:
                        Intent d = new Intent(AgendaActivity.this , ReferensiActivity.class);
                        d.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(d);
                        break;
                    case R.id.nav_logout:
                        SessionUtils.logout(AgendaActivity.this);
                        Intent intent = new Intent(AgendaActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            return false;
            }
        });
    }
}