package ge.anthony_kharin.tapyoutesttask.feature.main.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.enableSavedStateHandles
import ge.anthony_kharin.tapyoutesttask.base.BaseFragment
import ge.anthony_kharin.tapyoutesttask.databinding.FragmentMainBinding
import ge.anthony_kharin.tapyoutesttask.di.component.ActivityComponent
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.model.MainScreenState
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.viewModel.MainViewModel
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.viewModel.MainViewModelFactory
import ge.anthony_kharin.tapyoutesttask.feature.utils.setThrottleClickListener
import javax.inject.Inject

class MainFragment : BaseFragment<FragmentMainBinding>() {

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { vmFactory }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupViews() {
        enableSavedStateHandles()
        // По ТЗ неизвестно когда мы должны делать валидацию введенных символов, поэтому
        // я валидирую при каждом изменении текста
        binding.enterCountEditText.addTextChangedListener(afterTextChanged = { text ->
            viewModel.onEnterCountEditTextChanged(text?.toString().orEmpty())
        })

        binding.continueButton.setThrottleClickListener {
            viewModel.onContinueButtonClicked()
        }
    }

    override fun subscribeStates() {
        viewModel.screenState.subscribeInViewLifecycle(::onEditTextStateReceived)
    }

    private fun onEditTextStateReceived(state: MainScreenState) {
        when (state) {
            is MainScreenState.Initial -> onScreenInitial()
            is MainScreenState.Loaded -> onScreenLoaded(state)
            is MainScreenState.Loading -> onScreenLoading()
            is MainScreenState.Error -> onScreenError(state)
        }
    }

    private fun onScreenInitial() {
        binding.enterCountEditText.isEnabled = true
        binding.enterCountEditText.error = null
        binding.continueButton.isEnabled = false
        binding.errorTextView.isVisible = false
    }

    private fun onScreenLoaded(state: MainScreenState.Loaded) {
        binding.enterCountEditText.isEnabled = true
        binding.enterCountEditText.error = state.editTextError
        binding.continueButton.isEnabled = state.editTextError == null
        binding.errorTextView.isVisible = false
    }

    private fun onScreenLoading() {
        binding.enterCountEditText.isEnabled = false
        binding.continueButton.isEnabled = false
        binding.errorTextView.isVisible = false
    }

    private fun onScreenError(state: MainScreenState.Error) {
        binding.enterCountEditText.isEnabled = true
        binding.continueButton.isEnabled = true
        binding.errorTextView.text = state.message
        binding.errorTextView.isVisible = true
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }
}