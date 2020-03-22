package example.com.linetvtestdramaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.j256.ormlite.dao.Dao
import example.com.linetvtestdramaapp.Adapter.DramaAdapter
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.event.EventDramaList
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.serverApi.DramaApi
import example.com.linetvtestdramaapp.tool.Util
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.google.common.collect.Lists
import example.com.linetvtestdramaapp.event.EventNetworkStatusChange
import example.com.linetvtestdramaapp.serverApi.Data.SearchKey

class MainActivity : AppCompatActivity() {

    private val dramaAdapter: DramaAdapter = DramaAdapter()

    /**
     * 網路連線狀態
     */
    private var localNetConnected: Boolean = Util.isNetworkAvailable()

    //EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStatusChange(event: EventNetworkStatusChange) {
        when (event.networkStatus) {
            true -> {
                if (!localNetConnected) {
                    Snackbar.make(btnLoadDramas, "網路已連線", Snackbar.LENGTH_SHORT).show()
                    localNetConnected = true
                }

                //如果戲劇資料庫沒有第一次抓的資料,且網路已回復,現在抓取戲劇資料
                if (getDramaDao().queryForAll().isEmpty()) {
                    DramaApi.getDramaList()
                }
            }
            false -> {
                if (localNetConnected) {
                    localNetConnected = false
                    showSnackBarNetworkOff()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDramaList(event: EventDramaList) {
        val list = event.dramaList
        updateDramaAdapter(list)
        for (drama in list) {
            //存入資料庫
            getDramaDao().createOrUpdate(drama)
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
        localNetConnected = Util.isNetworkAvailable()
        AppConfig.instance.setAppNetConnected(localNetConnected)
        
        Util.setLinearRecyclerView(dramaRecyclerView)
        dramaRecyclerView.adapter = dramaAdapter

        initSearchBar()
        initBtnLoadDramas()

        when (getDramaDao().queryForAll().isEmpty()) {
            //戲劇資料庫無資料,嘗試從server取回戲劇資料
            true -> {
                when (localNetConnected) {
                    true -> DramaApi.getDramaList()
                    false -> showSnackBarNetworkOff()
                }
            }
            //資料庫有資料
            else -> {
                updateDramaAdapter(getDramaDao().queryForAll())
            }
        }

        val searchKeys = getSearchKeyDao().queryForAll()
        if (searchKeys.isNotEmpty()) {
            val keyword = searchKeys[0].key
            updateSearchResult(keyword)
        }
        
        if(!localNetConnected) {
            //啟動時網路已斷線提示
            showSnackBarNetworkOff()
        }
    }

    /**
     * 初始化搜尋Bar
     */
    private fun initSearchBar() {
        //搜尋框
        searchView.queryHint = "輸入搜尋關鍵字"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val userInput: String = searchView.query.toString()
                updateSearchResult(userInput)
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
                true -> DramaApi.getDramaList()

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
     */
    private fun updateSearchResult(userInput: String) {
        val dramas: MutableList<Drama> = getDramaDao().queryForAll()
        var searchList: List<Drama> = Lists.newArrayList()

        //搜尋字串集合(以" "空格拆開)
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
            false -> Util.mToast("找不到符合的戲劇!")
        }
        updateDramaAdapter(searchList.toMutableList())
    }

    /**
     * 提醒使用者:顯示不會消失的網路未連線SnackBar
     */
    private fun showSnackBarNetworkOff() {
        Snackbar.make(btnLoadDramas, "網路已斷線", Snackbar.LENGTH_INDEFINITE).show()
    }

    /**
     * 戲劇Dao
     */
    private fun getDramaDao(): Dao<Drama, Int> {
        return AppConfig.instance.getDramaDaoInstance()
    }

    /**
     * 查詢字Dao
     */
    private fun getSearchKeyDao(): Dao<SearchKey, String> {
        return AppConfig.instance.getSearchKeyDaoInstance()
    }

}
