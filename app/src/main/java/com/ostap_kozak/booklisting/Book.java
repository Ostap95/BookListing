package com.ostap_kozak.booklisting;

import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;

/**
 * Created by ostapkozak on 24/06/2017.
 */

public class Book {

    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
