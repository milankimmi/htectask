package com.htec.task.utils

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
fun isRefreshing(view: SwipeRefreshLayout): Boolean {
    return view.isRefreshing
}

@BindingAdapter("refreshing")
fun setRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
    if (refreshing != view.isRefreshing) {
        view.isRefreshing = refreshing
    }
}

@BindingAdapter("addItemDecoration")
fun addItemDecoration(view: RecyclerView, itemDecoration: ItemDecoration?) {
    if (itemDecoration != null) view.addItemDecoration(itemDecoration)
}