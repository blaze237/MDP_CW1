package com.example.psydrw.mdp_cw1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final int COLOUR_REQUEST = 1;
    static final int BRUSH_REQUEST = 2;

    private FingerPainterView fView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fView = (FingerPainterView)findViewById(R.id.fingerview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            //Set new brush colour
            if (requestCode == COLOUR_REQUEST)
            {
                int col = data.getIntExtra("New Colour",0xff000000);
                fView.setColour(col);
            }
        }
    }


    public void SetBrush(View view) {
       // Intent brushScreen = new Intent(getApplicationContext(),BrushSelection.class);
        //   brushScreen.putExtra("Brush Type")




    }

    public void SetColour(View view)
    {
        Intent colScreen = new Intent(MainActivity.this,ColourSelection.class);
        colScreen.putExtra("Current Colour",fView.getColour());

        startActivityForResult(colScreen,COLOUR_REQUEST); //Arb int
    }

}
