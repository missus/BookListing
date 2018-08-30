/*
 * Created by Karolin Fornet.
 * Copyright (c) 2017.  All rights reserved.
 */

package com.example.android.mybooks;

import java.util.List;

public class Book {

    private double mRating;
    private String mTitle;
    private List<String> mAuthors;
    private String mDate;
    private String mUrl;

    public Book(double rating, String title, String date, String url, List<String> authors) {
        mRating = rating;
        mTitle = title;
        mAuthors = authors;
        mDate = date;
        mUrl = url;
    }

    public double getRating() {
        return mRating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        StringBuilder authors = new StringBuilder(mAuthors.get(0));
        for (int i = 1; i < mAuthors.size(); i++) {
            authors.append(", ").append(mAuthors.get(i));
        }
        return authors.toString();
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
