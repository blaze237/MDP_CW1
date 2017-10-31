package com.example.psydrw.mdp_cw1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int COLOUR_REQUEST = 1;
    private static final int BRUSH_REQUEST = 2;
    private static final int IMAGE_REQUEST = 3;


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

        //Initialise inger painter setting variables
        brushColour = fView.getColour();
        brushWidth = fView.getBrushWidth();
        brushType = fView.getBrush();

        //Restore state after app is rotated etc
        if(savedInstanceState != null)
        {
            brushColour = savedInstanceState.getInt("Colour");
            fView.setColour(brushColour);
        }
    }

    //Save state on rotation etc
    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putInt("Colour",brushColour);
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
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
}
