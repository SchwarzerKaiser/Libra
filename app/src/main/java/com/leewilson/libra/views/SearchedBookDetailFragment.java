package com.leewilson.libra.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;

public class SearchedBookDetailFragment extends Fragment {

    // Model
    private SearchBooksViewModel mViewModel;
    private Book mBook;

    // UI elements


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(SearchBooksViewModel.class);
        mViewModel.getBookDetail().observe(getActivity(), book -> mBook = book);
        int index = SearchedBookDetailFragmentArgs.fromBundle(getArguments()).getIndex();
        mViewModel.setBookDetailIndex(index);
    }
}
