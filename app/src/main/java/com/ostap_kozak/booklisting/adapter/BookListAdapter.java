package com.ostap_kozak.booklisting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostap_kozak.booklisting.Book;
import com.ostap_kozak.booklisting.R;

import java.util.ArrayList;

/**
 * Created by ostapkozak on 24/06/2017.
 */

public class BookListAdapter extends ArrayAdapter<Book>{


    public BookListAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Book book = getItem(position);
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item_layout, parent, false);

        TextView bookTitle = (TextView) convertView.findViewById(R.id.book_title);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.book_author);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());

        return convertView;
    }
}
