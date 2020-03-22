package example.com.linetvtestdramaapp.tool

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.voxel.classbox.UI.WrapContentLinearLayoutManager
import example.com.linetvtestdramaapp.config.AppConfig

object Util {

    /**
     * Snackbar顯示網路變化
     */
    fun snackNetStatus(view: View, isNetOn: Boolean) {
        val msg: String
        val duration: Int
        when (isNetOn) {
            true -> {
                msg = "網路已連線!"
                duration = Snackbar.LENGTH_SHORT
            }
            
            false -> {
                msg = "網路已斷線!"
                //為提醒使用者 : 網路未連線SnackBar不會消失
                duration = Snackbar.LENGTH_INDEFINITE
            }
        }
        Snackbar.make(view, msg, duration).show()
    }

    /**
     * 顯示Toast
     */
    fun mToast(msg: String) {
        Toast.makeText(AppConfig.instance.getAppContext(), msg, Toast.LENGTH_SHORT).show()
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
}