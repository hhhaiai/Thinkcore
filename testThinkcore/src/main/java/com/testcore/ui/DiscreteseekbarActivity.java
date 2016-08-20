package com.testcore.ui;

import android.app.Activity;
import android.os.Bundle;

import com.testcore.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class DiscreteseekbarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discreteseekbar_activity_main);
        DiscreteSeekBar discreteSeekBar1 = (DiscreteSeekBar) findViewById(R.id.discrete1);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value * 100;
            }
        });
    }
}
