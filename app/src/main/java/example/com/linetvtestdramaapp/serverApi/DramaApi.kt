package example.com.linetvtestdramaapp.serverApi

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import org.json.JSONArray
import org.json.JSONObject


/**
 * 從Server 取得戲劇資料Api
 */
object DramaApi {
    val TAG: String = "TAG"

    private val dramaListType = object : TypeToken<List<Drama>>() {}.type

    /**
     * 取得戲劇列表
     */
    fun getDramaList(callback: (MutableList<Drama>) -> Unit) {
        val apiService =
            AppClientManager.client.create(AppClientManager.DramasApiService::class.java)

        apiService.index().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: retrofit2.Call<JsonObject>,
                response: retrofit2.Response<JsonObject>) {
                val jsonArray: JSONArray = JSONObject(response.body()!!.toString()).getJSONArray("data")
                val dramaList: MutableList<Drama> =
                    Gson().fromJson(jsonArray.toString(), dramaListType)

                //Kotlin協程(以同步的方式操作非同步的事)
                callback(dramaList)
            }
            
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.e("tag", "onFailure")
            }
        })
    }

}
