package com.redditizie.icomunicator.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.redditizie.icomunicator.databinding.ActivityLoginBinding
import com.redditizie.icomunicator.utilities.exibirMensagem

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {

        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String

    //Firebase
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarEventosClique()
        // firebaseAuth.signOut()

    }

    override fun onStart() {
        super.onStart()

        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {

        val usuarioAtual = firebaseAuth.currentUser
        if(usuarioAtual != null){
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )

        }
    }

    private fun inicializarEventosClique() {

        binding.textCadastro.setOnClickListener{
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }

        binding.btnLogar.setOnClickListener {
            if( ValidarCampos() ){
                logarUsuario()
            }
        }
    }

    private fun logarUsuario() {

        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            exibirMensagem("Sucesso ao fazer login")
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }.addOnFailureListener { erro ->

            try{
                throw erro
            }catch(erroUsuarioInvalido: FirebaseAuthInvalidUserException){
                erroUsuarioInvalido.printStackTrace()
                exibirMensagem("E-mail não cadastrado")

            }catch(erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidas.printStackTrace()
                exibirMensagem("E-mail ou senha estão  incorretos!")
            }

        }
    }

    private fun ValidarCampos(): Boolean {

        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if(email.isNotEmpty() ){
            binding.textInputLayoutLoginEmail.error = null
            if( senha.isNotEmpty() ){
                binding.textInputLayoutLoginSenha.error = null
                return true
            }else{
                binding.textInputLayoutLoginSenha.error = "Preencha sua senha"
                return false
            }

        }else{
            binding.textInputLayoutLoginEmail.error = "Preencha o e-mail"
            return false

        }
    }

}