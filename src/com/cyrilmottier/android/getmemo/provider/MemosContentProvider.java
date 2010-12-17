/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cyrilmottier.android.getmemo.provider;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.cyrilmottier.android.getmemo.provider.MemosContract.Memos;
import com.cyrilmottier.android.getmemo.util.Config;

public class MemosContentProvider extends ContentProvider {

    private static final String LOG_TAG = MemosContentProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MEMOS = 100;
    private static final int MEMOS_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MemosContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "memos", MEMOS);
        matcher.addURI(authority, "memos/*", MEMOS_ID);

        return matcher;
    }

    private MemosDatabase mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MemosDatabase(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMOS:
                return Memos.CONTENT_TYPE;
            case MEMOS_ID:
                return Memos.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        }

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMOS:
            case MEMOS_ID:
            default:
                qb.setTables(MemosDatabase.Tables.MEMOS);
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "insert(uri=" + uri + ", values=" + values + ")");
        }
        
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case MEMOS:
                long memoId = db.insertOrThrow(MemosDatabase.Tables.MEMOS, null, values);
                return MemosContract.Memos.buildMemoUri(memoId);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // XXX Cyril
        return 0;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // XXX Cyril
        return 0;
    }

}
