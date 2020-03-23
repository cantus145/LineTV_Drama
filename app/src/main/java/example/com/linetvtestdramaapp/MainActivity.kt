package example.com.linetvtestdramaapp

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.common.collect.Lists
import com.j256.ormlite.dao.Dao
import example.com.linetvtestdramaapp.Adapter.DramaAdapter
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.event.EventNetworkStatusChange
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.serverApi.Data.SearchKey
import example.com.linetvtestdramaapp.serverApi.DramaApi
import example.com.linetvtestdramaapp.tool.Util
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 列出所有資料中的戲劇頁面,並可以可以關鍵字過濾戲劇
 */
class MainActivity : AppCompatActivity() {
    
    private val dramaAdapter: DramaAdapter = DramaAdapter()
    
    /**
     * 網路連線狀態
     */
    private var localNetConnected: Boolean = false
    
    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetStatusChange(event: EventNetworkStatusChange) {
        when (event.networkStatus) {
            true -> {
                if (!localNetConnected) {
                    Util.snackNetStatus(btnLoadDramas, true)
                    localNetConnected = true
                }

                //如果戲劇資料庫還沒有資料,且網路已回復,現在抓取戲劇資料
                if (getDramaDao().queryForAll().isEmpty()) {
                    DramaApi.getDramaList { receivedDramasFromServer(it) }
                }
            }
            false -> {
                if (localNetConnected) {
                    localNetConnected = false
                    Util.snackNetStatus(btnLoadDramas, false)
                }
            }
        }
    }
    //EventBus
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        localNetConnected = AppConfig.instance.getAppNetConnected()
        if (!localNetConnected) {
            //提示網路斷線
            Util.snackNetStatus(btnLoadDramas, false)
        }
    }

    /**
     * 接收server端回傳的戲劇清單
     */
    private fun receivedDramasFromServer(dramas: MutableList<Drama>) {
        updateDramaAdapter(dramas)
        for (drama in dramas) {
            //存入資料庫
            getDramaDao().createOrUpdate(drama)
        }
    }

    /**
     * 更新戲劇Adapter
     */
    private fun updateDramaAdapter(dramas: MutableList<Drama>) {
        dramaAdapter.clearList()
        dramaAdapter.addDramaList(dramas)
        dramaAdapter.notifyDataSetChanged()
    }

    /**
     * 主頁面初始化
     */
    private fun init() {
        AppConfig.instance.refreshAppNetConnected()
        localNetConnected = AppConfig.instance.getAppNetConnected()

        Util.setLinearRecyclerView(dramaRecyclerView)
        dramaRecyclerView.adapter = dramaAdapter

        initSearchBar()
        initBtnLoadDramas()

        when (getDramaDao().queryForAll().isEmpty()) {
            //戲劇資料庫無資料,嘗試從server取回戲劇資料
            true -> {
                if (localNetConnected) {
                    DramaApi.getDramaList { receivedDramasFromServer(it) }
                }
            }
            //戲劇資料庫有資料
            else -> {
                updateDramaAdapter(getDramaDao().queryForAll())
            }
        }

        val searchKeys = getSearchKeyDao().queryForAll()
        if (searchKeys.isNotEmpty()) {
            val keyword = searchKeys[0].key
            updateSearchResult(keyword, true)
        }

        if (!localNetConnected) {
            // 啟動APP時網路已斷線提示
            Util.snackNetStatus(btnLoadDramas, false)
        }
    }

    /**
     * 初始化搜尋Bar
     */
    private fun initSearchBar() {
        //搜尋框         
        searchView.queryHint = "請輸入搜尋關鍵字"
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                val userInput: String = searchView.query.toString().trim()

                if (userInput.isNotEmpty()) {
                    updateSearchResult(userInput, false)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val userInput: String = searchView.query.toString().trim()
                when (userInput.isNotEmpty()) {
                    true -> updateSearchResult(userInput, true)
                    false -> Util.mToast("請輸入關鍵字!")
                }
                return false
            }
        })         
    }
    
    
    /**
     * 初始化載入戲劇清單按鈕
     */
    private fun initBtnLoadDramas() {
        btnLoadDramas.setOnClickListener {
            Util.hideSoftKeyboard(btnLoadDramas)
            when (localNetConnected) {
                //有網路時 
                true -> DramaApi.getDramaList { receivedDramasFromServer(it) }
                
                //無網路時,從資料庫取戲劇資料
                else -> {
                    updateDramaAdapter(getDramaDao().queryForAll())
                    Util.mToast("網路已斷線! \n列表為本機儲存資料")
                }
            }
        }
    }

    /**
     * 搜尋結果更新到Adapter
     * userInput: 使用者輸入的字串
     * isClickSubmit: 使用者是否按下搜尋按鈕
     */
    private fun updateSearchResult(userInput: String, isClickSubmit: Boolean) {
        val dramas: MutableList<Drama> = getDramaDao().queryForAll()
        var searchList: List<Drama> = Lists.newArrayList()

        //搜尋字串清單集合(以" "空格拆開)
        val keys: List<String> = userInput.split(" ").filter { it.isNotEmpty() } //過濾出有內容的字串
        for (key in keys) {
            //過濾出劇名含有關鍵字的戲劇
            val keywordList: List<Drama> = dramas.filter { it.name.contains(key) }

            //以聯集加到總清單內
            searchList = ArrayList(searchList.union(keywordList).distinctBy { it.drama_id })
        }

        when (searchList.isNotEmpty()) {
            true -> {
                //關鍵字有找到符合的戲劇=>才把關鍵字存入資料庫
                val searchKey = SearchKey()
                searchKey.key = userInput
                getSearchKeyDao().createOrUpdate(searchKey)
            }
            false -> if (isClickSubmit) Util.mToast("找不到符合的戲劇!")
        }

        //最後更新到戲劇Adapter
        updateDramaAdapter(searchList.toMutableList())
    }

    /**
     * 戲劇Dao
     */
    private fun getDramaDao(): Dao<Drama, Int> {
        return AppConfig.instance.getDramaDao()
    }

    /**
     * 查詢字Dao
     */
    private fun getSearchKeyDao(): Dao<SearchKey, String> {
        return AppConfig.instance.getSearchKeyDao()
    }
 
}
