package example.com.linetvtestdramaapp.event

import example.com.linetvtestdramaapp.serverApi.Data.Drama

/**
 * 取得戲劇清單事件
 */
class EventDramaList (list: MutableList<Drama>) {

    /**
     * 戲劇清單
     */
    var dramaList : MutableList<Drama> = list
    
}