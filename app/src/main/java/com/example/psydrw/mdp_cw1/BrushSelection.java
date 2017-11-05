package com.example.psydrw.mdp_cw1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class BrushSelection extends AppCompatActivity
{
    //References to GUI elements
    private SeekBar widthBar;
    private ToggleButton roundToggle;
    private ToggleButton squareToggle;

    //Store current brush settings
    private int brushWidth;
    private Paint.Cap brushType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_selection);

        //Restore data on activity resume
        if(savedInstanceState != null)
        {
            brushType =(Paint.Cap) savedInstanceState.getSerializable("Brush Type");
            brushWidth = savedInstanceState.getInt("Brush Width");
        }
        else //Or if this is the first time the activity is being run, then grab the brush settings from the intent
        {
            brushWidth =  getIntent().getIntExtra("Brush Width",20);
            brushType = (Paint.Cap) getIntent().getSerializableExtra("Brush Type");
        }

       initGUI();
    }

    //Called from onCreate, initialises all GUI elements
    private void initGUI()
    {
        //Init brush brushWidth seek bar
        widthBar = (SeekBar) findViewById(R.id.widthBar);
        widthBar.setProgress(brushWidth);

        //Setup seekbar behaviour to automaticly update stored value for brush width to be equal to seekbar value
        widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brushWidth = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        //Init brushType buttons
        roundToggle = (ToggleButton) findViewById(R.id.roundToggle);
        squareToggle = (ToggleButton) findViewById(R.id.squareToggle);

        //Initialise the brush type toggles based on the current brush type
        if(brushType == Paint.Cap.ROUND)
        {
            roundToggle.setChecked(true);
            roundToggle.setSelected(true);
        }
        else
        {
            squareToggle.setChecked(true);
            squareToggle.setSelected(true);
        }
    }

    //Save state on activity suspension
    @Override
    protected void onSaveInstanceState(Bundle state) 
    {
        super.onSaveInstanceState(state);
        state.putSerializable("Brush Type" , brushType);
        state.putInt("Brush Width" , brushWidth);
    }

    //Called when the Round toggle button is pressed
    public void onRoundToggleClick(View view)
    {
        //If the toggle has been set to false, then this means we need to set the square toggle to be true now and update the brush type acordingly
        if(!roundToggle.isChecked())
        {
            squareToggle.setChecked(true);
            squareToggle.setSelected(true);
            brushType = Paint.Cap.SQUARE;
        }
        //Same process for the inverse
        else
        {
            squareToggle.setChecked(false);
            squareToggle.setSelected(false);
            brushType = Paint.Cap.ROUND;
        }

    }
    //Called when the Square toggle button is pressed
    public void onSquareToggleClick(View view)
    {
        //If the toggle has been set to false, then this means we need to set the rounnd toggle to be true now and update the brush type acordingly
        if(!squareToggle.isChecked())
        {
            roundToggle.setChecked(true);
            roundToggle.setSelected(true);
            brushType = Paint.Cap.ROUND;
        }
        //Same process for the inverse
        else
        {
            roundToggle.setChecked(false);
            roundToggle.setSelected(false);
            brushType = Paint.Cap.SQUARE;
        }
    }



    //Called when user presses back button on the decvice
    @Override
    public void onBackPressed()
    {
        //Package the current brush settings into an intent and pass it back to main activity
        Intent result = new Intent();
        result.putExtra("New Width",brushWidth);
        result.putExtra("New Type",brushType);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    //Called when Done button pressed,
    public void returnMain(View view)
    {
        //Package the current brush settings into an intent and pass it back to main activity
        Intent result = new Intent();
        result.putExtra("New Width",brushWidth);
        result.putExtra("New Type",brushType);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
