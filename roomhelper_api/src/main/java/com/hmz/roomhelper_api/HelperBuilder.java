package com.hmz.roomhelper_api;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.io.File;
import java.util.concurrent.Executor;

public class HelperBuilder<T extends RoomDatabase> {
    RoomDatabase.Builder builder;
    T roomDatabase;

    public HelperBuilder(RoomDatabase.Builder builder) {
        this.builder = builder;
    }

    public T getRoomDatabase() {
        return roomDatabase;
    }

    @NonNull
    public HelperBuilder<T> createFromAsset(@NonNull String databaseFilePath) {
        this.builder.createFromAsset(databaseFilePath);
        return this;
    }

    @NonNull
    public HelperBuilder<T> createFromFile(@NonNull File databaseFile) {
        this.builder.createFromFile(databaseFile);
        return this;
    }


    @NonNull
    public HelperBuilder<T> openHelperFactory(@Nullable SupportSQLiteOpenHelper.Factory factory) {
        this.builder.openHelperFactory(factory);
        return this;
    }

    @NonNull
    public HelperBuilder<T> addMigrations(@NonNull Migration... migrations) {
        this.builder.addMigrations(migrations);
        return this;
    }


    @NonNull
    public HelperBuilder<T> allowMainThreadQueries() {
        this.builder.allowMainThreadQueries();
        return this;
    }


    @NonNull
    public HelperBuilder<T> setJournalMode(@NonNull RoomDatabase.JournalMode journalMode) {
        this.builder.setJournalMode(journalMode);
        return this;
    }


    @NonNull
    public HelperBuilder<T> setQueryExecutor(@NonNull Executor executor) {
        this.builder.setQueryExecutor(executor);
        return this;
    }


    @NonNull
    public HelperBuilder<T> setTransactionExecutor(@NonNull Executor executor) {
        this.builder.setTransactionExecutor(executor);
        return this;
    }


    @NonNull
    public HelperBuilder<T> enableMultiInstanceInvalidation() {
        this.builder.enableMultiInstanceInvalidation();
        return this;
    }


    @NonNull
    public HelperBuilder<T> fallbackToDestructiveMigration() {
        this.builder.fallbackToDestructiveMigration();
        return this;
    }


    @NonNull
    public HelperBuilder<T> fallbackToDestructiveMigrationOnDowngrade() {
        this.builder.fallbackToDestructiveMigrationOnDowngrade();
        return this;
    }

    @NonNull
    public HelperBuilder<T> fallbackToDestructiveMigrationFrom(int... startVersions) {
        this.builder.fallbackToDestructiveMigrationFrom(startVersions);
        return this;
    }

    @NonNull
    public HelperBuilder<T> addCallback(@NonNull RoomDatabase.Callback callback) {
        this.builder.addCallback(callback);
        return this;
    }


    @SuppressLint("RestrictedApi")
    @NonNull
    public T build() {
        this.roomDatabase = (T) this.builder.build();
        return roomDatabase;
    }

}
