package com.prajwalch.torrentsearch.data.local

import android.content.Context
import android.util.Log
import androidx.room.AutoMigration

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.prajwalch.torrentsearch.data.local.dao.BookmarkedTorrentDao
import com.prajwalch.torrentsearch.data.local.dao.SearchHistoryDao
import com.prajwalch.torrentsearch.data.local.dao.TorznabSearchProviderDao
import com.prajwalch.torrentsearch.data.local.entities.BookmarkedTorrent
import com.prajwalch.torrentsearch.data.local.entities.SearchHistoryEntity
import com.prajwalch.torrentsearch.data.local.entities.TorznabSearchProviderEntity

/** Application database. */
@Database(
    entities = [
        BookmarkedTorrent::class,
        SearchHistoryEntity::class,
        TorznabSearchProviderEntity::class,
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
abstract class TorrentSearchDatabase : RoomDatabase() {
    abstract fun bookmarkedTorrentDao(): BookmarkedTorrentDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun torznabSearchProviderDao(): TorznabSearchProviderDao

    companion object {
        private const val TAG = "TorrentSearchDatabase"
        
        /** Name of the database file. */
        private const val DB_NAME = "torrentsearch.db"

        /**
         * Single instance of the database.
         *
         * Recommended to re-use the reference once database is created.
         */
        private var Instance: TorrentSearchDatabase? = null

        /** Returns the instance of the database. */
        fun getInstance(context: Context): TorrentSearchDatabase {
            return Instance ?: createInstance(context = context)
        }

        /** Creates, stores and returns the instance of the database. */
        private fun createInstance(context: Context): TorrentSearchDatabase {
            val databaseBuilder = Room.databaseBuilder(
                context = context,
                klass = TorrentSearchDatabase::class.java,
                name = DB_NAME,
            )
                // Add crash-safe fallback for destructive migration
                // This prevents app crashes if migration fails
                .fallbackToDestructiveMigration()
                // Add callback for logging and handling database events
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i(TAG, "Database created successfully")
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Log.i(TAG, "Database opened successfully (version: ${db.version})")
                    }
                    
                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        Log.w(TAG, "Destructive migration occurred - database was reset")
                    }
                })

            return databaseBuilder.build().also { Instance = it }
        }
        
        /**
         * Manual migrations for cases where AutoMigration is not sufficient.
         * Add new migrations here as needed for schema changes.
         */
        private val MIGRATIONS = arrayOf<Migration>(
            // Example migration from version X to Y:
            // object : Migration(X, Y) {
            //     override fun migrate(database: SupportSQLiteDatabase) {
            //         // Migration SQL statements
            //     }
            // }
        )
    }
}