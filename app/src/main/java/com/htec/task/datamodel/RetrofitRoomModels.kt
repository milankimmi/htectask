package com.htec.task.datamodel

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Post")
data class Post(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose val id: Long,
    @ColumnInfo(name = "userId") @SerializedName("userId") @Expose val userId: Long,
    @ColumnInfo(name = "title") @SerializedName("title") val title: String,
    @ColumnInfo(name = "body") @SerializedName("body") val body: String
) {
    constructor() : this(0, 0, "", "")
}

@Entity(tableName = "User")
data class User(
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose var id: Long,
    @ColumnInfo(name = "name") @SerializedName("name") @Expose var name: String,
    @ColumnInfo(name = "username") @SerializedName("username") @Expose var username: String?,
    @ColumnInfo(name = "email") @SerializedName("email") @Expose var email: String?,
    @Ignore @SerializedName("address") @Expose var address: Address?,
    @ColumnInfo(name = "phone") @SerializedName("phone") @Expose var phone: String?,
    @ColumnInfo(name = "website") @SerializedName("website") @Expose var website: String?,
    @Ignore @SerializedName("company") @Expose var company: Company?
) {
    constructor() : this(0, "", "", "", null, "", "", null)
}

@Entity(
    tableName = "Address",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Address(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @Transient var id: Long,
    @ColumnInfo(name = "userId") @Transient var userId: Long,
    @ColumnInfo(name = "street") @SerializedName("street") @Expose var street: String?,
    @ColumnInfo(name = "suite") @SerializedName("suite") @Expose var suite: String?,
    @ColumnInfo(name = "city") @SerializedName("city") @Expose var city: String?,
    @ColumnInfo(name = "zipcode") @SerializedName("zipcode") @Expose var zipcode: String?,
    @Ignore @SerializedName("geo") @Expose var geo: Geo?
) {
    constructor() : this(0, 0, "", "", "", "", null)
}

@Entity(
    tableName = "Geo",
    foreignKeys = [ForeignKey(
        entity = Address::class,
        parentColumns = ["id"],
        childColumns = ["addressId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Geo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @Transient var id: Long,
    @ColumnInfo(name = "addressId") @Transient var addressId: Long,
    @ColumnInfo(name = "lat") @SerializedName("lat") @Expose var latitude: String?,
    @ColumnInfo(name = "lng") @SerializedName("lng") @Expose var longitute: String?
)

@Entity(
    tableName = "Company",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class Company(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @Transient var id: Long,
    @ColumnInfo(name = "userId") @Transient var userId: Long,
    @ColumnInfo(name = "name") @SerializedName("name") @Expose var name: String?,
    @ColumnInfo(name = "catchPhrase") @SerializedName("catchPhrase") @Expose var catchPhrase: String?,
    @ColumnInfo(name = "bs") @SerializedName("bs") @Expose var bs: String?
)

data class UserWithPosts(
    @Embedded val user: User,
    @Relation(parentColumn = "id", entityColumn = "userId", entity = Post::class)
    val posts: List<Post>
)

data class UserData(
    @Embedded val user: User,

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Address::class)
    val addressWithGeo: AddressWithGeo,

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Company::class)
    val company: Company
)

data class AddressWithGeo(
    @Embedded val address: Address,
    @Relation(parentColumn = "id", entityColumn = "addressId", entity = Geo::class)
    val geo: Geo
)