package edu.upc.dsa.dsa_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventUsersActivity extends AppCompatActivity {

    private LinearLayout contenedorUsuarios;
    private ApiService apiService;
    private int eventId;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_users);

        eventId = getIntent().getIntExtra("eventId", -1);
        eventName = getIntent().getStringExtra("eventName");
        contenedorUsuarios = findViewById(R.id.contenedorUsuarios);
        apiService = RetrofitClient.getInstance().getApi();

        Button btnVolver = findViewById(R.id.btnVolver);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        if (eventId == -1) {
            Toast.makeText(this, "Evento no valido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadEventUsers();
    }

    private void loadEventUsers() {
        contenedorUsuarios.removeAllViews();
        addMessage("Cargando inscritos" + (eventName != null ? " de " + eventName : "") + "...");

        apiService.getEventUsers(eventId).enqueue(new Callback<List<UserEvent>>() {
            @Override
            public void onResponse(Call<List<UserEvent>> call, Response<List<UserEvent>> response) {
                contenedorUsuarios.removeAllViews();

                if (response.isSuccessful() && response.body() != null) {
                    List<UserEvent> users = response.body();
                    if (users.isEmpty()) {
                        addMessage("No hay usuarios inscritos en este evento");
                        return;
                    }

                    for (UserEvent user : users) {
                        addUserRow(user);
                    }
                } else if (response.code() == 404) {
                    addMessage("Evento no encontrado");
                } else {
                    addMessage("Error cargando inscritos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserEvent>> call, Throwable t) {
                contenedorUsuarios.removeAllViews();
                addMessage("Fallo de conexion: " + t.getMessage());
            }
        });
    }

    private void addUserRow(UserEvent user) {
        LinearLayout row = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.row_event_user, contenedorUsuarios, false);

        ImageView avatar = row.findViewById(R.id.ivEventUserAvatar);
        TextView name = row.findViewById(R.id.tvEventUserName);
        TextView surname = row.findViewById(R.id.tvEventUserSurname);

        name.setText(user.getNombre() != null ? user.getNombre() : "");
        surname.setText(user.getApellidos() != null ? user.getApellidos() : "");

        String imageUrl = user.getImagen();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.combined_logo)
                    .error(R.drawable.combined_logo)
                    .into(avatar);
        } else {
            avatar.setImageResource(R.drawable.combined_logo);
        }

        contenedorUsuarios.addView(row);
    }

    private void addMessage(String message) {
        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(16);
        tv.setPadding(12, 20, 12, 20);
        contenedorUsuarios.addView(tv);
    }
}
