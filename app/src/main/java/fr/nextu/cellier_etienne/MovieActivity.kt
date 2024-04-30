package fr.nextu.cellier_etienne

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import fr.nextu.cellier_etienne.adapter.MoviesAdapter
import fr.nextu.cellier_etienne.databinding.ActivityMovieBinding
import fr.nextu.cellier_etienne.entity.CatalogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MovieActivity : AppCompatActivity() {
    val db: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }
    lateinit var movies_recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.button_movies).setOnClickListener {
            getPictureList()
        }

        movies_recycler = findViewById<RecyclerView>(R.id.recyclerview_third).apply {
            adapter = MoviesAdapter(CatalogEntity(emptyList()))
            layoutManager = LinearLayoutManager(this@MovieActivity)
        }
    }

    fun getPictureList() = CoroutineScope(Dispatchers.IO).launch {
        val result = requestMoviesList()
        withContext(Dispatchers.Main) {
            movies_recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
            val gson = Gson()
            val moviesEntity = gson.fromJson(result, CatalogEntity::class.java)

            for (movie in moviesEntity.movies) {
                Log.d("Movie", movie.toString())
            }

            movies_recycler.adapter = MoviesAdapter(moviesEntity)

            findViewById<TextView>(R.id.textview_third).text = "Movies"
        }
    }

    private fun requestMoviesList(): String {
        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("https://api.betaseries.com/movies/list")
            .get()
            .addHeader("X-BetaSeries-Key", getString(R.string.movie_api_key))
            .build()

        val response: Response = client.newCall(request).execute()

        return response.body?.string() ?: ""
    }
}