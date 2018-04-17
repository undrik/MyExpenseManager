package com.scorpio.myexpensemanager.commons;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

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

    public static String getDirectory(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
//            int column_index = cursor.getColumnIndex(OpenableColumns.)
            cursor.moveToFirst();
//            result = cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(Constants.APP_NAME, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }
}
