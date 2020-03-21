package com.example.bdmcrudrev

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bdmcrudrev.`object`.EmpModel
import com.example.bdmcrudrev.helper.EmpAdapter
import com.example.bdmcrudrev.model.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewRecord()//memanggil fun viewRecord

        //mengatur item tarik untuk refresh(swipe to refresh) list data
        itemsswipetorefresh.setProgressBackgroundColorSchemeColor(//mengubah bg swipe to refresh
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        itemsswipetorefresh.setColorSchemeColors(Color.WHITE)
        itemsswipetorefresh.setOnRefreshListener {//menjalankan sorce code dibawah ketika swipe dilakukan
            viewRecord()
            itemsswipetorefresh.isRefreshing = false
        }

        //mengatur item floatingbutton untuk fun tambah data
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            createRecord()
        }
    }

    //fun untuk menambah data
    @SuppressLint("InflateParams")
    fun createRecord() {
        //pembuatan AlertDialog untuk menambah data
        val dialogBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        val inflater = this.layoutInflater
        //menggunakan layout custom yaitu action_dialog.xml
        val dialogView = inflater.inflate(R.layout.action_dialog,null)
        dialogBuilder.setView(dialogView)

        //insisalisasi editText yang akan muncul pada dialog box
        val etId = dialogView.findViewById(R.id.editTextId) as EditText
        val etName = dialogView.findViewById(R.id.editTextName) as EditText
        val etEmail = dialogView.findViewById(R.id.editTextEmail) as EditText
        val etAddress = dialogView.findViewById(R.id.editTextAddress) as EditText

        //pengaturan tentang dialog box yang akan dibuat
        dialogBuilder.setTitle("Tambah Data")
        dialogBuilder.setMessage("Masukkan data yang sesuai")
        //ketika user menekan simpan
        dialogBuilder.setPositiveButton("Simpan", DialogInterface.OnClickListener { _, _ ->
            //mengambil data dari editText
            val id = etId.text.toString()
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val address = etAddress.text.toString()
            val databaseHandler = DatabaseHandler(this)
            //filter untuk editText kosong
            if (id.trim() != "" && name.trim() != "" && email.trim() != "" && address.trim() != "") {
                //memanggil fun dari databaseHandler untuk menambah data melalui model
                val status =
                    databaseHandler.addEmployee(
                        EmpModel(
                            Integer.parseInt(id),
                            name,
                            email,
                            address
                        )
                    )
                if (status > -1) {
                    Toast.makeText(applicationContext, "Data Tersimpan", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(applicationContext, "Lengkapi data", Toast.LENGTH_LONG).show()
            }
        })
        dialogBuilder.setNeutralButton("Batal", DialogInterface.OnClickListener { _, _ ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }

    // fun untuk membaca data dari database dan menampilkannya dari listview
    @SuppressLint("WrongConstant")
    fun viewRecord() {
        // membuat instanisasi databasehandler
        val databaseHandler = DatabaseHandler(this)
        // memamnggil fungsi viewemployee dari databsehandler untuk mengambil data
        val emp: ArrayList<EmpModel> = databaseHandler.viewEmployee()
        val empArrayId = Array(emp.size) { "0" }
        val empArrayName = Array(emp.size) { "null" }
        val empArrayEmail = Array(emp.size) { "null" }
        val empArrayAddress = Array(emp.size) { "null" }
        // setiap data yang didapatkan dari database akan dimasukkan ke array
        for ((index, e) in emp.withIndex()) {
            empArrayId[index] = e.userId.toString()
            empArrayName[index] = e.userName
            empArrayEmail[index] = e.userEmail
            empArrayAddress[index] = e.userAddress
        }
        // membuat customadapter untuk view UI
        val recyclerView = findViewById<RecyclerView>(R.id.itemsrv)
        val adapter = EmpAdapter(emp) { empItem: EmpModel -> partItemClicked(empItem) }
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    }

    // fun untuk memperbarui data sesuai id user
    @SuppressLint("ResourceAsColor")
    fun updateRecord(userId: String, userName: String, userEmail: String, userAddress: String) {
        //membuat AlertDialog untuk mengupdate data
        val dialogBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        //pengaturan tentang dialog box yang akan dibuat
        dialogBuilder.setTitle("Pembaruan data")
        dialogBuilder.setMessage("Yakin untuk perbarui data dengan Id = $userId")
        //ketika user menekan simpan
        dialogBuilder.setPositiveButton("Simpan", DialogInterface.OnClickListener { _, _ ->
            //mengambil data dari editText
            val updateId = userId
            val updateName = userName
            val updateEmail = userEmail
            val updateAddress = userAddress
            val databaseHandler = DatabaseHandler(this)
            //filter untuk data kosong
            if (updateId.trim() != "" && updateName.trim() != "" && updateEmail.trim() != "") {
                //memanggil fun dari databaseHandler untuk memperbarui data melalui model
                val status = databaseHandler.updateEmployee(
                    EmpModel(
                        Integer.parseInt(updateId), updateName, updateEmail, updateAddress
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "Data Terupdate", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Lengkapi data",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        //ketika user menekan batal
        dialogBuilder.setNeutralButton("Batal", DialogInterface.OnClickListener { dialog, which ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }

    // fun untuk menghapus data berdasarkan id
    fun deleteRecord(userId: String) {
        //membuat AlertDialog untuk menghapus data
        val dialogBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        //pengaturan tentang dialog box yang akan dibuat
        dialogBuilder.setTitle("Hapus data")
        dialogBuilder.setMessage("Yakin hapus data dengan Id = $userId")
        //ketika user menekan hapus
        dialogBuilder.setPositiveButton("Hapus", DialogInterface.OnClickListener { _, _ ->
            val deleteId = userId
            val databaseHandler = DatabaseHandler(this)
            //filter untuk data kosong
            if (deleteId.trim() != "") {
                //memanggil fun dari databaseHandler untuk menhapus data melalui model
                val status =
                    databaseHandler.deleteEmployee(
                        EmpModel(
                            Integer.parseInt(userId),
                            "",
                            "",
                            ""
                        )
                    )
                if (status > -1) Toast.makeText(applicationContext, "Data Terhapus", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Lengkapi Data", Toast.LENGTH_LONG)
                    .show()
            }
        })
        //ketika user menekan batal
        dialogBuilder.setNeutralButton("Batal", DialogInterface.OnClickListener { _, _ ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }

    //fungsi untuk menampilkan Action dan Data ketika RecylerView diklik
    @SuppressLint("InflateParams")
    private fun partItemClicked(empItem: EmpModel) {
        //membuat AlertDialog untuk menampilkan data
        val dialogBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        val inflater = this.layoutInflater
        //menggunakan layout custom yaitu action_dialog.xml
        val dialogView = inflater.inflate(R.layout.action_dialog,null)
        dialogBuilder.setView(dialogView)

        //insisalisasi editText yang akan muncul pada dialog box
        val etId = dialogView.findViewById(R.id.editTextId) as EditText
        val etName = dialogView.findViewById(R.id.editTextName) as EditText
        val etEmail = dialogView.findViewById(R.id.editTextEmail) as EditText
        val etAddress = dialogView.findViewById(R.id.editTextAddress) as EditText

        //mengambil data dari Model
        val conId = empItem.userId.toString()
        val conName = empItem.userName
        val conEmail = empItem.userEmail
        val conAddress = empItem.userAddress

        etId.isEnabled = false//menonaktifkan editText untuk Id

        //menampilkan data ke editText
        etId.setText(conId)
        etName.setText(conName)
        etEmail.setText(conEmail)
        etAddress.setText(conAddress)

        //pengaturan tentang dialog box yang akan dibuat
        dialogBuilder.setTitle("Aksi")
        dialogBuilder.setMessage("Pilih aksi yang akan dilakukan")
        //ketika user menekan simpan
        dialogBuilder.setPositiveButton("Ubah", DialogInterface.OnClickListener { _, _ ->
            val userId = etId.text.toString()
            val userName = etName.text.toString()
            val userEmail = etEmail.text.toString()
            val userAddress = etAddress.text.toString()
            updateRecord(userId, userName, userEmail, userAddress)
        })
        //ketika user menekan hapus
        dialogBuilder.setNegativeButton("Hapus", DialogInterface.OnClickListener { _, _ ->
            val userId = etId.text.toString()
            deleteRecord(userId)
        })
        //ketika user menekan batal
        dialogBuilder.setNeutralButton("Batal", DialogInterface.OnClickListener { _, _ ->
            // tidak melakukan apa2 :)
        })
        val b = dialogBuilder.create()
        b.show()
    }
}
