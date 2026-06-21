package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTeamActivity extends AppCompatActivity {

    private static final String TAG = "MyTeamActivity";
    private ApiService apiService;
    private MemberAdapter adapter;
    private TextView tvTeamTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        tvTeamTitle = findViewById(R.id.tvTeamTitle);
        Button btnVolver = findViewById(R.id.btnVolver);
        RecyclerView recyclerView = findViewById(R.id.recyclerMembers);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MemberAdapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);
        }

        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        apiService = RetrofitClient.getInstance().getApi();

        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty()) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            showNoTeamState("NO ESTAS EN NINGUN EQUIPO");
        } else {
            loadTeamInfo(username);
        }
    }

    private void loadTeamInfo(String username) {
        Call<TeamInfoResponse> call = apiService.getTeamMembership(username);
        call.enqueue(new Callback<TeamInfoResponse>() {
            @Override
            public void onResponse(Call<TeamInfoResponse> call, Response<TeamInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TeamInfoResponse teamInfo = response.body();

                    if (teamInfo.getTeam() == null || teamInfo.getTeam().trim().isEmpty()) {
                        showNoTeamState("NO ESTAS EN NINGUN EQUIPO");
                        return;
                    }

                    if (tvTeamTitle != null) {
                        tvTeamTitle.setText("EQUIPO: " + teamInfo.getTeam().toUpperCase());
                    }

                    if (teamInfo.getMembers() != null) {
                        adapter = new MemberAdapter(teamInfo.getMembers());
                        RecyclerView recyclerView = findViewById(R.id.recyclerMembers);
                        if (recyclerView != null) {
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } else if (response.code() == 404) {
                    showNoTeamState("NO ESTAS EN NINGUN EQUIPO");
                } else {
                    Log.e(TAG, "Error onResponse: " + response.code() + " " + response.message());
                    Toast.makeText(MyTeamActivity.this, "Error al cargar la informacion del equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamInfoResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(MyTeamActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showNoTeamState(String message) {
        if (tvTeamTitle != null) {
            tvTeamTitle.setText(message);
        }

        adapter = new MemberAdapter(new ArrayList<>());
        RecyclerView recyclerView = findViewById(R.id.recyclerMembers);
        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }
}
