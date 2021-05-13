package com.cagataykolus.productapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Each Migration has a start and end versions and Room runs these migrations to bring the
 * database to the latest version. The migration object that can modify the database and
 * to the necessary changes.
 */
object MigrationDatabase {
    const val DB_VERSION = 2

    val MIGRATION_PRODUCT: Array<Migration>
        get() = arrayOf(
            migrationProduct()
        )
    val MIGRATION_REVIEW: Array<Migration>
        get() = arrayOf(
            migrationReview()
        )

    /**
     *  Creates a new migration between version 1 and 2 for [Product.TABLE_PRODUCT] table.
     */
    private fun migrationProduct(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${Product.TABLE_PRODUCT} ADD COLUMN body TEXT")
        }
    }

    /**
     *  Creates a new migration between version 1 and 2 for [Review.TABLE_REVIEW] table.
     */
    private fun migrationReview(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${Review.TABLE_REVIEW} ADD COLUMN body TEXT")
        }
    }
}

