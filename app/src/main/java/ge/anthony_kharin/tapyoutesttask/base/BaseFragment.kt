package ge.anthony_kharin.tapyoutesttask.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import ge.anthony_kharin.tapyoutesttask.MainActivity
import ge.anthony_kharin.tapyoutesttask.di.component.ActivityComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)

    protected open fun setupViews() = Unit
    protected open fun subscribeStates() = Unit

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies((requireActivity() as MainActivity).activityComponent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        subscribeStates()
    }

    protected fun <T> Flow<T>.subscribeInViewLifecycle(action: suspend (T) -> Unit): Job =
        this.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach(action)
            .launchIn(viewLifecycleOwner.lifecycleScope)


    protected fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
