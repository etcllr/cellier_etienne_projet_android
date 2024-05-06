package fr.nextu.cellier_etienne

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.nextu.cellier_etienne.adapter.MoviesAdapter
import fr.nextu.cellier_etienne.entity.CatalogEntity
import fr.nextu.cellier_etienne.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CatalogActivity : AppCompatActivity() {
    val db: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    lateinit var movies_recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalog)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        GlobalScope.launch(Dispatchers.IO) {
            val moviesList = db.movieDao().getAll() ?: emptyList<MovieEntity>()
            val catalog = CatalogEntity(moviesList)

            launch(Dispatchers.Main) {
                movies_recycler = findViewById<RecyclerView>(R.id.recyclerview_catalog).apply {
                    adapter = MoviesAdapter(catalog)
                    layoutManager = LinearLayoutManager(this@CatalogActivity)
                }
            }
        }

        findViewById<Button>(R.id.button_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}