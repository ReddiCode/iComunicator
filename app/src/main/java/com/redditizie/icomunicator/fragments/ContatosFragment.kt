package com.redditizie.icomunicator.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.redditizie.icomunicator.R
import com.redditizie.icomunicator.activities.MensagensActivity
import com.redditizie.icomunicator.adapters.ContatosAdapter
import com.redditizie.icomunicator.databinding.FragmentContatosBinding
import com.redditizie.icomunicator.model.Usuario
import com.redditizie.icomunicator.utilities.Constantes


class ContatosFragment : Fragment() {

    private lateinit var binding: FragmentContatosBinding

    private lateinit var eventoSnapshot: ListenerRegistration // usado para remover o Listener

    private lateinit var contatosAdapter: ContatosAdapter

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContatosBinding.inflate(
            inflater, container, false
        )

        contatosAdapter = ContatosAdapter{ usuario ->
            val intent = Intent( context, MensagensActivity::class.java)
            intent.putExtra("dadosDestinatario", usuario)
            intent.putExtra("origem", Constantes.ORIGEM_CONTATO)
            startActivity(intent)
        }
        binding.rvContatos.adapter = contatosAdapter
        binding.rvContatos.layoutManager = LinearLayoutManager( context )
        binding.rvContatos.addItemDecoration(// colocar linha divisoria
            DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
        )


        return binding.root

    }

    override fun onStart() {
        super.onStart()

        adicionarListenerContatos()
    }

    private fun adicionarListenerContatos() {

        eventoSnapshot = firestore

            .collection("usuarios")
            .addSnapshotListener{ querySnapshot, erro ->

                val documentos = querySnapshot?.documents
                val listaContatos = mutableListOf<Usuario>()

                documentos?.forEach { documentSnapshot ->
                    val idUsuarioLogado = firebaseAuth.currentUser?.uid
                    val usuario = documentSnapshot.toObject( Usuario::class.java)
                    if( usuario != null && idUsuarioLogado != null  ){
                        if(idUsuarioLogado != usuario.id ){
                            listaContatos.add( usuario )
                        }

                    }

                }
                // Lista de contatos (atualizar o Recycler View

                if( listaContatos.isNotEmpty() ){
                    contatosAdapter.adicionarLista( listaContatos )
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        eventoSnapshot.remove() // quando o fragment for destruido nao queremos continuar recuperando dados do Firebase
    }


}