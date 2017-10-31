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

    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;
    private View colourPreview;

    private int r,g,b;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_selection);

        redBar =(SeekBar) findViewById(R.id.redBar);
        greenBar =(SeekBar) findViewById(R.id.greenBar);
        blueBar =(SeekBar) findViewById(R.id.blueBar);


        int colour =  getIntent().getIntExtra("Current Colour",0xff000000);

        //Extract rgb vals from colour
        r = (colour >> 16) & 0xff;
        g = (colour >> 8) & 0xff;
        b = colour & 0xff;

        colourPreview = (View)findViewById(R.id.colourPreview);
        colourPreview.setBackgroundColor(Color.argb(255, r, g, b));


        //Init scroll bars
        redBar.setProgress(r);
        greenBar.setProgress(g);
        blueBar.setProgress(b);

        //Set up anonymous listener to grab data from the colour sliders
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

    @Override
    public void onBackPressed()
    {
        Intent result = new Intent();

        int colour = Color.argb(255, r, g, b);

        result.putExtra("New Colour",colour);
        setResult(Activity.RESULT_OK, result);
        finish();
    }



    public void returnMain(View view)
    {
        Intent result = new Intent();

        int colour = Color.argb(255, r, g, b);

        result.putExtra("New Colour",colour);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
