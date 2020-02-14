package com.leewilson.libra.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.leewilson.libra.data.Repository;
import com.leewilson.libra.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksViewModel extends AndroidViewModel implements Repository.GoogleBooksApiListener {

    private Repository mRepository;
    private MutableLiveData<List<Book>> mSearchBooks;

    // Exposed public methods:

    public SearchBooksViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(getApplication().getApplicationContext(), this);
        mSearchBooks = new MutableLiveData<>();
    }

    public LiveData<List<Book>> getBookLiveData(){
        if(mSearchBooks.getValue() == null || mSearchBooks.getValue().isEmpty()) {
            mSearchBooks.setValue(new ArrayList<Book>());
            return mSearchBooks;
        } else {
            return mSearchBooks;
        }
    }

    public void updateSearchQuery(String query) {
        mRepository.updateBooks(query);
    }


    @Override
    public void onReceiveFromApi(List<Book> books) {
        mSearchBooks.setValue(books);
    }

    @Override
    public void onApiFailure() {
        // TODO
    }
}
