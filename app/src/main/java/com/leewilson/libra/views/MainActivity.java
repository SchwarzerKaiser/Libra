package com.leewilson.libra.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.leewilson.libra.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavView = findViewById(R.id.nvView);

        initNavigation();
    }

    private void initNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavView, mNavController);
        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.mainMenuFragment, false)
                .build();

        switch (item.getItemId()) {

            case R.id.navdrawer_home:
                mNavController.navigate(R.id.mainMenuFragment, null, navOptions);
                break;

            case R.id.navdrawer_search:
                if (isValidDestination(R.id.search_nav_graph))
                    mNavController.navigate(R.id.search_nav_graph, null, navOptions);
                break;

            case R.id.navdrawer_find_a_store:
                // TODO: Implement location feature
                break;

            case R.id.navdrawer_mylibrary:
                if (isValidDestination(R.id.mylibrary_nav_graph))
                    mNavController.navigate(R.id.mylibrary_nav_graph, null, navOptions);
                break;

            case R.id.navdrawer_scanbarcode:
                if (isValidDestination(R.id.barcode_scanner_graph))
                    mNavController.navigate(R.id.barcode_scanner_graph, null, navOptions);
                break;

            case R.id.navdrawer_settings:
                // TODO: Add settings page
                break;
        }

        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private boolean isValidDestination(int destination) {
        return destination != mNavController.getCurrentDestination().getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mDrawerLayout);
    }
}
