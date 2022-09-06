package com.example.klikin.provider

import com.example.klikin.modelo.domain.CategoryShops
import com.example.klikin.modelo.domain.TypeCategoryShop

class ProviderCategory {
    private val listCategory = arrayListOf<CategoryShops>()

    init {
        generateCategories()
    }

    private fun generateCategories() {
        val foodCategory = CategoryShops(TypeCategoryShop.Food, "FOOD")
        val beautyCategory = CategoryShops(TypeCategoryShop.Beauty, "BEAUTY")
        val leisureCategory = CategoryShops(TypeCategoryShop.Leisure, "LEISURE")
        val shoppingCategory = CategoryShops(TypeCategoryShop.Shopping, "SHOPPING")
        val otherCategory = CategoryShops(TypeCategoryShop.Other, "OTHER")
        listCategory.add(foodCategory)
        listCategory.add(beautyCategory)
        listCategory.add(leisureCategory)
        listCategory.add(shoppingCategory)
        listCategory.add(otherCategory)
    }

    fun getCategories(): List<CategoryShops> = listCategory
}