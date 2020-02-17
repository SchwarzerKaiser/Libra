package com.leewilson.libra.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.leewilson.libra.R;
import com.leewilson.libra.model.Book;
import com.leewilson.libra.views.SearchFragmentDirections;
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
        holder.listIndex = position;
        holder.book = book;
        String thumbnailUrl = book.getThumbnailURL();
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView authors;
        ImageView cover;
        int listIndex;
        Book book;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_list_item_book_title);
            authors = itemView.findViewById(R.id.search_list_item_book_authors);
            cover = itemView.findViewById(R.id.book_search_list_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NavDirections action = SearchFragmentDirections.navActionToSearchedBookDetail().setIndex(listIndex);
            Navigation.findNavController(itemView).navigate(action);
        }
    }
}
