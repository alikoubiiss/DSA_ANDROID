using UnityEngine;

/// <summary>
/// AndroidBridge.cs — Script C# para comunicar Unity con Android.
///
/// INSTRUCCIONES:
/// 1. En Unity, crea un GameObject vacío llamado exactamente "AndroidBridge"
/// 2. Arrastra este script a ese GameObject
/// 3. El GameObject debe estar presente en la primera escena que carga el juego
/// </summary>
public class AndroidBridge : MonoBehaviour
{
    private AndroidJavaObject _androidActivity;

    void Start()
    {
        // Obtener referencia a la Activity Android (solo funciona en dispositivo real)
        if (Application.platform == RuntimePlatform.Android)
        {
            using (AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
            {
                _androidActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
            }
            Debug.Log("[AndroidBridge] Conectado con la Activity Android");
        }
    }

    // ── Métodos llamados DESDE Android ────────────────────────────────────────

    /// <summary>
    /// Llamado desde UnityGameActivity.java via UnityPlayer.UnitySendMessage(...)
    /// Recibe el nombre de usuario del jugador logueado en Android.
    /// </summary>
    public void SetUserName(string userName)
    {
        Debug.Log("[AndroidBridge] Usuario recibido desde Android: " + userName);
        // Aquí puedes inicializar el juego con el nombre del jugador,
        // mostrar un saludo en la UI, etc.
        // Ejemplo: PlayerNameText.text = "Bienvenido, " + userName;
    }

    // ── Métodos para llamar HACIA Android ─────────────────────────────────────

    /// <summary>
    /// Llama a este método cuando el jugador gane monedas/puntos.
    /// Ejemplo de uso: AndroidBridge.Instance.SendCoinsToAndroid(50);
    /// </summary>
    public void SendCoinsToAndroid(int coins)
    {
        if (Application.platform == RuntimePlatform.Android && _androidActivity != null)
        {
            Debug.Log("[AndroidBridge] Enviando " + coins + " monedas a Android");
            _androidActivity.Call("onCoinsEarned", coins.ToString());
        }
        else
        {
            // En el editor de Unity, solo logueamos
            Debug.Log("[AndroidBridge] (Editor) Monedas que se enviarían: " + coins);
        }
    }

    /// <summary>
    /// Llama a este método cuando el jugador quiera salir del juego y volver a Android.
    /// </summary>
    public void CloseGameAndReturn()
    {
        if (Application.platform == RuntimePlatform.Android && _androidActivity != null)
        {
            Debug.Log("[AndroidBridge] Cerrando juego y volviendo a Android");
            _androidActivity.Call("onGameFinished", "completed");
        }
        else
        {
            Debug.Log("[AndroidBridge] (Editor) Se cerraría el juego");
            Application.Quit();
        }
    }

    // ── Singleton opcional para acceso desde otros scripts ────────────────────

    public static AndroidBridge Instance { get; private set; }

    void Awake()
    {
        if (Instance != null && Instance != this)
        {
            Destroy(gameObject);
            return;
        }
        Instance = this;
        DontDestroyOnLoad(gameObject); // Persiste entre escenas
    }
}
