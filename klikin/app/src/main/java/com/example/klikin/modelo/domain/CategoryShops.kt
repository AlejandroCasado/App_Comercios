package com.example.klikin.modelo.domain

enum class TypeCategoryShop{
    Food, Beauty, Leisure, Shopping, Other
}

data class CategoryShops(
    val type: TypeCategoryShop,
    val name: String,
)
