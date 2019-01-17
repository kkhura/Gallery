package kkhura.com.quotes.app.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import java.util.*

object Utils {

    fun convertSpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, metrics)
    }


    fun getAge(year: Int, month: Int, day: Int): Int {
        val cal = GregorianCalendar()
        val y: Int
        val m: Int
        val d: Int
        var a: Int
        try {

            y = cal.get(Calendar.YEAR)
            m = cal.get(Calendar.MONTH) + 1
            d = cal.get(Calendar.DAY_OF_MONTH)
            cal.set(year, month, day)
            a = y - year
            if (m < month || m == month && d < day) {
                --a
            }
        } catch (e: Exception) {
            e.printStackTrace()
            a = 0
        }

        return a
    }

    fun pixelsToSp(context: Context, px: Float): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }

    fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.getBackground()
        if (bgDrawable != null) {
            bgDrawable!!.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
}