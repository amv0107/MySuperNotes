package com.amv.simple.app.mysupernotes.monetisation

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

fun loadBannerAds(
    context: Context,
    adsFrameLayout: FrameLayout,
    adSize: AdSize,
    adUnitIdl: Int
){
    val adView = AdView(context)
    adView.setAdSize(adSize)
    adView.adUnitId = context.getString(adUnitIdl)
    adsFrameLayout.addView(adView)

    val adRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)
    adView.adListener = object : AdListener(){
        override fun onAdFailedToLoad(adError: LoadAdError) {
            super.onAdFailedToLoad(adError)
            adsFrameLayout.gone()
            adView.loadAd(adRequest)
        }

        override fun onAdLoaded() {
            // Когда реклама загрузилась
            super.onAdLoaded()
            adsFrameLayout.visible()
        }
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}