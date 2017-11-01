package com.example.psydrw.mdp_cw1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//Do resume saving for sub activities to

public class MainActivity extends AppCompatActivity {

    private static final int COLOUR_REQUEST = 1;
    private static final int BRUSH_REQUEST = 2;
    private static final int IMAGE_REQUEST = 3;
    private static final int STORAGE_PERMISSION_REQUEST = 4;


    private FingerPainterView fView;
    private int brushColour;
    private int brushWidth;
    private Paint.Cap brushType;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab reference to finger painter
        fView = (FingerPainterView)findViewById(R.id.fingerview);

        fView.load(getIntent().getData());

        //Initialise inger painter setting variables
        brushColour = fView.getColour();
        brushWidth = fView.getBrushWidth();
        brushType = fView.getBrush();

        //Restore state after app is rotated etc
        if(savedInstanceState != null)
        {
            brushColour = savedInstanceState.getInt("Colour");
            brushType = (Paint.Cap)savedInstanceState.getSerializable("Brush Type");
            brushWidth = savedInstanceState.getInt("Brush Width");
            fView.setBrush(brushType);
            fView.setBrushWidth(brushWidth);
            fView.setColour(brushColour);
        }
    }

    //Save state on rotation etc
    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putInt("Colour",brushColour);
        state.putInt("Brush Width", brushWidth);
        state.putSerializable("Brush Type",brushType);
    }



    //Read in data from brushColour and brush sub activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            //Handle new brushColour info
            if (requestCode == COLOUR_REQUEST)
            {
                brushColour = data.getIntExtra("New Colour",0xff000000);
                fView.setColour(brushColour);
            }
            //Handle new brush info
            else if(requestCode == BRUSH_REQUEST)
            {
                //Set brush size
                brushWidth = data.getIntExtra("New Width",20);
                fView.setBrushWidth(brushWidth);

                //Set brush shape
                brushType = (Paint.Cap) data.getSerializableExtra("New Type");
                fView.setBrush(brushType);
            }
            //Handle loading of images
            else if(requestCode == IMAGE_REQUEST)
            {
                Uri uri = data.getData();
                fView.loadCustom(uri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        //If return a permision request of granted for external storage, then continue with opening the image picker activity the user had previously attempted to launch prior to enabling permission to do so
        if(requestCode == STORAGE_PERMISSION_REQUEST)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        }
    }


    public void SetBrush(View view)
    {
        Intent brushScreen = new Intent(getApplicationContext(),BrushSelection.class);
        brushScreen.putExtra("Brush Width", fView.getBrushWidth());
        brushScreen.putExtra("Brush Type", fView.getBrush());
        startActivityForResult(brushScreen,BRUSH_REQUEST);
    }

    //Starts the brushColour selection activity.
    public void SetColour(View view)
    {
        Intent colScreen = new Intent(MainActivity.this,ColourSelection.class);
        colScreen.putExtra("Current Colour",fView.getColour());
        startActivityForResult(colScreen,COLOUR_REQUEST);
    }

    public void onClickImageSelect(View view)
    {
        //Check if storage permissions have been granted for the app
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
        //If not, request them
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST);
        }

    }


}
