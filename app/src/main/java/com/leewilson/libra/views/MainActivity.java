package com.leewilson.libra.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.leewilson.libra.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.navdrawer_search:
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.searchedBookDetailFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNavDrawerItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.navdrawer_home:
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.mainMenuFragment);
                mDrawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.navdrawer_search:
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.searchFragment);
                mDrawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.navdrawer_find_a_store:
                // TODO: Implement location feature
                break;

            case R.id.navdrawer_mylibrary:
                // TODO: Implement MyLibrary feature
                break;

            case R.id.navdrawer_scanbarcode:
                // TODO: Research and implement barcode scanner
                break;

            case R.id.navdrawer_settings:
                // TODO: Add settings page
                break;
        }
    }
}
