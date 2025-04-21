package com.redditizie.icomunicator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.redditizie.icomunicator.databinding.ActivityCadastroBinding
import com.redditizie.icomunicator.databinding.ActivityLoginBinding

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {

        ActivityCadastroBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarToolbar()

    }

    fun inicializarToolbar(){

        val toolbar = binding.includeToolbar.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Faça o seu cadastro"
            setDisplayHomeAsUpEnabled(true) // Exibi um botao voltar na tooltbar. Configurar no manifest o nome da activity pai: android:parentActivityName=".LoginActivity"
        }
    }
}