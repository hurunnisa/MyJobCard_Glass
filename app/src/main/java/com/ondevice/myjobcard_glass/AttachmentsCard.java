package com.ondevice.myjobcard_glass;

import com.google.android.glass.content.Intents;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class AttachmentsCard extends Activity {

    private static final int TAKE_PICTURE_REQUEST = 1;
    public static final String IMAGE_DIRECTORY_NAME = "ODS File Upload";
    public static String filePath = null;
    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScrollView;
    private List<CardBuilder> mCards;
    private AttachmentsScrollAdapter mAdapter;
    CameraView cameraView;
    GestureDetector mGestureDetector;
    public static Uri fileUri;
    private static String capturedImageTitle;
    Camera.ShutterCallback myShutterCallback;
    Camera.PictureCallback myPictureCallback_RAW;
    Camera.PictureCallback myPictureCallback_JPG;
    boolean isAttachment=false;
    String encodedImage;
    byte[] b;
    String className="Attachment";
    String attData;
    // byte[] temp;
    @Override
    protected void onCreate(Bundle bundle) {
        try {
            super.onCreate(bundle);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            cameraView = new CameraView(this);

            // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            myShutterCallback = new Camera.ShutterCallback(){

                public void onShutter()
                {
                    // TODO Auto-generated method stub
                }};

            myPictureCallback_RAW = new Camera.PictureCallback(){

                public void onPictureTaken(byte[] arg0, Camera arg1)
                {
                    // TODO Auto-generated method stub
                }};

            myPictureCallback_JPG = new Camera.PictureCallback(){

                public void onPictureTaken(byte[] arg0, Camera arg1) {
                    // TODO Auto-generated method stub
                    try {
                        isAttachment=true;
                        //                    byte[] b= Base64.encode(arg0,0);
                        //                    byte[] bt=Base64.decode(b,0,b.length,0);

                        openOptionsMenu();
                        invalidateOptionsMenu();
                        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

                        // Create the storage directory if it does not exist
                        if (!mediaStorageDir.exists()) {
                            if (!mediaStorageDir.mkdirs()) {
                                Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                                return;
                            }
                        }
                        // Create a media file name
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                                Locale.getDefault()).format(new Date());
                        File mediaFile;

                        capturedImageTitle = "IMG_" + timeStamp + ".jpg";
                        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                                + capturedImageTitle);
                        //mediaFile = File.createTempFile(capturedImageTitle, ".jpg", mediaStorageDir);
                        filePath = mediaFile.getAbsolutePath();
                        //                    File photo=new File(Environment.getExternalStorageDirectory(), capturedImageTitle);
                        //
                        //                    if (photo.exists()) {
                        //                        photo.delete();
                        //                    }

                        try {
                            FileOutputStream fos=new FileOutputStream(filePath);
                           // Toast.makeText(AttachmentsCard.this, filePath, Toast.LENGTH_SHORT).show();
                            fos.write(arg0);
                            fos.close();
                        }
                        catch (java.io.IOException e) {
                            Log.e("PictureDemo", "Exception in photoCallback", e);
                        }
                        // bimatp factory
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // options.inJustDecodeBounds = true;


                        // down sizing image as it throws OutOfMemory Exception for larger
                        // images
                        options.inSampleSize = 2;

                        final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

                        // ExifInterface exif = new ExifInterface(filePath);
                        //                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                        //                    int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

                        int rotationAngle = 90;
                        //                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        //                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                        //                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
                        //

                        //Bitmap correctBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        b = baos.toByteArray();
                        attData=Base64.encodeToString(b, Base64.DEFAULT);

                        //byte[] image = bytes.toByteArray();

                        //Then I retrieve the bitmap from string to set an activity's background just like that:
                        //temp = Base64.decode(encodedImage, Base64.DEFAULT);

                        // Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

                        //Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), null, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }};
            createCards();

            mCardScrollView = new CardScrollView(this);
            mAdapter = new AttachmentsScrollAdapter();
            mCardScrollView.setAdapter(mAdapter);
            mCardScrollView.activate();
            setupClickListener();
            setContentView(mCardScrollView);
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attachment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_sendAttach:
                    try {
                        //BluetoothConnectionActivity.mService.write(b);
                        Toast.makeText(AttachmentsCard.this, "Attachment has Succuessfully Uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AttachmentsCard.this,AttachmentsCard.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.action_cancelAttach:
                    startActivity(new Intent(AttachmentsCard.this,AttachmentsCard.class));
                    finish();
                    return true;
            }
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private GestureDetector createGestureDetector(final Context context)
    {
        GestureDetector gestureDetector= null;
        try {
            gestureDetector = new GestureDetector(context);
            gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
                @Override
                public boolean onGesture(Gesture gesture) {
                    if(cameraView!=null)
                    {
                        if(gesture==Gesture.TAP)
                        {
                            try {
                                CameraView.camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);

                                //                             Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //
                                //
                                //                                 fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, context);
                                //                                 intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                //                             //if (intent.resolveActivity(AttachmentsCard.this.getPackageManager()) != null)
                                //                                 startActivityForResult(intent, TAKE_PICTURE_REQUEST);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return gestureDetector;
    }
   /* private static Uri getOutputMediaFileUri(int type, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                File file = getOutputMediaFile(type, context);
                if (file != null) {
                    Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName()+".provider", file);
                    return fileUri;
                }
            }
            return Uri.fromFile(getOutputMediaFile(type, context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
    private static File getOutputMediaFile(int type, Context context) {
        try {
            // External sdcard location
            //String TAG = DocsUtil.class.getSimpleName();
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;

            if (type == MEDIA_TYPE_IMAGE) {
                capturedImageTitle = "IMG_" + timeStamp + ".jpg";
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + capturedImageTitle);
                //mediaFile = File.createTempFile(capturedImageTitle, ".jpg", mediaStorageDir);
                filePath = mediaFile.getAbsolutePath();
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    /*Take out ints */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
                String thumbnailPath = data.getStringExtra(Intents.EXTRA_THUMBNAIL_FILE_PATH);
                String picturePath = data.getStringExtra(Intents.EXTRA_PICTURE_FILE_PATH);

                processPictureWhenReady(picturePath);
                // TODO: Show the thumbnail to the user while the full picture is being
                // processed.
            }
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void processPictureWhenReady(final String picturePath) {
        try {
            final File pictureFile = new File(picturePath);

            if (pictureFile.exists()) {
                // The picture is ready; process it.
            } else {
                // The file does not exist yet. Before starting the file observer, you
                // can update your UI to let the user know that the application is
                // waiting for the picture (for example, by displaying the thumbnail
                // image and a progress indicator).

                final File parentDirectory = pictureFile.getParentFile();
                FileObserver observer = new FileObserver(parentDirectory.getPath(),
                        FileObserver.CLOSE_WRITE | FileObserver.MOVED_TO) {
                    // Protect against additional pending events after CLOSE_WRITE
                    // or MOVED_TO is handled.
                    private boolean isFileWritten;

                    @Override
                    public void onEvent(int event, String path) {
                        if (!isFileWritten) {
                            // For safety, make sure that the file that was created in
                            // the directory is actually the one that we're expecting.
                            File affectedFile = new File(parentDirectory, path);
                            isFileWritten = affectedFile.equals(pictureFile);

                            if (isFileWritten) {
                                stopWatching();

                                // Now that the file is ready, recursively call
                                // processPictureWhenReady again (on the UI thread).
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        processPictureWhenReady(picturePath);
                                    }
                                });
                            }
                        }
                    }
                };
                observer.startWatching();
            }
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if(mGestureDetector!=null)
            return mGestureDetector.onMotionEvent(event);
        return false;
    }

    private void createCards() {
        try {
            mCards = new ArrayList<CardBuilder>();

            mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                    .setText("Upload Photo")
                    .setFootnote("Tap on this for take a picture"));
            //.setTimestamp("just now"));

            mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                    .setText("Upload Video")
                    .setFootnote("Tap on this for take a video"));
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //.setTimestamp("just now"));

    }

    private class AttachmentsScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return CardBuilder.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }
    }

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.TAP);
//                try {
//                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, AttachmentsCard.this);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                    //if (intent.resolveActivity(AttachmentsCard.this.getPackageManager()) != null)
//                    startActivityForResult(intent, TAKE_PICTURE_REQUEST);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                if(HomeData.workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Completed")||HomeData.workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("SUSPENDED")||HomeData.workOrderGlassEntity.getMobileObjStatus().equalsIgnoreCase("Transfer")) {
                    Toast.makeText(AttachmentsCard.this, "Work Order has "+ HomeData.workOrderGlassEntity.getMobileObjStatus(), Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        mGestureDetector = createGestureDetector(AttachmentsCard.this);
                        setContentView(cameraView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                openOptionsMenu();
                invalidateOptionsMenu();
                return true;
            }
            if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN)
            {

            }
            else if(keyCode==KeyEvent.KEYCODE_BACK)
            {
                if(isAttachment)
                {
                    isAttachment=false;
                    startActivity(new Intent(AttachmentsCard.this, AttachmentsCard.class));
                    finish();
                    return true;
                }
                else {
                    startActivity(new Intent(AttachmentsCard.this, WorkOrderCard.class));
                    finish();
                    return true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(AttachmentsCard.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mCardScrollView.activate();
//    }
//
//    @Override
//    protected void onPause() {
//        mCardScrollView.deactivate();
//        super.onPause();
//    }
}