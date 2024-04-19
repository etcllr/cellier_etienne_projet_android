package fr.nextu.cellier_etienne

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import fr.nextu.cellier_etienne.adapter.MoviesAdapter
import fr.nextu.cellier_etienne.databinding.FragmentMovieBinding
import fr.nextu.cellier_etienne.entity.CatalogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMovies.setOnClickListener {
            getPictureList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    fun getPictureList() = CoroutineScope(Dispatchers.IO).launch {
        val result = requestMoviesList()
        withContext(Dispatchers.Main) {
            val recyclerView = binding.recyclerviewThird
            recyclerView.layoutManager = LinearLayoutManager(context)
            val gson = Gson()
            val moviesEntity = gson.fromJson(result, CatalogEntity::class.java)

            for (movie in moviesEntity.movies) {
                Log.d("Movie", movie.toString())
            }

            recyclerView.adapter = MoviesAdapter(moviesEntity)

            val textView = binding.textviewThird
            textView.text = ""
        }
    }
}