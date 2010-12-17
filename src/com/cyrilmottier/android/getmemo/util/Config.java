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
package com.cyrilmottier.android.getmemo.util;

@SuppressWarnings("all")
public class Config {

    private Config() {
    }

    private static final int LOG_LEVEL_INFO = 3;
    private static final int LOG_LEVEL_WARNING = 2;
    private static final int LOG_LEVEL_ERROR = 1;
    private static final int LOG_LEVEL_NONE = 0;

    private static final int LOG_LEVEL = LOG_LEVEL_INFO;

    public static final boolean INFO_LOGS_ENABLED = (LOG_LEVEL == LOG_LEVEL_INFO);
    public static final boolean WARNING_LOGS_ENABLED = INFO_LOGS_ENABLED || (LOG_LEVEL == LOG_LEVEL_WARNING);
    public static final boolean ERROR_LOGS_ENABLED = (LOG_LEVEL == LOG_LEVEL_ERROR) || WARNING_LOGS_ENABLED;

}
