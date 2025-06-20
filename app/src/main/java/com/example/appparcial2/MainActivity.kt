package com.example.appparcial2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appparcial2.adapter.PeliculaAdapter
import com.example.appparcial2.databinding.ActivityMainBinding
import com.example.appparcial2.model.Genero
import com.example.appparcial2.model.Pelicula
import com.example.appparcial2.viewmodel.PeliculaViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PeliculaViewModel by lazy {
        ViewModelHolder.getSharedPeliculaViewModel()
    }

    private lateinit var peliculaAdapter: PeliculaAdapter

    /* VARIABLES PARA FILTROS VARIOS */
    private var peliculasOriginales = listOf<Pelicula>()
    private val generosSeleccionados = mutableSetOf<Genero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFiltrosGeneros()
        setupSearchView()
        setupFab()

        viewModel.cargarPeliculasDesdeArchivo(applicationContext)
        observePeliculas()

        binding.btnResetear.setOnClickListener {
            val archivo = applicationContext.filesDir.resolve("peliculas.json")
            if (archivo.exists()) {
                archivo.delete()
                showToast("Películas reseteadas")
            } else {
                showToast("No hay datos para borrar")
            }
            viewModel.cargarPeliculasDesdeArchivo(applicationContext)
        }
    }

    /* PARA BORRAR EL BUSCADOR A LA HORA DE VOLVER AL MAIN ACTIVITY */
    override fun onResume() {
        super.onResume()
        // Resetea el buscador y filtros cuando volvés del otro activity
        binding.svBuscar.setQuery("", false)
        generosSeleccionados.clear()
        resetearChips()
        aplicarFiltros()
    }


    /* ENVIA la informacion x recycler view */
    private fun setupRecyclerView() {
        peliculaAdapter = PeliculaAdapter(emptyList()) { pelicula ->
            val intent = Intent(this, RegistroPeliculaActivity::class.java).apply {
                putExtra("pelicula", pelicula)
            }
            startActivity(intent)
        }
        binding.rvPeliculas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = peliculaAdapter
        }
    }


    private fun setupSearchView() {
        binding.svBuscar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                aplicarFiltros()
                return true
            }
        })
    }

    /* DEFINE los filtros de generos */
    private fun setupFiltrosGeneros() {
        binding.chipGroupGeneros.removeAllViews()

        Genero.values().forEach { genero ->
            val chip = com.google.android.material.chip.Chip(this).apply {
                text = genero.name.lowercase().replaceFirstChar { it.uppercase() }
                isCheckable = true
                isChecked = false
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        generosSeleccionados.add(genero)
                    } else {
                        generosSeleccionados.remove(genero)
                    }
                    aplicarFiltros()
                }
            }
            binding.chipGroupGeneros.addView(chip)
        }
    }

    /* RESETEA los filtros de genero */
    private fun resetearChips() {
        for (i in 0 until binding.chipGroupGeneros.childCount) {
            val chip = binding.chipGroupGeneros.getChildAt(i) as com.google.android.material.chip.Chip
            chip.isChecked = false
        }
    }

    /* APLICA el filtro que sea (texto o genero) */
    private fun aplicarFiltros() {
        val textoBusqueda = binding.svBuscar.query?.toString()?.lowercase() ?: ""

        val listaFiltrada = peliculasOriginales.filter { pelicula ->
            val cumpleTexto = pelicula.titulo.lowercase().contains(textoBusqueda)
            val cumpleGenero = if (generosSeleccionados.isEmpty()) true else generosSeleccionados.contains(pelicula.genero)
            cumpleTexto && cumpleGenero
        }
        peliculaAdapter.updatePeliculas(listaFiltrada)
    }

    private fun observePeliculas() {
        viewModel.peliculas.observe(this) { peliculas ->
            peliculasOriginales = peliculas
            aplicarFiltros()
        }
    }

    private fun setupFab() {
        binding.fabAddPelicula.setOnClickListener {
            val intent = Intent(this, RegistroPeliculaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
    }
}
