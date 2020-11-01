package com.leewilson.libra.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
        CardView findAStore = thisView.findViewById(R.id.find_a_store_cardview);

        searchBooks.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.search_nav_graph);
        });

        barcodeScan.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.barcode_scanner_graph);
        });

        myLibrary.setOnClickListener(view -> {
            Navigation.findNavController(thisView).navigate(R.id.mylibrary_nav_graph);
        });

        findAStore.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=book stores");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });
    }
}
