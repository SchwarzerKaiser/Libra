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

    private CardView mSearchBooks;
    private CardView mBarcodeScan;
    private CardView mMyLibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View thisView, Bundle savedInstanceState) {
        super.onViewCreated(thisView, savedInstanceState);

        mSearchBooks = thisView.findViewById(R.id.search_books_cardview);
        mBarcodeScan = thisView.findViewById(R.id.scan_barcode_cardview);
        mMyLibrary = thisView.findViewById(R.id.menu_mylibrary_cardview);

        mSearchBooks.setOnClickListener(view -> {
            NavDirections action = MainMenuFragmentDirections.navActionToSearchScreen();
            Navigation.findNavController(thisView).navigate(action);
        });

        mBarcodeScan.setOnClickListener(view -> {
            NavDirections action = MainMenuFragmentDirections.navActionToBarcodeScanner();
            Navigation.findNavController(thisView).navigate(action);
        });

        mMyLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = MainMenuFragmentDirections.actionMainMenuFragmentToMyLibraryFragment();
                Navigation.findNavController(thisView).navigate(action);
            }
        });
    }
}
