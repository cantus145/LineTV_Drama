package com.voxel.classbox.UI

import android.content.Context

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 處理RecyclerView
 */
class WrapContentLinearLayoutManager : LinearLayoutManager {
    
    constructor(context: Context) : super(context)
    
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}