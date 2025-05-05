package com.redditizie.icomunicator.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.redditizie.icomunicator.R
import com.redditizie.icomunicator.databinding.ActivityMensagensBinding
import com.redditizie.icomunicator.model.Mensagem
import com.redditizie.icomunicator.model.Usuario
import com.redditizie.icomunicator.utilities.Constantes
import com.redditizie.icomunicator.utilities.exibirMensagem
import com.squareup.picasso.Picasso

class MensagensActivity : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val binding by lazy {
        ActivityMensagensBinding.inflate( layoutInflater )
    }

    private var dadosDestinatario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recuperarDadosUsuariosDestinatario()
        inicializarToolbar()
        inicializarEventoClique()

    }

    private fun inicializarEventoClique() {

        binding.fabEnviar.setOnClickListener {
            val mensagem = binding.editMensagem.text.toString()
            salvarMensagem( mensagem )
        }
    }

    private fun salvarMensagem(textoMensagem: String) {

       if(textoMensagem.isNotEmpty() ){

           val idUsuarioRemetente = firebaseAuth.currentUser?.uid
           val idUsuarioDestinatario = dadosDestinatario?.id
           if(idUsuarioRemetente != null && idUsuarioDestinatario != null){
               val mensagem = Mensagem(
                   idUsuarioRemetente, textoMensagem
               )

               //Salvar para o Remetente
                salvaMensagemFirestore(
                    idUsuarioRemetente, idUsuarioDestinatario, mensagem
                )

               //Salvar mensagem Destinatario
               salvaMensagemFirestore(
                   idUsuarioDestinatario, idUsuarioRemetente, mensagem
               )

               binding.editMensagem.setText("") // limpar edit texto apos envio da mensagem

           }

       }

    }

    private fun salvaMensagemFirestore(
        idUsuarioRemetente: String,
        idUsuarioDestinatario: String,
        mensagem: Mensagem
    ) {
        firestore
            .collection(Constantes.MENSAGENS)
            .document( idUsuarioRemetente)
            .collection( idUsuarioDestinatario)
            .add( mensagem)
            .addOnFailureListener {
                exibirMensagem("Erro ao enviar mensagem")
            }
    }

    fun inicializarToolbar() {

        val toolbar = binding.tbMensagens
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            if(dadosDestinatario != null ){

                binding.textNome.text = dadosDestinatario!!.nome
                Picasso.get()
                    .load(dadosDestinatario!!.foto)
                    .into(binding.imageFotoPerfil)

            }
            setDisplayHomeAsUpEnabled(true) // Exibi um botao voltar na tooltbar. Configurar no manifest o nome da activity pai: android:parentActivityName=".LoginActivity"
        }
    }



    private fun recuperarDadosUsuariosDestinatario() {
        val extras = intent.extras
        if( extras != null ){
            val origem = extras.getString("origem")
            if( origem == Constantes.ORIGEM_CONTATO ){

                //Recuperar os dados do destinatario(usuario)

                dadosDestinatario = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    extras.getParcelable("dadosDestinatario", Usuario::class.java)
                }else{

                    extras.getParcelable("dadosDestinatario")
                }

            }else if( origem == Constantes.ORIGEM_CONVERSA ) {

                //Recuperar os dados da conversa
            }
        }
    }
}