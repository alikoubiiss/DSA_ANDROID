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
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    //  AUTENTICACIÃ“N
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** POST /game/auth/register */
    @POST("game/auth/register")
    Call<User> registerUser(@Body RegisterRequest request);

    /** POST /game/auth/login */
    @POST("game/auth/login")
    Call<User> loginUser(@Body LoginRequest request);

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    //  ITEMS (catÃ¡logo / tienda)
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** GET /game/items â€” obtener todos los items del catÃ¡logo */
    @GET("game/items")
    Call<List<Item>> getAllItems();

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    //  USUARIOS
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** GET /game/users â€” obtener todos los usuarios */
    @GET("game/users")
    Call<List<User>> getAllUsers();

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    //  INVENTARIO DEL JUGADOR
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** POST /game/players/{playerId}/inventory â€” comprar un item */
    @POST("game/players/{playerId}/inventory")
    Call<Purchase> buyItem(@Path("playerId") int playerId, @Body BuyItemRequest request);

    /** GET /game/players/{playerId}/inventory â€” ver inventario del jugador */
    @GET("game/players/{playerId}/inventory")
    Call<List<InventoryEntry>> getInventory(@Path("playerId") int playerId);

    /** GET /game/players/{playerId}/purchases â€” historial de compras */
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
}
