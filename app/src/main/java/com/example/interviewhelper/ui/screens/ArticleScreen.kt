package com.example.interviewhelper.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.interviewhelper.ui.component.TopBar
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun ArticleScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = "面经详情", navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            MarkdownText(
                modifier = Modifier.padding(horizontal = 16.dp),
                markdown = """
# 前端开发岗位面试经验分享（2025）

> 面试公司：某知名互联网公司  
> 岗位方向：Web 前端开发（React + TypeScript）  
> 面试轮次：3 轮技术 + 1 轮 HR  
> 面试时间：2025 年 6 月中旬  
> 面试形式：线上视频

---

## 一、简历筛选

简历重点突出以下内容：
- 使用 React / Vue 参与核心业务开发；
- 熟练使用 TypeScript 和模块化；
- 熟悉常用性能优化策略（首屏优化、懒加载等）；
- 有一定的工程化能力（Webpack/Vite、CI/CD）；
- 有实际项目上线经验，并能量化成果。

---

## 二、面试流程

### ✅ 第一轮（技术电话面）

1. 自我介绍 + 项目讲解（5 分钟）
2. HTML/CSS 基础
   - `position: absolute` 和 `fixed` 区别？
   - 如何实现一个 3 列等高布局？
3. JavaScript 基础
   - 手写 `call`, `apply`, `bind`
   - 实现防抖和节流
   - `setTimeout` 与 `Promise` 的事件循环顺序
4. React
   - 什么是 `useEffect` 的依赖项问题？
   - React 中状态提升的应用场景
5. 小编程题（白板代码）
   - 实现一个数组扁平化函数 `flattenArray`

### ✅ 第二轮（系统设计 + 项目深入）

1. 项目深挖
   - 如何拆分大型前端项目？
   - 微前端方案是否实践过？
   - 如何优化首屏加载速度？
2. 性能优化
   - 懒加载策略、路由懒加载
   - Chrome Performance 面板使用经验
3. 工程化
   - Webpack 和 Vite 对比
   - 如何配置打包分析？tree shaking 实践
4. 编程题
   - LRU 缓存的实现

### ✅ 第三轮（算法 + 综合能力）

1. 中等难度算法题：
   - 有效括号判断
   - 数组滑动窗口最大值
2. 职业规划与问题反问
   - 对前端的长期规划
   - 是否愿意参与跨端（如小程序、Electron）

### ✅ 第四轮（HR）

1. 自我介绍
2. 离职原因
3. 期望薪资
4. 过往项目中的沟通协作经历

---

## 三、总结与建议

### ✅ 技术准备建议

- 扎实掌握 JavaScript 的底层原理（闭包、原型链、事件循环等）
- 熟悉 React 生命周期和 Hooks 的常见问题
- 项目经验要能讲“为什么这么做”，不仅仅是“做了什么”
- 编程题建议熟练刷 LeetCode 的前 100 道题

### ✅ 面试小贴士

- 回答问题要结构清晰，分点作答
- 多用 “我在项目中是怎么做的” 举例来支撑你的回答
- 别怕不知道答案，诚实表达并尝试思考是加分项

---

> 🚀 最后祝大家都能拿到理想的 offer！保持学习、保持思考，前端的世界很精彩！
                """,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 底部信息：日期和浏览量（淡灰色）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "发布日期：2025-07-22",
                    color = Color.Gray
                )
                Text(
                    text = "浏览量：1024",
                    color = Color.Gray
                )
            }
        }
    }
}
