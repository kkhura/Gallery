package kkhura.com.quotes.app.customview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.widget.AppCompatEditText
import android.text.*
import android.text.method.KeyListener
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.TextView
import kkhura.com.quotes.app.R
import kkhura.com.quotes.app.utility.Utils

@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
class CustomFontEditTextView : TextInputLayout {
    private var editText: TextInputEditText? = null

    private val drawableClickListener: DrawableClickListener? = null

    var text: String
        get() {
            try {
                return this.editText!!.text.toString().trim { it <= ' ' }
            } catch (e: Exception) {
                return ""
            }

        }
        set(s) = this.editText!!.setText(s)

    //            editText.setInputType(InputType.TYPE_CLASS_TEXT);
    /*
         if(editText.getText().toString().length()>0)
             editText.setSelection(0,(editText.getText().length()-1));
*/ var transformationMethod: TransformationMethod?
        get() = editText!!.transformationMethod
        set(method) = if (method != null) {
            editText!!.transformationMethod = method

        } else
            editText!!.transformationMethod = null


    val compoundDrawables: Array<Drawable>
        get() = editText!!.compoundDrawables

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    constructor(context: Context) : super(context) {
        editText = createEditText(context, null)
        super.addView(editText)
        setHintTextAppearance(R.style.TextInputStyle)
        applyCustomFont(context, null)
        error = ""
        //setErrorTextColor(this, ContextCompat.getColor(getContext(), R.color.red_500));

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        editText = createEditText(context, attrs)
        applyFieldAttributes(attrs)
        super.addView(editText)
        setHintTextAppearance(R.style.TextInputStyle)
        applyCustomFont(context, attrs)
        error = ""
        //setErrorTextColor(this, ContextCompat.getColor(getContext(), R.color.red_500));
    }


    override fun setOnTouchListener(l: View.OnTouchListener) {
        /*        super.setOnTouchListener(l);*/
        editText!!.setOnTouchListener(l)

    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        editText!!.setOnClickListener(l)
    }

    override fun setFocusable(focusable: Boolean) {
        editText!!.isFocusable = focusable
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        editText = createEditText(context, attrs)
        applyFieldAttributes(attrs)
        super.addView(editText)
        setHintTextAppearance(R.style.TextInputStyle)
        applyCustomFont(context, attrs)
    }

    fun applyFieldAttributes(attrs: AttributeSet) {
        val attributeArray = context!!.obtainStyledAttributes(attrs, R.styleable.customFontTextView)
        //setting input type
        val inputType = attributeArray.getString(R.styleable.customFontTextView_inputType)
        setEditText_inputType(inputType)
        //setting maxlength

        // editText.setFilters(new InputFilter[]{inputfilter});

        val length = attributeArray.getInt(R.styleable.customFontTextView_maxLength, -1)
        setMaxLength(length)

        val isdrawableRight = attributeArray.getBoolean(R.styleable.customFontTextView_is_password_view, false)
        if (isdrawableRight && !isInEditMode) {
            editText!!.compoundDrawablePadding = 5
            editText!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0)
        }

        val gravity = attributeArray.getInt(R.styleable.customFontTextView_gravity, -1)

        when (gravity) {
            GRAVITY_CENTER -> editText!!.gravity = Gravity.START
            GRAVITY_START -> editText!!.gravity = Gravity.START
            GRAVITY_END -> editText!!.gravity = Gravity.END
        }

        //setting number of lines
        //setting maxlength

        //setting single line


        //setting text size

        //setting number of lines
        //setting maxlength
        val linew = attributeArray.getInt(R.styleable.customFontTextView_lines, -1)
        if (linew != -1)
            editText!!.maxLines = linew

        //setting single line
        val singleLine = attributeArray.getBoolean(R.styleable.customFontTextView_singleLine, true)
        editText!!.setSingleLine(singleLine)


        //setting text size
        editText!!.textSize = Utils.pixelsToSp(context, context!!.resources.getDimension(R.dimen.theme_edittext_text_size))

        editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                this@CustomFontEditTextView.error = ""
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                this@CustomFontEditTextView.error = ""
            }

            override fun afterTextChanged(editable: Editable) {
                this@CustomFontEditTextView.error = ""
            }

        })

        editText!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (this@CustomFontEditTextView.error == null) {
                    this@CustomFontEditTextView.error = ""
                    setErrorTextColor(this@CustomFontEditTextView, context!!.resources.getColor(R.color.theme_edit_text_error_color))
                }
            }
        }


        val DRAWABLE_RIGHT = 2
        val DRAWABLE_LEFT = 0

        if (editText!!.compoundDrawables[DRAWABLE_RIGHT] != null || editText!!.compoundDrawables[DRAWABLE_LEFT] != null) {
            editText!!.setOnTouchListener(OnTouchListener { v, event ->
                var DRAWABLE_DIRECTION = DRAWABLE_RIGHT
                var MOTIONEVENT = MotionEvent.ACTION_UP
                DRAWABLE_DIRECTION = DRAWABLE_RIGHT
                MOTIONEVENT = MotionEvent.ACTION_UP

                if (event.action == MOTIONEVENT) {
                    if (event.rawX >= editText!!.right - editText!!.compoundDrawables[DRAWABLE_DIRECTION].bounds.width())
                        setIconClick()
                    return@OnTouchListener false
                }
                false
            })
        }

    }


    private fun setIconClick() {
        if (editText!!.transformationMethod == null) {
            editText!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show_password, 0)
            editText!!.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            editText!!.transformationMethod = null
            editText!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_hide_password, 0)
        }
        postDelayed({ editText!!.setSelection(editText!!.text!!.length) }, 80)
    }


    internal fun setMaxLength(maxLength: Int) {
        if (maxLength == -1)
            return
        else {
            editText!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
    }

    private fun applyCustomFont(context: Context, attrs: AttributeSet?) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.customFontTextView)
        val fontName = attributeArray.getString(R.styleable.customFontTextView_fontName)
        val customFont = selectTypeface(context, fontName, fontName)
        setTypeface(customFont)
        editText!!.typeface = customFont
        attributeArray.recycle()
        clipToPadding = false
    }

    private fun selectTypeface(context: Context, fontName: String?, textStyle: String?): Typeface? {
        return if (textStyle == null) {
            FontCache.getTypeface("sans/OpenSans-Regular.ttf", context)
        } else FontCache.getTypeface("sans/OpenSans-Semibold.ttf", context)
        if(textStyle.equals(context.getString(R.string.font_bold))){
            FontCache.getTypeface("sans/OpenSans-Bold.ttf", context)
        } else if(textStyle.equals(context.getString(R.string.font_semibold))){
            return FontCache.getTypeface("sans/OpenSans-Semibold.ttf", context)
        } else if(textStyle.equals(context.getString(R.string.font_light))){
            return FontCache.getTypeface("sans/OpenSans-Light.ttf", context)
        } else if(textStyle.equals(context.getString(R.string.font_regular))){
            return FontCache.getTypeface("sans/OpenSans-Regular.ttf", context)
        }else if(textStyle.equals(context.getString(R.string.font_italic))){
            return FontCache.getTypeface("sans/OpenSans-Italic.ttf", context)
        }

    }

    private fun createEditText(context: Context, attrs: AttributeSet?): TextInputEditText {
        editText = TextInputEditText(context)
        editText!!.gravity = GRAVITY_START
        editText!!.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        editText!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        editText!!.setHorizontallyScrolling(false)
        editText!!.hint = ""
        editText!!.setTextColor(context.resources.getColor(R.color.navy_blue_approx))
        return editText as TextInputEditText
    }

    fun setEditText_inputType(type: String?) {
        if (TextUtils.isEmpty(type)) {
            editText!!.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            return
        }
        val typeArr = type!!.trim { it <= ' ' }.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var finalString: String
        finalString = ""
        for (s in typeArr) {

            when (s) {
                "number" -> finalString = addInputType(InputType.TYPE_CLASS_NUMBER, finalString)
                "decimal" -> finalString = addInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, finalString)
                "textPassword", "Password", "password" -> {
                    editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    editText!!.transformationMethod = PasswordTransformationMethod()
                }

                "textCapWords" -> finalString = addInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS, finalString)
                "none" -> finalString = addInputType(InputType.TYPE_NULL, finalString)
                "textEmail" -> finalString = addInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, finalString)
                "textEmailAddress" -> finalString = addInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, finalString)
                "textMultiLine" -> editText!!.setSingleLine(false)
            }
        }
        //	editText.setInputType(Integer.parseInt(finalString));
    }


    internal fun addInputType(type: Int, list: String): String {
        var list = list

        if (list == "") {
            editText!!.inputType = type
            list = "" + list
        } else {
            editText!!.inputType = editText!!.inputType or type
            list = "$list|$type"
        }
        return list
    }


    fun addTextChangedListener(watcher: TextWatcher) {
        editText!!.addTextChangedListener(watcher)
    }

    fun setKeyListener(input: KeyListener) {
        editText!!.keyListener = input
    }

    fun setFilters(filters: Array<InputFilter>) {
        editText!!.filters = filters
    }

    fun setCursorVisible(visible: Boolean) {
        editText!!.isCursorVisible = visible
    }

    fun setInputType(type: Int) {
        editText!!.inputType = type
    }

    fun setSelection(index: Int) {
        editText!!.setSelection(index)
    }

    fun setHint(RID: Int) {
        val hint = context!!.getString(RID)
        setHint(hint)
    }

    override fun getEditText(): AppCompatEditText? {
        return editText
    }

    fun setErrorTextColor(textInputLayout: TextInputLayout, color: Int) {
        try {
            val fErrorView = TextInputLayout::class.java.getDeclaredField("mErrorView")
            fErrorView.isAccessible = true
            val mErrorView = fErrorView.get(this) as TextView
            val fCurTextColor = TextView::class.java.getDeclaredField("mCurTextColor")
            fCurTextColor.isAccessible = true
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            if (mErrorView != null) {
                fCurTextColor.set(mErrorView, color)
                mErrorView.layoutParams = params
                mErrorView.gravity = Gravity.END
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setErrorGravity() {
        try {
            val fErrorView = TextInputLayout::class.java.getDeclaredField("mErrorView")
            fErrorView.isAccessible = true
            val mErrorView = fErrorView.get(this) as TextView
            val fCurTextColor = TextView::class.java.getDeclaredField("mCurTextColor")
            fCurTextColor.isAccessible = true
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END
            if (mErrorView != null) {
                mErrorView.layoutParams = params
                mErrorView.gravity = Gravity.END
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun setError(error: CharSequence?) {
        //        super.setError("");
        /*if (!this.isErrorEnabled())a
            setErrorEnabled(true);*/
        try {
            if (error != null && error != "")
                setErrorTextColor(this, context!!.resources.getColor(R.color.theme_edit_text_error_color))
            super.setErrorEnabled(false)
            super.setError("none")
            super.setError(error)
            setErrorGravity()
        } catch (e: Exception) {
        }

    }


    override fun setOnKeyListener(l: View.OnKeyListener) {
        editText!!.setOnKeyListener(l)
    }

    fun length(): Int {
        return editText!!.length()
    }

    fun setCapsWords() {
        editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }

    fun setTextColor(color: Int) {
        editText!!.setTextColor(color)
    }

    companion object {
        val ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android"
        private val GRAVITY_LEFT = 0
        private val GRAVITY_RIGHT = 1
        private val GRAVITY_CENTER = 2
        private val GRAVITY_START = 8388611
        private val GRAVITY_END = 8388613
    }

}
