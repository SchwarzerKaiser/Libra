package com.leewilson.libra.data;

import android.content.Context;

import com.leewilson.libra.data.local.AppDatabase;
import com.leewilson.libra.data.local.BookDao;
import com.leewilson.libra.data.local.ReviewDao;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.model.Review;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalRepository {

    private final BookDao mBookDao;
    private final ReviewDao mReviewDao;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public interface OnFetchItemListener { void onReceive(Book book); }
    public interface OnFetchAllItemsListener { void onReceive(List<Book> books); }
    public interface OnContainsItemListener { void onCheckedItem(boolean isStoredLocally); }

    public interface OnFetchAllReviewsListener { void onReceive(List<Review> reviews); }
    public interface OnFetchReviewListener{ void onReceive(Review review); }

    public LocalRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        mBookDao = appDatabase.getBookDao();
        mReviewDao = appDatabase.getReviewDao();
    }

    public synchronized void fetchAllReviews(OnFetchAllReviewsListener listener) {
        mExecutor.execute(() -> {
            List<Review> reviews = mReviewDao.getAllReviews();
            listener.onReceive(reviews);
        });
    }

    public synchronized void fetchAllBooks(OnFetchAllItemsListener listener) {
        mExecutor.execute(() -> {
            List<Book> books = mBookDao.getAllBooks();
            listener.onReceive(books);
        });
    }

    public synchronized void fetchBookById(int id, OnFetchItemListener listener) {
        mExecutor.execute(() -> {
            Book result = mBookDao.findBookById(id);
            listener.onReceive(result);
        });
    }

    public synchronized void fetchReviewByBookId(int bookId, OnFetchReviewListener listener) {
        mExecutor.execute(() -> {
            Review result = mReviewDao.findReviewByBookId(bookId);
            listener.onReceive(result);
        });
    }

    public synchronized void insertBook(Book book) {
        mExecutor.execute(() -> mBookDao.insertBook(book));
    }

    public synchronized void insertReview(Review review) {
        mExecutor.execute(() -> mReviewDao.insertReview(review));
    }

    public synchronized void deleteBook(Book book) {
        mExecutor.execute(() -> mBookDao.deleteBook(book));
    }

    public synchronized void deleteReview(Review review) {
        mExecutor.execute(() -> mReviewDao.deleteReview(review));
    }

    public synchronized void updateReview(Review review) {
        mExecutor.execute(() -> {
            mReviewDao.updateReview(review);
        });
    }

    public synchronized void checkIsStoredLocally(String apiId, OnContainsItemListener listener) {
        mExecutor.execute(() -> {
            int numMatches = mBookDao.getCountItemsByApiId(apiId);
            listener.onCheckedItem(numMatches > 0);
        });
    }
}
