package com.amirhossein.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.amirhossein.shoppinglist.data.local.ShoppingItemDao
import com.amirhossein.shoppinglist.data.local.ShoppingItemDatabase
import com.amirhossein.shoppinglist.data.remote.PixabayApi
import com.amirhossein.shoppinglist.repositories.DefaultShoppingRepository
import com.amirhossein.shoppinglist.repositories.ShoppingRepository
import com.amirhossein.shoppinglist.util.Constants.BASE_URL
import com.amirhossein.shoppinglist.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context:Context
    ) = Room.databaseBuilder(context,ShoppingItemDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingItemDao,
        api: PixabayApi
    ) = DefaultShoppingRepository(dao,api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingItemDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayApi::class.java)
    }
}