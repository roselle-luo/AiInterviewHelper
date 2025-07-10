package com.example.interviewhelper.data.model

data class Article(
    val id: Int,
    val title: String,
    val author: String,
    val category: String,
    val content: String,
    val images: List<Int>,
)
