package com.example.studentprofileapp

import android.os.Parcel
import android.os.Parcelable

// Hapus tulisan @Parcelize karena kita tidak pakai plugin lagi
data class UserProfile(
    val nama: String,
    val nim: String,
    val prodi: String,
    val jenisKelamin: String,
    val hobi: String
) : Parcelable {

    // Membaca data dari Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Menulis data ke Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nama)
        parcel.writeString(nim)
        parcel.writeString(prodi)
        parcel.writeString(jenisKelamin)
        parcel.writeString(hobi)
    }

    override fun describeContents(): Int {
        return 0
    }

    // Creator wajib untuk Parcelable manual
    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }
}