package com.amirhossein.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.amirhossein.shoppinglist.data.local.ShoppingItem
import com.amirhossein.shoppinglist.data.remote.responses.ImageResponse
import com.amirhossein.shoppinglist.util.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}