package com.wrx.quickeats.util;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.wrx.quickeats.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.InputStream;



///**
// * Created by Rajeev Sharma [rajeevrocker7@gmail.com] on 18/4/17.
// */

///*
//*
//* To work with Take photo,prerequisite are:
//
//1.file_paths.xml file:
//
//<?xml version="1.0" encoding="utf-8"?>
//<paths xmlns:android="http://schemas.android.com/apk/res/android">
//    <external-path name="external_files" path="." />
//</paths>
//
//2.in the AndroidManifest.xml:
//<application>
//..
//   <provider
//            android:name="android.support.v4.content.FileProvider"
//            android:authorities="com.skylist.skylist.fileprovider"
//            android:exported="false"
//            android:grantUriPermissions="true">
//            <meta-data
//                android:name="android.support.FILE_PROVIDER_PATHS"
//                android:resource="@xml/file_paths" />
//        </provider>
//
//    </application>
//
//*
//***/

public class TakePhoto extends AppCompatActivity {


    public static String FROM = "from";
    public static String GALLERY = "gallery";
    public static String CAMERA = "camera";
    public static String SIZE = "size";
    public static String WANT_THUMBNAIL = "thumbnail";
    public static String WANT_FULL_SIZE = "full";

    private final String TAG = "TakePhoto";

    //   START ACTIVITY FOR RESULT: REQUEST CODES

    private final int REQ_CAMERA_FULL_CODE = 12;
    private final int REQ_GALLERY_PICK_CODE = 14;

    //  REQUEST PERMISSIONS: REQUEST CODES
    private final int REQ_CODE_ASK_ALL_PERMISSIONS = 20;

    protected String AUTHORITY_NAME = BuildConfig.APPLICATION_ID + ".fileprovider";

    private String mediaDirectory
            = Environment.getExternalStorageDirectory()+ File.separator+ "QuickEATS"
            + File.separator+"Pictures"+ File.separator;

    protected String size = null;
    private String currentPhotoPath = "";
    private Uri photoURI = null;
    private String fromWhere = null;


//   NO-ARG CONSTRUCTOR
    public TakePhoto() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedIntent = getIntent();
        String fromWhere = receivedIntent.getStringExtra(FROM);
        String size = receivedIntent.getStringExtra(SIZE);
        if(size == null)
        {
            this.size = WANT_FULL_SIZE;  // BY DEFAULT WE PROVIDE FULL SIZE IMAGE
        }
        else {
            this.size = size;       // GIVEN BY PROGRAMMER
        }

        if(fromWhere !=null)
        {
            this.fromWhere = fromWhere;
//          start CHECKING FOR RUNTIME PERMISSIONS
            doAllPermissionChecking();
        }

    }

//    METHOD:TO START TAKING PHOTOS via DISPATCHING CAMERA AND GALLERY INTENTS BASED ON fromWhere TYPE
    private void startTakingPhotos()
    {
        if(fromWhere != null)
        {
            if(fromWhere.equalsIgnoreCase(CAMERA))
            {
                dispatchCameraIntent();
            }
            else if(fromWhere.equalsIgnoreCase(GALLERY))
            {
                dispatchGalleryIntent();
            }
        }
    }

//    METHOD:TO DISPATCH GALLERY INTENT TO CHOOSE IMAGE FROM GALLERY
    private void dispatchGalleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
