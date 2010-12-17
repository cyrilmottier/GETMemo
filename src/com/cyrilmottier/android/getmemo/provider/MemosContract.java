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

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MemosContract {

    interface MemosColumns {
        String DATETIME = "datetime";
        String CONTENT = "content";
        String STARRED = "starred";
    }

    public static final String CONTENT_AUTHORITY = "com.cyrilmottier.android.getmemo";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_MEMOS = "memos";

    public static class Memos implements MemosColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMOS).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.getmemo.memo";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.getmemo.memo";
        
        public static Uri buildMemoUri(long memoId) {
            return ContentUris.withAppendedId(CONTENT_URI, memoId);
        }

    }

}
