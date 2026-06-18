package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEvents;
    private ProgressBar progressBarEventos;
    private TextView textViewEventosError;
    private Button btnVolver;

    private EventoAdapter adapter;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        progressBarEventos = findViewById(R.id.progressBarEventos);
        textViewEventosError = findViewById(R.id.textViewEventosError);
        btnVolver = findViewById(R.id.btnVolver);

        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        apiService = RetrofitClient.getInstance().getApi();

        adapter = new EventoAdapter(this, new ArrayList<>(), this::registerToEvent, this::loadEventUsers);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEvents.setAdapter(adapter);

        btnVolver.setOnClickListener(v -> finish());

        loadEvents();
    }

    private void loadEvents() {
        showLoading();

        Call<List<Evento>> call = apiService.getEvents();

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();

                    if (eventos.isEmpty()) {
                        showError("No hay eventos disponibles");
                        return;
                    }

                    textViewEventosError.setVisibility(View.GONE);
                    recyclerViewEvents.setVisibility(View.VISIBLE);
                    adapter.setEventos(eventos);

                } else {
                    Log.e("EventosActivity", "Error getEvents: " + response.code());
                    showError("Error cargando eventos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                hideLoading();
                Log.e("EventosActivity", "onFailure getEvents", t);
                showError("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    private void registerToEvent(Evento evento) {
        int userId = sharedPreferences.getInt("userId", -1);
        String username = sharedPreferences.getString("username", "unknown");

        if (userId == -1) {
            Toast.makeText(this,
                    "Error: usuario no identificado",
                    Toast.LENGTH_LONG).show();
            return;
        }

        RegistroEventoRequest request = new RegistroEventoRequest(userId, username);

        Call<ResponseBody> call = apiService.registerToEvent(evento.getId(), request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EventosActivity.this,
                            "Inscripción realizada en " + evento.getName(),
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 409) {
                    Toast.makeText(EventosActivity.this,
                            "Ya estás inscrito en este evento",
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 404) {
                    Toast.makeText(EventosActivity.this,
                            "Usuario o evento no encontrado",
                            Toast.LENGTH_LONG).show();

                } else if (response.code() == 400) {
                    Toast.makeText(EventosActivity.this,
                            "Datos de inscripción inválidos",
                            Toast.LENGTH_LONG).show();

                } else {
                    Log.e("EventosActivity", "Error registerToEvent: " + response.code());
                    Toast.makeText(EventosActivity.this,
                            "No se pudo realizar la inscripción",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("EventosActivity", "onFailure registerToEvent", t);
                Toast.makeText(EventosActivity.this,
                        "Fallo al inscribirse: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadEventUsers(Evento evento) {
        adapter.setExpandedEvent(evento.getId());
        adapter.setLoadingUsers(evento.getId(), true);

        Call<List<UserEvent>> call = apiService.getEventUsers(evento.getId());

        call.enqueue(new Callback<List<UserEvent>>() {
            @Override
            public void onResponse(Call<List<UserEvent>> call, Response<List<UserEvent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setEventUsers(evento.getId(), response.body());
                } else if (response.code() == 404) {
                    adapter.setLoadingUsers(evento.getId(), false);
                    adapter.collapseExpandedEvent();
                    Toast.makeText(EventosActivity.this,
                            "Evento no encontrado",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.e("EventosActivity", "Error getEventUsers: " + response.code());
                    adapter.setLoadingUsers(evento.getId(), false);
                    adapter.collapseExpandedEvent();
                    Toast.makeText(EventosActivity.this,
                            "Error cargando inscritos: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserEvent>> call, Throwable t) {
                Log.e("EventosActivity", "onFailure getEventUsers", t);
                adapter.setLoadingUsers(evento.getId(), false);
                adapter.collapseExpandedEvent();
                Toast.makeText(EventosActivity.this,
                        "Fallo al cargar inscritos: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading() {
        progressBarEventos.setVisibility(View.VISIBLE);
        textViewEventosError.setVisibility(View.GONE);
        recyclerViewEvents.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBarEventos.setVisibility(View.GONE);
    }

    private void showError(String message) {
        recyclerViewEvents.setVisibility(View.GONE);
        textViewEventosError.setText(message);
        textViewEventosError.setVisibility(View.VISIBLE);
    }
}