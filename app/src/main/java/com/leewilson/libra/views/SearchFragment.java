package com.leewilson.libra.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leewilson.libra.R;
import com.leewilson.libra.adapters.BookSearchListAdapter;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private SearchBooksViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private Button mSearchButton;
    private EditText mSearchField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_screen_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.search_recyclerview);
        mSearchButton = view.findViewById(R.id.search_screen_search_btn);
        mSearchField = view.findViewById(R.id.books_search_bar);

        BookSearchListAdapter adapter = new BookSearchListAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mViewModel = new ViewModelProvider(this).get(SearchBooksViewModel.class);
        mViewModel.getBookLiveData().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                adapter.updateCache(books);
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.updateSearchQuery(mSearchField.getText().toString());
            }
        });
    }
}
