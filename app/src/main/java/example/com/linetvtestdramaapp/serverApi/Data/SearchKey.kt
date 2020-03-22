package example.com.linetvtestdramaapp.serverApi.Data

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * 紀錄使用者搜尋的關鍵字
 */
@DatabaseTable(tableName = "SearchKeyTable")
class SearchKey {
    
    /**
     * 主鍵 
     */
    @DatabaseField(columnName = "id", id = true)
    val id: Int = 1

    /**
     * 搜尋關鍵字
     */
    @DatabaseField(columnName = "keyWord", canBeNull = false)
    var key: String = ""

}