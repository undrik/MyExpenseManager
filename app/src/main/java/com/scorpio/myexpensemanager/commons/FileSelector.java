package com.scorpio.myexpensemanager.commons;

import android.net.Uri;

/**
 * Created by hkundu on 02-03-2018.
 */

public class FileSelector {

    public static String getFilePath(Uri uri) {
        String result = null;

        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        return result;
    }
}
