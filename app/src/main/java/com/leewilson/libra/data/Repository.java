package com.leewilson.libra.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    private Context mContext;
    private Executor mExecutor;
    private GoogleBooksApiListener mListener;
    private RequestQueue mRequestQueue;

    public interface GoogleBooksApiListener {
        void onReceiveFromApi(List<Book> books);
        void onApiFailure();
    }

    public Repository(Context context, GoogleBooksApiListener listener) {
        mContext = context;
        mExecutor = Executors.newSingleThreadExecutor();
        mListener = listener;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void updateBooks(String query) {
        mExecutor.execute(() -> searchBooksByQuery(query));
    }

    private synchronized void searchBooksByQuery(String query) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&orderBy=relevance";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            List<Book> bookList = new ArrayList<>();
                            for(int i = 0; i < items.length(); i++) {
                                bookList.add(new Book((JSONObject) items.get(i)));
                            }
                            mListener.onReceiveFromApi(bookList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mListener.onApiFailure();
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }
}
