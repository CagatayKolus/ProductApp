package com.cagataykolus.productapp.ui.products.dialog

import android.content.Context
import android.view.LayoutInflater
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.DialogAddProductBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.ui.detail.dialog.ReviewDialog
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by Çağatay Kölüş on 13.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [AddProductDialog] is dialog for [Product] data.
 */
class AddProductDialog(context: Context) : BottomSheetDialog(context) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private var action: ((AddProductDialogAction) -> Unit)? = null

    private lateinit var product: Product

    init {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DialogAddProductBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.buttonDialogAddProductOkay.setOnClickListener {
            if (isInputCorrect(binding)) {
                product = Product(
                    id = binding.textinputlayoutDialogAddProductId.editText?.text.toString(),
                    name = binding.textinputlayoutDialogAddProductName.editText?.text.toString(),
                    description = binding.textinputlayoutDialogAddProductDescription.editText?.text.toString(),
                    currency = "",
                    price = 0
                )
                action?.invoke(AddProductDialogAction.AddProductClicked)
            }
        }
    }

    private fun isInputCorrect(binding: DialogAddProductBinding): Boolean {
        val isSuccessful = true

        // Check TextInputLayout for ProductID
        if (binding.textinputlayoutDialogAddProductId.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogAddProductId.isErrorEnabled = true
            binding.textinputlayoutDialogAddProductId.error =
                context.getString(R.string.empty_field)
            return false
        } else {
            binding.textinputlayoutDialogAddProductId.error = null
            binding.textinputlayoutDialogAddProductId.isErrorEnabled = false
        }

        // Check TextInputLayout for ProductName
        if (binding.textinputlayoutDialogAddProductName.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogAddProductName.isErrorEnabled = true
            binding.textinputlayoutDialogAddProductName.error =
                context.getString(R.string.empty_field)
            return false
        } else {
            binding.textinputlayoutDialogAddProductName.error = null
            binding.textinputlayoutDialogAddProductName.isErrorEnabled = false
        }

        // Check TextInputLayout for ProductDescription
        if (binding.textinputlayoutDialogAddProductDescription.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogAddProductDescription.isErrorEnabled = true
            binding.textinputlayoutDialogAddProductDescription.error =
                context.getString(R.string.empty_field)
            return false
        } else {
            binding.textinputlayoutDialogAddProductDescription.error = null
            binding.textinputlayoutDialogAddProductDescription.isErrorEnabled = false
        }

        return isSuccessful
    }

    fun onAction(action: (AddProductDialogAction) -> Unit) = apply {
        this.action = action
    }

    fun showDialog() = apply {
        dialog.show()
    }

    fun dismissDialog() = apply {
        dialog.dismiss()
    }

    fun getProduct(): Product {
        return product
    }
}

sealed class AddProductDialogAction {
    object AddProductClicked : AddProductDialogAction()
}