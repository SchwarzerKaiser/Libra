package com.leewilson.libra.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leewilson.libra.data.LocalRepository;
import com.leewilson.libra.data.SearchRepository;
import com.leewilson.libra.model.Book;

import java.util.List;

public class ScannerViewModel extends AndroidViewModel implements SearchRepository.GoogleBooksApiListener {

    private SearchRepository mSearchRepository;
    private LocalRepository mLocalRepository;

    private MutableLiveData<Book> mScannedBook = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsStoredLocally = new MutableLiveData<>();

    public ScannerViewModel(@NonNull Application application) {
        super(application);
        mSearchRepository = new SearchRepository(application.getApplicationContext(), this);
        mLocalRepository = new LocalRepository(application.getApplicationContext());
    }

    public LiveData<Book> getScannedBookLiveData() {
        return mScannedBook;
    }

    public LiveData<Boolean> getIsStoredLocallyLiveData() { return mIsStoredLocally; }

    public void setScannedBook(String isbn) {
        mSearchRepository.fetchScannedBook(isbn);
    }

    public void addToMyLibrary(Book book) {
        mLocalRepository.insertBook(book);
    }

    public void checkIsStoredLocally(Book book) {
        mLocalRepository.checkIsStoredLocally(book.getApiId(), isStoredLocally -> mIsStoredLocally.postValue(isStoredLocally));
    }

    @Override
    public void onReceiveBookSearchList(List<Book> books) {
        // Not used here.
    }

    @Override
    public void onReceiveBookByISBN(Book book) {
        mScannedBook.setValue(book);
    }

    @Override
    public void onApiFailure() {

    }
}
