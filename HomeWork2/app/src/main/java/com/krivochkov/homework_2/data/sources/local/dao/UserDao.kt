package com.krivochkov.homework_2.data.sources.local.dao

import androidx.room.*
import com.krivochkov.homework_2.data.sources.local.entity.UserEntity
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Single<List<UserEntity>>

    @Query("SELECT * FROM users WHERE isMe = 1")
    fun getMyUser(): Single<UserEntity>

    @Query("DELETE FROM users")
    fun deleteAllUsers()

    @Query("DELETE FROM users WHERE isMe = 1")
    fun deleteMyUser()

    @Transaction
    fun updateAllUsers(newUsers: List<UserEntity>) {
        deleteAllUsers()
        insertUsers(newUsers)
    }

    @Transaction
    fun updateMyUser(myUser: UserEntity) {
        deleteMyUser()
        insertUser(myUser)
    }
}