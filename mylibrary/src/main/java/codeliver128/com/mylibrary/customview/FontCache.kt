package codeliver128.com.mylibrary.customview

import android.content.Context
import android.graphics.Typeface

import java.util.Hashtable

object FontCache {
    private val fontCache = Hashtable<String, Typeface>()

    fun getTypeface(fontName: String, context: Context): Typeface? {
        var typeface: Typeface? = fontCache[fontName]
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                return null
            }

            fontCache[fontName] = typeface
        }

        return typeface
    }

}