//      Show only images, no videos or anything else
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        Always show the chooser (if there are multiple options available)
            startActivityForResult(intent, REQ_GALLERY_PICK_CODE);
        }
        else
        {
            Toast.makeText(this, "No Gallery app found", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }

//     METHOD:TO DISPATCH CAMERA INTENT TO CAPTURE IMAGE FROM CAMERA
    private void dispatchCameraIntent()
    {
        Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takeImageIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the Uri where the photo should go
            Uri photoURI = createImageUri();
            // Continue only if the Uri /Image File was successfully created
            if (photoURI != null)
            {
                takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeImageIntent, REQ_CAMERA_FULL_CODE);
            }
        }
        else
        {
            Toast.makeText(this, "No Camera app found", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }


//   METHOD: TO CREATE IMAGE URI & GET IMAGE FILE_PATH AND IMAGE URI ABSOLUTE_PATH
     private Uri createImageUri()  {
        Uri imgUri = null  ;
        try
        {
            File imgFile  ;
            File mediaStorageDir  ;
            // Create an image fileName
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd_HHmmmss", Locale.getDefault());
            s.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeStamp = s.format(new Date());
            String imageFileName = "PIC_" + timeStamp + ".jpg";

//        FOR PRIMARY EXTERNAL STORAGE (SHAREABLE) DATA path OF APP
            if(isExternalStorageAvailable())
            {
                 mediaStorageDir = new File(mediaDirectory);
                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Failed to create directory");
                }

                imgFile = new File(mediaStorageDir.getAbsolutePath() + File.separator + imageFileName);

                // Return the file target uri for the photo based on image filename
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // See https://guides.codepath.com/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    imgUri = FileProvider.getUriForFile(TakePhoto.this, AUTHORITY_NAME, imgFile);

                } else {
                    imgUri = Uri.fromFile(imgFile);
                }

                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = imgFile.getAbsolutePath();
                photoURI = imgUri;
            }
            Log.w(TAG, "CurrentPhotoPath: " + currentPhotoPath);
            Log.w(TAG, "PhotoURI: " + photoURI);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e(TAG,"ERROR IN CREATION OF IMAGE FILE / URI");
        }

        return imgUri;
    }

 //   METHOD: TO CREATE GALLERY IMAGE FILE COPY  & GET IMAGE FILE_PATH
    private String saveGalleryImage() {

        String imagePath="";
        File mediaStorageDir  ;
        // Create an image fileName
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd_HHmmmss", Locale.getDefault());
        s.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeStamp = s.format(new Date());
        String imageFileName = "PIC_" + timeStamp + ".jpg";

//        FOR PRIMARY EXTERNAL STORAGE (SHAREABLE) DATA path OF APP
        if(isExternalStorageAvailable())
        {
            mediaStorageDir = new File(mediaDirectory);
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Failed to create directory");
            }

            File imgFile  = new File(mediaStorageDir.getAbsolutePath() + File.separator + imageFileName);
            imagePath = imgFile.getAbsolutePath();


        }
        currentPhotoPath = imagePath;
        Log.w(TAG, "imagePath: " + imagePath);


        return imagePath;
    }

    // Returns true if Primary External Storage is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        else {
            Toast.makeText(this, "PRIMARY EXTERNAL STORAGE NOT MOUNTED.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    /*********** startActivityForResult() CALLBACK   *******************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            // results from CAMERA for FULL SIZE Image
            case REQ_CAMERA_FULL_CODE:
                if (resultCode == RESULT_OK)
                {
                    try {

                        final Handler handler = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what)
                                {
                                    case 150:
                                        Bitmap bitmap = (Bitmap) msg.obj;
//                                  GIVE BACK RESULTING BITMAP, FILEPATH, ETC TO CALLING ACTIVITY
                                        giveResultsBack(bitmap, photoURI, currentPhotoPath, true);
//                                  Update Captured Image in Default 'GALLERY' App OF DEVICE
                                        File capturedImageFile = new File(currentPhotoPath);
                                        scanFile(capturedImageFile.getAbsolutePath());

                                        break;
                                    default:
                                        super.handleMessage(msg);
                                }

                            }
                        };

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

                                Message message = Message.obtain();
                                message.what = 150;
                                message.obj = getCorrectOrientedImage(currentPhotoPath);
                                handler.sendMessage(message);

                            }
                        };

