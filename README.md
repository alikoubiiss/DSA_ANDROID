# DSA_ANDROID - Mínimo 2

Módulo de la aplicación Android para el Mínimo 2 de DSA.

## Implementado
- **MyTeamActivity**: Modificada para realizar una llamada a la API REST `/game/user/{username}/team` a través de Retrofit.
- **TeamMember**: Nuevo modelo de datos en Android para representar a cada miembro (nombre, avatar, puntos).
- **TeamInfoResponse**: Modificado para soportar una lista de `TeamMember`.
- **MemberAdapter**: Actualizado para poblar el RecyclerView de miembros con nombres, avatares (descargados dinámicamente con Picasso) y puntos.

## Cómo compilar
Para compilar la aplicación, utiliza:
```bash
./gradlew assembleDebug
```
