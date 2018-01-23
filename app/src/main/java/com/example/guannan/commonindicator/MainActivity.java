package com.example.guannan.commonindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] texts = {"詹姆斯", "韦德", "史密斯", "汤普森","勒夫","托马斯","欧文"};
        CommonIndicator indicator = (CommonIndicator) findViewById(R.id.indicator);
        indicator.setTabTitles(Arrays.asList(texts));
    }
}
