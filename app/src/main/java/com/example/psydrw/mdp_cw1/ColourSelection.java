package com.example.psydrw.mdp_cw1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class ColourSelection extends AppCompatActivity {

    //Store references to GUI elements
    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;
    private View colourPreview;

    //Store current colour setting as individual colour channels
    private int r,g,b;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_selection);

        //Restore state after app when it is resumed
        //Not technically needed in this case as the rgb vals are reconstructed by the seek bars on progress methods once they are rebuilt but included for completeness.
        if(savedInstanceState != null)
        {
            r = savedInstanceState.getInt("Red");
            g = savedInstanceState.getInt("Green");
            b = savedInstanceState.getInt("Blue");
        }
        else //If not restoring and this is first run, instead init values from intent
        {
            int colour = getIntent().getIntExtra("Current Colour", 0xff000000);

            //Extract rgb vals from colour
            r = (colour >> 16) & 0xff;
            g = (colour >> 8) & 0xff;
            b = colour & 0xff;
        }
        initGUI();
    }

    //Called from onCreate, initialises all GUI elements
    private void initGUI()
    {
        //Init seekbar references
        redBar =(SeekBar) findViewById(R.id.redBar);
        greenBar =(SeekBar) findViewById(R.id.greenBar);
        blueBar =(SeekBar) findViewById(R.id.blueBar);

        //Init colour preview box
        colourPreview = (View)findViewById(R.id.colourPreview);
        colourPreview.setBackgroundColor(Color.argb(255, r, g, b));

        //Init scroll bars
        redBar.setProgress(r);
        greenBar.setProgress(g);
        blueBar.setProgress(b);

        //Setup seekbar behaviour to automatically update stored values for colour channles to be equal to corresponding seekbar value
        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                colourPreview.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                colourPreview.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                colourPreview.setBackgroundColor(Color.argb(255, r, g, b));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });
    }

    //Save state activity suspension
    @Override
    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        state.putInt("Red",r);
        state.putInt("Green",g);
        state.putInt("Blue",b);
    }

    //Called when user presses back button on device
    @Override
    public void onBackPressed()
    {
        //Save the current colour channel values into a single int and package them into the intent
        Intent result = new Intent();
        result.putExtra("New Colour",Color.argb(255, r, g, b));
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    //Called when user presses Done button
    public void returnMain(View view)
    {
        //Save the current colour channel values into a single int and package them into the intent
        Intent result = new Intent();
        result.putExtra("New Colour",Color.argb(255, r, g, b));
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
