package com.leewilson.libra.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
    private String mIsbn;
    private SearchBooksViewModel mViewModel;

    public ScannedBookFragment() { /*Required empty public constructor */ }

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
        mViewModel = new ViewModelProvider(requireActivity()).get(SearchBooksViewModel.class);
        mViewModel.getScannedBookLiveData().observe(getViewLifecycleOwner(), book -> {
            mBook = book;
            updateViews();
        });
        mViewModel.setScannedBook(mIsbn);
    }

    private void updateViews() {
        if (mBook == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(getResources().getString(R.string.scan_book_not_found_message))
                    .setTitle(getResources().getString(R.string.scan_book_not_found_title))
                    .setNeutralButton("OK", (dialogInterface, i) -> {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                                .navigate(R.id.mainMenuFragment);
                        dialogInterface.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        Picasso.get().load(mBook.getThumbnailURL()).into(mCoverImage);
        mTitle.setText(mBook.getTitle());
        mDescription.setText(mBook.getDescription());
        mAuthors.setText(mBook.getAuthors());
    }
}
