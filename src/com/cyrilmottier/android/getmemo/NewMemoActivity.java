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
 */package com.cyrilmottier.android.getmemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.cyrilmottier.android.getmemo.provider.MemosContract;
import com.cyrilmottier.android.getmemo.util.Config;

public class NewMemoActivity extends Activity {

    private static final String LOG_TAG = NewMemoActivity.class.getSimpleName();

    private final RecognitionListener mRecognitionListener = new RecognitionListener();
    private ViewSwitcher mViewSwitcher;
    private Button mSaveButton;

    private String mLastMemo;

    private SpeechRecognizer mRecognizer;

    // TODO Cyril: Handle errors in a better way.
    private final class RecognitionListener implements android.speech.RecognitionListener {
        public void onBeginningOfSpeech() {
            if (Config.INFO_LOGS_ENABLED) {
                Log.i(LOG_TAG, "onBeginningOfSpeech");
            }
        }

        public void onBufferReceived(byte[] buffer) {
            if (Config.INFO_LOGS_ENABLED) {
                Log.i(LOG_TAG, "onBufferReceived");
            }
        }

        public void onEndOfSpeech() {
            if (Config.INFO_LOGS_ENABLED) {
                Log.i(LOG_TAG, "onEndOfSpeech");
            }
            displayInfo(R.string.loading);
        }

        public void onError(int error) {
            if (Config.INFO_LOGS_ENABLED) {
                Log.i(LOG_TAG, "onError");
            }
            displayInfo(R.string.error_occured_try_again);
        }

        public void onEvent(int eventType, Bundle params) {
        }

        public void onPartialResults(Bundle partialResults) {
        }

        public void onReadyForSpeech(Bundle params) {
            if (Config.INFO_LOGS_ENABLED) {
                Log.i(LOG_TAG, "onReadyForSpeech(" + params + ")");
            }
            // We're now ready to listen to the user !
            displayInfo(R.string.ready_for_speech);
        }

        public void onResults(Bundle results) {
            ArrayList<String> resultsArray = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (resultsArray != null && resultsArray.size() > 0) {
                if (Config.INFO_LOGS_ENABLED) {
                    for (String result : resultsArray) {
                        Log.i(LOG_TAG, result);
                    }
                }

                mSaveButton.setEnabled(true);
                // We're using the first result as it's considered as the most
                // relevant one. However, other results may be more accurate and
                // it could be great to display them ...
                mLastMemo = resultsArray.get(0);
                displayInfo(mLastMemo);
            }
        }

        public void onRmsChanged(float rmsdB) {
            // It could be great to use that callback to animate an
            // "audio-level meter".
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_memo);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        mSaveButton = (Button) findViewById(R.id.btn_save);
        mSaveButton.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(mRecognitionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mRecognizer.stopListening();
        mRecognizer.destroy();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // HACK Cyril: Prevent gradient banding
        // Won't be necessary in Gingerbread anymore
        getWindow().setFormat(PixelFormat.RGBA_8888);
    }

    public void onNewMemo(View v) {

        Intent intent = new Intent();
        // HACK Cyril: Should be Locale.getDefault().toString() but the
        // SpeechRecognizer wants locale like FR-fr where Locale.FRANCE return
        // FR_fr ... :(
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "US-en");

        displayInfo(R.string.wait);
        mRecognizer.startListening(intent);
        mSaveButton.setEnabled(false);
    }

    public void onSaveMemo(View v) {
        // Create a new ContentValues object
        ContentValues values = new ContentValues();
        values.put(MemosContract.Memos.DATETIME, System.currentTimeMillis());
        values.put(MemosContract.Memos.STARRED, false);
        values.put(MemosContract.Memos.CONTENT, mLastMemo);

        // Store those result in the ContentProvider
        getContentResolver().insert(MemosContract.Memos.CONTENT_URI, values);

        finish();
    }

    private void displayInfo(int stringId) {
        displayInfo(getString(stringId));
    }

    private void displayInfo(String string) {
        ((TextView) mViewSwitcher.getNextView()).setText(string);
        mViewSwitcher.showNext();
    }

}
