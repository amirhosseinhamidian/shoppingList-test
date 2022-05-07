package com.amirhossein.shoppinglist.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.amirhossein.shoppinglist.R

class ShoppingFragment: Fragment(R.layout.fragment_shopping) {

    lateinit var viewModel : ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}