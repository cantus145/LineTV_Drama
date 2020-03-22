package example.com.linetvtestdramaapp.config

import android.app.Application
import android.content.Context
import com.j256.ormlite.dao.Dao
import example.com.linetvtestdramaapp.dramaDb.DramaOrmHelper
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.serverApi.Data.SearchKey
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
     * 寫入網路是否連線
     */
    fun setAppNetConnected(bo: Boolean) {
        appNetConnected = bo
    }

    /**
     * 取回網路是否連線
     */
    fun getAppNetConnected(): Boolean {
        return appNetConnected
    }

    /**
     * @return 取得DramaDao
     */
    fun getDramaDaoInstance(): Dao<Drama, Int> {
        return dramaDao
    }

    /**
     * @return 取得searchKeyDao
     */
    fun getSearchKeyDaoInstance(): Dao<SearchKey, String> {
        return searchKeyDao
    }
}