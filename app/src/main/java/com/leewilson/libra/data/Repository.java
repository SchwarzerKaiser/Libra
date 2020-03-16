package com.leewilson.libra.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leewilson.libra.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    private static final String TAG = "Repository";
    private Context mContext;
    private Executor mExecutor;
    private GoogleBooksApiListener mListener;
    private RequestQueue mRequestQueue;

    // Singleton instance
    private static Repository mInstance;

    public interface GoogleBooksApiListener {
        void onReceiveBookSearchList(List<Book> books);
        void onReceiveBookByISBN(Book book);
        void onApiFailure();
    }

    private Repository(Context context, GoogleBooksApiListener listener) {
        mContext = context;
        mExecutor = Executors.newSingleThreadExecutor();
        mListener = listener;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static Repository getInstance(Context context, GoogleBooksApiListener listener) {
        if (mInstance != null) {
            return mInstance;
        } else {
            mInstance = new Repository(context, listener);
            return mInstance;
        }
    }

    public void updateBooks(String query) {
        mExecutor.execute(() -> searchBooksByQuery(query));
    }

    public void fetchScannedBook(String isbn) {
        mExecutor.execute(() -> fetchBookByIsbn(isbn));
    }

    private synchronized void searchBooksByQuery(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&orderBy=relevance";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                JSONArray items = response.getJSONArray("items");
                                List<Book> bookList = new ArrayList<>();
                                for(int i = 0; i < items.length(); i++) {
                                    bookList.add(new Book((JSONObject) items.get(i)));
                                }
                                mListener.onReceiveBookSearchList(bookList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> mListener.onApiFailure());

        mRequestQueue.add(jsonObjectRequest);
    }

    private synchronized void fetchBookByIsbn(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url,
                        response -> {
                            try {
                                JSONArray items = response.getJSONArray("items");
                                List<Book> bookList = new ArrayList<>();
                                for (int i = 0; i < items.length(); i++) {
                                    bookList.add(new Book((JSONObject) items.get(i)));
                                }
                                if(!bookList.isEmpty()) {
                                    mListener.onReceiveBookByISBN(bookList.get(0));
                                }
                            } catch (JSONException e) {
                                // Means the book probably wasn't found.
                                mListener.onReceiveBookByISBN(null);
                                e.printStackTrace();
                            }
                        }, error -> { Log.e(TAG, "Something went wrong with this ISBN."); });

        mRequestQueue.add(jsonObjectRequest);
    }
}
