package com.amirhossein.shoppinglist.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.amirhossein.shoppinglist.getOrAwaitValue
import com.amirhossein.shoppinglist.launchFragmentInHiltContainer
import com.amirhossein.shoppinglist.ui.ShoppingFragment
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingItemDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.shoppingItemDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name",1f,1,"url",1)
        dao.insertShoppingItem(shoppingItem)

        val allShopItems = dao.observeAllShoppingItem().getOrAwaitValue()

        assertThat(allShopItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name",1f,1,"url",1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShopItems = dao.observeAllShoppingItem().getOrAwaitValue()
        assertThat(allShopItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPrice() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name",10f,3,"url",1)
        val shoppingItem2 = ShoppingItem("name",5.5f,2,"url",2)
        val shoppingItem3 = ShoppingItem("name",100f,0,"url",3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPrice = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(10f*3 + 5.5f*2)
    }
}