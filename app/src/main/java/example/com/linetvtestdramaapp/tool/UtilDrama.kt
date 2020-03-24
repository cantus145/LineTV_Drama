package example.com.linetvtestdramaapp.tool

import example.com.linetvtestdramaapp.serverApi.Data.Drama

/**
 * 戲劇過濾工具
 */
object UtilDrama {

    /**
     * 過濾出劇名含有"關鍵字"的戲劇
     */
    fun filterDramaByKey(dramas: MutableList<Drama>, keyword: String): List<Drama> {
        var filterDramaList: List<Drama> = arrayListOf()
        
        //搜尋字串清單集合(如果有" ", 以" "空格拆開成多個字串)
        val keys: List<String> = keyword.split(" ").filter { it.isNotEmpty() } //過濾出有內容的字串
        for (key in keys) {
            //過濾出劇名含有關鍵字的戲劇
            val subListByKey: List<Drama> = dramas.filter { it.name.contains(key) }

            //以聯集加到總清單內
            filterDramaList = ArrayList(filterDramaList.union(subListByKey).distinctBy { it.drama_id })
        }
        return filterDramaList
    }
    
}