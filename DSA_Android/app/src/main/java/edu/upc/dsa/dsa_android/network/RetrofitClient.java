package edu.upc.dsa.dsa_android.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Mismo servidor que Swagger (p. ej. .../dsaApp/swagger.json en el navegador).
    // Retrofit usa la raíz de la API (/dsaApp/); los paths están en ApiService (game/users/login, etc.).
<<<<<<< HEAD
    private static final String BASE_URL_RAW = "http://192.168.1.107:8080/dsaApp/";
=======
    private static final String BASE_URL_RAW = "http://10.0.2.2:8080/dsaApp/";
>>>>>>> 3908cffb7067d51d30eeba5fec50b4cb7561c3eb

    private static RetrofitClient instance;
    private final ApiService apiService;

    private static String normalizeBaseUrl(String url) {
        url = url.trim();
        if (url.endsWith("swagger.json")) {
            url = url.substring(0, url.length() - "swagger.json".length());
        }
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        return url;
    }

    private RetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        String baseUrl = normalizeBaseUrl(BASE_URL_RAW);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApi() {
        return apiService;
    }
}
