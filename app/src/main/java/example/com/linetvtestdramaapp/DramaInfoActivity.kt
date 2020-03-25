package example.com.linetvtestdramaapp

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.event.EventNetworkStatusChange
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.tool.Util
import kotlinx.android.synthetic.main.activity_drama_info.*
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

        if (!AppConfig.instance.getAppNetConnected()) {
            //提示網路斷線
            Util.snackNetStatus(txvName, false)
        }
    }

    private fun init() {
        if (!AppConfig.instance.getAppNetConnected()) {
            Util.snackNetStatus(txvName, false)
        }

        val drama: Drama = intent.getSerializableExtra("Drama") as Drama
        Util.glideImgLoader(imgDrama, drama.thumb)

        //名稱
        val name = drama.name

        //評分
        val rating =
            getString(R.string.rating) + ": " + drama.rating.toBigDecimal().toPlainString()
        //出版日期
        val createdTime =
            getString(R.string.create_date) + ": " + Util.dataTimeFormat(drama.created_at)
        //觀看次數
        val totalViews =
            getString(R.string.total_views) + ": " + drama.total_views.toString()

        txvName.text = name
        txvRating.text = rating
        txvCreated.text = createdTime
        txvTotalViews.text = totalViews
    }

}
