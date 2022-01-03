package com.frankliang.findburritoproject.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frankliang.findburritoproject.R
import com.frankliang.findburritoproject.model.Restaurant

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("app:setRestaurantInfo")
    fun setRestaurantInfo(view: TextView, restaurant: Restaurant?) {
        restaurant?.let {
            if (!it.phone.isNullOrBlank() && !it.priceLevel.isNullOrBlank()) {
                view.text = view.context.getString(R.string.restaurant_info, it.priceLevel, it.phone)
            } else {
                view.text = if (it.priceLevel.isNullOrBlank())
                    it.phone
                else
                    it.priceLevel
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:visibleIf")
    fun visibleIf(view: View?, isVisible: Boolean) {
        view?.let { v ->
            v.visibility = if(isVisible) View.VISIBLE else View.GONE
        }
    }
}