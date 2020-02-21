package com.leewilson.libra.views;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;
import com.squareup.picasso.Picasso;

public class ScannedBookFragment extends Fragment {

    private Book mBook;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mAuthors;
    private ImageView mCoverImage;
    private String mIsbn;
    private SearchBooksViewModel mViewModel;

    public ScannedBookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = view.findViewById(R.id.book_detail_title);
        mDescription = view.findViewById(R.id.book_detail_description);
        mAuthors = view.findViewById(R.id.book_detail_authors);
        mCoverImage = view.findViewById(R.id.toolbarBookImage);

        mIsbn = getArguments().getString("isbn");
        mViewModel = new ViewModelProvider(getActivity()).get(SearchBooksViewModel.class);
        mViewModel.getScannedBookLiveData().observe(getActivity(), book -> {
            mBook = book;
            updateViews();
        });
        mViewModel.setScannedBook(mIsbn);
    }

    private void updateViews() {
        Picasso.get().load(mBook.getThumbnailURL()).into(mCoverImage);
        mTitle.setText(mBook.getTitle());
        mDescription.setText(mBook.getDescription());
        mAuthors.setText(mBook.getAuthors());
    }
}