//                      USE HANDLER AND RUNNABLE TO WORK IN BACKGROUND AND UPDATE UI LATER
                        handler.removeCallbacks(runnable);
                        handler.post(runnable);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                        currentPhotoPath = "";
                        photoURI = null;
                        setResult(RESULT_CANCELED);
                        this.finish();

                    }
                }
                else {
                    currentPhotoPath = "";
                    photoURI = null;
                    Toast.makeText(TakePhoto.this, "Photo not captured!", Toast.LENGTH_SHORT).show();
                    this.finish();

                }
                break;

            // results from GALLERY for FULL SIZE IMAGE
            case REQ_GALLERY_PICK_CODE:
                if (resultCode == RESULT_OK)
                {
                    photoURI = data.getData();
                    try {
//                  GIVE BACK RESULTING BITMAP, FILEPATH, ETC TO CALLING ACTIVITY
                        saveGalleryImage();
                        giveResultsBack(null,photoURI,currentPhotoPath,false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        currentPhotoPath = "";
                        photoURI = null;
                        setResult(RESULT_CANCELED);
                        this.finish();
                    }

                }
                else {
                    currentPhotoPath = "";
                    photoURI = null;
                    this.finish();
                }

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void compressRecycle(final Uri imageUri, Bitmap bm, boolean isFromCamera)
    {
        if(isFromCamera) //camera compression
        {
            //Write file on disk, later decode this file stream in other Activity ,when retrieving image
            FileOutputStream fos ;
            try {
                fos = new FileOutputStream(new File(currentPhotoPath));
                bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                //Cleanup
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            bm.recycle();

        }
        else  //gallery compression
        {

           final Handler handler = new Handler(Looper.getMainLooper())
           {
               @Override
               public void handleMessage(Message msg) {
                   switch (msg.what)
                   {
                       case 130:
                           Bitmap bitmapGallery = (Bitmap) msg.obj;
                           //Write file on disk, later decode this file stream in other Activity ,when retrieving image
                           FileOutputStream fos ;
                           try {
                               fos = new FileOutputStream(new File(currentPhotoPath));
                               bitmapGallery.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                               //Cleanup
                               fos.flush();
                               fos.close();
                           } catch (Exception e) {
                               e.printStackTrace();
                           }

                           bitmapGallery.recycle();

                           break;
                       default:
                           super.handleMessage(msg);
                   }

               }
           };

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = 130;
                    InputStream inputStream ;
                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);
                        message.obj = doGalleryCorrectRotation(inputStream);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    message.obj = BitmapFactory.decodeStream(inputStream);
                    handler.sendMessage(message);
                }
            };


            handler.removeCallbacks(runnable);
            handler.post(runnable);
        }

    }


    private Bitmap doGalleryCorrectRotation(InputStream inputStream )
    {

        OutputStream outputStream = null;
        File correctFile = new File(currentPhotoPath);
        Bitmap rotatedBitmap=null;

        try {
            // read this file into InputStream

            // write the inputStream to a FileOutputStream
            outputStream = new FileOutputStream(correctFile);

            int read ;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }


            System.out.println("Done!");

            rotatedBitmap= getCorrectOrientedImage(correctFile.getAbsolutePath());



        } catch (Exception e) {
            e.printStackTrace();
        }


        finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                     outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return rotatedBitmap;

    }



    //  METHOD: TO GIVE BACK RESULTING BITMAP, FILEPATH, ETC TO CALLING ACTIVITY
    private void giveResultsBack(Bitmap bitmap, Uri photoUri, String currentPhotoPath, boolean fromCamera)
    {
        try {

            if(!currentPhotoPath.isEmpty() && photoUri != null)
            {
                Log.w(TAG,"FROM CAMERA: "+fromCamera);
                if(fromCamera)
                {
                   compressRecycle(photoUri,bitmap,true);

                }
                else {
                    compressRecycle(photoUri,bitmap,false);
                }

                /*Pop the intent*/
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("uri",photoUri);
                bundle.putString("path",currentPhotoPath);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                this.finish();
            }
            else {
//           FOR THUMBNAIL TYPE IMAGE ONLY, DO THIS:
                 /*Pop the intent*/
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("image",bitmap);
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                this.finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



//        METHOD:TO SET FULL SIZE IMAGE [WITH CORRECT ORIENTATION]CAPTURED FROM CAMERA /GALLERY
    public Bitmap getCorrectOrientedImage(String photoFilePath) {
        Bitmap rotatedBitmap = null;
        Bitmap bm  ;
        try {
            // Create and configure BitmapFactory
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoFilePath, bounds);
            BitmapFactory.Options opts = new BitmapFactory.Options();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                bm =  BitmapFactory.decodeStream(new FileInputStream(photoFilePath),null,opts);
            }
            else
            {
                bm = BitmapFactory.decodeFile(photoFilePath, opts);
            }

            // Read EXIF Data
            ExifInterface exif = new ExifInterface(photoFilePath);
            int orientation =  exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationAngle = 0;

            if (orientation == ExifInterface.ORIENTATION_NORMAL)
                rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                rotationAngle = 270;

            // Rotate Bitmap
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle);
            rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "-- Exception in setting image");
//            Toast.makeText(this, "Error in setting image", Toast.LENGTH_SHORT).show();
            TakePhoto.this.finish();
        }
        catch(OutOfMemoryError oom) {
            oom.printStackTrace();
            Log.e(TAG, "OutOfMemoryError in setting image");
            TakePhoto.this.finish();
        }
        // Return result
        return rotatedBitmap;
    }

    //  METHOD: TO SCAN IMAGE FILE THAT IS CAPTURED CURRENTLY AND REFLECT IT IN GALLERY App OF DEVICE
    private void scanFile(String path) {

//        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri fileContentUri = Uri.fromFile(new File(path)); // With 'permFile' being the File object
//        mediaScannerIntent.setData(fileContentUri);
//        TakePhoto.this.sendBroadcast(mediaScannerIntent); //

        MediaScannerConnection.scanFile(TakePhoto.this.getApplicationContext(), new String[]{ path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i(TAG, "Finished scanning of " + path);

                    }
                });
    }


