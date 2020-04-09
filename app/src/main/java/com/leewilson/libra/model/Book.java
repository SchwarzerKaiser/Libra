package com.leewilson.libra.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "books")
public class Book {

    // Locally defined
    @PrimaryKey(autoGenerate = true) private int id;
    private int rating;

    // API-defined
    private String apiId;
    private String title;
    private String subtitle;
    private String authors;
    private String description;
    private String thumbnailURL;

    // required public no-arg constructor
    public Book(){}

    @Ignore
    public Book(JSONObject jsonObject) {
        try {
            JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");

            subtitle = volumeInfo.optString("subtitle", "");
            title = volumeInfo.getString("title");
            if(!subtitle.isEmpty()) title += (": " + subtitle);
            apiId = jsonObject.getString("id");

            JSONArray authorsArray = volumeInfo.optJSONArray("authors");
            if(authorsArray == null) {
                authors = "No authors";
            } else authors = authorsArray.join(", ").replace("\"", "");

            thumbnailURL = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail");
            description = volumeInfo.optString("description", "No description");

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
