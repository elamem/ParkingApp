package com.elamparithi.parkypark.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Created by elam on 01/12/21
 */
class ItemDecorationMargin(private val margin: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.top = margin
        outRect.right = margin
        outRect.bottom = margin
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = margin
        } else {
            outRect.left = 0
        }
    }
}