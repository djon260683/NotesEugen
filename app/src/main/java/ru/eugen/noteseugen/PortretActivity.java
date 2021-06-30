package ru.eugen.noteseugen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

public class PortretActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portret_activity);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }
        if(savedInstanceState == null) {
            FragmentNote details = new FragmentNote();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.container, details).commit();
        }
    }
}