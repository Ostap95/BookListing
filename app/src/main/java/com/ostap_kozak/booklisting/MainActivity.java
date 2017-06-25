package com.ostap_kozak.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ostap_kozak.booklisting.adapter.BookListAdapter;
import com.ostap_kozak.booklisting.loader.BookLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    private String queryUrl = null;
    private ArrayList<Book>  books = new ArrayList<>();
    private ListView bookListView;
    private BookListAdapter adapter;
    private boolean isConnected =  false;
    private TextView infoMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookListView = (ListView) findViewById(R.id.list_view_books);
        infoMessage = (TextView) findViewById(R.id.info_message);
        setBookListAdapter(books);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                seeInternetConnection();

                if (isConnected) {
                    queryUrl = getResources().getString(R.string.google_books_api_query) + query;
                    LoaderManager loaderManager = getSupportLoaderManager();
                    loaderManager.initLoader(0, null, MainActivity.this);
                    getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    private void setBookListAdapter(List<Book> list) {

        ArrayList<Book> books = (ArrayList)list;

        adapter = new BookListAdapter(this, books);
        bookListView.setAdapter(adapter);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, queryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        infoMessage.setVisibility(View.GONE);
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", bookListView.getVerticalScrollbarPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSupportLoaderManager().initLoader(0, null, this);
        int position = savedInstanceState.getInt("position");
        bookListView.setVerticalScrollbarPosition(position);
    }

    private void seeInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
