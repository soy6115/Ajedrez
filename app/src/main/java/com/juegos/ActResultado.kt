package com.juegos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.juegos.databinding.ActivityActResultadoBinding

class ActResultado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityActResultadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recogemos la información de la pantalla anterior
        val bundle = intent.extras
        val color = bundle?.getInt(Constantes.VICTORIA)

        // tengo puesto que automáticamente el fondo sea de victoria de blancos, por lo que solo
        // necesito cambiar el fondo cuando el que gane son negras
        if (color == Constantes.NEGRAS) {
            val divFondo = binding.divResultado
            divFondo.setBackgroundResource(R.drawable.bgnegras)
        }

        binding.btVolverAJugar.setOnClickListener{
            irAPrincipal()
        }
    }

    private fun irAPrincipal() {
        val intentPrincipal = Intent(this, MainActivity::class.java)
        startActivity(intentPrincipal)
        finish()
    }
}