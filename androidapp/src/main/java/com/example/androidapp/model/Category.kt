package com.example.androidapp.model

import com.example.blogmultiplatform.CategoryCommon

enum class Category(override val color: String): CategoryCommon {
    Programming(color = "FFEC45"),
    Technology(color = "00FF94"),
    Design(color = "8B6DFF")
}