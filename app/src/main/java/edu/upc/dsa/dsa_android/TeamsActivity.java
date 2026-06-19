package edu.upc.dsa.dsa_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnVolver;
    private ApiService apiService;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        TextView tvTitulo = findViewById(R.id.tvTitulo);
        if (tvTitulo != null) {
            tvTitulo.setText("EQUIPOS");
        }

        recyclerView = findViewById(R.id.recycler);
        btnVolver = findViewById(R.id.btnVolver);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> runLoadingAnimation(this::finish));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        apiService = RetrofitClient.getInstance().getApi();
        loadRanking();
    }

    public void loadRanking() {
        apiService.getTeamMembership(username).enqueue(new Callback<TeamInfoResponse>() {
            @Override
            public void onResponse(Call<TeamInfoResponse> call, Response<TeamInfoResponse> response) {
                String myTeamName = "";
                if (response.isSuccessful() && response.body() != null) {
                    myTeamName = response.body().getTeam();
                }
                loadTeams(myTeamName);
            }

            @Override
            public void onFailure(Call<TeamInfoResponse> call, Throwable t) {
                Log.e("TeamsActivity", "Error cargando equipo del usuario", t);
                loadTeams("");
            }
        });
    }

    private void loadTeams(String myTeamName) {
        apiService.getTeamsRanking().enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TeamAdapter adapter = new TeamAdapter(response.body(), TeamsActivity.this, username, myTeamName);
                    if (recyclerView != null) {
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(TeamsActivity.this, "Error cargando equipos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                Log.e("TeamsActivity", "Fallo de conexion", t);
                Toast.makeText(TeamsActivity.this, "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
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
