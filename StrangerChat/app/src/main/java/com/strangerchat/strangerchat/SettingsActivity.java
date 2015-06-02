package com.strangerchat.strangerchat;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import Cache.Cache;


public class SettingsActivity extends ActionBarActivity {

    private SeekBar seekBar;
    private TextView textView;
    private SeekBar MinseekBar;
    private TextView MintextView;
    private SeekBar MaxseekBar;
    private TextView MaxtextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Radius
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView3);
        //Min age
        MinseekBar = (SeekBar) findViewById(R.id.MinSeekBar);
        MintextView = (TextView) findViewById(R.id.textMin);
        //Max age
        MaxseekBar = (SeekBar) findViewById(R.id.MaxSeekBar);
        MaxtextView = (TextView) findViewById(R.id.textMax);


        //Radius baren
        seekBar.setProgress((int) Cache.radius);
        textView.setText("Maximum distance: " + seekBar.getProgress() + "km");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = (int) Cache.radius;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView.setText("Maximum distance: " + seekBar.getProgress() + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Cache.radius = progress;
            }

        });

        //min baren
        MinseekBar.setProgress((int) Cache.minAge);
        MintextView.setText("Minimum age: " + MinseekBar.getProgress() + " years");
        MinseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = (int) Cache.minAge;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                MintextView.setText("Minimum age: " + MinseekBar.getProgress() + " years");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Cache.maxAge < progress) {
                    idiot();
                    Cache.minAge = 10;
                }
                Log.d("Set", "min " + Cache.maxAge + " progress = " + progress);
                Cache.minAge = progress;
            }

        });


        //max baren
        MaxseekBar.setProgress((int) Cache.radius);
        MaxtextView.setText("Maximum age: " + MaxseekBar.getProgress() + "years");
        MaxseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = (int) Cache.radius;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                MaxtextView.setText("Maximum age: " + seekBar.getProgress() + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Cache.minAge > progress){


                    idiot();
                    Cache.maxAge = 100;
                }
                Log.d("Set", "min " + Cache.minAge + " progress = " + progress);
                Cache.maxAge = progress;
            }

        });
    }


    public void onGenderClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBoth:
                if (checked)
                    Log.d("set", "both");
                    Cache.desiredSex = "Both";
                    break;
            case R.id.radioMale:
                if (checked)
                    Cache.desiredSex = "Male";
                Log.d("set", "male");
                    break;
            case R.id.radioFemale:
                if (checked)
                    Log.d("set", "Female");
                    Cache.desiredSex = "Female";
                    break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void idiot(){
       Toast.makeText(this, "Idiot", Toast.LENGTH_SHORT).show();
    }
}
