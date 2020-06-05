package com.leewilson.libra.views;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.ScannerViewModel;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;
import com.squareup.picasso.Picasso;

public class ScannedBookFragment extends Fragment {

    private ScannerViewModel mViewModel;

    private Book mBook;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mAuthors;
    private ImageView mCoverImage;
    private FloatingActionButton mFab;

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
        initViewModel();
        subscribeObservers();

        String isbn = getArguments().getString("isbn");
        mViewModel.setScannedBook(isbn);

        mFab.setOnClickListener(v -> {
            if (mBook != null) {
                mViewModel.addToMyLibrary(mBook);
                changeFabToStoredLocally();
                Toast.makeText(getContext(), "Added to MyLibrary", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subscribeObservers() {
        mViewModel.getScannedBookLiveData().observe(getViewLifecycleOwner(), book -> {
            mBook = book;
            updateViews();
            mViewModel.checkIsStoredLocally(book);
        });

        mViewModel.getIsStoredLocallyLiveData().observe(getViewLifecycleOwner(), isStoredLocally -> {
            if (isStoredLocally) {
                changeFabToStoredLocally();
            }
        });
    }

    private void initViewModel() {
        ViewModelStoreOwner owner = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .getViewModelStoreOwner(R.id.barcode_scanner_graph);
        mViewModel = new ViewModelProvider(owner).get(ScannerViewModel.class);
    }

    private void initViews(View view) {
        mTitle = view.findViewById(R.id.book_detail_title);
        mDescription = view.findViewById(R.id.book_detail_description);
        mAuthors = view.findViewById(R.id.book_detail_authors);
        mCoverImage = view.findViewById(R.id.toolbarBookImage);
        mFab = view.findViewById(R.id.add_book_to_library_fab);
    }

    private void updateViews() {
        if (mBook != null) {
            Picasso.get().load(mBook.getThumbnailURL()).into(mCoverImage);
            mTitle.setText(mBook.getTitle());
            mDescription.setText(mBook.getDescription());
            mAuthors.setText(mBook.getAuthors());
        }
    }

    private void changeFabToStoredLocally() {
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCheckMarkGreen)));
        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp, getContext().getTheme()));
        mFab.setClickable(false);
    }
}
