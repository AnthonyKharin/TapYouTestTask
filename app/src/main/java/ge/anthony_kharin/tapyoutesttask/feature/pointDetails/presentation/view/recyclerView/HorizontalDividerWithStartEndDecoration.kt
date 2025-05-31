package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.recyclerView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Добавил ItemDecoration чтобы список выглядел как таблица
class HorizontalDividerWithStartEndDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerDrawable: Drawable? = run {
        val typesArray = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        val dividerDrawable = typesArray.getDrawable(0)
        typesArray.recycle()
        dividerDrawable
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val divider = dividerDrawable ?: return

        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount

        if (childCount == 0) return

        // 1) Рисуем слева от первого видимого элемента
        val firstChild = parent.getChildAt(0) ?: return
        val firstParams = firstChild.layoutParams as RecyclerView.LayoutParams
        val dividerWidth = divider.intrinsicWidth

        val leftOfFirst = firstChild.left - firstParams.leftMargin - dividerWidth
        val rightOfFirst = leftOfFirst + dividerWidth
        divider.setBounds(leftOfFirst, top, rightOfFirst, bottom)
        divider.draw(canvas)

        // 2) Рисуем между всеми видимыми элементами
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i) ?: continue
            val params = child.layoutParams as RecyclerView.LayoutParams

            val leftLine = child.right + params.rightMargin
            val rightLine = leftLine + dividerWidth

            divider.setBounds(leftLine, top, rightLine, bottom)
            divider.draw(canvas)
        }

        // 3) Рисуем справа от последнего видимого элемента, только если он действительно последний в адаптере
        //    (иначе «последний видимый» не всегда = «последний по списку»)
        val adapter = parent.adapter ?: return
        val lastAdapterIndex = adapter.itemCount - 1

        // Получим индекс последнего видимого ребенка в адаптере
        val lastVisibleIndex = parent.getChildAdapterPosition(parent.getChildAt(childCount - 1))
        if (lastVisibleIndex == lastAdapterIndex) {
            // Рисуем divider справа от него
            val lastChild = parent.getChildAt(childCount - 1)
            val lastParams = lastChild.layoutParams as RecyclerView.LayoutParams

            val leftOfLast = lastChild.right + lastParams.rightMargin
            val rightOfLast = leftOfLast + dividerWidth

            divider.setBounds(leftOfLast, top, rightOfLast, bottom)
            divider.draw(canvas)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val divider = dividerDrawable ?: run {
            outRect.set(0, 0, 0, 0)
            return
        }

        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter
        val dividerWidth = divider.intrinsicWidth

        if (adapter == null) {
            outRect.set(0, 0, 0, 0)
            return
        }

        val lastIndex = adapter.itemCount - 1

        when (position) {
            0 -> outRect.set(dividerWidth, 0, 0, 0)
            lastIndex -> outRect.set(0, 0, dividerWidth, 0)
            else -> outRect.set(0, 0, 0, 0)
        }
    }
}
