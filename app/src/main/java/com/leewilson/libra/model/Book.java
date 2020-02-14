package com.leewilson.libra.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Book {

    // Locally defined
    private int id;
    private int rating;

    // API-defined
    private String apiId;
    private String title;
    private String authors;
    private String description;
    private String thumbnailURL;

    public Book(){}

    public Book(JSONObject jsonObject) {
        try {
            JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");
            title = volumeInfo.getString("title");
            authors = volumeInfo.getJSONArray("authors").join(", ").replace("\"", "");
            thumbnailURL = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
