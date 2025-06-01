package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ge.anthony_kharin.tapyoutesttask.databinding.ItemPointBinding
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model.PointDetailsTableItemUiModel
import ge.anthony_kharin.tapyoutesttask.feature.utils.setThrottleClickListener

// Сейчас нам не нужен колбэк, но я оставил его для наглядности как я бы это сделал в проекте
// Однако на самом деле в реальном проекте я бы не использовал такой адаптер в чистом виде
// Я бы использовал делагат адаптер или что-то в таком духе, не обязательно готовое решение
class PointDetailsRecyclerViewAdapter(
    private val onItemClicked: (PointDetailsTableItemUiModel) -> Unit = {}
) : RecyclerView.Adapter<PointDetailsRecyclerViewAdapter.ViewHolder>() {

    var items: List<PointDetailsTableItemUiModel> = emptyList()
        set(value) {
            val diffCallback = PointDetailsRecyclerViewDiffUtilsCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = items[position]
        holder.bind(point)
    }

    inner class ViewHolder(
        private val binding: ItemPointBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(point: PointDetailsTableItemUiModel) {
            binding.root.setThrottleClickListener { onItemClicked.invoke(point) }
            binding.indexTextView.text = point.index
            binding.pointXTextView.text = point.x
            binding.pointYTextView.text = point.y
        }
    }
}