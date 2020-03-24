package example.com.linetvtestdramaapp

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.tool.UtilDrama
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.a ndroid.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    val TAG = "KotlinUnit"

    private lateinit var context: Context

    @Before
    fun setup() {
        Log.d(TAG, "Before")
        context = InstrumentationRegistry.getInstrumentation().context

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("example.com.linetvtestapp_albert", appContext.packageName)
    }

    /**
     * 戲劇總數量
     */
    @Test
    fun dramaTotalSize() {
        val dramList: List<Drama> = genLocalDramas()
        Assert.assertTrue(dramList.isNotEmpty())
        assertEquals(6, dramList.size)
    }

    /**
     * 測試關鍵字過濾戲劇數量
     */
    @Test
    fun testDramaFilterSize() {
        assertEquals(sizeOfFilterDramaByKey("好 士 101"), 4)
        assertEquals(sizeOfFilterDramaByKey("101"), 1)
        assertEquals(sizeOfFilterDramaByKey("黑"), 1)
        assertEquals(sizeOfFilterDramaByKey("事"), 1)
        assertEquals(sizeOfFilterDramaByKey("好"), 2)
        assertEquals(sizeOfFilterDramaByKey("事 好"), 3)
        assertEquals(sizeOfFilterDramaByKey("事 務 所 好"), 3)
        assertEquals(sizeOfFilterDramaByKey("1 了"), 2)
        assertEquals(sizeOfFilterDramaByKey("果如"), 0)
        assertEquals(sizeOfFilterDramaByKey("10101"), 0)
    }

    /**
     * 關鍵字過濾戲劇的數量
     */
    private fun sizeOfFilterDramaByKey(userInput: String): Int {
        return UtilDrama.filterDramaByKey(genLocalDramas(), userInput).size
    }

    private fun genLocalDramas(): MutableList<Drama> {
        val dramaStr = "{\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"drama_id\": 1,\n" +
                "            \"name\": \"致我們單純的小美好\",\n" +
                "            \"total_views\": 23562274,\n" +
                "            \"created_at\": \"2017-11-23T02:04:39.000Z\",\n" +
                "            \"thumb\": \"https://i.pinimg.com/originals/61/d4/be/61d4be8bfc29ab2b6d5cab02f72e8e3b.jpg\",\n" +
                "            \"rating\": 4.4526\n" +
                "        },\n" +
                "        {\n" +
                "            \"drama_id\": 2,\n" +
                "            \"name\": \"獅子王強大\",\n" +
                "            \"total_views\": 14611380,\n" +
                "            \"created_at\": \"2017-12-02T03:34:41.000Z\",\n" +
                "            \"thumb\": \"https://i.pinimg.com/originals/12/ce/16/12ce16bd184253837326599b26e16c44.jpg\",\n" +
                "            \"rating\": 4.3813\n" +
                "        },\n" +
                "        {\n" +
                "            \"drama_id\": 3,\n" +
                "            \"name\": \"如果有妹妹就好了\",\n" +
                "            \"total_views\": 2931180,\n" +
                "            \"created_at\": \"2017-10-21T12:34:41.000Z\",\n" +
                "            \"thumb\": \"https://i.pinimg.com/originals/32/c1/7a/32c17af1c085be75657e965954f8d601.jpg\",\n" +
                "            \"rating\": 4.0647\n" +
                "        },\n" +
                "        {\n" +
                "            \"drama_id\": 5,\n" +
                "            \"name\": \"黑騎士\",\n" +
                "            \"total_views\": 7473757,\n" +
                "            \"created_at\": \"2017-12-06T22:09:45.000Z\",\n" +
                "            \"thumb\": \"https://s-media-cache-ak0.pinimg.com/originals/88/2c/dc/882cdca85526dfb9d9f03cf192c0846c.png\",\n" +
                "            \"rating\": 4.2167\n" +
                "        },\n" +
                "        {\n" +
                "            \"drama_id\": 6,\n" +
                "            \"name\": \"出境事務所\",\n" +
                "            \"total_views\": 79310,\n" +
                "            \"created_at\": \"2017-10-21T12:34:41.000Z\",\n" +
                "            \"thumb\": \"https://s-media-cache-ak0.pinimg.com/564x/30/35/49/30354912dd432a2475428a003661784a.jpg\",\n" +
                "            \"rating\": 4.2667\n" +
                "        },\n" +
                "        {\n" +
                "            \"drama_id\": 10,\n" +
                "            \"name\": \"101次求婚\",\n" +
                "            \"total_views\": 239001,\n" +
                "            \"created_at\": \"2017-11-08T16:40:06.000Z\",\n" +
                "            \"thumb\": \"https://i.pinimg.com/originals/61/67/b2/6167b2d94d305203b43a230874139dab.jpg\",\n" +
                "            \"rating\": 4.0777\n" +
                "        }\n" +
                "    ]\n" +
                "}"

        val dramaListType = object : TypeToken<List<Drama>>() {}.type
        val jsonArray: JSONArray = JSONObject(dramaStr).getJSONArray("data")
        return Gson().fromJson(jsonArray.toString(), dramaListType)
    }

}
