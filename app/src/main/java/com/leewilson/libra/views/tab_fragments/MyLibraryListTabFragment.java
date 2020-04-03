package com.leewilson.libra.views.tab_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leewilson.libra.R;
import com.leewilson.libra.adapters.BookSearchListAdapter;
import com.leewilson.libra.adapters.MyLibraryAdapter;
import com.leewilson.libra.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryListTabFragment extends Fragment implements Tabbable {

    private MyLibraryAdapter mAdapter;

    private static final String TAG = "MyLibraryListTabFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_mylibrary_tab_all, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.mylibrary_all_recyclerview);
        mAdapter = new MyLibraryAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setData(List<Book> books) {
        mAdapter.updateCache(books);
    }

    @Override
    public void clearData() {
        mAdapter.updateCache(new ArrayList<>());
    }
}
