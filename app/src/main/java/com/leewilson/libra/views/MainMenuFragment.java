package com.leewilson.libra.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.leewilson.libra.R;

public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View thisView, Bundle savedInstanceState) {
        super.onViewCreated(thisView, savedInstanceState);

        CardView searchBooks = thisView.findViewById(R.id.search_books_cardview);
        CardView barcodeScan = thisView.findViewById(R.id.scan_barcode_cardview);
        CardView myLibrary = thisView.findViewById(R.id.menu_mylibrary_cardview);

        searchBooks.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.search_nav_graph);
        });

        barcodeScan.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.barcodeScannerFragment);
        });

        myLibrary.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.mylibrary_nav_graph);
        });
    }
}
