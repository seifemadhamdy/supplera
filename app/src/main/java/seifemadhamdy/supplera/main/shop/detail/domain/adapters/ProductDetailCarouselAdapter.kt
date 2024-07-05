package seifemadhamdy.supplera.main.shop.detail.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import seifemadhamdy.supplera.R
import seifemadhamdy.supplera.api.data.models.Photo

class ProductDetailCarouselAdapter :
  ListAdapter<Photo, ProductDetailCarouselAdapter.ViewHolder>(ItemCallback()) {
  var onItemClick: ((Int) -> Unit)? = null

  class ItemCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
      return oldItem == newItem
    }
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val carouselImageView: ImageView = itemView.findViewById(R.id.carousel_image_view)

    init {
      itemView.setOnClickListener { onItemClick?.invoke(bindingAdapterPosition) }
    }

    fun bindData(photo: Photo) {
      carouselImageView.apply { load(photo.photoUrl) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_carousel_item, parent, false)
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bindData(photo = getItem(position))
  }
}
