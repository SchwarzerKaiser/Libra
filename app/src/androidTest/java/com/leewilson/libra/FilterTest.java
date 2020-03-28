package com.leewilson.libra;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.leewilson.libra.model.Book;
import com.leewilson.libra.utils.FilterKt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class FilterTest {

    private List<Book> mTestData = new ArrayList<>();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        String raw = TestingUtils.getAssetJsonData(context);
        try {
            JSONObject json = new JSONObject(raw);
            JSONArray array = json.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                mTestData.add(new Book(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void filterToSingleItemSuccess() {
        List<Book> filtered = FilterKt.filterBy(mTestData, "The Highway Code");
        assertThat(filtered.size(), is(equalTo(1)));
        Book book = filtered.get(0);
        assertThat(book.getTitle(), is(equalTo("The Highway Code")));
    }

    @Test
    public void filterItemNotFound() {
        List<Book> filtered = FilterKt.filterBy(mTestData, "sfdjhbsfdjskjfhbaskjb");
        assertThat(filtered.size(), is(equalTo(0)));
    }
}
