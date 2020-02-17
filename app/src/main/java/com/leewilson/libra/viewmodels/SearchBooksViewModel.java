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
    private MutableLiveData<Book> mBookDetail;

    // Exposed public methods:

    public SearchBooksViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(getApplication().getApplicationContext(), this);
        mSearchBooks = new MutableLiveData<>();
        mBookDetail = new MutableLiveData<>();
    }

    public LiveData<List<Book>> getBookLiveData(){
        if(mSearchBooks.getValue() == null || mSearchBooks.getValue().isEmpty()) {
            mSearchBooks.setValue(new ArrayList<Book>());
            return mSearchBooks;
        } else {
            return mSearchBooks;
        }
    }

    public LiveData<Book> getBookDetail() {
        return mBookDetail;
    }

    public void setBookDetailIndex(int index) {
        if (mSearchBooks.getValue() != null) {
            mBookDetail.setValue(mSearchBooks.getValue().get(index));
        } else throw new IllegalArgumentException("Book list is empty");
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

    }
}
