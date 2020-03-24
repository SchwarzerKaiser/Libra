package com.leewilson.libra.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.leewilson.libra.data.LocalRepository;
import com.leewilson.libra.data.SearchRepository;
import com.leewilson.libra.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksViewModel extends AndroidViewModel implements SearchRepository.GoogleBooksApiListener {

    private SearchRepository mSearchRepository;
    private LocalRepository mLocalRepository;
    private MutableLiveData<List<Book>> mSearchBooks = new MutableLiveData<>();
    private MutableLiveData<Book> mSearchListBook = new MutableLiveData<>();
    private MutableLiveData<Book> mScannedBook = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsStoredLocally = new MutableLiveData<>();

    public SearchBooksViewModel(@NonNull Application application) {
        super(application);
        mSearchRepository = new SearchRepository(application.getApplicationContext(), this);
        mLocalRepository = new LocalRepository(application.getApplicationContext());
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

    public LiveData<Boolean> getIsStoredLocallyLiveData() { return mIsStoredLocally; }

    public void setBookDetailIndex(int index) {
        if (mSearchBooks.getValue() != null) {
            mSearchListBook.setValue(mSearchBooks.getValue().get(index));
        } else throw new IllegalArgumentException("Book list is empty");
    }

    public void setScannedBook(String isbn) {
        mSearchRepository.fetchScannedBook(isbn);
    }

    public void updateSearchQuery(String query) {
        mSearchRepository.updateBooks(query);
    }

    public void addToMyLibrary(Book book) {
        mLocalRepository.insertBook(book);
    }

    public void checkIsStoredLocally(Book book) {
        mLocalRepository.checkIsStoredLocally(book.getApiId(), isStoredLocally -> mIsStoredLocally.postValue(isStoredLocally));
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
    public void onApiFailure() {}
}
