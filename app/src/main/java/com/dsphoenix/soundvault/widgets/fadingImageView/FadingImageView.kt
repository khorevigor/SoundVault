package com.dsphoenix.soundvault.widgets.fadingImageView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.BitmapFactory.decodeResource
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.scale
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dsphoenix.soundvault.utils.constants.Attributes

// Since there is two similar LinearGradient constructors with difference that
// API level 1 version doesn't throw exception, while API level 29 version does
@SuppressLint("NewApi")

class FadingImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val srcResource = attrs?.getAttributeResourceValue(Attributes.Schemas.ANDROID, Attributes.SRC, 0) as Int
    private var bitmap: Bitmap = decodeResource(context.resources, srcResource)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Opaque white to transparent
    private val colors = intArrayOf(0xffffffff.toInt(), 0x55ffffff, 0x00ffffff)
    private lateinit var gradientShader: Shader
    private lateinit var bitmapShader: Shader

    override fun onDraw(canvas: Canvas) {
        if (bitmap.width != width || bitmap.height != height) {
            setupShaders()
        }
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
    }

    fun loadImage(context: Context, uri: String) {
        Glide.with(context).asBitmap().load(uri)
            .into(object : CustomTarget<Bitmap>(bitmap.width, bitmap.height){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmap = resource
                    setupShaders()
                    invalidate()
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun setupShaders() {
        bitmap = bitmap.scale(width, height)

        gradientShader = LinearGradient(
            (width * 0.875).toFloat(), height.toFloat(), width.toFloat(), height.toFloat(),
            colors, null, Shader.TileMode.CLAMP
        )

        val shaderAMatrix = Matrix()
        gradientShader.getLocalMatrix(shaderAMatrix)
        shaderAMatrix.postRotate(90f)
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        gradientShader.setLocalMatrix(shaderAMatrix)

        paint.shader = ComposeShader(gradientShader, bitmapShader, PorterDuff.Mode.SRC_IN)
    }
}