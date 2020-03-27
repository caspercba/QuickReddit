package com.gaspardeelias.quickreddit.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gaspardeelias.quickreddit.R

class ShimmerView : View {

    var color = 0
    val p = Paint()

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        color = context.resources.getColor(R.color.grey)

    }


    override fun onDraw(canvas: Canvas) {
        val currentTime= System.currentTimeMillis()
        val globalTimePassed =currentTime - startTime

        if(globalTimePassed> ANIMATION_DURATION)
        {
            startTime=currentTime
            slope=!slope
        }

        val timeDelta = globalTimePassed % ANIMATION_DURATION
        var globalProgress = timeDelta.toFloat()/ ANIMATION_DURATION

        //use the slope flag to achieve smooth color transition
        if(!slope){ globalProgress = 1f-globalProgress}

        val gradient = RadialGradient(globalProgress * width.toFloat() - width,
            (height / 2).toFloat(),
            (width * 2).toFloat(),
            color,
            Color.WHITE,
            Shader.TileMode.CLAMP)

        p.isDither = true
        p.shader = gradient
        p.alpha=(0.8f*255f).toInt()

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), p)

        postDelayed({ invalidate() }, 16)
    }

    companion object {

        internal var startTime = System.currentTimeMillis()
        internal var  ANIMATION_DURATION = 1000f
        internal var slope = true


    }
}