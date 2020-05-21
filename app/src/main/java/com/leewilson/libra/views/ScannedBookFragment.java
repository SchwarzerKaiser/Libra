package com.leewilson.libra.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        String isbn = getArguments().getString("isbn");
        SearchBooksViewModel viewModel = new ViewModelProvider(requireActivity()).get(SearchBooksViewModel.class);
        viewModel.getScannedBookLiveData().observe(getViewLifecycleOwner(), book -> {
            mBook = book;
            updateViews();
        });
        viewModel.setScannedBook(isbn);
    }

    private void initViews(View view) {
        mTitle = view.findViewById(R.id.book_detail_title);
        mDescription = view.findViewById(R.id.book_detail_description);
        mAuthors = view.findViewById(R.id.book_detail_authors);
        mCoverImage = view.findViewById(R.id.toolbarBookImage);
    }

    private void updateViews() {
        if (mBook != null) {
            Picasso.get().load(mBook.getThumbnailURL()).into(mCoverImage);
            mTitle.setText(mBook.getTitle());
            mDescription.setText(mBook.getDescription());
            mAuthors.setText(mBook.getAuthors());
        }
    }
}
