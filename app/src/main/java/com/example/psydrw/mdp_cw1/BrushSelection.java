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
    private int brushWidth;
    private Paint.Cap brushType;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_selection);



        //Restore data
        if(savedInstanceState != null)
        {
            brushType =(Paint.Cap) savedInstanceState.getSerializable("Brush Type");
            brushWidth = savedInstanceState.getInt("Brush Width");
        }
        else
        {
            brushWidth =  getIntent().getIntExtra("Brush Width",20);
            brushType = (Paint.Cap) getIntent().getSerializableExtra("Brush Type");
        }

        //Init brush brushWidth seek bar
        widthBar = (SeekBar) findViewById(R.id.widthBar);
        widthBar.setProgress(brushWidth);

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

    //Save state on rotation etc
    @Override
    protected void onSaveInstanceState(Bundle state) 
    {
        super.onSaveInstanceState(state);
        state.putSerializable("Brush Type" , brushType);
        state.putInt("Brush Width" , brushWidth);
    }

    public void onRoundToggleClick(View view)
    {
        if(!roundToggle.isChecked())
        {
            squareToggle.setChecked(true);
            squareToggle.setSelected(true);

            brushType = Paint.Cap.SQUARE;
        }
        else
        {
            squareToggle.setChecked(false);
            squareToggle.setSelected(false);

            brushType = Paint.Cap.ROUND;
        }

    }

    public void onSquareToggleClick(View view)
    {
        if(!squareToggle.isChecked())
        {
            roundToggle.setChecked(true);
            roundToggle.setSelected(true);

            brushType = Paint.Cap.ROUND;
        }
        else
        {
            roundToggle.setChecked(false);
            roundToggle.setSelected(false);

            brushType = Paint.Cap.SQUARE;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent result = new Intent();
        result.putExtra("New Width",brushWidth);
        result.putExtra("New Type",brushType);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public void returnMain(View view)
    {
        Intent result = new Intent();
        result.putExtra("New Width",brushWidth);
        result.putExtra("New Type",brushType);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
