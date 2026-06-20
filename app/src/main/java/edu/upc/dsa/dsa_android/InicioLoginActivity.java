package edu.upc.dsa.dsa_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.content.Context;

import android.widget.Toast;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.view.animation.DecelerateInterpolator;

public class InicioLoginActivity extends AppCompatActivity {

    Button buttonTienda;
    TextView buttonLogOut;
    Button buttonJugar;
    Button buttonInventario;
    Button buttonJugadores;
    Button buttonExtras;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonTienda = findViewById(R.id.buttonTienda);
        buttonJugar = findViewById(R.id.buttonJugar);
        buttonInventario = findViewById(R.id.buttonInventario);
        buttonJugadores = findViewById(R.id.buttonJugadores);
        buttonExtras = findViewById(R.id.buttonExtras);

        buttonLogOut.setOnClickListener(v -> {
            runLoadingAnimation(() -> {
                SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(InicioLoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        });

        buttonTienda.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, TiendaActivity.class);
            runLoadingAnimation(() -> startActivity(intent));
        });

        buttonInventario.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, InventarioActivity.class);
            runLoadingAnimation(() -> startActivity(intent));
        });

        buttonJugadores.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, RankingActivity.class);
            runLoadingAnimation(() -> startActivity(intent));
        });

        buttonExtras.setOnClickListener(v -> {
            Intent intent = new Intent(InicioLoginActivity.this, ExtrasActivity.class);
            runLoadingAnimation(() -> startActivity(intent));
        });

        // ── Botón JUGAR → abre el juego Unity embebido ────────────────────────
        buttonJugar.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            if (!username.isEmpty()) {
                runLoadingAnimation(() -> {
                    Intent intent = new Intent(InicioLoginActivity.this, UnityGameActivity.class);
                    startActivityForResult(intent, UnityGameActivity.REQUEST_CODE);
                });
            } else {
                Toast.makeText(this, "Error: No hay usuario logueado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── Resultado al volver del juego Unity ───────────────────────────────────
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UnityGameActivity.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int coinsEarned = data.getIntExtra("coinsEarned", 0);
            if (coinsEarned > 0) {
                Toast.makeText(this,
                    "¡Has ganado " + coinsEarned + " monedas!", Toast.LENGTH_LONG).show();
            }
        }
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
                public void onAnimationEnd(Animator animation) {
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

