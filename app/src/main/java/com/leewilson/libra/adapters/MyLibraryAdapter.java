package com.leewilson.libra.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyLibraryAdapter extends RecyclerView.Adapter<MyLibraryAdapter.ViewHolder> {

    private List<Book> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyLibraryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void updateCache(List<Book> books) {
        mData = books;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.mylibrary_book_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book thisBook = mData.get(position);
        holder.title.setText(thisBook.getTitle());
        holder.authors.setText(thisBook.getAuthors());
        String thumbnailUrl = thisBook.getThumbnailURL();
        holder.cover.setImageDrawable(null);
        if(!TextUtils.isEmpty(thumbnailUrl)) {
            Picasso.get().load(thumbnailUrl)
                    .error(R.drawable.book_placeholder)
                    .placeholder(R.drawable.book_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.cover);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        final TextView title;
        final TextView authors;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.mylibrary_all_item_image);
            title = itemView.findViewById(R.id.mylibrary_all_item_title);
            authors = itemView.findViewById(R.id.mylibrary_all_item_authors);
        }
    }
}
