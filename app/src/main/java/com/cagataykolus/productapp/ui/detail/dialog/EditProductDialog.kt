package com.cagataykolus.productapp.ui.detail.dialog

import android.content.Context
import android.view.LayoutInflater
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.DialogEditProductBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.ui.products.ProductsFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [EditProductDialog] is dialog for [Product] data.
 */
class EditProductDialog(context: Context, productId: String) : BottomSheetDialog(context) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private var action: ((EditProductDialogAction) -> Unit)? = null

    private lateinit var product: Product

    init {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DialogEditProductBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.buttonDialogEditProductOkay.setOnClickListener {
            if (isInputCorrect(binding)) {
                product = Product(
                    id = productId,
                    name = binding.textinputlayoutDialogEditProductName.editText?.text.toString(),
                    description = binding.textinputlayoutDialogEditProductDescription.editText?.text.toString(),
                    currency = "",
                    price = 0
                )
                action?.invoke(EditProductDialogAction.EditProductClicked)
            }
        }
    }

    private fun isInputCorrect(binding: DialogEditProductBinding): Boolean {
        val isSuccessful = true

        // Check TextInputLayout for ProductName
        if (binding.textinputlayoutDialogEditProductName.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogEditProductName.isErrorEnabled = true
            binding.textinputlayoutDialogEditProductName.error =
                context.getString(R.string.empty_field)
            return false
        } else {
            binding.textinputlayoutDialogEditProductName.error = null
            binding.textinputlayoutDialogEditProductName.isErrorEnabled = false
        }

        // Check TextInputLayout for ProductDescription
        if (binding.textinputlayoutDialogEditProductDescription.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogEditProductDescription.isErrorEnabled = true
            binding.textinputlayoutDialogEditProductDescription.error =
                context.getString(R.string.empty_field)
            return false
        } else {
            binding.textinputlayoutDialogEditProductDescription.error = null
            binding.textinputlayoutDialogEditProductDescription.isErrorEnabled = false
        }

        return isSuccessful
    }

    fun onAction(action: (EditProductDialogAction) -> Unit) = apply {
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

sealed class EditProductDialogAction {
    object EditProductClicked : EditProductDialogAction()
}