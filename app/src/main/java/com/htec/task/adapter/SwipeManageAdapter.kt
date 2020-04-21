package com.htec.task.adapter

import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.htec.task.R
import com.htec.task.datamodel.Post

class SwipeManageAdapter(
    var swipeCallback: (position: Int, post: Post?) -> Unit,
    var adapter: PostRecycleViewAdapter,
    var context: Context,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val bkgPaint: Paint = Paint()
    private val binIcon: Bitmap = getBitmapFromVectorDrawable(context, R.drawable.ic_delete)
    private val rightMargin: Float = context.resources.getDimension(R.dimen.recycleItem_binIcon_rightMargin)

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 75F * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 75F * defaultValue
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.66F // 66%
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            swipeCallback(position, adapter.getDataAtPosition(position))
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            var height = itemView.bottom.toFloat() - itemView.top.toFloat()

            if (dX < 0) {
                bkgPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)
                val rectF = RectF(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                c.drawRect(rectF, bkgPaint)

                val icon_dest = RectF(
                    itemView.right.toFloat() - (binIcon.width / 2) - rightMargin,
                    itemView.top.toFloat() + height / 2 - binIcon.height / 2,
                    itemView.right.toFloat() + (binIcon.width / 2) - rightMargin,
                    itemView.bottom.toFloat() - height / 2 + binIcon.height / 2
                )
                c.drawBitmap(binIcon, null, icon_dest, bkgPaint)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}