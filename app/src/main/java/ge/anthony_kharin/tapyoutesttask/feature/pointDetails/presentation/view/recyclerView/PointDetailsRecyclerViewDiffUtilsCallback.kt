package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.recyclerView

import androidx.recyclerview.widget.DiffUtil
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model.PointDetailsTableItemUiModel

class PointDetailsRecyclerViewDiffUtilsCallback(
    private val oldList: List<PointDetailsTableItemUiModel>,
    private val newList: List<PointDetailsTableItemUiModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.javaClass == newItem.javaClass
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.hashCode() == newItem.hashCode()
    }
}
