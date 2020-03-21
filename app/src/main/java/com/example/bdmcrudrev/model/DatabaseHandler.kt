package com.example.bdmcrudrev.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.bdmcrudrev.`object`.EmpModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //inisialisasi Database
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val TABLE_CONTACTS = "EmployeeTable"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ADDRESS = "address"
    }

    //gun untuk create table
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_ADDRESS + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    //fun untuk upgrade database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    // fun untuk menambahkan data
    fun addEmployee(emp: EmpModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL, emp.userEmail)
        contentValues.put(KEY_ADDRESS, emp.userAddress)
        // menambahkan data pada tabel
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // fun untuk menampilkan data dari tabel ke UI
    @SuppressLint("Recycle")
    fun viewEmployee(): ArrayList<EmpModel> {
        val empList: ArrayList<EmpModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var userName: String
        var userEmail: String
        var userAddress: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userAddress = cursor.getString(cursor.getColumnIndex("address"))
                val emp = EmpModel(userId = userId, userName = userName, userEmail = userEmail, userAddress = userAddress)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    // fun untuk memperbarui data pegawai
    fun updateEmployee(emp: EmpModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName)
        contentValues.put(KEY_EMAIL, emp.userEmail)
        contentValues.put(KEY_ADDRESS, emp.userAddress)
        // memperbarui data
        val success = db.update(TABLE_CONTACTS, contentValues, "id=" + emp.userId, null)
        db.close()
        return success
    }

    // fun untuk menghapus data
    fun deleteEmployee(emp: EmpModel):Int{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        // employee id dari data yang akan dihapus
        contentValues.put(KEY_ID,emp.userId)
        val success=db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        db.close()
        return success
    }
}