package davydov.dmytro.simplecatalog.catalog.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class CatalogLandscapeDividerDecoration(
    context: Context,
    private val orientation: Int
) : DividerItemDecoration(context, orientation) {

    private val mBounds = Rect()

    private val divider: Drawable
        get() = drawable!!

    private val shouldDrawDividerPredicate: (RecyclerView, View) -> Boolean =
        { recyclerView, view ->
            val notLoaderFooter =
                recyclerView.getChildViewHolder(view) !is LoaderFooterAdapter.Holder
            notLoaderFooter
        }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (shouldDrawDividerPredicate(parent, view)) {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        for (child in parent.children) {
            if (shouldDrawDividerPredicate(parent, child)) {
                parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                val left = mBounds.right
                val right = left + divider.intrinsicWidth
                val top = mBounds.top
                val bottom = mBounds.bottom
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        for (child in parent.children) {
            if (shouldDrawDividerPredicate(parent, child)) {
                parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                val left = mBounds.left
                val right = mBounds.right
                val top = mBounds.bottom
                val bottom = top + divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
    }
}