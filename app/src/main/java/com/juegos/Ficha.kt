package com.juegos

class Ficha (val color: Int, val tipo: Int, val movimientos: IntArray, private val longitud : Int) {

    fun isPeon() : Boolean {
        if (tipo == Constantes.FICHA_PEON)
            return true
        return false
    }

    fun isCaballo(): Boolean {
        if (tipo == Constantes.FICHA_CABALLO)
            return true
        return false
    }

    fun isTorre(): Boolean {
        if (tipo == Constantes.FICHA_TORRE)
            return true
        return false
    }

    fun isAlfil(): Boolean {
        if (tipo == Constantes.FICHA_ALFIL)
            return true
        return false
    }

    fun isReina(): Boolean {
        if (tipo == Constantes.FICHA_REINA)
            return true
        return false
    }

    fun isRey() : Boolean {
        if (tipo == Constantes.FICHA_REY)
            return true
        return false
    }

    fun isBlanca() : Boolean {
        if (color == Constantes.BLANCAS)
            return true
        return false
    }

    fun isDistanciaCorta() : Boolean {
        if (longitud == Constantes.LONGITUD_CORTO)
            return true
        return false
    }

}