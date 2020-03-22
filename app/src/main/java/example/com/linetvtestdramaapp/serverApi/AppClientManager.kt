package example.com.linetvtestdramaapp.serverApi

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET

class AppClientManager private constructor() {
    var server: String = "https://static.linetv.tw/interview/"

    var retrofit: Retrofit
    private val okHttpClient = OkHttpClient()

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(server)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * 取得戲戲劇表單介面
     */
    interface DramasApiService {
        @GET("dramas-sample.json")
        fun index(): Call<JsonObject>
    }
    
    companion object { /* java static變數 companion伴侶物件 */
        private val manager = AppClientManager()
        val client: Retrofit get() = manager.retrofit
    }
    
}