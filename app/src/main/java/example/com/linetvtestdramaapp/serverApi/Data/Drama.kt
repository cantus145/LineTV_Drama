package example.com.linetvtestdramaapp.serverApi.Data

import java.io.Serializable
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 *  (1)戲劇物件
 *  (2)OrmLite database table
 */
@DatabaseTable(tableName = "SearchDramaTable")
data class Drama(

    /**
     * 戲劇ID (主鍵)
     */
    @DatabaseField(columnName = "drama_id", id = true)
    @SerializedName("drama_id")
    var drama_id: Int = 1,

    /**
     * 戲劇名稱
     */
    @DatabaseField(columnName = "name", canBeNull = false)
    @SerializedName("name")
    var name: String = "",


    /**
     * 出版日期
     */
    @DatabaseField(columnName = "created_at", canBeNull = false)
    @SerializedName("created_at")
    var created_at: String = "",

    /**
     * 觀看次數
     */
    @DatabaseField(columnName = "total_views", canBeNull = false)
    @SerializedName("total_views")
    var total_views: Int = 1,

    /**
     * 縮圖
     */
    @DatabaseField(columnName = "thumb", canBeNull = false)
    @SerializedName("thumb")
    var thumb: String = "",

    /**
     * 評分
     */
    @DatabaseField(columnName = "rating", canBeNull = false)
    @SerializedName("rating")
    var rating: Double = 1.0001

) : Serializable


    
    

