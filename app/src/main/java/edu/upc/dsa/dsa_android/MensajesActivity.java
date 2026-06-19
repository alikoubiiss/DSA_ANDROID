package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MensajesActivity extends AppCompatActivity {

    private int topicId;
    private String topicTitle;
    private String username;

    private RecyclerView recyclerView;
    private MensajesAdapter adapter;
    private List<ForumMessage> messageList = new ArrayList<>();

    private EditText etInput;
    private ImageButton btnSend;
    private ProgressBar progressBar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        topicId = getIntent().getIntExtra("topicId", -1);
        topicTitle = getIntent().getStringExtra("topicTitle");

        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        username = prefs.getString("username", "usuario");

        tvTitle = findViewById(R.id.tvTopicTitleHeader);
        if (topicTitle != null) {
            tvTitle.setText(topicTitle);
        }

        recyclerView = findViewById(R.id.recyclerMessages);
        progressBar = findViewById(R.id.progressBarMessages);
        etInput = findViewById(R.id.etMessageInput);
        btnSend = findViewById(R.id.btnSendMessage);

        adapter = new MensajesAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());

        findViewById(R.id.btnBackMessages).setOnClickListener(v -> finish());

        loadMessages();
    }

    private void loadMessages() {
        if (topicId == -1) return;

        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApi().getForumMessages(topicId).enqueue(new Callback<List<ForumMessage>>() {
            @Override
            public void onResponse(Call<List<ForumMessage>> call, Response<List<ForumMessage>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (messageList.size() > 0) {
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                } else {
                    Toast.makeText(MensajesActivity.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ForumMessage>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MensajesActivity.this, "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = etInput.getText().toString().trim();
        if (text.isEmpty()) return;

        CreateForumMessageRequest req = new CreateForumMessageRequest(username, text);
        btnSend.setEnabled(false);
        RetrofitClient.getInstance().getApi().createForumMessage(topicId, req).enqueue(new Callback<ForumMessage>() {
            @Override
            public void onResponse(Call<ForumMessage> call, Response<ForumMessage> response) {
                btnSend.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    etInput.setText("");
                    messageList.add(response.body());
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(MensajesActivity.this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForumMessage> call, Throwable t) {
                btnSend.setEnabled(true);
                Toast.makeText(MensajesActivity.this, "Sin conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
