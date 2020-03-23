package example.com.linetvtestdramaapp.config

import android.app.Application
import android.content.Context
import com.j256.ormlite.dao.Dao
import example.com.linetvtestdramaapp.dramaDb.DramaOrmHelper
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.serverApi.Data.SearchKey
import example.com.linetvtestdramaapp.tool.Util
import java.sql.SQLException

class AppConfig: Application() {

    /**
     * 取得 Application Context
     */
    fun getAppContext(): Context = applicationContext
    
    ////////////////////////////////////////////////////////////
    //戲劇資料庫
    private lateinit var dramaOrmHelper: DramaOrmHelper
    private lateinit var dramaDao: Dao<Drama, Int>
    private lateinit var searchKeyDao: Dao<SearchKey, String>
    ////////////////////////////////////////////////////////////
            
    companion object { 
        lateinit var instance: AppConfig 
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        dramaOrmHelper = DramaOrmHelper(this, null)
        try {
            dramaDao = dramaOrmHelper.getDramaDao()
            searchKeyDao = dramaOrmHelper.getSearchKeyDao()
        } catch (e : SQLException) {
            e.printStackTrace()
        }
    }

    /**
     * 手機網路是否連線
     */
    private var appNetConnected: Boolean = false

    /**
     * 更新網路是否連線
     */
    fun refreshAppNetConnected() {
        appNetConnected = Util.isNetworkAvailable()
    }
    
    /**
     * 寫入網路是否連線
     */
    fun setAppNetConnected(bool: Boolean) {
        appNetConnected = bool
    }

    /**
     * 取回網路是否連線
     */
    fun getAppNetConnected(): Boolean {
        val networkNow = Util.isNetworkAvailable()
        if(appNetConnected != networkNow) {
            appNetConnected = networkNow
        }
        
        return appNetConnected
    }

    /**
     * @return 取得DramaDao
     */
    fun getDramaDao(): Dao<Drama, Int> {
        return dramaDao
    }

    /**
     * @return 取得searchKeyDao
     */
    fun getSearchKeyDao(): Dao<SearchKey, String> {
        return searchKeyDao
    }
}