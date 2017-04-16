package com.example.kamhi.ex12;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements MyDialog.ResultsListener, SharedPreferences.OnSharedPreferenceChangeListener {

    public static int beginNumber = 10;
    public static int counter;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    ImageView egg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){ // toast name
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            String user = sp.getString("userName", "guest");
            Toast.makeText(this, "Hello " + user, Toast.LENGTH_LONG).show();
            String num = sp.getString("beginNumber", "10");
            beginNumber = Integer.parseInt(num);
            counter = beginNumber;
        }
        //orentation
        else{
            counter = savedInstanceState.getInt("counter");
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("userName")) {
                }
                if(key.equals("beginNumber")) {
                    String num = prefs.getString("beginNumber",String.valueOf(beginNumber));
                    beginNumber = Integer.parseInt(num);
                }

            }
        };

            prefs.registerOnSharedPreferenceChangeListener(listener);

        final TextView counterTV = (TextView) findViewById(R.id.counter);
        //set text view
        if(counter <= 0){
            counterTV.setText("You won");
            ImageView photo = (ImageView)findViewById(R.id.egg);
            photo.setImageResource(R.drawable.chicken);
        }
        else {
            counterTV.setText(Integer.toString(counter));
        }

        egg = (ImageView) findViewById(R.id.egg);

        Button reset = (Button) findViewById(R.id.reset);
        //reset counter
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = beginNumber;
                counterTV.setText(Integer.toString(counter));
                ImageView photo = (ImageView)findViewById(R.id.egg);
                photo.setImageResource(R.drawable.egg);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                getFragmentManager().beginTransaction().add(android.R.id.content, new MyPrefernces()).addToBackStack(null).commit();
                return true;
            case R.id.action_exit:
                MyDialog.newInstance(MyDialog.EXIT).show(getFragmentManager(), "exit dialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void lockOrientation(View v){
        if(((CheckBox)v).isChecked()){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }
        else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }*/


    public void PhotoClicked(View v){
        //shake animation
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        egg.startAnimation(shake);
        //phone vibrate
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        // counter down
        counter--;
        TextView counterTV = (TextView) findViewById(R.id.counter);
        if(counter > 0){
            counterTV.setText(Integer.toString(counter));
            if(counter == (beginNumber/4)*3){
                //ImageView photo = (ImageView)findViewById(R.id.egg);
                egg.setImageResource(R.drawable.egg2);
                egg.startAnimation(shake);
            }
            if(counter == (beginNumber/4)*2){
                //ImageView photo = (ImageView)findViewById(R.id.egg);
                egg.setImageResource(R.drawable.egg3);
                egg.startAnimation(shake);
            }
            if(counter == beginNumber/4){
                //ImageView photo = (ImageView)findViewById(R.id.egg);
                egg.setImageResource(R.drawable.egg4);
                egg.startAnimation(shake);
            }

        }
        else{
            counterTV.setText("You won");
            ImageView photo = (ImageView)findViewById(R.id.egg);
            photo.setImageResource(R.drawable.chicken);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void OnfinishDialog(int requestCode, Object result) { // exit dialog
        switch(requestCode)
        {
            case MyDialog.EXIT: {
                finish();
                break;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case "orientation":
                if(sharedPreferences.getBoolean("orientation", false)){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                }
                else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("orientation", false)){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        super.onDestroy();
    }

    public static class MyPrefernces extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        // shared preferences
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
        //give color to the preference
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundColor(getResources().getColor(android.R.color.white));
            return view;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key){
                case "orientation":
                    if(sharedPreferences.getBoolean("orientation", false)){
             //           setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                    }
                    else{
               //         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    break;

            }
        }
    }
}
