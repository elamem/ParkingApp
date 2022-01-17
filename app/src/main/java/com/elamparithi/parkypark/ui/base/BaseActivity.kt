package com.elamparithi.parkypark.ui.base

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    open var useSharedViewModel: Boolean = false
    protected lateinit var viewModel: VM
    protected abstract fun getViewModelClass(): Class<VM>

    private var _binding: VB? = null
    val binding get() = _binding!!

    protected abstract fun getMenuLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        _binding = inflateViewBinding()
        setContentView(binding.root)
        setupView()
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuLayoutId = getMenuLayoutId()
        return if (menuLayoutId == -1) {
            false
        } else {
            menuInflater.inflate(getMenuLayoutId(), menu)
            setupMenu(menu)
            true
        }
    }


    private fun init() {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
    }

    protected abstract fun setupMenu(menu: Menu)

    protected abstract fun setupView()

    protected open fun observeData() {

    }

    abstract fun inflateViewBinding(): VB

}