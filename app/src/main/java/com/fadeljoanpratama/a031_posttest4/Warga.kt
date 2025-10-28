package com.fadeljoanpratama.a031_posttest4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warga")
data class WargaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nama: String,
    val nik: String,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: String,
    val rw: String,
    val jenisKelamin: String,
    val statusPernikahan: String
)