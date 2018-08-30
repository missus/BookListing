/*
 * Created by Karolin Fornet.
 * Copyright (c) 2017.  All rights reserved.
 */

package com.example.android.mybooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;
    private ListView mBookListView;
    private TextView mEmptyStateTextView;
    private LoaderManager mLoaderManager;
    private EditText mSearchView;
    private String mSearchTerm;
    private String mUri;
    private ProgressBar mProgress;

    public static final String LOG_TAG = BookActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private static final String BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String MAX_RESULTS = "&maxResults=40";
    private static final String URI = "URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        mBookListView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty);
        mBookListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Book>());
        mBookListView.setAdapter(mAdapter);
        mSearchView = findViewById(R.id.search_for);
        mProgress = findViewById(R.id.loading_spinner);
        mProgress.setVisibility(View.GONE);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (savedInstanceState != null) {
                mUri = savedInstanceState.getString(URI);
                {
                    if (!mUri.isEmpty()) {
                        mLoaderManager = getLoaderManager();
                        mLoaderManager.initLoader(BOOK_LOADER_ID, savedInstanceState, BookActivity.this);
                    }
                }
            } else {
                mEmptyStateTextView.setText(R.string.no_search_term);
            }
        } else {
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTerm = mSearchView.getText().toString();
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    if (!mSearchTerm.isEmpty()) {
                        mProgress.setVisibility(View.VISIBLE);
                        mEmptyStateTextView.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mSearchTerm = mSearchTerm.replace(" ", "+");
                        mUri = BOOKS_REQUEST_URL + mSearchTerm + MAX_RESULTS;
                        Bundle search = new Bundle();
                        search.putString(URI, mUri);
                        mLoaderManager = getLoaderManager();
                        getLoaderManager().restartLoader(BOOK_LOADER_ID, search, BookActivity.this);
                    } else {
                        mEmptyStateTextView.setText(R.string.no_search_term);
                    }
                } else {
                    mAdapter.clear();
                    mEmptyStateTextView.setText(R.string.no_internet);
                }
            }
        });
        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(URI, mUri);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, bundle.getString(URI));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        ProgressBar progress = findViewById(R.id.loading_spinner);
        progress.setVisibility(View.GONE);
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter = new BookAdapter(BookActivity.this, books);
            mBookListView.setAdapter(mAdapter);
        } else {
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
