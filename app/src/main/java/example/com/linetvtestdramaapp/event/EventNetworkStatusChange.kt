package example.com.linetvtestdramaapp.event

/**
 * 網路連線變更事件
 */
class EventNetworkStatusChange(bool :Boolean) {

    /**
     * 網路連線狀態
     * true:連線
     * false:斷線
     */
    var networkStatus = bool
    
}