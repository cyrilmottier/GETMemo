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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cyrilmottier.android.getmemo.provider.MemosContract.Memos;
import com.cyrilmottier.android.getmemo.util.Config;

public class MemosDatabase extends SQLiteOpenHelper {

    private static final String LOG_TAG = MemosDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "get_memo.db";
    private static final int DATABASE_VERSION = 1;

    interface Tables {
        String MEMOS = "memos";
    }

    public MemosDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onCreate()");
        }
        
        db.execSQL("CREATE TABLE " + Tables.MEMOS + " (" 
                + Memos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Memos.DATETIME + " DATETIME NOT NULL,"
                + Memos.CONTENT + " TEXT NOT NULL,"
                + Memos.STARRED + " BOOLEAN NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (Config.INFO_LOGS_ENABLED) {
            Log.i(LOG_TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);
        }

        db.execSQL("DROP TABLE IF EXISTS " + Tables.MEMOS);
        onCreate(db);
    }

}
