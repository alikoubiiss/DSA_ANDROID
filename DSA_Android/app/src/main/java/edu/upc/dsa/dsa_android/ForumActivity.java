package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ForumAdapter adapter;
    private List<ForumTopic> topicList = new ArrayList<>();
    private ProgressBar progressBar;
    private Button btnNewTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        recyclerView = findViewById(R.id.recyclerForum);
        progressBar = findViewById(R.id.progressBarForum);
        btnNewTopic  = findViewById(R.id.btnNewTopic);

        adapter = new ForumAdapter(topicList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnNewTopic.setOnClickListener(v -> showCreateTopicDialog());

        loadTopics();
    }

    private void loadTopics() {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi().getForumTopics().enqueue(new Callback<List<ForumTopic>>() {
            @Override
            public void onResponse(Call<List<ForumTopic>> call, Response<List<ForumTopic>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    topicList.clear();
                    topicList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumActivity.this, "Error al cargar temáticas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ForumTopic>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ForumActivity.this, "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCreateTopicDialog() {
        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "usuario");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_topic, null);
        EditText etTitle = dialogView.findViewById(R.id.etTopicTitle);
        EditText etDesc  = dialogView.findViewById(R.id.etTopicDesc);

        new AlertDialog.Builder(this)
                .setTitle("Nueva temática")
                .setView(dialogView)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String desc  = etDesc.getText().toString().trim();
                    if (title.isEmpty()) {
                        Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    createTopic(title, desc, username);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void createTopic(String title, String desc, String author) {
        CreateForumTopicRequest req = new CreateForumTopicRequest(title, desc, author);
        RetrofitClient.getInstance().getApi().createForumTopic(req).enqueue(new Callback<ForumTopic>() {
            @Override
            public void onResponse(Call<ForumTopic> call, Response<ForumTopic> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topicList.add(0, response.body());
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                    Toast.makeText(ForumActivity.this, "Temática creada ✓", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForumActivity.this, "Error al crear temática", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForumTopic> call, Throwable t) {
                Toast.makeText(ForumActivity.this, "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
