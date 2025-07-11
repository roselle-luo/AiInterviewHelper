package com.example.interviewhelper.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.interviewhelper.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatedArticle(
    navController: NavController, viewModel: ArticleViewModel = hiltViewModel()
) {
    val categories = viewModel.categories
    val articles = viewModel.allArticles
    val searchQuery = viewModel.searchQuery
    val focusManager = LocalFocusManager.current
    val selectedId = viewModel.selectedId
    val pageState = rememberPagerState { categories.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = 6.dp)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {
        // 搜索框
        OutlinedTextField(
            value = viewModel.searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("请输入搜索内容") },
            shape = RoundedCornerShape(16.dp),
            maxLines = 1,
            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "搜索") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )


        Spacer(
            modifier = Modifier.padding(vertical = 6.dp)
        )

        // 菜单栏
        TabRow(
            selectedTabIndex = selectedId.intValue,
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    selected = selectedId.intValue == index,
                    onClick = {
                        coroutineScope.launch {
                            selectedId.intValue = index
                            pageState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }


        // 文章排布页面
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn {
                    items(articles.size) { index ->
                        TextImageCard(
                            articles[index].title,
                            articles[index].content,
                            articles[index].images
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TextImageCard(
    title: String,
    content: String,
    @DrawableRes imageRes: List<Int>
) {
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左侧：文字部分
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = content,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis
                )

            }

            // 右侧：图片部分
            Image(
                painter = painterResource(id = imageRes[0]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .padding(12.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
