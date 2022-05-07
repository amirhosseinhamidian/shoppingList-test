package com.amirhossein.shoppinglist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirhossein.shoppinglist.data.local.ShoppingItem
import com.amirhossein.shoppinglist.data.remote.responses.ImageResponse
import com.amirhossein.shoppinglist.repositories.ShoppingRepository
import com.amirhossein.shoppinglist.util.Constants.MAX_NAME_LENGTH
import com.amirhossein.shoppinglist.util.Constants.MAX_PRICE_LENGTH
import com.amirhossein.shoppinglist.util.Event
import com.amirhossein.shoppinglist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
): ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String){
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The field must not be empty")))
            return
        }
        if (name.length > MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The name of the item" +
                    "must not exceed $MAX_NAME_LENGTH character")))
            return
        }
        if (priceString.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.Error("The price of the item" +
                    "must not exceed $MAX_PRICE_LENGTH + character")))
            return
        }
        val amount = try {
            amountString.toInt()
        }catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.Error("please enter valid amount.")))
            return
        }
        val shopItem = ShoppingItem(name,priceString.toFloat(),amount,_curImageUrl.value ?: "")
        insertShoppingItemIntoDb(shopItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.Success(shopItem)))
    }

    fun searchForImage(imageQuery: String){

    }
}