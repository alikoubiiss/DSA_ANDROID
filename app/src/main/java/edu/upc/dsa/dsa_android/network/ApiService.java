package edu.upc.dsa.dsa_android.network;

import java.util.List;

import edu.upc.dsa.dsa_android.BuyItemRequest;
import edu.upc.dsa.dsa_android.CreateForumMessageRequest;
import edu.upc.dsa.dsa_android.CreateForumTopicRequest;
import edu.upc.dsa.dsa_android.Evento;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantRequest;
import edu.upc.dsa.dsa_android.Faq.FaqAssistantResponse;
import edu.upc.dsa.dsa_android.ForumMessage;
import edu.upc.dsa.dsa_android.ForumTopic;
import edu.upc.dsa.dsa_android.InventoryEntry;
import edu.upc.dsa.dsa_android.Item;
import edu.upc.dsa.dsa_android.LoginRequest;
import edu.upc.dsa.dsa_android.Purchase;
import edu.upc.dsa.dsa_android.RegisterRequest;
import edu.upc.dsa.dsa_android.RegistroEventoRequest;
import edu.upc.dsa.dsa_android.Team;
import edu.upc.dsa.dsa_android.TeamInfoResponse;
import edu.upc.dsa.dsa_android.User;
import edu.upc.dsa.dsa_android.UserEvent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("game/auth/register")
    Call<User> registerUser(@Body RegisterRequest request);

    @POST("game/auth/login")
    Call<User> loginUser(@Body LoginRequest request);

    @GET("game/items")
    Call<List<Item>> getAllItems();

    @GET("game/users")
    Call<List<User>> getAllUsers();

    @POST("game/players/{playerId}/inventory")
    Call<Purchase> buyItem(@Path("playerId") int playerId, @Body BuyItemRequest request);

    @GET("game/players/{playerId}/inventory")
    Call<List<InventoryEntry>> getInventory(@Path("playerId") int playerId);

    @GET("game/players/{playerId}/purchases")
    Call<List<Purchase>> getPurchases(@Path("playerId") int playerId);

    @GET("game/events")
    Call<List<Evento>> getEvents();

    @POST("game/events/{eventId}/register")
    Call<ResponseBody> registerToEvent(
            @Path("eventId") int eventId,
            @Body RegistroEventoRequest request
    );

    @GET("game/events/{eventId}/users")
    Call<List<UserEvent>> getEventUsers(@Path("eventId") int eventId);

    // ── Equipos ───────────────────────────────────────────────────────────────
    @GET("teams/ranking")
    Call<List<Team>> getTeamsRanking();

    @PUT("teams/join/{teamName}/{userName}")
    Call<Team> joinTeam(@Path("teamName") String teamName, @Path("userName") String username);

    @DELETE("teams/leave/{userName}")
    Call<Void> leaveTeam(@Path("userName") String username);

    @GET("game/user/{username}/team")
    Call<TeamInfoResponse> getMyTeamInfo(@Path("username") String username);

    @GET("teams/user/{userName}/team")
    Call<TeamInfoResponse> getTeamMembership(@Path("userName") String username);

    // ── FAQ ───────────────────────────────────────────────────────────────────
    @POST("game/assistant/faq")
    Call<FaqAssistantResponse> askFaq(@Body FaqAssistantRequest request);

    // ── Foro: temáticas ───────────────────────────────────────────────────────
    @GET("forum/topics")
    Call<List<ForumTopic>> getForumTopics();

    @POST("forum/topics")
    Call<ForumTopic> createForumTopic(@Body CreateForumTopicRequest request);

    // ── Foro: mensajes ────────────────────────────────────────────────────────
    @GET("forum/topics/{id}/messages")
    Call<List<ForumMessage>> getForumMessages(@Path("id") int topicId);

    @POST("forum/topics/{id}/messages")
    Call<ForumMessage> createForumMessage(@Path("id") int topicId, @Body CreateForumMessageRequest request);

    @POST("api/game/coins/earn")
    Call<edu.upc.dsa.dsa_android.EarnCoinsResponse> earnCoins(@Body edu.upc.dsa.dsa_android.EarnCoinsRequest request);

    @GET("game/users/{userId}")
    Call<User> getUserById(@Path("userId") int userId);
}
