package com.amirhossein.shoppinglist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingItem::class],
    version = 2
)
abstract class ShoppingItemDatabase: RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}