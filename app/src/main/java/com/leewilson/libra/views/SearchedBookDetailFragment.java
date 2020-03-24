package com.leewilson.libra.views;


import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
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
import com.google.android.material.snackbar.Snackbar;
import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;
import com.squareup.picasso.Picasso;

public class SearchedBookDetailFragment extends Fragment {

    // Model
    private SearchBooksViewModel mViewModel;

    // UI elements
    private ImageView mBookToolbarImage;
    private TextView mDescription;
    private TextView mTitle;
    private TextView mAuthors;
    private FloatingActionButton mFab;
    private Book mBook;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBookToolbarImage = view.findViewById(R.id.toolbarBookImage);
        mDescription = view.findViewById(R.id.book_detail_description);
        mTitle = view.findViewById(R.id.book_detail_title);
        mAuthors = view.findViewById(R.id.book_detail_authors);
        mFab = view.findViewById(R.id.add_book_to_library_fab);

        NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ViewModelStoreOwner owner = controller.getViewModelStoreOwner(R.id.search_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(SearchBooksViewModel.class);
        mViewModel.getSearchedBookLiveData().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                Picasso.get().load(book.getThumbnailURL()).into(mBookToolbarImage);
                mBook = book;
                mTitle.setText(book.getTitle());
                mDescription.setText(book.getDescription());
                mAuthors.setText(book.getAuthors());
                mViewModel.checkIsStoredLocally(book);
            }
        });
        int index = SearchedBookDetailFragmentArgs.fromBundle(getArguments()).getIndex();
        mViewModel.setBookDetailIndex(index);

        mViewModel.getIsStoredLocallyLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isStoredLocally) {
                if (isStoredLocally) {
                    changeFabToStoredLocally();
                }
            }
        });

        mFab.setOnClickListener(v -> {
            if (mBook != null) {
                mViewModel.addToMyLibrary(mBook);
                changeFabToStoredLocally();
                Toast.makeText(getContext(), "Added to MyLibrary", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Add social features
        // TODO: Amazon purchase link?
    }

    private void changeFabToStoredLocally() {
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCheckMarkGreen)));
        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp, getContext().getTheme()));
        mFab.setClickable(false);
    }
}
