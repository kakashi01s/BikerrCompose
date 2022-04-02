package com.firefly.bikerr_compose.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T, L : BaseRecyclerListener?>(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!) {
    /**
     * Bind data to the item and set listener if needed.
     *
     * @param item     object, associated with the item.
     * @param listener listener a listener [BaseRecyclerListener] which has to b set at the item (if not `null`).
     */
    abstract fun onBind(item: T, listener: L?)
}
