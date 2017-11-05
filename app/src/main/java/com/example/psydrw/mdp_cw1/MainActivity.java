package com.example.psydrw.mdp_cw1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    //Constants used for handling activity results and permission requests
    private static final int COLOUR_REQUEST = 1;
    private static final int BRUSH_REQUEST = 2;
    private static final int IMAGE_REQUEST = 3;
    private static final int STORAGE_PERMISSION_REQUEST_READ = 4;
    private static final int STORAGE_PERMISSION_REQUEST_WRITE = 5;


    private FingerPainterView fView;

    //Store current brush settings
    private int brushColour;
    private int brushWidth;
    private Paint.Cap brushType;

    //References to the two linear layouts containing buttons have to be kept in order to solve a
    //rare bug with the fingerPainter system in which the user can permanently draw over elements outside
    //of the fingerPainter canvas view. We solve the issue by invalidating these layouts every time the fingerPainter canvas is touched
    private LinearLayout buttonPanel1, buttonPanel2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab reference to finger painter
        fView = (FingerPainterView)findViewById(R.id.fingerview);
        //Grab references to button layouts containing buttons
        buttonPanel1 = (LinearLayout)findViewById(R.id.buttonPanel1);
        buttonPanel2 = (LinearLayout)findViewById(R.id.buttonPanel2);

        //Handle implicit intent requests by passing image uri to fingerPainter
        fView.load(getIntent().getData());

        //Initialise fingerPainter variables
        brushColour = fView.getColour();
        brushWidth = fView.getBrushWidth();
        brushType = fView.getBrush();

        //Restore state after on activity resume
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

    //Save state on activity suspend (e.g rotation)
    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putInt("Colour",brushColour);
        state.putInt("Brush Width", brushWidth);
        state.putSerializable("Brush Type",brushType);
    }

    //Read in new brush information from sub activties and apply it to the fingerPainter view
    //Also handles loading in of images to fingerPainter initiated the image selection button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Check the subActivity ended properly
        if(resultCode == Activity.RESULT_OK)
        {
            //Handle new brushColour info
            if (requestCode == COLOUR_REQUEST)
            {
                //Set brush colour
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
                final Uri imgURI = data.getData();
                /*
                The call to load the new image must be added as a runnable object.
                The reason for this is that the loading function involves accessing the fingerView object's dimensions
                And, in cases where the view has not yet finished being reconstructed, these dimensions will
                be returned as zero, causing errors. Hence we use runnable to ensure the loading is only done
                once the view has finished being reconstructed.
                */
                fView.post(new Runnable() {
                    @Override
                    public void run() {
                        fView.loadCustom(imgURI);
                    }
                });
            }
        }
    }

    //Handle results of permission requests needed to read and write to storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        //If return a permision request of granted for reading external storage, then continue with opening the image picker activity the user had previously attempted to launch prior to enabling permission to do so
        if(requestCode == STORAGE_PERMISSION_REQUEST_READ)
        {
            //Check if there is some result returned, and if it was a granted or denied request
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
            else if((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED))
                Toast.makeText(this,"Please enable storage permissions to enable image loading!",Toast.LENGTH_SHORT).show();
        }
        //Conversely, if the user was attempting to save an image, then continue with this after verfiying permission was granted
        else if(requestCode == STORAGE_PERMISSION_REQUEST_WRITE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fView.saveToFile();
            else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this,"Please enable storage permissions to enable image saving!",Toast.LENGTH_SHORT).show();

        }
    }


    //Called when Brush button is pressed
    public void SetBrush(View view)
    {
        //Setup intent for brush settings activity and start it
        Intent brushScreen = new Intent(getApplicationContext(),BrushSelection.class);
        brushScreen.putExtra("Brush Width", fView.getBrushWidth());
        brushScreen.putExtra("Brush Type", fView.getBrush());
        startActivityForResult(brushScreen,BRUSH_REQUEST);
    }

    //Called when Colour button is pressed
    public void SetColour(View view)
    {
        //Setup intent for colour settings activity and start it
        Intent colScreen = new Intent(MainActivity.this,ColourSelection.class);
        colScreen.putExtra("Current Colour",fView.getColour());
        startActivityForResult(colScreen,COLOUR_REQUEST);
    }

    //Caled when the Select An Image button is called
    public void onClickImageSelect(View view)
    {
        //Check if storage permissions have been granted for the app
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            //If they have, start image picker activity
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
        //If not, request them
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST_READ);
    }

    //Called when the Save To Image button is pressed
    public void onClickSave(View view)
    {
        //Check if permissions have been granted. If so, tell the fingerPainter view to save the canvas to a png file
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            fView.saveToFile();
        //If not, request them
        else
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_REQUEST_WRITE);
    }

    //Called when the Clear button is pressed
    public void clearCanvas(View view)
    {
        //Present a dialog box to confirm clearing of the canvas before actually doing it to prevent accidental clearing
        AlertDialog.Builder confirmBox = new AlertDialog.Builder(this);
        confirmBox.setMessage("Erase canvas contents?");
        //If the user confirms their choice, then tell the fingerPainter view to clear its canvas and invalidate so that the view is refreshed making the change instantly visible
        confirmBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fView.clear();
                        fView.invalidate();
                    }
                });
        //Do nothing if user cancels
        confirmBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        confirmBox.show();
    }

    //Called by fingerPainter when it is touched to prevent bug discussed earlier. Somewhat inelegant soultion but only thing i can think of past compelete rewrite of fingerPainter underlying functionality
    public void refresh()
    {
        buttonPanel1.invalidate();
        buttonPanel2.invalidate();
    }
}
