package com.ostap_kozak.booklisting.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ostap_kozak.booklisting.Book;
import com.ostap_kozak.booklisting.Utils.QueryUtils;

import java.util.List;

/**
 * Created by ostapkozak on 24/06/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        return QueryUtils.fetchBooksData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
