package fr.nextu.cellier_etienne

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.nextu.cellier_etienne.adapter.MoviesAdapter
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
    var catalog = CatalogEntity(emptyList())

    lateinit var movies_recycler: RecyclerView
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        findViewById<Button>(R.id.button_save).setOnClickListener {
            saveMoviesInDB()
        }

        movies_recycler = findViewById<RecyclerView>(R.id.recyclerview_third).apply {
            adapter = MoviesAdapter(CatalogEntity(emptyList()))
            layoutManager = LinearLayoutManager(this@MovieActivity)
        }

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.saved_movies))
                        .setContentText(catalog.movies.toString() ?: "")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            1
                        )
                    }
                    NotificationManagerCompat.from(this).notify(1, builder.build())
                }
            }
    }

    fun getPictureList() = CoroutineScope(Dispatchers.IO).launch {
        val result = requestMoviesList()
        withContext(Dispatchers.Main) {
            movies_recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
            val gson = Gson()
            catalog = gson.fromJson(result, CatalogEntity::class.java)

            for (movie in catalog.movies) {
                Log.d("Movie", movie.toString())
            }

            movies_recycler.adapter = MoviesAdapter(catalog)

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

    private fun saveMoviesInDB() {
        if (catalog.movies.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().insertAll(*catalog.movies.toTypedArray())
                notifyNewData(catalog)
            }

            Snackbar.make(
                findViewById(R.id.main),
                getString(R.string.saved_movies),
                Snackbar.LENGTH_SHORT
            )
                .show()
        } else {
            Snackbar.make(
                findViewById(R.id.main),
                getString(R.string.no_movies),
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun notifyNewData(response: CatalogEntity) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(getString(R.string.saved_movies))
                    .setContentText(response.movies.toString() ?: "")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                NotificationManagerCompat.from(this).notify(1, builder.build())
            }

            else -> {
                // Utilisez le ActivityResultLauncher enregistré précédemment
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "fr_nextu_etienne_cellier_channel_notification"
    }
}