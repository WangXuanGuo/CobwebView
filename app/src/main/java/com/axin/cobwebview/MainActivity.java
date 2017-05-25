package com.axin.cobwebview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cobwebview.axin.cobwebview.CobwebView;

public class MainActivity extends AppCompatActivity {

    private CobwebView mPathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPathView = (CobwebView) findViewById(R.id.path_view);
        mPathView.setMaxValue(100)
                .setPolygonNumber(6)
                .setGriddingNumber(3)
                .setValues(new Integer[]{20, 30, 50, 40, 10, 80})
                .setDesignation(new String[]{"一", "二", "三", "四", "五", "六"})
                .invalidateView();
    }
}
