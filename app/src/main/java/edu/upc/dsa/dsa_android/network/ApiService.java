package edu.upc.dsa.dsa_android.network;

import java.util.List;

import edu.upc.dsa.dsa_android.BuyItemRequest;
import edu.upc.dsa.dsa_android.Evento;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantRequest;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantResponse;
import edu.upc.dsa.dsa_android.InventoryEntry;
import edu.upc.dsa.dsa_android.Item;
import edu.upc.dsa.dsa_android.LoginRequest;
import edu.upc.dsa.dsa_android.Purchase;
import edu.upc.dsa.dsa_android.RegisterRequest;
import edu.upc.dsa.dsa_android.RegistroEventoRequest;
import edu.upc.dsa.dsa_android.User;
import edu.upc.dsa.dsa_android.UserEvent;
import edu.upc.dsa.dsa_android.TeamInfoResponse;
<<<<<<< HEAD
import edu.upc.dsa.dsa_android.ForumTopic;
import edu.upc.dsa.dsa_android.CreateForumTopicRequest;
=======
import edu.upc.dsa.dsa_android.Team;
>>>>>>> 3908cffb7067d51d30eeba5fec50b4cb7561c3eb
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ─────────────────────────────────────────────
    //  AUTENTICACIÓN
    // ─────────────────────────────────────────────

    /** POST /game/auth/register */
    @POST("game/auth/register")
    Call<User> registerUser(@Body RegisterRequest request);

    /** POST /game/auth/login */
    @POST("game/auth/login")
    Call<User> loginUser(@Body LoginRequest request);

    // ─────────────────────────────────────────────
    //  ITEMS (catálogo / tienda)
    // ─────────────────────────────────────────────

    /** GET /game/items — obtener todos los items del catálogo */
    @GET("game/items")
    Call<List<Item>> getAllItems();

    // ─────────────────────────────────────────────
    //  USUARIOS
    // ─────────────────────────────────────────────

    /** GET /game/users — obtener todos los usuarios */
    @GET("game/users")
    Call<List<User>> getAllUsers();

    // ─────────────────────────────────────────────
    //  INVENTARIO DEL JUGADOR
    // ─────────────────────────────────────────────

    /** POST /game/players/{playerId}/inventory — comprar un item */
    @POST("game/players/{playerId}/inventory")
    Call<Purchase> buyItem(@Path("playerId") int playerId, @Body BuyItemRequest request);

    /** GET /game/players/{playerId}/inventory — ver inventario del jugador */
    @GET("game/players/{playerId}/inventory")
    Call<List<InventoryEntry>> getInventory(@Path("playerId") int playerId);

    /** GET /game/players/{playerId}/purchases — historial de compras */
    @GET("game/players/{playerId}/purchases")
    Call<List<Purchase>> getPurchases(@Path("playerId") int playerId);

    @GET("game/events")
    Call<List<Evento>> getEvents();

    @POST("game/events/{eventId}/register")
    Call<ResponseBody> registerToEvent(
            @Path("eventId") int eventId,
            @Body RegistroEventoRequest request
    );

    /** GET /game/events/{eventId}/users — usuarios inscritos en un evento */
    @GET("game/events/{eventId}/users")
    Call<List<UserEvent>> getEventUsers(@Path("eventId") int eventId);

    @GET("game/user/{username}/team")
    Call<TeamInfoResponse> getMyTeamInfo(@Path("username") String username);

    /** POST /game/assistant/faq */
    @POST("game/assistant/faq")
    Call<FaqAssistantResponse> askFaq(@Body FaqAssistantRequest request);

<<<<<<< HEAD
    // ── FORO ──────────────────────────────────────────────────────────────────
    @GET("forum/topics")
    Call<List<ForumTopic>> getForumTopics();

    @POST("forum/topics")
    Call<ForumTopic> createForumTopic(@Body CreateForumTopicRequest request);
=======
    @GET("teams/ranking")
    Call<List<Team>> getTeamsRanking();

    @PUT("teams/join/{teamName}/{userName}")
    Call<Team> joinTeam(@Path("teamName") String teamName, @Path("userName") String userName);

    @DELETE("teams/leave/{userName}")
    Call<Void> leaveTeam(@Path("userName") String userName);
>>>>>>> 3908cffb7067d51d30eeba5fec50b4cb7561c3eb
}

