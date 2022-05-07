package com.amirhossein.shoppinglist.repositories

import androidx.lifecycle.LiveData
import com.amirhossein.shoppinglist.data.local.ShoppingItem
import com.amirhossein.shoppinglist.data.local.ShoppingItemDao
import com.amirhossein.shoppinglist.data.remote.PixabayApi
import com.amirhossein.shoppinglist.data.remote.responses.ImageResponse
import com.amirhossein.shoppinglist.util.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    val shoppingItemDao: ShoppingItemDao,
    val pixabayApi: PixabayApi
): ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItemDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingItemDao.observeAllShoppingItem()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingItemDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayApi.searchForImage(imageQuery)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("Unkown Error")
            }else{
                Resource.Error("Unkown Error")
            }
        }catch (e: Exception) {
            Resource.Error("error... check connection")
        }
    }
}