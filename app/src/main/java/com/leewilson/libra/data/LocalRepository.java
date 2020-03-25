package com.leewilson.libra.data;

import android.content.Context;
import android.os.AsyncTask;

import com.leewilson.libra.data.local.AppDatabase;
import com.leewilson.libra.data.local.BookDao;
import com.leewilson.libra.model.Book;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocalRepository {

    private AppDatabase mAppDatabase;
    private BookDao mDao;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public interface OnFetchItemListener { void onReceive(Book book); }
    public interface OnFetchAllItemsListener { void onReceive(List<Book> books); }
    public interface OnContainsItemListener { void onCheckedItem(boolean isStoredLocally); }

    public LocalRepository(Context context) {
        mAppDatabase = AppDatabase.getInstance(context);
        mDao = mAppDatabase.getBookDao();
    }

    public synchronized void fetchAllBooks(OnFetchAllItemsListener listener) {
        AsyncTask.execute(() -> {
            List<Book> books = mDao.getAllBooks();
            listener.onReceive(books);
        });
    }

    public synchronized void fetchBookById(String apiId, OnFetchItemListener listener) {
        AsyncTask.execute(() -> {
            Book result = mDao.findBookByApiId(apiId);
            listener.onReceive(result);
        });
    }

    public synchronized void insertBook(Book book) {
        mExecutor.execute(() -> mDao.insertBook(book));
    }

    public synchronized void deleteBook(Book book) {
        mExecutor.execute(() -> mDao.deleteBook(book));
    }

    public synchronized void checkIsStoredLocally(String apiId, OnContainsItemListener listener) {
        mExecutor.execute(() -> {
            int numMatches = mDao.getCountItemsByApiId(apiId);
            listener.onCheckedItem(numMatches > 0);
        });
    }
}
