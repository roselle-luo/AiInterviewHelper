package com.example.interviewhelper

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.interviewhelper.common.GlobalData
import com.example.interviewhelper.navigation.AppNavHost
import com.example.interviewhelper.ui.theme.InterviewHelperTheme
import com.iflytek.sparkchain.core.SparkChain
import com.iflytek.sparkchain.core.SparkChainConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var globalData: GlobalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val config = SparkChainConfig.builder()
            .appID("faae76c6")
            .apiKey("018a2c283da3e2d91ef7ee4e5e3a1817")
            .apiSecret("ZTNjODZlOTZjYjI4NDVhZmRiNGVjMmZh")
        val ret = SparkChain.getInst().init(this, config)
        var result: String?
        if (ret == 0) {
            result = "SDK初始化成功,请选择相应的功能点击体验。"
        } else {
            result = "SDK初始化失败,错误码:" + ret
        }
        Log.d("SparkChain", result)
        setContent {
            InterviewHelperTheme {
                AppNavHost(globalData = globalData)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        SparkChain.getInst().unInit()
        Log.d("SparkChain", "资源已经释放")
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InterviewHelperTheme {
        Greeting("Android")
    }
}