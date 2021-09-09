package com.example.mymoney

import android.content.res.Resources
import android.graphics.Rect
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}

fun DialogFragment.setWidthPercent(percentageWidth: Int, percentageHeight: Int) {
    val percentW = percentageWidth.toFloat() / 100
    val percentH = percentageHeight.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percentW
    val percentHeight = rect.height() * percentH
    dialog?.window?.setLayout(percentWidth.toInt(), percentHeight.toInt())
    //dialog?.window?.setLayout(percentHeight.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun DialogFragment.setFullScreen() {
    dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}