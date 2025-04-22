package com.redditizie.icomunicator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.redditizie.icomunicator.databinding.ActivityCadastroBinding
import com.redditizie.icomunicator.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    private val binding by lazy {

        ActivityPerfilBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarToolbar()

    }

    fun inicializarToolbar(){

        val toolbar = binding.includeToolbarPerfil.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Editar perfil"
            setDisplayHomeAsUpEnabled(true) // Exibi um botao voltar na tooltbar. Configurar no manifest o nome da activity pai: android:parentActivityName=".LoginActivity"
        }
    }
}