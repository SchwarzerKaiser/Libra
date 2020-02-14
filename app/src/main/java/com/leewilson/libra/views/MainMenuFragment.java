package com.leewilson.libra.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.leewilson.libra.R;

public class MainMenuFragment extends Fragment {

    private Button mSearchBooksField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_menu_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View thisView, Bundle savedInstanceState) {
        super.onViewCreated(thisView, savedInstanceState);

        mSearchBooksField = thisView.findViewById(R.id.main_menu_search_btn);

        mSearchBooksField.setOnClickListener(view -> {
            NavDirections action = MainMenuFragmentDirections.navActionToSearchScreen();
            Navigation.findNavController(thisView).navigate(action);
        });
    }
}
