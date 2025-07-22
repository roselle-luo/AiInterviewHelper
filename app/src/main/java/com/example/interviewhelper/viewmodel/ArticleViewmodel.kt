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
        Article(1, "Jetpack Compose 入门", "张三", "前端", "1. 自我介绍 + 项目讲解（5 分钟）" +"2. HTML/CSS 基础" + "   - `position: absolute` 和 `fixed` 区别？"+  " -" + "`setTimeout` 与 `Promise` 的事件循环顺序",listOf(R.drawable.logo)),
        Article(2, "用 AI 写代码", "李四", "机器学习", "`position: absolute` 和 `fixed` 区别？"+  " -" + "`setTimeout` 与 `Promise` 的事件循环顺序",listOf(R.drawable.logo)),
        Article(3, "Vue3 新特性", "王五", "云计算", "懒加载策略、路由懒加载,实现防抖和节流",listOf(R.drawable.logo)),
        Article(4, "Compose MVVM 实战", "赵六", "大数据", "Chrome Performance 面板使用经验,如何配置打包分析？tree shaking 实践",listOf(R.drawable.logo)),
        Article(5, "Spring Boot 快速开发", "小明", "后端", "实现一个数组扁平化函数 `flattenArray`，手写 `call`, `apply`, `bind`",listOf(R.drawable.logo)),
        Article(6, "Flutter 动画详解", "小红", "前端", "Webpack 和 Vite 对比",listOf(R.drawable.logo)),
    )

    val categories: List<String>
        get() = allCategories

    init {
        repeat (10) {
            allArticles.addAll(allArticles)
        }
    }

}