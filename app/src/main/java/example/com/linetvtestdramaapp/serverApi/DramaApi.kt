package example.com.linetvtestdramaapp.serverApi

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import example.com.linetvtestdramaapp.event.EventDramaList
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject


/**
 * 從Server 取得戲劇資料Api
 */
object DramaApi {
    val TAG: String = "TAG"

    private var isOkHttpInitialized: Boolean = false
    private lateinit var okHttpClient: OkHttpClient

    ///
    private const val contentTypeStr = "application/json; charset=utf-8"
    private val contentType = MediaType.parse(contentTypeStr)
    ///

    private val dramaListType = object : TypeToken<List<Drama>>() {}.type

    /**
     * 取得戲劇列表
     */
    fun getDramaList() {
        val apiService =
            AppClientManager.client.create(AppClientManager.DramasApiService::class.java)

        apiService.index().enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(
                call: retrofit2.Call<JsonObject>,
                response: retrofit2.Response<JsonObject>
            ) {
                val jsonArray: JSONArray =
                    JSONObject(response.body()!!.toString()).getJSONArray("data")
                val dramaList: MutableList<Drama> =
                    Gson().fromJson(jsonArray.toString(), dramaListType)
                try {
                    EventBus.getDefault().post(EventDramaList(dramaList.toMutableList()))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.e("tag", "onFailure")
            }
        })
    }

    /**
     * 取得戲劇列表
     */
    /*
    fun getDramaList_okhttp() {
        val fullUrl = "https://static.linetv.tw/interview/dramas-sample.json"
        captureData(fullUrl)
    }
    
    private fun captureData(fullUrl: String) {
        Log.d(TAG, "fullUrl = $fullUrl")

        if (Util.isNetworkAvailable()) {
            mSSLOkHttpClient().newCall(
                genGetRequest(genRequestBody(null), fullUrl)
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, e.toString())
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseBodyStr: String = response.body()!!.string()
                    Log.d(TAG, responseBodyStr)
                    dramaDataHandle(responseBodyStr)
                }
            })
        } else {
            Log.e(TAG, "網路不通")
            Log.e(TAG, "例外處理")
        }
    }
    */

    /**
     * 戲劇JSON資料處理
     */
    fun dramaDataHandle(jsonBodyStr: String) {
        //string -> jObj -> jsonArray -> string -> list
        val jsonArray: JSONArray = JSONObject(jsonBodyStr).getJSONArray("data")
        val dramaList: MutableList<Drama> = Gson().fromJson(jsonArray.toString(), dramaListType)
        EventBus.getDefault().post(EventDramaList(dramaList))
    }

    /**
     * @return 產生OkHttpClient
     */
    private fun mSSLOkHttpClient(): OkHttpClient {
        if (!isOkHttpInitialized) {
            synchronized(DramaApi::class.java) {
                okHttpClient = OkHttpClient()
                okHttpClient.retryOnConnectionFailure()
                isOkHttpInitialized = true
            }
        }
        return okHttpClient
    }

    /**
     * @param requestBody
     * @param strUrl
     * @return 產生GET的request
     */
    private fun genGetRequest(requestBody: RequestBody?, strUrl: String): Request {
        val request: Request
        val headers = genHttpHeader()
        request = if (requestBody != null) {
            Request.Builder()
                .post(requestBody)
                .url(strUrl)
                .headers(headers)
                .build()
        } else {
            Request.Builder()
                .url(strUrl)
                .get()
                .headers(headers)
                .build()
        }
        return request
    }

    private fun genHttpHeader(): Headers {
        val okHeader: Headers
        val builder = Headers.Builder()
        builder.add("Content-Type", contentTypeStr)
        okHeader = builder.build()
        return okHeader
    }

    /**
     * @param mediaTypeStr
     * @return 產生 GET RequestBody
     */
    private fun genRequestBody(mediaTypeStr: String?): RequestBody? {
        return if (mediaTypeStr != null) {
            RequestBody.create(contentType, "")
        } else {
            null
        }
    }

}
