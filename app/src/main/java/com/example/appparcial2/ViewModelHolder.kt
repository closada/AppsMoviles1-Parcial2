// ViewModelHolder.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.appparcial2.viewmodel.PeliculaViewModel

object ViewModelHolder {
    // Almacén único GLOBAL para ViewModels
    private val globalViewModelStore = ViewModelStore()

    // Instancia única del ViewModel
    private val peliculaViewModel by lazy {
        ViewModelProvider(globalViewModelStore, PeliculaViewModelFactory()).get(PeliculaViewModel::class.java)
    }

    // Función para obtener la instancia compartida
    fun getSharedPeliculaViewModel(): PeliculaViewModel {
        return peliculaViewModel
    }
}

// Factory para crear el ViewModel
class PeliculaViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T { /*class T es de tipo genérico o heredada de ViewModel*/
        if (modelClass.isAssignableFrom(PeliculaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") /*para evitar que el compilador se queje del cast a tipo genérico T */
            return PeliculaViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}