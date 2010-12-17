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
package com.cyrilmottier.android.getmemo;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cyrilmottier.android.getmemo.provider.MemosContract;
import com.cyrilmottier.android.getmemo.provider.MemosContract.Memos;

public class MemosListActivity extends ListActivity {

    private Cursor mCursor;

    @SuppressWarnings("unused")
    private static final String LOG_TAG = MemosListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memos_list);

        findViewById(R.id.dimmer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.dim));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCursor == null) {
            mCursor = managedQuery(MemosContract.Memos.CONTENT_URI, MemosQuery.PROJECTION, null, null,
                    MemosQuery.SORT_ORDER);
            setListAdapter(new MemoCursorAdapter(this, mCursor));
        } else {
            mCursor.requery();
        }
    }

    public void onGoHome(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void onTakePicture(View v) {
        try {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.no_way_to_take_photo, Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreateMemo(View v) {
        Intent intent = new Intent(this, NewMemoActivity.class);
        startActivity(intent);
    }

    public class MemoCursorAdapter extends CursorAdapter {

        private final String mInFormat;

        public MemoCursorAdapter(Context context, Cursor c) {
            super(context, c);
            mInFormat = getString(R.string.date_format);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view.findViewById(R.id.text_content)).setText(cursor.getString(MemosQuery.CONTENT));
            final long timestamp = cursor.getLong(MemosQuery.DATETIME);
            ((TextView) view.findViewById(R.id.text_datetime)).setText(DateFormat.format(mInFormat, timestamp));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.memo_list_item_view, parent, false);
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }

    private interface MemosQuery {
        String[] PROJECTION = {
                Memos._ID, Memos.DATETIME, Memos.CONTENT
        };

        @SuppressWarnings("unused")
        int _ID = 0;
        int DATETIME = 1;
        int CONTENT = 2;

        String SORT_ORDER = Memos.DATETIME + " DESC";
    }

}
