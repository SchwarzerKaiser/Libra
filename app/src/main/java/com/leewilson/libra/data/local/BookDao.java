package com.leewilson.libra.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.leewilson.libra.model.Book;

import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM books ORDER BY title DESC")
    List<Book> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    Book findBookById(int id);

    @Query("SELECT COUNT(*) FROM books WHERE apiId = :apiId")
    int getCountItemsByApiId(String apiId);

    @Delete
    void deleteBook(Book book);

    @Update
    void updateBook(Book book);

    @Insert
    void insertBook(Book book);
}
