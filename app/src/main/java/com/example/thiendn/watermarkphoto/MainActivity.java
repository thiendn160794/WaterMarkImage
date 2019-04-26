package com.example.thiendn.watermarkphoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fr_main, SelectImageFragment.newInstance(), SelectImageFragment.TAG)
                .addToBackStack(SelectImageFragment.TAG)
                .commit();
    }
}
