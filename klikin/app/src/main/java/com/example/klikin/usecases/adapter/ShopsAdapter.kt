package com.example.klikin.usecases.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.klikin.R
import com.example.klikin.modelo.domain.ShopApi
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.roundToInt

class ShopsAdapter(private val context: Context, list:List<ShopApi>, private val onClickShop: OnClickShop)
    : RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>()  {

    private var itemsShopsOriginal = ArrayList(list)
    private var itemsShopsFiltered = ArrayList(list)
    private var visibilityDistance: Boolean = false

    interface OnClickShop {
        fun onClickItem(shop: ShopApi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder = ShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false))

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val item = itemsShopsFiltered[position]
        controlNameAndDescription(item, holder)
        controlLogoShop(item, holder)
        controlDrawDistanceItemShop(holder, item)
        controlDrawIconAndBackgroundColorItemShop(item, holder)
        logicClickItemCardShop(holder, item)
    }

    override fun getItemCount(): Int = itemsShopsFiltered.size

    private fun logicClickItemCardShop(holder: ShopViewHolder, item: ShopApi) {
        holder.cardItemShop.setOnClickListener {
            onClickShop.onClickItem(item)
        }
    }

    private fun controlDrawDistanceItemShop(holder: ShopViewHolder, item: ShopApi) {
        if (visibilityDistance) {
            holder.txtDistance.visibility = View.VISIBLE
            item.distancia?.let {
                holder.txtDistance.text = context.getString(R.string.txt_distance_item_card_shop,it.roundToInt().toString())
            }
        } else {
            holder.txtDistance.visibility = View.INVISIBLE
        }
    }

    private fun controlDrawIconAndBackgroundColorItemShop(item: ShopApi, holder: ShopViewHolder) {
        when (item.category ?: "OTHER") {
            "FOOD" -> {
                holder.iconCategoryShop.background =
                    AppCompatResources.getDrawable(context, R.drawable.catering_white)
                holder.llBackgroundColorCategory.setBackgroundColor(context.getColor(R.color.yellow))
            }
            "BEAUTY" -> {
                holder.iconCategoryShop.background =
                    AppCompatResources.getDrawable(context, R.drawable.ic_beauty_white)

                holder.llBackgroundColorCategory.setBackgroundColor(context.getColor(R.color.blue))

            }
            "LEISURE" -> {
                holder.iconCategoryShop.background =
                    AppCompatResources.getDrawable(context, R.drawable.leisure_white)
                holder.llBackgroundColorCategory.setBackgroundColor(context.getColor(R.color.purple))

            }
            "SHOPPING" -> {
                holder.iconCategoryShop.background =
                    AppCompatResources.getDrawable(context, R.drawable.cart_white)
                holder.llBackgroundColorCategory.setBackgroundColor(context.getColor(R.color.maroon))

            }
            "OTHER" -> {
                holder.iconCategoryShop.background =
                    AppCompatResources.getDrawable(context, R.drawable.ic_other_white)
                holder.llBackgroundColorCategory.setBackgroundColor(context.getColor(R.color.green))
            }
            else -> {
            }
        }
    }

    private fun controlLogoShop(item: ShopApi, holder: ShopViewHolder) {
        var url = ""
        item.photos.let {
            if (it.isNotEmpty()) {
                url = it[0].url
            }
        }
        if (url.isNotEmpty()) {
            Glide.with(context).load(url).placeholder(R.drawable.placeholder).into(holder.iconShop)
        }else{
            Glide.with(context).load(R.drawable.placeholder).into(holder.iconShop)
        }
    }

    private fun controlNameAndDescription(item: ShopApi, holder: ShopViewHolder) {
        val splitName = item.name.split("*")
        val finalName = splitName[splitName.size - 1].trim()
        showNameAndDescription(holder, finalName, item)
    }

    private fun showNameAndDescription(holder: ShopViewHolder, finalName: String, item: ShopApi) {
        holder.txtNameShop.text = finalName.ifEmpty { item.name }
        holder.txtDescriptionShop.text = item.shortDescription
    }

    fun getList(): List<ShopApi> = itemsShopsOriginal

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ShopApi>?) {
        itemsShopsFiltered.clear()
        if (list != null) {
            itemsShopsFiltered.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setVisibilityDistanceShops(visibility: Boolean) {
        visibilityDistance = visibility
    }

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llBackgroundColorCategory: LinearLayout = itemView.findViewById(R.id.llBackgroundCategory)
        val iconCategoryShop: ImageView = itemView.findViewById(R.id.iconCategory)
        val iconShop: ShapeableImageView = itemView.findViewById(R.id.iconLogoShop)
        val txtDistance: TextView = itemView.findViewById(R.id.txtDistance)
        val txtNameShop: TextView = itemView.findViewById(R.id.txtNameShop)
        val txtDescriptionShop: TextView = itemView.findViewById(R.id.txtDescriptionShop)
        val cardItemShop: CardView = itemView.findViewById(R.id.cardItemShop)
    }
}