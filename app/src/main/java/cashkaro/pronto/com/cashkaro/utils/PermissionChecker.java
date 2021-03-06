package cashkaro.pronto.com.cashkaro.utils;

/**
 * Created by Ankush Goyal on 12/5/16.
 */
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cashkaro.pronto.com.cashkaro.R;

public class PermissionChecker {

    private final int MY_PERMISSIONS_REQUEST = 1;
    private PermissionCallback callback;
    private Activity context;
    private String[] permissions;

    private static PermissionChecker instance;

    public interface PermissionCallback {
        int REQUEST_IMAGE_CAPTURE = 1;
        int RESULT_LOAD_IMAGE = 101;
        int REQUEST_FILECHOOSER = 2;

        void allPermissionsGranted();

        void permissionsNotGranted();
    }

    public void attachListener(PermissionCallback callback) {
        this.callback = callback;
    }

    private PermissionChecker() {

    }

    public static PermissionChecker getInstance() {
        if(instance == null) {
            instance = new PermissionChecker();
        }
        return instance;
    }

    public void initPermissionChecker(Activity context, String[] permissions) {
        initPermissionChecker(context, permissions, null);
    }

    public void initPermissionChecker(Activity context, String[] permissions, PermissionCallback callback) {
        this.context = context;
        attachListener(callback);
        this.permissions = permissions;
    }

    private PermissionChecker(Activity context, String[] permissions) {
        this(context, permissions, null);
    }

    private PermissionChecker(Activity context, String[] permissions, PermissionCallback callback) {
        this.context = context;
        attachListener(callback);
        this.permissions = permissions;
        instance = getInstance();
    }

    public void checkPermissions() {
        // Here, thisActivity is the current activity
        Boolean allPermissonsNotGranted = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissonsNotGranted = true;
                break;
            }
        }

        if (allPermissonsNotGranted) {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[0])) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/

            ActivityCompat.requestPermissions(context, permissions, MY_PERMISSIONS_REQUEST);

        } else {
            if (callback != null)
                callback.allPermissionsGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (callback != null)
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST:
                    if (permissions.length == grantResults.length) {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length > 0) {
                            for (Integer grant : grantResults) {
                                if (grant != PackageManager.PERMISSION_GRANTED) {
                                    permissionNotGranted();
                                    return;
                                }
                            }
                            // permission was granted, yay! Do the
                            // contacts-related task you need to do.
                            callback.allPermissionsGranted();
                        } else {
                            // permission denied, boo! Disable the
                            // functionality that depends on this permission.
                            permissionNotGranted();
                        }
                    } else {
                        permissionNotGranted();
                    }
                    // other 'case' lines to check for other
                    // permissions this app might request
                    break;
            }
    }

    private void permissionNotGranted() {
        Toast.makeText(context, context.getResources().getString(R.string.permission_not_granted_message), Toast.LENGTH_LONG).show();
        if(callback != null) {
            callback.permissionsNotGranted();
        }
    }

    public void dispatchPickImageFromGallery(int CODE) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(i, CODE);
    }

    public String dispatchTakePictureIntent(int CODE) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(context, "Some error in taking photo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                context.startActivityForResult(takePictureIntent, CODE);
                return photoFile.getAbsolutePath();
            }
        }
        return null;
    }

    public void loadFileChooser(int CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            context.startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(context, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageDir = new File(storageDir.getAbsolutePath(), context.getString(R.string.app_name));
        if (imageDir.exists() ? imageDir.exists() : imageDir.mkdirs()) {
            // Save a file: path for use with ACTION_VIEW intents
            File f;
            try{

                f = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir);

            }catch (Exception e){
                e.printStackTrace();
            }


            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */

            );

        }
        return null;
    }

    public String saveBitmap(Bitmap image) throws Exception {
        File file = createImageFile();
        if (file != null) {
            OutputStream outStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return file.getAbsolutePath();
        }
        return null;
    }

    public Boolean checkLocationProviderAvailable() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "Enable location services for accurate data", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String getPathUri(Context context,Uri uri) throws URISyntaxException {
        Log.d("File",
                "Authority: " + uri.getAuthority() +
                        ", Fragment: " + uri.getFragment() +
                        ", Port: " + uri.getPort() +
                        ", Query: " + uri.getQuery() +
                        ", Scheme: " + uri.getScheme() +
                        ", Host: " + uri.getHost() +
                        ", Segments: " + uri.getPathSegments().toString()
        );
        String selection = null;
        String[] selectionArgs = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("image".equals(type)) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[] {
                            split[1]
                    };
                }
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String filePath = null;
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = 0;
                if (cursor != null) {
                    column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(column_index);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            if(filePath == null) {
                try {
                    InputStream is = context.getContentResolver().openInputStream(Uri.parse(uri.toString()));
                    saveBitmap(BitmapFactory.decodeStream(is));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return filePath;
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void getPathUriWithCallBack(Context context, Uri uri, FilePathsCallBacks checkerCallBacks){
        try {
            Log.d("File",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );
            String selection = null;
            String[] selectionArgs = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    if (isExternalStorageDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        checkerCallBacks.filePath(Environment.getExternalStorageDirectory() + "/" + split[1]);
                        return;
                    } else if (isDownloadsDocument(uri)) {
                        final String id = DocumentsContract.getDocumentId(uri);
                        uri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    } else if (isMediaDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];
                        if ("image".equals(type)) {
                            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                        selection = "_id=?";
                        selectionArgs = new String[]{
                                split[1]
                        };
                    }
                }
            }
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String filePath = null;
                String[] projection = {
                        MediaStore.Images.Media.DATA
                };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
                    int column_index = 0;
                    if (cursor != null) {
                        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(column_index);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (filePath == null) {
                    saveBitmap(Picasso.with(context).load(uri).get(), checkerCallBacks);
                } else {
                    checkerCallBacks.filePath(filePath);
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                checkerCallBacks.filePath(uri.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            checkerCallBacks.filePath(null);
        }
    }

    public void saveBitmap(final Bitmap image, final FilePathsCallBacks checkerCallBacks) {
        new SaveImage(checkerCallBacks).execute(image);
    }

    public interface FilePathsCallBacks {
        void filePath(String path);
    }

    private class SaveImage extends AsyncTask<Bitmap, Void, String> {

        private FilePathsCallBacks filePathsCallBacks;

        public SaveImage(FilePathsCallBacks filePathsCallBacks) {
            this.filePathsCallBacks = filePathsCallBacks;
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            try {
                File file = createImageFile();
                if (file != null) {
                    OutputStream outStream = new FileOutputStream(file);
                    params[0].compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    return file.getAbsolutePath();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (filePathsCallBacks != null)
                filePathsCallBacks.filePath(s);
        }
    }

}