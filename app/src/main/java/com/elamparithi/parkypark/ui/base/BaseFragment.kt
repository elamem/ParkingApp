package com.elamparithi.parkypark.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : DialogFragment() {

    open var useSharedViewModel: Boolean = false

    protected lateinit var viewModel: VM
    protected abstract fun getViewModelClass(): Class<VM>

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = if (useSharedViewModel) {
            ViewModelProvider(requireActivity())[getViewModelClass()]
        } else {
            ViewModelProvider(this)[getViewModelClass()]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun setupView(view: View)

    protected abstract fun observeData()
}