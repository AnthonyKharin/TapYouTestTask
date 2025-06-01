package ge.anthony_kharin.tapyoutesttask.feature.utils

import android.os.SystemClock
import android.view.View

abstract class OnThrottleClickListener(
    private val minClickInterval: Long
) : View.OnClickListener {

    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(v: View?)

    override fun onClick(v: View?) {
        val currentClickTime: Long = SystemClock.elapsedRealtime()
        val elapsedTime = currentClickTime - mLastClickTime
        if (elapsedTime <= minClickInterval) return
        mLastClickTime = currentClickTime
        onSingleClick(v)
    }
}

fun View.setThrottleClickListener(millis: Long = 300, action: (v: View?) -> Unit) {
    setOnClickListener(object : OnThrottleClickListener(millis) {
        override fun onSingleClick(v: View?) {
            action(v)
        }
    })
}
