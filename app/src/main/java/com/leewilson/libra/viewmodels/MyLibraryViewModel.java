package com.leewilson.libra.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.leewilson.libra.data.LocalRepository;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.model.Review;
import com.leewilson.libra.utils.MyLibraryTab;

import java.util.List;

public class MyLibraryViewModel extends AndroidViewModel {

    private LocalRepository mLocalRepo;

    // LiveData fields:

    private MediatorLiveData<List<Book>> mBooks = new MediatorLiveData<>();
    private MediatorLiveData<List<Review>> mReviews = new MediatorLiveData<>();

    private MutableLiveData<Book> mSelectedBook = new MutableLiveData<>();
    private MutableLiveData<Review> mSelectedReview = new MutableLiveData<>();

    private MutableLiveData<MyLibraryTab> mCurrentTab = new MutableLiveData<MyLibraryTab>() {
        @Override
        protected void onActive() {
            super.onActive();
            mCurrentTab.setValue(MyLibraryTab.LIST_BOOKS);
        }
    };

    // Constructor:

    public MyLibraryViewModel(@NonNull Application application) {
        super(application);

        mLocalRepo = new LocalRepository(application.getApplicationContext());

        mBooks.addSource(mCurrentTab, myLibraryTab -> {
            if (myLibraryTab == MyLibraryTab.LIST_BOOKS) {
                mLocalRepo.fetchAllBooks(books -> mBooks.postValue(books));
            }
        });

        mReviews.addSource(mCurrentTab, myLibraryTab -> {
            if (myLibraryTab == MyLibraryTab.LIST_REVIEWS) {
                mLocalRepo.fetchAllReviews(reviews -> mReviews.postValue(reviews));
            }
        });
    }

    // Exposed methods:

    public void setCurrentTab(MyLibraryTab tab) {
        mCurrentTab.setValue(tab);
    }

    public void setSelectedBook(int id) {
        mLocalRepo.fetchBookById(id, book -> mSelectedBook.postValue(book));
    }

    public void setSelectedReview(int bookId) {
        mLocalRepo.fetchReviewByBookId(bookId, review -> mSelectedReview.postValue(review));
    }

    public void saveReview(Review review) {
        mLocalRepo.insertReview(review);
    }

    public LiveData<MyLibraryTab> getCurrentTab() {
        return mCurrentTab;
    }

    public LiveData<List<Book>> getBooks() {
        return mBooks;
    }

    public LiveData<List<Review>> getReviews() {
        return mReviews;
    }

    public LiveData<Book> getSelectedBook() {
        return mSelectedBook;
    }

    public LiveData<Review> getSelectedReview() {
        return mSelectedReview;
    }

    public void updateReview(Review review) {
        mLocalRepo.updateReview(review);
    }
}
