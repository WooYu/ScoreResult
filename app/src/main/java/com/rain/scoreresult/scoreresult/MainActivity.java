package com.rain.scoreresult.scoreresult;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBarBlue;
    private ProgressBar progressBarRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
//        progressBarBlue = (ProgressBar) findViewById(R.id.progress_blue);
//        progressBarRed = (ProgressBar) findViewById(R.id.progress_red);
//
//        initScoreProgress();
    }


    private void initScoreProgress() {
        ObjectAnimator objectAnimator_red = ObjectAnimator.ofInt(progressBarRed, "progress", 0, 90);
        objectAnimator_red.setDuration(8000);
        objectAnimator_red.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator_red.start();

        ObjectAnimator objectAnimator_blue = ObjectAnimator.ofInt(progressBarBlue, "progress", 0, 70);
        objectAnimator_blue.setDuration(7000);
        objectAnimator_blue.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator_blue.start();

    }

    public void showLoginDialog() {
        ScoreResultFragment dialog = new ScoreResultFragment();
        dialog.show(getFragmentManager(), "scoreresult");
    }
}
