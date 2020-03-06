package com.leewilson.libra.data.remote;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.leewilson.libra.data.Repository;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.views.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;


public class RemoteClientTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    Context mContext;
    RepositoryTd mRepository;

    static class RepositoryTd extends Repository {

        private BookResponseCallback mBookResponseCallback;
        private BookListResponseCallback mBookListResponseCallback;

        public void setCallback(ResponseCallback callback) {
            if (callback instanceof BookResponseCallback) {
                mBookResponseCallback = (BookResponseCallback) callback;
            } else if (callback instanceof BookListResponseCallback) {
                mBookListResponseCallback = (BookListResponseCallback) callback;
            }
        }

        public void clearCallbacks() {
            mBookResponseCallback = null;
            mBookListResponseCallback = null;
        }

        public RepositoryTd(Context context, GoogleBooksApiListener listener) {
            super(context, listener);
        }

        public interface ResponseCallback {}
        public interface BookResponseCallback extends ResponseCallback { void bookResponse(); }
        public interface BookListResponseCallback extends ResponseCallback { void bookListResponse(); }
    }


    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        mRepository = new RepositoryTd(mContext, new Repository.GoogleBooksApiListener() {
            @Override
            public void onReceiveBookSearchList(List<Book> books) { }

            @Override
            public void onReceiveBookByISBN(Book book) { }

            @Override
            public void onApiFailure() { }
        });
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void fetchBooksByQuery() {
        mRepository.updateBooks("Android");
        final Object syncObject = new Object();

    }
}