package com.cagataykolus.productapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cagataykolus.productapp.data.local.dao.ProductDao
import com.cagataykolus.productapp.data.local.dao.ReviewDao
import com.cagataykolus.productapp.model.Review

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * ReviewDatabase provides DAO [ReviewDao] by using method [getReviewDao].
 */
@Database(
    entities = [Review::class],
    version = MigrationDatabase.DB_VERSION
)
abstract class ReviewDatabase : RoomDatabase() {

    abstract fun getReviewDao(): ReviewDao

    companion object {
        private const val DB_NAME = "database_review"

        @Volatile
        private var INSTANCE: ReviewDatabase? = null

        fun getInstance(context: Context): ReviewDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReviewDatabase::class.java,
                    DB_NAME
                ).addMigrations(*MigrationDatabase.MIGRATION_REVIEW).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
