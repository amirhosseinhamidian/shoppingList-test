package com.amirhossein.shoppinglist.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amirhossein.shoppinglist.MainCoroutineRule
import com.amirhossein.shoppinglist.getOrAwaitValueTest
import com.amirhossein.shoppinglist.repositories.FakeShoppingRepository
import com.amirhossein.shoppinglist.util.Constants.MAX_NAME_LENGTH
import com.amirhossein.shoppinglist.util.Constants.MAX_PRICE_LENGTH
import com.amirhossein.shoppinglist.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field , return error`() {
        viewModel.insertShoppingItem("name","","8.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long name, returns error`(){
        val string = buildString {
            for (i in 1..MAX_NAME_LENGTH+1){
                append(1)
            }
        }
        viewModel.insertShoppingItem(string,"6","3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too long price, return error`() {
        val string = buildString {
            for (i in 1..MAX_PRICE_LENGTH+1){
                append(1)
            }
        }
        viewModel.insertShoppingItem("name","9",string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with too high amount, return error`(){
        viewModel.insertShoppingItem("name","999999999999999","5.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert shopping item with valid input, return ture`() {
        viewModel.insertShoppingItem("name","8","5.5")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }
}