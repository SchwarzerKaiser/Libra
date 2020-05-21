package com.leewilson.libra.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews")
public class Review {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int bookId;
    private String review;
    private float rating;
    private long timeStamp;

    public Review(int id, int bookId, String review, float rating, long timeStamp) {
        this.id = id;
        this.bookId = bookId;
        this.review = review;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Ignore
    @NonNull
    @Override
    public String toString() {
        return "Review(id="+id+", bookId="+bookId+", review="+review+", rating="+rating+", timestamp="+timeStamp;
    }
}
