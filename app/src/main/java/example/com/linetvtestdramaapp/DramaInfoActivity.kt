package example.com.linetvtestdramaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.event.EventNetworkStatusChange
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.tool.Util
import kotlinx.android.synthetic.main.activity_drama_info.*
import kotlinx.android.synthetic.main.activity_drama_info.imgDrama
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 「戲劇資訊」頁面
 */
class DramaInfoActivity : AppCompatActivity() {
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetStatusChange(event: EventNetworkStatusChange) {
        Util.snackNetStatus(txvName, event.networkStatus)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_drama_info)
        
        init()
    }

    override fun onPause() {
        super.onPause()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        if(!AppConfig.instance.getAppNetConnected()) {
            //提示網路斷線
            Util.snackNetStatus(txvName, false)
        }
    }

    private fun init() {
        if(!AppConfig.instance.getAppNetConnected()) {
            Util.snackNetStatus(txvName, false)
        }
        
        val drama: Drama = intent.getSerializableExtra("Drama") as Drama
        Util.glideImgLoader(imgDrama, drama.thumb)

        val name = drama.name
        val createdTime = "出版日期: " + Util.dataTimeFormat(drama.created_at)
        val rating = "評分: " + drama.rating.toBigDecimal().toPlainString()
        val totalViews =  "觀看次數: " + drama.total_views.toString()
        txvName.text = name
        txvCreated.text = createdTime
        txvRating.text = rating
        txvTotalViews.text = totalViews
    }
    
}
