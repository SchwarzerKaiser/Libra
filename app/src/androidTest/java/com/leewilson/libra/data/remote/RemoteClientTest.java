package com.leewilson.libra.data.remote;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.leewilson.libra.data.Repository;
import com.leewilson.libra.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;


public class RemoteClientTest {

    Context mContext;
    Repository mRepository;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        mRepository = new Repository(mContext);
    }

    @After
    public void tearDown() throws Exception {
        mRepository = null;
        mContext = null;
    }

    @Test
    public void fetchBooksByQuery() {
        List<Book> list = mRepository.searchBooksByQuery("android");
        assertThat(list, is(notNullValue()));
    }
}