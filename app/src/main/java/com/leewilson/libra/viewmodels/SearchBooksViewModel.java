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
    private MutableLiveData<Book> mSearchListBook;
    private MutableLiveData<Book> mScannedBook;

    public SearchBooksViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application.getApplicationContext(), this);
        mSearchBooks = new MutableLiveData<>();
        mSearchListBook = new MutableLiveData<>();
        mScannedBook = new MutableLiveData<>();
    }

    public LiveData<List<Book>> getSearchedBooksLiveData(){
        if(mSearchBooks.getValue() == null || mSearchBooks.getValue().isEmpty()) {
            mSearchBooks.setValue(new ArrayList<Book>());
            return mSearchBooks;
        } else {
            return mSearchBooks;
        }
    }

    public LiveData<Book> getSearchedBookLiveData() {
        return mSearchListBook;
    }

    public LiveData<Book> getScannedBookLiveData() {
        return mScannedBook;
    }

    public void setBookDetailIndex(int index) {
        if (mSearchBooks.getValue() != null) {
            mSearchListBook.setValue(mSearchBooks.getValue().get(index));
        } else throw new IllegalArgumentException("Book list is empty");
    }

    public void setScannedBook(String isbn) {
        mRepository.fetchScannedBook(isbn);
    }

    public void updateSearchQuery(String query) {
        mRepository.updateBooks(query);
    }

    @Override
    public void onReceiveBookSearchList(List<Book> books) {
        mSearchBooks.setValue(books);
    }

    @Override
    public void onReceiveBookByISBN(Book book) {
        mScannedBook.setValue(book);
    }

    @Override
    public void onApiFailure() {

    }
}
