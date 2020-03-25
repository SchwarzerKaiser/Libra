package com.leewilson.libra.views.tab_fragments;

import com.leewilson.libra.model.Book;

import java.util.List;

public interface Tabbable {
    void setData(List<Book> books);
    void clearData();
}
