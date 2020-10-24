package com.example.flow_arch.main.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flow_arch.R
import com.example.flow_arch.common.network.Movie
import com.example.flow_arch.common.ui.NetworkImageView

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var data: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    fun update(data: List<Movie>) {
        this.data = data
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val description = itemView.findViewById<TextView>(R.id.description)
        private val image = itemView.findViewById<NetworkImageView>(R.id.image)

        fun bind(item: Movie) {
            name.text = item.title
            description.text = item.overview
            image.load(item.image)
        }
    }

}
