package edu.upc.dsa.dsa_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("game/users/register")
    Call<User> registerUser(@Body Credentials credentials);

    @POST("game/users/login")
    Call<User> loginUser(@Body Credentials credentials);

    @POST("game/users/objects/buy")
    Call<User> comprarItem(@Body CompraRequest request);

    @GET("game/shop/objects")
    Call<List<GameObject>> getALLGameObjects();

    @GET("game/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @GET("game/users/objects/list")
    Call<List<GameObject>> getUserObjects(@Query("nombre") String nombre);

    @GET("game/events")
    Call<List<Evento>> getEventos();

    @POST("game/events/{id}/register")
    Call<Void> registerEvento(@Path("id") String id, @Body RegistroEventoRequest request);

    // ── Foro ──────────────────────────────────────────────────────────────────
    @GET("forum/topics")
    Call<List<ForumTopic>> getForumTopics();

    @POST("forum/topics")
    Call<ForumTopic> createForumTopic(@Body CreateForumTopicRequest request);
}
