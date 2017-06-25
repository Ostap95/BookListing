package com.ostap_kozak.booklisting.Utils;

import android.util.Log;

import com.ostap_kozak.booklisting.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ostapkozak on 24/06/2017.
 */

public class QueryUtils {


    private  QueryUtils() {};

    public static ArrayList<Book> fetchBooksData(String requestUrl) {
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractBooks(jsonResponse);
    }

    private static ArrayList<Book> extractBooks(String jsonResponse) {
        ArrayList<Book> books = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray itemsArray = root.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject bookInfo = book.getJSONObject("volumeInfo");
                String title = bookInfo.getString("title");
                String author = parseAuthors(bookInfo.getJSONArray("authors"));

                books.add(new Book(title, author));
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "A problem occurred while parsing json response ");
        }

        return books;
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(100);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils","Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }

        return stringBuilder.toString();
    }

    private static String parseAuthors(JSONArray array) throws  JSONException{
        String result = "";

        for (int i=0; i < array.length(); i++) {
            if (i < array.length() - 1) result += array.getString(i) + " | ";
            else result += array.getString(i);
        }
        return result;
    }
}
