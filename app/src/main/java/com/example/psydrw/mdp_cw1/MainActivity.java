package com.example.psydrw.mdp_cw1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FingerPainterView fView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   fView = (FingerPainterView)findViewById(R.id.fingerview);
    }


    public void SetBrush(View view) {
       // Intent brushScreen = new Intent(getApplicationContext(),BrushSelection.class);
        //   brushScreen.putExtra("Brush Type")




    }

    public void SetColour(View view) {
        Intent colScreen = new Intent(getApplicationContext(),ColourSelection.class);
        colScreen.putExtra("Colour Current",fView.getColour());

        startActivityForResult(colScreen,2); //Arb int

    }

}
//android:text="@string/btn_Example" /> string resource syntax