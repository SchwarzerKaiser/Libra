package com.leewilson.libra.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.leewilson.libra.model.Review;

import java.util.List;

@Dao
public interface ReviewDao {

    @Query("SELECT * FROM reviews ORDER BY timeStamp DESC")
    List<Review> getAllReviews();

    /**
     * Gets a book review from a book ID (primary key in the {@code books} table).
     * There is currently a 1:1 relationship between books and reviews at present,
     * hence this returns a single review and not a list of reviews.
     * @param bookId
     * @return
     */
    @Query("SELECT * FROM reviews WHERE bookId = :bookId LIMIT 1")
    Review findReviewByBookId(int bookId);

    @Insert
    void insertReview(Review review);

    @Update
    void updateReview(Review review);

    @Delete
    void deleteReview(Review review);
}
