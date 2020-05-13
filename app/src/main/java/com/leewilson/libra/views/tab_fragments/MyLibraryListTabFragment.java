package com.leewilson.libra.views.tab_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leewilson.libra.R;
import com.leewilson.libra.adapters.MyLibraryAdapter;
import com.leewilson.libra.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryListTabFragment extends Fragment {

    private MyLibraryAdapter mAdapter;
    private LiveData<List<Book>> mBooks;
    private MyLibraryItemsListener mListener;

    private static final String TAG = "MyLibraryListTabFragment";

    public MyLibraryListTabFragment(LiveData<List<Book>> books, MyLibraryItemsListener listener) {
        mBooks = books;
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mylibrary_tab_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(view);
        subscribeObservers();
    }

    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.mylibrary_all_recyclerview);
        mAdapter = new MyLibraryAdapter(getContext(), id -> mListener.onItemClick(id));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void subscribeObservers() {
        mBooks.observe(getViewLifecycleOwner(), books -> mAdapter.updateCache(books));
    }
}
