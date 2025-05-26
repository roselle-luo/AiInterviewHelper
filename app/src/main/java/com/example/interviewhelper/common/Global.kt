package com.example.interviewhelper.common
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.interviewhelper.data.model.UserInfo
import com.example.interviewhelper.data.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("user")

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class GlobalData @Inject constructor(
  @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
) {
    private val TOKEN_KEY = stringPreferencesKey("token")

    val token: MutableStateFlow<String?> = MutableStateFlow("")
    val userInfo = MutableStateFlow(UserInfo())

    init {
        // 在初始化时从 DataStore 加载 token 进内存
        CoroutineScope(Dispatchers.IO).launch {
            getToken()
            if (!token.value.isNullOrEmpty()) {

                try {
                    val response = userRepository.getUserInfo(token.value!!)
                    if (response.code == 200) {
                        userInfo.value = response.data!!
                    } else {
                        Log.e("初始化失败", response.message)
                        withContext (Dispatchers.Main) {
                            Toast.makeText(context, "初始化资源失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("初始化失败", e.toString())
                    withContext (Dispatchers.Main) {
                        Toast.makeText(context, "初始化资源失败: $e", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    suspend fun initData() {
        getToken()
        if (!token.value.isNullOrEmpty()) {
            val userInfo = withContext(Dispatchers.IO) {
                userRepository.getUserInfo(token.value!!).data
            }
            if (userInfo != null) {
                this.userInfo.value = userInfo
            }
        } else {
            Toast.makeText(context, "初始化资源失败", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getToken() {
        val savedToken = context.dataStore.data.map { it[TOKEN_KEY] }.firstOrNull()  // 只获取一次，不会挂起
        token.value = savedToken
    }

    suspend fun saveToken(token: String) = context.dataStore.edit { it[TOKEN_KEY] = token }

    suspend fun clearToken() = context.dataStore.edit { it.remove(TOKEN_KEY) }

}