package edu.upc.dsa.dsa_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa.dsa_android.Faq.FaqAssistantActivity;

public class ExtrasActivity extends AppCompatActivity {

    Button EquiposButton;
    Button EventosButton;
    Button Participantesbutton;
    Button MyTeamButton;
    Button FaqButton;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);

        EquiposButton = findViewById(R.id.EquiposButton);
        EventosButton = findViewById(R.id.EventosButton);
        Participantesbutton = findViewById(R.id.Participantesbutton);
        MyTeamButton = findViewById(R.id.MyTeamButton);
        FaqButton = findViewById(R.id.FaqButton);
        btnVolver = findViewById(R.id.btnVolver);

        EquiposButton.setOnClickListener(v ->
                startActivity(new Intent(ExtrasActivity.this, RankingActivity.class))
        );

        EventosButton.setOnClickListener(v ->
                startActivity(new Intent(ExtrasActivity.this, EventosActivity.class))
        );

        Participantesbutton.setOnClickListener(v ->
                startActivity(new Intent(ExtrasActivity.this, EventUsersActivity.class))
        );

        MyTeamButton.setOnClickListener(v ->
                startActivity(new Intent(ExtrasActivity.this, MyTeamActivity.class))
        );

        FaqButton.setOnClickListener(v ->
                startActivity(new Intent(ExtrasActivity.this, FaqAssistantActivity.class))
        );

        btnVolver.setOnClickListener(v -> {
            finish();
        });
    }
}