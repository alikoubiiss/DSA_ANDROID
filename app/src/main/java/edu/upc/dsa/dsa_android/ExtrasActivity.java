package edu.upc.dsa.dsa_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.animation.DecelerateInterpolator;

import edu.upc.dsa.dsa_android.Faq.FaqAssistantActivity;

public class ExtrasActivity extends AppCompatActivity {

    Button EquiposButton;
    Button EventosButton;
    Button MyTeamButton;
    Button FaqButton;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);

        EquiposButton = findViewById(R.id.EquiposButton);
        EventosButton = findViewById(R.id.EventosButton);
        MyTeamButton = findViewById(R.id.MyTeamButton);
        FaqButton = findViewById(R.id.FaqButton);
        btnVolver = findViewById(R.id.btnVolver);

        EquiposButton.setOnClickListener(v ->
                runLoadingAnimation(() -> startActivity(new Intent(ExtrasActivity.this, RankingActivity.class)))
        );

        EventosButton.setOnClickListener(v ->
                runLoadingAnimation(() -> startActivity(new Intent(ExtrasActivity.this, EventosActivity.class)))
        );

        MyTeamButton.setOnClickListener(v ->
                runLoadingAnimation(() -> startActivity(new Intent(ExtrasActivity.this, MyTeamActivity.class)))
        );

        FaqButton.setOnClickListener(v ->
                runLoadingAnimation(() -> startActivity(new Intent(ExtrasActivity.this, FaqAssistantActivity.class)))
        );

        btnVolver.setOnClickListener(v -> runLoadingAnimation(this::finish));
    }

    private void runLoadingAnimation(Runnable onCompleteAction) {
        android.view.View loadingOverlay = findViewById(R.id.loadingOverlay);
        ProgressBar progressBar = findViewById(R.id.horizontalProgressBar);

        if (loadingOverlay != null && progressBar != null) {
            progressBar.setProgress(0);
            loadingOverlay.setVisibility(android.view.View.VISIBLE);

            ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
            animator.setDuration(1200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator2) {
                    onCompleteAction.run();
                    loadingOverlay.setVisibility(android.view.View.GONE);
                }
            });
            animator.start();
        } else {
            onCompleteAction.run();
        }
    }
}
