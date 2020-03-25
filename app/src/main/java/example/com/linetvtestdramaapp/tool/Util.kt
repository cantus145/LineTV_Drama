package example.com.linetvtestdramaapp.tool

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.voxel.classbox.UI.WrapContentLinearLayoutManager
import example.com.linetvtestdramaapp.R
import example.com.linetvtestdramaapp.config.AppConfig

object Util {

    /**
     * 顯示Toast
     */
    fun mToast(msg: String) {
        Toast.makeText(AppConfig.instance.getAppContext(), msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 用SnackBar顯示網路異常狀態
     */
    fun snackNetStatus(view: View, isNetOK: Boolean) {
        val context: Context = AppConfig.instance.getAppContext()

        /**
         * 開啟手機網路設定
         */
        class CheckWifiConfig : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(AppConfig.instance.getAppContext(), intent, null)
            }
        }

        val msg: String
        val duration: Int
        when (isNetOK) {
            true -> {
                msg = context.resources.getString(R.string.net_is_on) //網路已連線
                duration = Snackbar.LENGTH_SHORT
            }

            false -> {
                msg = context.resources.getString(R.string.net_is_off) //網路已斷線
                //提醒使用者 : 網路未連線SnackBar持續顯示於螢幕上
                duration = Snackbar.LENGTH_INDEFINITE
            }
        }

        val snackBar = Snackbar.make(view, msg, duration)
        if (!isNetOK) {
            //加上使用者 檢查網路 按鈕
            snackBar.setAction(
                context.resources.getString(R.string.check_network),
                CheckWifiConfig()
            ).setActionTextColor(Color.YELLOW)
        }
        snackBar.show()
    }


    /**
     * @return 網路是否有連線
     */
    fun isNetworkAvailable(): Boolean {
        val context = AppConfig.instance.getAppContext()

        var netAvailable = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            netAvailable = connectivityManager.activeNetworkInfo != null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return netAvailable
    }

    /**
     * 關閉軟鍵盤
     */
    fun hideSoftKeyboard(view: View?) {
        //val view = this.getCurrentFocus()
        if (view != null) {
            val imm =
                AppConfig.instance.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 設定線性RecyclerView
     * @param recyclerView
     */
    fun setLinearRecyclerView(recyclerView: RecyclerView) {
        val mLayoutManager = WrapContentLinearLayoutManager(AppConfig.instance.getAppContext())
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    /**
     * Glide圖片載入器
     * @param img 圖形ImageView
     * @param url 圖形url
     */
    fun glideImgLoader(imgView: ImageView, url: String) {
        Glide.with(AppConfig.instance.getAppContext())
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .onlyRetrieveFromCache(false)
            .into(imgView)
    }

    /**
     * RecyclerView資料項目出現的動畫
     */
    fun setAnimation(view: View, animResId: Int?, position: Int, lastPosition: Int): Int {
        animResId ?: return position

        return when {
            position > lastPosition -> {
                // If the bound view wasn't previously displayed on screen, it's animated
                view.startAnimation(
                    AnimationUtils.loadAnimation(
                        AppConfig.instance.getAppContext(), animResId
                    )
                )
                position
            }
            else -> lastPosition
        }
    }

    /**
     * 日期時間format
     */
    fun dataTimeFormat(dateTime: String): String {
        // "2017-11-23T02:04.000Z"
        return dateTime.replace("T", "  ").replace(".000Z", "")
    }
}