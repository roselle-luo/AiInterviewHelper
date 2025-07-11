package com.example.interviewhelper.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.interviewhelper.R
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.data.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val globalData: GlobalData
) : ViewModel() {

    val searchQuery = mutableStateOf("")

    val selectedCategory = mutableStateOf("全部")

    val selectedId = mutableIntStateOf(0)

    private val allCategories = listOf("全部", "云计算", "前端", "后端", "大数据", "机器学习")

    val allArticles = mutableStateListOf<Article>(
        Article(1, "Jetpack Compose 入门", "张三", "前端", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
        Article(2, "用 AI 写代码", "李四", "机器学习", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
        Article(3, "Vue3 新特性", "王五", "云计算", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
        Article(4, "Compose MVVM 实战", "赵六", "大数据", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
        Article(5, "Spring Boot 快速开发", "小明", "后端", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
        Article(6, "Flutter 动画详解", "小红", "前端", "啊倒萨和方差拉萨机场佛匆匆哈桑年龄差距偶就阿萨十大吃撒随即从撒娇偶怕谁奶茶吗领导NSA v那块里面离开那里弄",listOf(R.drawable.logo)),
    )

    val categories: List<String>
        get() = allCategories

    init {
        repeat (10) {
            allArticles.addAll(allArticles)
        }
    }

}