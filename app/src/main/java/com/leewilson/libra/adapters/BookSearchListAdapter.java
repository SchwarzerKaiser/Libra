package com.leewilson.libra.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookSearchListAdapter extends RecyclerView.Adapter<BookSearchListAdapter.ViewHolder> {

    private List<Book> mData = new ArrayList<>();

    private LayoutInflater mLayoutInflater;
    private Context mContext;


    public BookSearchListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateCache(List<Book> newSearch) {
        mData = newSearch;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookSearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bookItemView = mLayoutInflater.inflate(R.layout.book_search_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(bookItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookSearchListAdapter.ViewHolder holder, int position) {
        Book book = mData.get(position);
        holder.title.setText(book.getTitle());
        holder.authors.setText(book.getAuthors());
        if(!TextUtils.isEmpty(book.getThumbnailURL())) {
            Picasso.get().load(book.getThumbnailURL())
                    .error(R.drawable.book_placeholder)
                    .placeholder(R.drawable.book_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.cover, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("PICASSO", "SUCCESS");
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView authors;
        ImageView cover;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_list_item_book_title);
            authors = itemView.findViewById(R.id.search_list_item_book_authors);
            cover = itemView.findViewById(R.id.book_search_list_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO
        }
    }
}
