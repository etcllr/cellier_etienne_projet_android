package fr.nextu.cellier_etienne.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.nextu.cellier_etienne.databinding.ItemMovieBinding
import fr.nextu.cellier_etienne.entity.CatalogEntity
import fr.nextu.cellier_etienne.entity.MovieEntity

class MoviesAdapter(private val movies: CatalogEntity) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieEntity) {
            binding.textView.text = movie.title
            Picasso.get().load(movie.poster).into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies.movies[position])
    }

    override fun getItemCount(): Int = movies.movies.size
}