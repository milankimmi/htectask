package com.htec.task.room.dao

import androidx.room.*
import com.htec.task.datamodel.*

@Dao
interface UserDao {

    fun addUser(user: User) {
        val userId = insertUser(user)

        // If user address is not null
        user.address?.let { address ->
            address.userId = userId

            val addressId = insertAddress(address)
            // If geo object is not null
            address.geo?.let { geo ->
                geo.addressId = addressId
                insertGeo(geo)
            }
        }

        user.company?.let { company ->
            company.userId = userId
            insertCompany(company)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeo(geo: Geo) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(company: Company)

    @Query("SELECT * FROM user WHERE id= :userId")
    fun getUserById(userId: Long): User

    @Query("SELECT * FROM address WHERE userId= :userId")
    fun getAddressByUserId(userId:Long): Address

    @Query("SELECT * FROM company WHERE userId= :userId")
    fun getCompanyByUserId(userId: Long): Company

    @Transaction
    @Query("SELECT * FROM user")
    fun loadUserWithAllPosts(): List<UserWithPosts>

    @Transaction
    @Query("SELECT * FROM user WHERE id=:userId")
    fun loadUserData(userId: Long): UserData?

    @Query("DELETE FROM user")
    fun deleteAllUsers()
}