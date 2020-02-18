package com.leewilson.libra.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.leewilson.libra.R;
import com.leewilson.libra.adapters.BookSearchListAdapter;
import com.leewilson.libra.viewmodels.SearchBooksViewModel;

public class SearchFragment extends Fragment {

    private SearchBooksViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TextInputEditText mSearchField;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_screen_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.search_recyclerview);
        mSearchField = view.findViewById(R.id.books_search_bar);
        mProgressBar = view.findViewById(R.id.search_progress_bar);
        mViewModel = new ViewModelProvider(getActivity()).get(SearchBooksViewModel.class);

        BookSearchListAdapter adapter = new BookSearchListAdapter(view.getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mViewModel.getBookLiveData().observe(getActivity(), books -> {
            adapter.updateCache(books);
            mProgressBar.setVisibility(View.GONE);
        });

        mSearchField.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO)
                performSearchOperation();
            return true;
        });

        // TODO: Improve layout for RecyclerView. It's currently very boring.
    }

    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void performSearchOperation() {
        mViewModel.updateSearchQuery(mSearchField.getText().toString());
        mProgressBar.setVisibility(View.VISIBLE);
        toggleKeyboard();
    }
}
