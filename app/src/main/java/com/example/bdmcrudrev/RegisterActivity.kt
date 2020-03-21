package com.example.bdmcrudrev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bdmcrudrev.`object`.LogModel
import com.example.bdmcrudrev.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //tombol register ketika ditekan
        btnRegister.setOnClickListener {
            //inisialisasi data
            val r_username = regUsername!!.text.toString()
            val r_pass = regPassword!!.text.toString()
            val databaseHandler = DatabaseHandler(this)
            //filter untuk mengecek data
            if (r_username != "" && r_pass != "") {
                if (!databaseHandler!!.checkUser(r_username.trim())) {
                    //jika user belum terdaftar, maka akan menambah data user baru
                    var user = LogModel(logUsername = r_username.trim(), logPassword = r_pass.trim())
                    databaseHandler!!.addUser(user)
                    Toast.makeText(applicationContext, "User berhasil dibuat", Toast.LENGTH_LONG)
                        .show()
                    //berpindah ke LoginActifity
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Username sudah terpakai", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Lengkapi data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
