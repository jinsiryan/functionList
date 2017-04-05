package com.functionlist.drawable;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.functionlist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawableActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        ButterKnife.bind(this);
        textView.setBackground(new StrDrawable(Color.BLUE,"夏天"));
    }
}
