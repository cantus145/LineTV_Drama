package example.com.linetvtestdramaapp.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.event.EventNetworkStatusChange
import example.com.linetvtestdramaapp.tool.Util
import org.greenrobot.eventbus.EventBus

/**
 * 觀察網路狀態變化
 */
class NetworkStatusReceiver : BroadcastReceiver() {

    /**
     * 接收廣播通知:網路連線狀態
     * (不同手機 觸發次數會不同 必須用AppConfig儲存狀態)
     */
    override fun onReceive(context: Context, ıntent: Intent) {
        val newConnected = Util.isNetworkAvailable()
        
        if (AppConfig.instance.getAppNetConnected() != newConnected) {
            
            //寫入最新網路狀態到AppConfig
            AppConfig.instance.setAppNetConnected(newConnected)
            
            Log.d("BroadNETWORK", "changed $newConnected")
            EventBus.getDefault().post(EventNetworkStatusChange(newConnected))
        }         
    }

}