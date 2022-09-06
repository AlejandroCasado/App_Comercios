package com.example.klikin.usecases.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.klikin.R
import com.example.klikin.modelo.domain.CategoryShops
import com.example.klikin.modelo.domain.TypeCategoryShop

class CategoryAdapter (private val context: Context, list:List<CategoryShops>, private val onClickCategory: OnClickCategory)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()  {

    private var itemsShopsOriginal = ArrayList(list)
    private var itemsShopsFiltered = ArrayList(list)

    interface OnClickCategory {
        fun onClickItemCategory(category: CategoryShops)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder = CategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = itemsShopsFiltered[position]

        controlDrawItemTypeShop(item, holder)
        logicClickItemTypeShop(holder, item)
    }

    override fun getItemCount(): Int = itemsShopsFiltered.size

    private fun logicClickItemTypeShop(holder: CategoryViewHolder, item: CategoryShops) {
        holder.cardItemCategory.setOnClickListener {
            onClickCategory.onClickItemCategory(item)
        }
    }

    private fun controlDrawItemTypeShop(item: CategoryShops, holder: CategoryViewHolder) {
        when (item.type) {
            TypeCategoryShop.Shopping -> {
                holder.iconCategory.background =
                    AppCompatResources.getDrawable(context, R.drawable.cart_colour)
                holder.txtNameCategory.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.maroon
                    )
                )
                holder.txtNameCategory.text = context.getString(R.string.type_shopping_shop)
            }
            TypeCategoryShop.Leisure -> {
                holder.iconCategory.background =
                    AppCompatResources.getDrawable(context, R.drawable.leisure_colour)
                holder.txtNameCategory.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.purple
                    )
                )
                holder.txtNameCategory.text = context.getString(R.string.type_leisure_shop)
            }
            TypeCategoryShop.Beauty -> {
                holder.iconCategory.background =
                    AppCompatResources.getDrawable(context, R.drawable.ic_beauty_colour)
                holder.txtNameCategory.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.blue
                    )
                )
                holder.txtNameCategory.text = context.getString(R.string.type_beauty_shop)

            }
            TypeCategoryShop.Food -> {
                holder.iconCategory.background =
                    AppCompatResources.getDrawable(context, R.drawable.catering_colour)
                holder.txtNameCategory.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.yellow
                    )
                )
                holder.txtNameCategory.text = context.getString(R.string.type_food_shop)
            }
            TypeCategoryShop.Other -> {
                holder.iconCategory.background =
                    AppCompatResources.getDrawable(context, R.drawable.ic_other_colour)
                holder.txtNameCategory.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.green
                    )
                )
                holder.txtNameCategory.text = context.getString(R.string.type_other_shop)
            }
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconCategory: ImageView = itemView.findViewById(R.id.iconCategory)
        val txtNameCategory: TextView = itemView.findViewById(R.id.txtNameCategory)
        val cardItemCategory: CardView = itemView.findViewById(R.id.cardItemCategory)
    }
}