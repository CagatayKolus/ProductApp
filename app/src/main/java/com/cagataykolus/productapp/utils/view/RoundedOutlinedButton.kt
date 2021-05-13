package com.cagataykolus.productapp.utils.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cagataykolus.productapp.R

/**
 * Created by Çağatay Kölüş on 13.05.2021.
 * cagataykolus@gmail.com
 */

class RoundedOutlinedButton : AppCompatButton {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    init {
        setBackgroundResource(R.drawable.rounded_outlined_button)
        isAllCaps = false
        textSize = 16F

        // Text Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.blue
                )
            )
        } else {
            @Suppress("DEPRECATION")
            setTextColor(resources.getColor(R.color.blue))
        }

        typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
        this.stateListAnimator = null
        letterSpacing = 0.038f
//        setBackgroundResource(R.drawable.ripple_effect_with_border)

//        val value = TypedValue()
//        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
//        setBackgroundResource(value.resourceId)
//        isFocusable = true // If needed for view type
    }
}