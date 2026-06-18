# Unity dentro de Android

Notas de referencia para integrar un proyecto Unity dentro de una app Android nativa.

## Enlaces base

- Android: https://github.com/jlopezr/Activities
- Unity: https://github.com/jlopezr/activities-unity
- Clase grabada: https://drive.google.com/open?id=1cbb1RRR0I-pmQYN1pBkiqKPCfWXPXVVN
- Guia Medium: https://medium.com/@davidbeloosesky/embedded-unity-within-android-app-7061f4f473a
- Problemas frecuentes:
  - https://stackoverflow.com/questions/48203572/importing-unity-project-into-android-studio
  - https://stackoverflow.com/questions/58877735/how-to-solve-libmain-so-not-found-while-using-unity-as-a-library-in-android-ap

## Idea principal

Unity se exporta como una libreria Android y se integra dentro del proyecto Android Studio.
La app Android sigue siendo la app principal, y Unity se abre desde una Activity cuando haga falta.

## Flujo previsto

1. Preparar el proyecto Unity.
2. Exportar desde Unity como proyecto Android / Android Library.
3. Copiar o importar el modulo generado por Unity dentro del proyecto Android.
4. Configurar `settings.gradle` para incluir el modulo Unity.
5. Configurar dependencias en el `build.gradle` de la app Android.
6. Crear una Activity Android que lance la Activity de Unity.
7. Probar primero que Unity abre sin conectar aun logica compleja.
8. Despues conectar datos entre Android y Unity si hace falta.

## Puntos delicados

- Revisar que las rutas del modulo Unity esten bien incluidas en Gradle.
- Revisar que las librerias nativas de Unity se copien correctamente.
- Si aparece error relacionado con `libmain.so`, suele ser problema de ABI, ruta o empaquetado de librerias nativas.
- Si Android Studio no importa bien el proyecto Unity, revisar que el modulo Unity este declarado correctamente en `settings.gradle`.
- Conviene probar primero en local con una escena Unity minima antes de integrarlo con la app completa.

## Estrategia para este proyecto

Para no romper la app DSA Android, lo mas seguro sera:

1. Trabajar en una copia o rama separada.
2. Integrar Unity como modulo independiente.
3. Anadir un boton temporal desde Android para abrir Unity.
4. Verificar que compila.
5. Verificar que abre Unity en movil.
6. Despues ya conectar la funcionalidad concreta que pida el minimo o la practica.

