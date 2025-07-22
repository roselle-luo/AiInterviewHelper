import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.interviewhelper.ui.component.TopBar
import com.example.interviewhelper.ui.theme.Aqua80
import com.example.interviewhelper.ui.theme.LightBlue40
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun StatictisScreen(navController: NavController) {
    val labels = listOf(
        "专业知识",
        "技能匹配",
        "语言表达",
        "逻辑思维",
        "创新能力",
        "应变抗压"
    )

    val values = listOf(80f, 70f, 85f, 75f, 90f, 65f)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TopBar(title = "面试结果报告", navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        RadarChartView(labels = labels, values = values)

        MarkdownText(
            markdown = "# \uD83D\uDCCA 面试分析报告\n" +
                    "\n" +
                    "## \uD83C\uDFAF 候选人综合评估\n" +
                    "\n" +
                    "| 指标         | 分数（满分 100） | 评估说明                                               |\n" +
                    "| ------------ | ---------------- | ------------------------------------------------------ |\n" +
                    "| 专业知识水平 | 80               | 候选人具备扎实的专业基础，知识点掌握全面，能灵活运用。 |\n" +
                    "| 技能匹配度   | 70               | 技术栈与岗位需求较为吻合，仍有提升空间。               |\n" +
                    "| 语言表达能力 | 85               | 表达清晰、逻辑通顺，能准确阐述自己的观点。             |\n" +
                    "| 逻辑思维能力 | 75               | 分析问题具备一定逻辑性，但在多维度推理上稍显不足。     |\n" +
                    "| 创新能力     | 90               | 能够提出独到见解，并尝试新思路解决问题。               |\n" +
                    "| 应变抗压能力 | 65               | 面对突发问题略显紧张，处理略显保守。                   |\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "## \uD83E\uDDE0 面试官综合评价\n" +
                    "\n" +
                    "候选人在 **专业知识** 和 **语言表达** 方面表现突出，能清晰阐述自己的经验与项目背景；在 **创新能力** 上也有惊喜表现，展现出对问题的深入思考和解决问题的创造性。\n" +
                    "\n" +
                    "尽管在 **应变抗压能力** 上表现稍弱，面对压力场景时存在一定紧张情绪，但整体表现仍属优异。\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "## ✅ 优势亮点\n" +
                    "\n" +
                    "- \uD83C\uDF1F **创新意识强**：有主动提出优化方案的能力；\n" +
                    "- \uD83D\uDDE3 **沟通表达佳**：逻辑清晰，能有效传达技术思路；\n" +
                    "- \uD83D\uDCDA **专业基础扎实**：掌握核心知识点，实战经验丰富。\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "## ⚠\uFE0F 建议提升方向\n" +
                    "\n" +
                    "- \uD83D\uDCA1 **加强抗压训练**：可以通过模拟压力环境训练临场反应；\n" +
                    "- \uD83E\uDDE0 **提升复杂问题的拆解能力**：锻炼多角度分析和系统性解决问题的能力；\n" +
                    "- \uD83D\uDD0D **深入拓展关键技能点**：根据岗位要求补足某些技术盲区。\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "## \uD83C\uDFC1 总评建议\n" +
                    "\n" +
                    "| 是否推荐 | 推荐程度 | 评语                                           |\n" +
                    "| -------- | -------- | ---------------------------------------------- |\n" +
                    "| ✅ 是     | ⭐⭐⭐⭐☆    | 综合能力较强，具备岗位胜任力，建议进入下一轮。 |\n" +
                    "\n"
        )

    }
}

@Composable
fun RadarChartView(labels: List<String>, values: List<Float>) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        factory = {
            RadarChart(context).apply {
                description.isEnabled = false
                webLineWidth = 1f
                webColor = Color.GRAY
                webLineWidthInner = 1f
                webColorInner = Color.LTGRAY
                webAlpha = 100

                // 数据集
                val entries = values.map { RadarEntry(it) }

                val dataSet = RadarDataSet(entries, "评估").apply {
                    color = Aqua80.toArgb()
                    fillColor = LightBlue40.toArgb()
                    setDrawFilled(true)
                    fillAlpha = 180
                    lineWidth = 2f
                    isDrawHighlightCircleEnabled = true
                    setDrawHighlightIndicators(false)
                }

                val radarData = RadarData(dataSet).apply {
                    setValueTextSize(14f)
                    setDrawValues(true)
                    setValueTextColor(Color.BLACK)
                }

                data = radarData

                // X轴：标签
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(labels)
                    textSize = 14f
                    textColor = Color.BLACK
                    yOffset = 0f
                    xOffset = 0f
                    position = XAxis.XAxisPosition.TOP_INSIDE
                }

                // Y轴：数值线
                yAxis.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    setLabelCount(5, true)
                    textColor = Color.DKGRAY
                    textSize = 12f
                }

                legend.isEnabled = false

                invalidate()
            }
        }
    )
}
