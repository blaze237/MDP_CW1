package com.example.psydrw.mdp_cw1;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class BrushSelection extends AppCompatActivity
{

    private SeekBar widthBar;
    private ToggleButton roundToggle;
    private ToggleButton squareToggle;
    
    private int width;
    private Paint.Cap type;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_selection);

        //Init brush width seek bar
        widthBar = (SeekBar) findViewById(R.id.widthBar);
        width =  getIntent().getIntExtra("Brush Width",20);
        widthBar.setProgress(width);

        widthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                width = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        //Init type buttons
        type = (Paint.Cap) getIntent().getSerializableExtra("Brush Type");

        roundToggle = (ToggleButton) findViewById(R.id.roundToggle);
        squareToggle = (ToggleButton) findViewById(R.id.squareToggle);


        if(type == Paint.Cap.ROUND)
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

    public void onRoundButtonClick(View view) {
        type = Paint.Cap.ROUND;
    }

    public void onSquareButtonPress(View view) {
        type = Paint.Cap.SQUARE;
    }

    public void onRoundToggleClick(View view)
    {
        if(!roundToggle.isChecked())
        {
            squareToggle.setChecked(true);
            squareToggle.setSelected(true);

            type = Paint.Cap.SQUARE;
        }
        else
        {
            squareToggle.setChecked(false);
            squareToggle.setSelected(false);

            type = Paint.Cap.ROUND;
        }

    }

    public void onSquareToggleClick(View view)
    {
        if(!squareToggle.isChecked())
        {
            roundToggle.setChecked(true);
            roundToggle.setSelected(true);

            type = Paint.Cap.ROUND;
        }
        else
        {
            roundToggle.setChecked(false);
            roundToggle.setSelected(false);

            type = Paint.Cap.SQUARE;
        }


    }

    public void onButtToggleClick(View view)
    {

    }

    public void returnMain(View view)
    {
        Intent result = new Intent();
        result.putExtra("New Width",width);
        result.putExtra("New Type",type);
        setResult(Activity.RESULT_OK, result);
        finish();
    }


}
