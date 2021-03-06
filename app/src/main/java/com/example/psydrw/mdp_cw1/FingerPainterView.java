package com.example.psydrw.mdp_cw1;

/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * Created by pszmdf on 13/10/16.
 *
 * Derived from android graphics API sample com.example.android.apis.graphics.Fingerpaint
 * android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java
 *
 */

public class FingerPainterView extends View {

    private Context context;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Path path;
    private Uri uri;



    public FingerPainterView(Context context) {
        super(context); // application context
        init(context);
    }

    public FingerPainterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FingerPainterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        path = new Path();
        paint = new Paint();

        // default brush style and colour
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(20);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setARGB(255,0,0,0);
    }

    public void setBrush(Paint.Cap brush) {
        paint.setStrokeCap(brush);
    }

    public Paint.Cap getBrush() {
        return paint.getStrokeCap();
    }

    public void setBrushWidth(int width) {
        paint.setStrokeWidth(width);
    }

    public int getBrushWidth() {
        return (int) paint.getStrokeWidth();
    }

    public void setColour(int colour) {
        paint.setColor(colour);
    }

    public int getColour() {
        return paint.getColor();
    }

    public void load(Uri uri) {
        this.uri = uri;
    }

    //Clears the canvas by writing white to all pixels
    public void clear(){bitmap.eraseColor(0xffffffff);}

    //Saves the canvas contents into a png file
    public void saveToFile()
    {
        //Set directory to save the image to
        File dirct = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Make the directory if it doesn't already exist
        dirct.mkdir();

        //File name is set to be current date+time to ensure the filename is always unique
        String fName = new Date().toString() + ".png";

        //Setup the new file.
        File pictureFile = new File(dirct,fName);

        try
        {
            //Setup output stream and pass bitmap into it
            FileOutputStream output = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

            //Close the stream and inform android of presence of the new file via broadcast call
            output.flush();
            output.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(pictureFile)));

            //Inform user the file was saved successfully
            Toast.makeText(context,"Image " + fName + " saved." ,Toast.LENGTH_SHORT).show();
        } catch (Exception e)
        {
            //If there was an error saving the file for some reason, then inform the user.
            e.printStackTrace();
            Toast.makeText(context,"Error saving file!",Toast.LENGTH_SHORT).show();
        }
    }

    //Allows for image loading to canvas on the fly.
    public void loadCustom(Uri uri)
    {
        int width = this.getWidth();
        int height = this.getHeight();

        try {
            // attempt to load the uri provided, scale to fit our canvas
            InputStream stream = context.getContentResolver().openInputStream(uri);
            Bitmap bm = BitmapFactory.decodeStream(stream);
            //Create a mutable copy of the bitmap. This is needed due to cases in which the read in image needs no scaling, leading to
            //'bitmap' not being scaled and hence inheriting the immutability of bm causing errors.
            bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
            bitmap  = Bitmap.createScaledBitmap(bitmap, width, height, false);
            stream.close();
            bm.recycle();
        } catch(IOException e) {
            Log.e("FingerPainterView", e.toString());
            Toast.makeText(context,"Failed to load image!",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(context,"Image Loaded",Toast.LENGTH_SHORT).show();
        canvas = new Canvas(bitmap);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // save superclass view state
        bundle.putParcelable("superState", super.onSaveInstanceState());

        try {
            // save bitmap to temporary cache file to overcome binder transaction size limit
            File f = File.createTempFile("fingerpaint", ".png", context.getCacheDir());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(f));
            // save temporary filename to bundle
            bundle.putString("tempfile", f.getAbsolutePath());
        } catch(IOException e) {
            Log.e("FingerPainterView", e.toString());
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            try {
                // load cache file from bundle stored filename
                File f = new File(bundle.getString("tempfile"));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                // need to copy the bitmap to create a mutable version
                bitmap = b.copy(b.getConfig(), true);
                b.recycle();
                f.delete();
            } catch(IOException e) {
                Log.e("FingerPainterView", e.toString());
            }

            state = bundle.getParcelable("superState");
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas is white with a bitmap with alpha channel drawn over the top
        canvas.drawColor(Color.WHITE);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        // show current drawing path
        canvas.drawPath(path, paint);
    }

    //Method altered to allways rescale canvas to fit current view size to prevent parts of image getting cut off.
    //Also prevents buggy behaviour on rotate where parts of the screen cannot be drawn to. I feel this is preferable to
    //ensuring the image is allways square. Method also changed to set canvas backrground to white for better appearance of saved images
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // called after the activity has been created when the view is inflated
        if(bitmap==null) {
            if(uri!=null) {
                try {
                    // attempt to load the uri provided, scale to fit our canvas
                    InputStream stream = context.getContentResolver().openInputStream(uri);
                    Bitmap bm = BitmapFactory.decodeStream(stream);
                    //Create a mutable copy of the bitmap. This is needed due to cases in which the read in image needs no scaling, leading to
                    //'bitmap' not being scaled and hence inheriting the immutability of bm causing errors.
                    bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
                    bitmap  = Bitmap.createScaledBitmap(bitmap, w, h, false);
                    stream.close();
                    bm.recycle();
                } catch(IOException e) {
                    Log.e("FingerPainterView", e.toString());
                }
            }
            else
            {
                bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(0xffffffff); //Make background white so looks better when saving files
            }
        }
          else
             bitmap  = Bitmap.createScaledBitmap(bitmap, w,h, false);
        canvas = new Canvas(bitmap);
    }

    //Slightly altered to call refresh() function if parent activity. Needed to fix bug with brush drawing over gui elements.
    //See main activity for more details
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                path.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();
                invalidate();
                break;
        }

        //Tells main activity to invalidate button layouts to fix rare  cases when the brush can draw over them
        //Somewhat inelegant but only way i can think to do this, as onTouchEvent consumes the touch event, preventing use
        //of custom onClick function in main activity.
        ((MainActivity)(getContext())).refresh();

        return true;
    }
}