package com.cagataykolus.productapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Abstract base fragment which binds [ViewBinding]
 */
abstract class BaseViewBindingFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null

    // This property is only valid between onCreateView and onDestroyView
    protected open val binding get() = _binding!!

    val supportActionBar: ActionBar?
        get() = (requireActivity() as? AppCompatActivity)?.supportActionBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = this.setBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setBinding(inflater: LayoutInflater, container: ViewGroup?): T

    fun isViewDestroyed(): Boolean = _binding == null

    fun navigate(destination: Int, bundle: Bundle? = null, navOptions: NavOptions) {
        findNavController().navigate(destination, bundle, navOptions)
    }

    fun navigate(destination: Int, bundle: Bundle? = null) {
        findNavController().navigate(destination, bundle)
    }

    fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }
}