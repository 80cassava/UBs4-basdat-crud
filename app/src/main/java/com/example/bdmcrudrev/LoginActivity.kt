package com.example.bdmcrudrev

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bdmcrudrev.model.DatabaseHandler
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //check SharedPrefrences dengan nama "CEKLOGIN"
        val sharedPreferences = getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
        val stat = sharedPreferences.getString("STATUS", "")
        if (stat == "1") {//jika status=1
            //langsung berpindah ke RegisterActifity tanpa login
            startActivity(Intent(this, MainActivity::class.java))
        } else {//jika status!=1
            //tombol buton mengirimkan data
            btnLogin.setOnClickListener {
                //inisialisasi data
                val u_username = logUsername!!.text.toString()
                val u_pass = logPassword!!.text.toString()
                //data dikirim ke fun checkLogin()
                checkLogin(u_username, u_pass)
            }
        }

        //tombol register ketika ditekan
        btnToRegister.setOnClickListener {
            //berpindah ke RegisterActifity
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    //fun untuk mengecek user yang akan login
    fun checkLogin(data1: String, data2: String) {
        //inisialisasi data
        val u_username = data1
        val u_pass = data2
        val databaseHandler = DatabaseHandler(this)
        //filter untuk mengecek data
        if (u_username.trim() != "" && u_pass.trim() != "") {
            if (databaseHandler!!.checkUser(u_username.trim { it <= ' ' })) {
                if (databaseHandler!!.checkUser(u_username.trim { it <= ' ' },
                        u_pass.trim { it <= ' ' })
                ) {
                    //jika user terdaftar, maka sharedPrefrences terisi untuk menyimpan data user sementara
                    val sharedPreferences =
                        getSharedPreferences("CEKLOGIN", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("STATUS", "1")
                    editor.putString("USERNAME", u_username)
                    editor.apply()
                    //berpindah ke MainActifity
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(applicationContext, "Password salah", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, "Username salah", Toast.LENGTH_LONG).show()
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
