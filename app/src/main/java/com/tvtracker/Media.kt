package com.tvtracker

import android.view.GestureDetector
import android.view.MotionEvent

class Media : GestureDetector.SimpleOnGestureListener() {
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return super.onSingleTapUp(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return super.onDown(e)
    }

    override fun onFling(e1: MotionEvent?,
                         e2: MotionEvent?,
                         velocityX: Float,
                         velocityY: Float
    ): Boolean {
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onScroll (e1: MotionEvent?,
                           e2: MotionEvent?,
                           distanceX: Float,
                           distanceY: Float
    ): Boolean {
        return super.onScroll(e1,e2,distanceX,distanceY)
    }
}