///*
// GALLERY:
// W/TakePhoto: CurrentPhotoPath: /storage/emulated/0/DCIM/100PINT/Pins/f206b39871ba10e38fd30d20d79cb6b7.jpg
// W/TakePhoto: PhotoURI: content://media/external/images/media/134173
// W/TakePhoto: imagePath: /storage/emulated/0/Skylist/Pictures/PIC_20170608_0700636.jpg
//
//CAMERA:

//    (for API > Marshmallow+ ):
// W/TakePhoto: CurrentPhotoPath: /storage/emulated/0/Skylist/Pictures/PIC_20170608_0702815.jpg
// W/TakePhoto: PhotoURI: content://com.skylist.skylist.fileprovider/external_files/Skylist/Pictures/PIC_20170608_0702815.jpg
//      (for API = Pre-Marshmallow : i.e. kitkat,lollipop):
//    W/TakePhoto: CurrentPhotoPath: /storage/emulated/0/Skylist/Pictures/PIC_20170609_0503624.jpg
//    W/TakePhoto: PhotoURI: file:///storage/emulated/0/Skylist/Pictures/PIC_20170609_0503624.jpg

//    URI CREATION--->>
//uri: FileProvider.getUriForFile(getActivity(),  BuildConfig.APPLICATION_ID + ".fileprovider", imgFile)
//uri: content://com.skylist.skylist.fileprovider/external_files/Skylist/Pictures/PIC_20170608_0602815.jpg
//
//
//uri: Uri.fromFile(imgFile)
//uri: file:///storage/emulated/0/Skylist/Pictures/PIC_20170608_0602815.jpg
//
//*/



    /*************** checking MULTIPLE RUNTIME PERMISSION *** --STARTS here-- *************/

    private void doAllPermissionChecking() {

        List<String> permissionsNeededForNow = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeededForNow.add("Read Storage");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeededForNow.add("Write Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeededForNow.add("Camera");

//        for Pre-Marshmallow the permissionsNeeded.size() will always be 0; , if clause don't run  Pre-Marshmallow
        if (permissionsList.size() > 0) {
            if (permissionsNeededForNow.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeededForNow.get(0);
                for (int i = 1; i < permissionsNeededForNow.size(); i++)
                    message = message + ", " + permissionsNeededForNow.get(i);

                showMessageOKCancel(message,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(TakePhoto.this,
                                permissionsList.toArray(new String[permissionsList.size()]),
                                REQ_CODE_ASK_ALL_PERMISSIONS);
                    }
                });

                return ;
            }
            ActivityCompat.requestPermissions(TakePhoto.this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQ_CODE_ASK_ALL_PERMISSIONS);

            return ;
        }

//        start doing things if all PERMISSIONS are Granted whensoever
//        for Marshmallow+ and Pre-Marshmallow both
        startTakingPhotos();


    }

    ////    adding RUNTIME PERMISSION to permissionsList and checking If user has GRANTED Permissions or Not  /////
    private boolean addPermission(List<String> permissionsList, String permission) {
        // Marshmallow+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(TakePhoto.this,permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (ActivityCompat.shouldShowRequestPermissionRationale(TakePhoto.this,permission))
                    return false;
            }
        }
        // Pre-Marshmallow
        return true;
    }

    //    Handle "Don't / Never Ask Again" when asking permission
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener)
    {
        new AlertDialog.Builder(TakePhoto.this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // *********** PERMISSION GRANTED FUNCTIONALITY ***********
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {

            case REQ_CODE_ASK_ALL_PERMISSIONS:

                Map<String, Integer> permissionsMap = new HashMap<>();
                // Initial
                permissionsMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permissionsMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permissionsMap.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    permissionsMap.put(permissions[i], grantResults[i]);
                // Check for permissions
                if (permissionsMap.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && permissionsMap.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && permissionsMap.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted at once
                    // do start recording for Marshmallow+ case
                    startTakingPhotos();
                }
                else {
                    // Permission Denied

                    Toast.makeText(TakePhoto.this, "Some Permission is denied. Allow it in App settings", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", TakePhoto.this.getApplicationContext().getPackageName(), null);
                    intent.setData(uri);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.finish();
                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /* ************** checking MULTIPLE RUNTIME PERMISSION **** --ENDS here-- ***********/



}