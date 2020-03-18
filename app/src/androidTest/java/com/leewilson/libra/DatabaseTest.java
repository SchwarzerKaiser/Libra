package com.leewilson.libra;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.leewilson.libra.data.local.AppDatabase;
import com.leewilson.libra.data.local.BookDao;
import com.leewilson.libra.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private BookDao mDao;
    private AppDatabase db;
    private Context mContext;
    private JSONArray mTestData;

    @Before
    public void setup() throws IOException {
        mContext = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(mContext, AppDatabase.class)
                .fallbackToDestructiveMigration()
                .build();
        mDao = db.getBookDao();

        String jsonRaw = getAssetJsonData(mContext);
        try {
            JSONObject json = new JSONObject(jsonRaw);
            mTestData = json.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Insert item
    @Test
    public void insertBook() {
        Book book = null;
        try {
            book = new Book(mTestData.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDao.insertBook(book);
        int count = mDao.getCountItemsByApiId("OfOCAAAACAAJ");
        assertThat(count, is(equalTo(1)));
        db.clearAllTables();
    }

    // Get all items
    @Test
    public void getAllItems() {
        populateDatabase();
        List<Book> list = mDao.getAllBooks();
        assertThat(list.size(), is(equalTo(10)));
        db.clearAllTables();
    }

    // Get single item by API id
    @Test
    public void getSingleItemByApiId() {
        String id = "cR5pNv76HMgC";
        populateDatabase();
        Book book = mDao.findBookByApiId(id);
        assertThat(book.getApiId(), is(equalTo(id)));
        db.clearAllTables();
    }

    // Delete item
    @Test
    public void deleteItem() {
        populateDatabase();
        Book bookToRemove = null;
        try {
            bookToRemove = new Book(mTestData.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bookToRemove.setId(1);
        mDao.deleteBook(bookToRemove);
        int count = mDao.getAllBooks().size();
        assertThat(count, is(equalTo(9)));
        db.clearAllTables();
    }

    // Update item
    @Test
    public void updateItem() {
        populateDatabase();
        Book book = mDao.findBookByApiId("E0yH0NdySrQC");
        String newTitle = "Test updated title";
        book.setTitle(newTitle);
        mDao.updateBook(book);
        Book updatedBook = mDao.findBookByApiId("E0yH0NdySrQC");
        assertThat(updatedBook.getTitle(), is(equalTo(newTitle)));
        db.clearAllTables();
    }

    // Query database for specific book - boolean operation
    @Test
    public void checkForBookSuccess() throws JSONException {
        populateDatabase();
        Book bookToSearch = new Book(mTestData.getJSONObject(0));
        boolean dbHasBook = mDao.getCountItemsByApiId(bookToSearch.getApiId()) > 0;
        assertThat(dbHasBook, is(equalTo(true)));
        db.clearAllTables();
    }

    @Test
    public void checkForBookFailure() {
        populateDatabase();
        Book bookToSearch = new Book();
        bookToSearch.setId(-1);
        boolean dbHasBook = mDao.getCountItemsByApiId(bookToSearch.getApiId()) > 0;
        assertThat(dbHasBook, is(equalTo(false)));
        db.clearAllTables();
    }

    /**
     * Helper method to get dummy JSON data.
     *
     * @param context
     * @return JSON string
     */
    private static String getAssetJsonData(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("test_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("data", json);
        return json;
    }

    private void populateDatabase() {
        try {
            for (int i = 0; i < mTestData.length(); i++)
                mDao.insertBook(new Book(mTestData.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
