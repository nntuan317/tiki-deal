package vn.tiki.android.androidhometest

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.di.initDependencies
import vn.tiki.android.androidhometest.di.inject
import vn.tiki.android.androidhometest.di.releaseDependencies
import java.lang.ref.WeakReference
import android.support.v7.widget.DividerItemDecoration
import android.view.View


class MainActivity : AppCompatActivity() {

    val apiServices by inject<ApiServices>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val asyncTask = LoadData(apiServices, this)
        asyncTask.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseDependencies()
    }

    class LoadData(apiService: ApiServices, activity: Activity) : AsyncTask<Unit, Unit, List<Deal>>() {
        private val mApiService = apiService
        private val mActivityRef = WeakReference<Activity>(activity)

        override fun doInBackground(vararg p0: Unit?): List<Deal> {
            return mApiService.getDeals()
        }

        override fun onPostExecute(result: List<Deal>?) {
            mActivityRef.get()?.let {
                it.progress.visibility = View.GONE
                val viewManager = GridLayoutManager(it, 2)
                val viewAdapter = DealAdapter(result.orEmpty())

                val drawable = ContextCompat.getDrawable(it, R.drawable.grid_divider)
                val dividerHorizontal = DividerItemDecoration(it, DividerItemDecoration.HORIZONTAL)
                dividerHorizontal.setDrawable(drawable!!)
                val dividerVertical = DividerItemDecoration(it, DividerItemDecoration.VERTICAL)
                dividerVertical.setDrawable(drawable!!)

                it.rv_deal.apply {
                    layoutManager = viewManager
                    adapter = viewAdapter
                    it.rv_deal.addItemDecoration(dividerHorizontal)
                    it.rv_deal.addItemDecoration(dividerVertical)
                }
            }
        }
    }
}
