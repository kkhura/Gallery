package kkhura.com.quotes.app.utility

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import codeliver128.com.mylibrary.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


    }


    fun loadAdd() {
        MobileAds.initialize(this, getString(R.string.ad_mob_id))

        var mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("TAG", "")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                Log.d("TAG", "Error : " + errorCode)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("TAG", "")
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d("TAG", "")
            }

            override fun onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.d("TAG", "")
            }
        }
    }

    open fun setToolBar(title: String, isUpEnable: Boolean) {
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(isUpEnable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else {
            // Do some other things to other menu
            return super.onOptionsItemSelected(item)
        }
    }

    /*open fun selectItem(position: Int) {
        when (position) {
            1 -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.frameContainer, QuotesCategoryFragment.newInstance())
                transaction.commit()
            }
            2 -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frameContainer, HomeFragment.newInstance())
                transaction.commit()
            }
        }
    }*/
}