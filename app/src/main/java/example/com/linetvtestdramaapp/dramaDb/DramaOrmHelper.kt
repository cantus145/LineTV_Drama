package example.com.linetvtestdramaapp.dramaDb

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.serverApi.Data.SearchKey
import java.sql.SQLException

/**
 * 戲劇資料庫
 */
class DramaOrmHelper(context: Context?, factory: SQLiteDatabase.CursorFactory?) 
    : OrmLiteSqliteOpenHelper(context, "drama.db", factory, 1) {

    //戲劇清單表Dao
    private var dramaDao: Dao<Drama, Int>? = null
    
    //使用者搜尋的關鍵字Dao
    private var searchKeyDao: Dao<SearchKey, String>? = null

    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        try {
            TableUtils.createTable<Drama>(connectionSource, Drama::class.java)
            TableUtils.createTable<SearchKey>(connectionSource, SearchKey::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        
    }

    override fun onUpgrade(
        database: SQLiteDatabase?, connectionSource: ConnectionSource?,
        oldVersion: Int, newVersion: Int
    ) {
    }
    
    /**
     * 取得 dramaDao
     */
    @Throws(SQLException::class)
    fun getDramaDao(): Dao<Drama, Int> {
        dramaDao?.let {
            //do nothing
        } ?: run {
            dramaDao =
                getDao<Dao<Drama, Int>, Drama>(Drama::class.java)
        }
        return dramaDao as Dao<Drama, Int>
    }

    /**
     * 取得 searchKeyDao
     */
    @Throws(SQLException::class)
    fun getSearchKeyDao(): Dao<SearchKey, String> {
        searchKeyDao?.let {
            //do nothing
        } ?: run {
            searchKeyDao = 
                getDao<Dao<SearchKey, String>, SearchKey>(SearchKey::class.java)
        }
        return searchKeyDao as Dao<SearchKey, String>
    }

}