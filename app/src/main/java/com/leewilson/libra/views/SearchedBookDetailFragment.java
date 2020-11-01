package com.leewilson.libra.views;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leewilson.libra.R;
import com.leewilson.libra.adapters.BookSearchListAdapter;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;
import com.squareup.picasso.Picasso;

public class SearchedBookDetailFragment extends Fragment {

    private SearchBooksViewModel mViewModel;

    private ImageView mBookToolbarImage;
    private TextView mDescription;
    private TextView mTitle;
    private TextView mAuthors;
    private FloatingActionButton mFab;
    private Book mBook;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        subscribeObservers();

        int index = getArguments().getInt(BookSearchListAdapter.BOOK_INDEX_TAG);
        mViewModel.setBookDetailIndex(index);

        mFab.setOnClickListener(v -> {
            if (mBook != null) {
                mViewModel.addToMyLibrary(mBook);
                changeFabToStoredLocally();
                Toast.makeText(getContext(), "Added to MyLibrary", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {
        mBookToolbarImage = view.findViewById(R.id.toolbarBookImage);
        mDescription = view.findViewById(R.id.book_detail_description);
        mTitle = view.findViewById(R.id.book_detail_title);
        mAuthors = view.findViewById(R.id.book_detail_authors);
        mFab = view.findViewById(R.id.add_book_to_library_fab);
    }

    private void initViewModel() {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        ViewModelStoreOwner owner = controller.getViewModelStoreOwner(R.id.search_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(SearchBooksViewModel.class);
    }

    private void subscribeObservers() {
        mViewModel.getSearchedBookLiveData().observe(getViewLifecycleOwner(), book -> {
            Picasso.get().load(book.getThumbnailURL()).into(mBookToolbarImage);
            mBook = book;
            mTitle.setText(book.getTitle());
            mDescription.setText(book.getDescription());
            mAuthors.setText(book.getAuthors());
            mViewModel.checkIsStoredLocally(book);
        });

        mViewModel.getIsStoredLocallyLiveData().observe(getViewLifecycleOwner(), isStoredLocally -> {
            if (isStoredLocally) {
                changeFabToStoredLocally();
            }
        });
    }

    private void changeFabToStoredLocally() {
        mFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorCheckMarkGreen)));
        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp, getContext().getTheme()));
        mFab.setClickable(false);
    }
}
