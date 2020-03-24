package com.leewilson.libra.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.leewilson.libra.R;

import static androidx.navigation.Navigation.findNavController;

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

        ActionBarDrawerToggle drawerToggle = getDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mDrawer.addDrawerListener(drawerToggle);
    }

    private ActionBarDrawerToggle getDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNavDrawerItemSelected(MenuItem item) {

        NavController controller = findNavController(this, R.id.nav_host_fragment);

        switch(item.getItemId()) {

            case R.id.navdrawer_home:
                controller.navigate(R.id.mainMenuFragment);
                break;

            case R.id.navdrawer_search:
                controller.navigate(R.id.search_nav_graph);
                break;

            case R.id.navdrawer_find_a_store:
                // TODO: Implement location feature
                break;

            case R.id.navdrawer_mylibrary:
                controller.navigate(R.id.myLibraryFragment);
                break;

            case R.id.navdrawer_scanbarcode:
                controller.navigate(R.id.barcodeScannerFragment);
                break;

            case R.id.navdrawer_settings:
                // TODO: Add settings page
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
    }
}
