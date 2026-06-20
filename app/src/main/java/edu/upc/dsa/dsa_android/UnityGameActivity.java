package edu.upc.dsa.dsa_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerGameActivity;

import edu.upc.dsa.dsa_android.network.ApiService;
import edu.upc.dsa.dsa_android.network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UnityGameActivity — puente entre la app Android y el juego Unity.
 *
 * Flujo:
 *   1. InicioLoginActivity lanza esta Activity con startActivityForResult()
 *   2. Esta Activity arranca Unity con el username del jugador
 *   3. Cuando Unity termina, llama a onGameFinished() o onCoinsEarned()
 *   4. Los datos se guardan en el backend y se devuelve al menú
 */
public class UnityGameActivity extends UnityPlayerGameActivity {

    private static final String TAG = "UnityGameActivity";
    public static final int REQUEST_CODE = 1001;

    private ApiService apiService;
    private int userId;
    private String username;

    // ── Ciclo de vida ──────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getInstance().getApi();

        // Leer sesión guardada
        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        username = prefs.getString("username", "");
        userId   = prefs.getInt("userId", -1);

        Log.d(TAG, "Juego iniciado — usuario: " + username + " (id=" + userId + ")");

        // Enviar el username al juego una vez que Unity haya cargado.
        // Usamos un pequeño delay para asegurar que la escena está lista.
        if (!username.isEmpty()) {
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                UnityPlayer.UnitySendMessage(
                    "AndroidBridge",   // Nombre del GameObject en Unity
                    "SetUserName",     // Método C# del script en ese GameObject
                    username
                ), 2000 // 2 segundos de margen para que cargue la escena
            );
        }
    }

    // ── Métodos llamados DESDE Unity ──────────────────────────────────────────

    /**
     * Unity llama a este método cuando el jugador gana monedas.
     * En el script C# de Unity: androidActivity.Call("onCoinsEarned", coins.ToString());
     *
     * @param coinsStr número de monedas ganadas (como String, que es lo que Unity envía)
     */
    public void onCoinsEarned(String coinsStr) {
        int coins = 0;
        try {
            coins = Integer.parseInt(coinsStr.trim());
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parseando monedas: " + coinsStr);
        }

        final int coinsToAdd = coins;
        Log.d(TAG, "Monedas ganadas desde Unity: " + coinsToAdd);

        // Guardar en el backend si hay un userId válido
        if (userId != -1 && coinsToAdd > 0) {
            saveCoinsToBackend(coinsToAdd);
        }

        // Devolver resultado a InicioLoginActivity
        Intent result = new Intent();
        result.putExtra("coinsEarned", coinsToAdd);
        setResult(RESULT_OK, result);
    }

    /**
     * Unity llama a este método cuando el jugador cierra el juego.
     * En el script C#: androidActivity.Call("onGameFinished", "ok");
     */
    public void onGameFinished(String message) {
        Log.d(TAG, "Juego terminado: " + message);
        runOnUiThread(this::finish);
    }

    // ── Lógica interna ────────────────────────────────────────────────────────

    private void saveCoinsToBackend(int coins) {
        // Usamos el endpoint de coins del backend
        // Ajusta el endpoint y el request body según tu ApiService
        Log.d(TAG, "Enviando " + coins + " monedas al backend para userId=" + userId);

        // TODO: cuando el backend tenga el endpoint de sumar monedas, descomentar:
        /*
        EarnCoinsRequest request = new EarnCoinsRequest(userId, coins);
        apiService.earnCoins(request).enqueue(new Callback<EarnCoinsResponse>() {
            @Override
            public void onResponse(Call<EarnCoinsResponse> call, Response<EarnCoinsResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Monedas guardadas correctamente en el backend");
                    // Actualizar SharedPreferences con el nuevo saldo
                    if (response.body() != null) {
                        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                        prefs.edit().putFloat("saldo", (float) response.body().getNewBalance()).apply();
                    }
                } else {
                    Log.e(TAG, "Error guardando monedas: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<EarnCoinsResponse> call, Throwable t) {
                Log.e(TAG, "Fallo de red al guardar monedas", t);
            }
        });
        */

        // Por ahora, actualiza el saldo localmente en SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        float currentBalance = prefs.getFloat("saldo", 0f);
        prefs.edit().putFloat("saldo", currentBalance + coins).apply();
        Log.d(TAG, "Saldo local actualizado: " + (currentBalance + coins));
    }
}
