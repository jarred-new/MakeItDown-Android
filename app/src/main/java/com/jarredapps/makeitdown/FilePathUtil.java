package com.jarredapps.makeitdown;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FilePathUtil {

    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) return null;

        // If it's already a standard file URI, just return its path
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        // Handle content:// URIs (Works flawlessly on Android 4.4 up to Android 11+)
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String fileName = getFileName(context, uri);
            if (fileName == null) {
                // Fallback name if the provider doesn't give one
                fileName = "temp_file_" + System.currentTimeMillis();
            }

            // Create a temporary file in the app's internal cache directory
            File cacheFile = new File(context.getCacheDir(), fileName);

            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(cacheFile)) {

                if (inputStream == null) return null;

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();

                // Return the working, local absolute path string
                return cacheFile.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // Helper method to retrieve the original filename from the URI provider
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index != -1) {
                    result = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

