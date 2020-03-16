package com.leewilson.libra.views;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searched_book_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController controller = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ViewModelStoreOwner owner = controller.getViewModelStoreOwner(R.id.search_nav_graph);
        mViewModel = new ViewModelProvider(owner).get(SearchBooksViewModel.class);
        mViewModel.getSearchedBookLiveData().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                Picasso.get().load(book.getThumbnailURL()).into(mBookToolbarImage);
                mTitle.setText(book.getTitle());
                mDescription.setText(book.getDescription());
                mAuthors.setText(book.getAuthors());
            }
        });
        int index = SearchedBookDetailFragmentArgs.fromBundle(getArguments()).getIndex();
        mViewModel.setBookDetailIndex(index);

        mBookToolbarImage = view.findViewById(R.id.toolbarBookImage);
        mDescription = view.findViewById(R.id.book_detail_description);
        mTitle = view.findViewById(R.id.book_detail_title);
        mAuthors = view.findViewById(R.id.book_detail_authors);

        // TODO: Add social features
        // TODO: Amazon purchase link?
    }
}
