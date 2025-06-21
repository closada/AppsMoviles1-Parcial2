# AppRegistroPeliculas 

Aplicación Android desarrollada en Android Studio que permite registrar, modificar, eliminar, buscar y filtrar películas. Está pensada como una app simple de gestión de películas, con persistencia de datos local en formato JSON y arquitectura MVVM con `ViewModel` compartido.

---

## Tecnologías y herramientas utilizadas

- Kotlin
- Android Studio
- ViewModel y LiveData
- RecyclerView
- ViewBinding
- Persistencia con archivos JSON (GSON)
- Material Design (Componentes como `SearchView`, `Chip`, `FloatingActionButton`, `RatingBar`, etc.)

---

## Estructura general de la app

### Pantalla principal (`MainActivity`)
- Muestra una **lista de películas** en un `RecyclerView`.
- Incluye un **buscador** (`SearchView`) que filtra por título.
- Permite aplicar **filtros por género** usando `Chip`s dinámicos.
- Acceso al botón **Agregar Película**.
- Al tocar una película existente, permite modificarla o eliminarla.

### Pantalla de registro / edición (`RegistroPeliculaActivity`)
- Formulario para crear o editar una película.
- Campos: título, reseña, año, género (spinner), puntuación (rating).
- Botones para:
  - Registrar
  - Modificar
  - Eliminar
  - Volver

---

## Lógica y arquitectura

- **MVVM (Model-View-ViewModel)**:
  - `PeliculaViewModel`: gestiona la lógica de negocio y acceso a datos.
  - `ViewModelHolder`: patrón singleton para compartir instancia del `ViewModel` entre actividades.
  - `LiveData`: permite actualizar automáticamente la vista al cambiar los datos.

- **Persistencia**:
  - Los datos se almacenan en un archivo `peliculas.json` en memoria interna del dispositivo.
  - Se utiliza GSON para serialización y deserialización.

- **Validaciones**:
  - Se valida que el título y la reseña no estén vacíos.
  - La puntuación debe ser mayor a 0.
  - No se permite registrar películas duplicadas (por título).

---

## Funcionalidades destacadas

✅ Registrar nuevas películas  
✅ Editar películas existentes  
✅ Eliminar películas  
✅ Persistencia de datos en archivo local  
✅ Filtro en tiempo real por título (buscador)  
✅ Filtro múltiple por género con chips  
✅ Validaciones personalizadas y manejo de excepciones  
✅ Vista intuitiva y compatible con Material Design

---

##Estructura de carpetas

```
├── adapter
│   └── PeliculaAdapter.kt
├── exception
│   ├── excepciones.kt
├── model
│   └── Pelicula.kt
├── repository
│   └── PeliculasRepository.kt
├── viewmodel
│   └── PeliculaViewModel.kt
├── MainActivity.kt
├── RegistroPeliculaActivity.kt
└── ViewModelHolder.kt
```

---

### Autores:
Mailen Acosta Vera, Mangialavore Matias y Losada Camila
