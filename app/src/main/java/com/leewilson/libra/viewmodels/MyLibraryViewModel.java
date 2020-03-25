package com.leewilson.libra.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.leewilson.libra.data.LocalRepository;
import com.leewilson.libra.model.Book;

import java.util.List;

public class MyLibraryViewModel extends AndroidViewModel {

    private LocalRepository mLocalRepo;
    private MutableLiveData<List<Book>> mListBooks = new MutableLiveData<>();

    public MyLibraryViewModel(@NonNull Application application) {
        super(application);
        mLocalRepo = new LocalRepository(application.getApplicationContext());
    }

    public LiveData<List<Book>> getAllBooksLiveData() {
        return mListBooks;
    }

    public void updateBookList() {
        mLocalRepo.fetchAllBooks(books -> mListBooks.postValue(books));
    }
}
