package com.juegos

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import com.juegos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    // FUNDAMENTAL, Tablero [fila][columna]
    private var tablero = Array(8) {arrayListOf<Casilla>()}
    // para saber el turno de la partida
    private var swTurnoBlanco = true
    // contenedores visuales
    private lateinit var divPosibles : LinearLayout
    private lateinit var divPiezas : LinearLayout
    // variables para el control del enroque
    private var swBlancasReyMov = false
    private var swNegrasReyMov = false
    private var swBlancasTorreIzqMov = false
    private var swBlancasTorreDerMov = false
    private var swNegrasTorreIzqMov = false
    private var swNegrasTorreDerMov = false
    private var estadoEnRoque = Constantes.ENROQUE_NO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inciarPartida()
    }

    private fun inciarPartida() {
        // guardamos en la varaible las cajas para no tener que estar instanciandolas después desde
        // cada función
        divPosibles = binding.divContenedorPosibles
        divPiezas = binding.divContenedorPiezas

        // generación de la lógica y la parte visual de la partida
        generarCasillas()
        generarFichas()
        pintarFichas()

        // para saber que fichas son las que podemos pulsar
        actualizarClicables()
    }

    // aquí estamos creando la estrucutra de 8x8 que representa el tablero
    private fun generarCasillas() {
        // para recorrer las 8 filas (de 0 a 7 incluido)
        for (i in  Constantes.TABLERO_INICIO .. Constantes.TABLERO_FIN){
            // indicamos que es un array el contenido
            val fila = arrayListOf<Casilla>()
            // para recorrer las 8 columnas (0-7 inc)
            for (j in Constantes.TABLERO_INICIO .. Constantes.TABLERO_FIN) {
                // creamos la casilla con su posición y sin ficha
                val casilla = Casilla(i,j,null)
                fila.add(casilla)
            }
            //añadimos en el tablero la fila cuando ya la tenemos completada
            tablero[i] = fila
        }
    }

    // creación de las fichas a nivel lógico de programación
    private fun generarFichas() {
        // para cada ficha indicamos los tipos de movimientos que puede hacer
        val movimientoPeon = getMovimientosPeon()
        val movimientoTorre = getMovimientosTorre()
        val movimientoCaballo = getMovimientoCaballo()
        val movimientoAlfil = getMovimientoAlfil()
        val movimientoTotal = getMovimientosTotal()

        // BLANCAS
        val peonB = Ficha(Constantes.BLANCAS, Constantes.FICHA_PEON, movimientoPeon,
            Constantes.LONGITUD_CORTO)
        val torreB = Ficha(Constantes.BLANCAS, Constantes.FICHA_TORRE, movimientoTorre,
            Constantes.LONGITUD_LIBRE)
        val caballoB = Ficha(Constantes.BLANCAS, Constantes.FICHA_CABALLO, movimientoCaballo,
            Constantes.LONGITUD_CORTO)
        val alfilB = Ficha(Constantes.BLANCAS, Constantes.FICHA_ALFIL, movimientoAlfil,
            Constantes.LONGITUD_LIBRE)
        val reinaB = Ficha(Constantes.BLANCAS, Constantes.FICHA_REINA, movimientoTotal,
            Constantes.LONGITUD_LIBRE)
        val reyB = Ficha(Constantes.BLANCAS, Constantes.FICHA_REY, movimientoTotal,
            Constantes.LONGITUD_CORTO)
        // asignamos el objeto ficha en la casilla correspondiente
        tablero[0][0].ficha = torreB
        tablero[0][7].ficha = torreB
        tablero[0][1].ficha = caballoB
        tablero[0][6].ficha = caballoB
        tablero[0][2].ficha = alfilB
        tablero[0][5].ficha = alfilB
        tablero[0][3].ficha = reyB
        tablero[0][4].ficha = reinaB
        for (i in 0 .. 7)
            tablero[1][i].ficha = peonB

        // NEGRAS
        val peonN = Ficha(Constantes.NEGRAS, Constantes.FICHA_PEON, movimientoPeon,
            Constantes.LONGITUD_CORTO)
        val torreN = Ficha(Constantes.NEGRAS, Constantes.FICHA_TORRE, movimientoTorre,
            Constantes.LONGITUD_LIBRE)
        val caballoN = Ficha(Constantes.NEGRAS, Constantes.FICHA_CABALLO, movimientoCaballo,
            Constantes.LONGITUD_CORTO)
        val alfilN = Ficha(Constantes.NEGRAS, Constantes.FICHA_ALFIL, movimientoAlfil,
            Constantes.LONGITUD_LIBRE)
        val reinaN = Ficha(Constantes.NEGRAS, Constantes.FICHA_REINA, movimientoTotal,
            Constantes.LONGITUD_LIBRE)
        val reyN = Ficha(Constantes.NEGRAS, Constantes.FICHA_REY, movimientoTotal,
            Constantes.LONGITUD_CORTO)
        tablero[7][0].ficha = torreN
        tablero[7][7].ficha = torreN
        tablero[7][1].ficha = caballoN
        tablero[7][6].ficha = caballoN
        tablero[7][2].ficha = alfilN
        tablero[7][5].ficha = alfilN
        tablero[7][3].ficha = reyN
        tablero[7][4].ficha = reinaN
        for (i in 0 .. 7)
            tablero[6][i].ficha = peonN
    }

    // Lo he sacado fuera del generar fichas para poder usarlo desde la coronación
    private fun getMovimientosPeon() : IntArray {
        return intArrayOf(Constantes.MOVIMIENTO_VERTICAL)
    }

    private fun getMovimientosTorre() : IntArray {
        return intArrayOf(Constantes.MOVIMIENTO_VERTICAL,
            Constantes.MOVIMIENTO_HORIZONTAL)
    }

    private fun getMovimientoCaballo() : IntArray {
        return intArrayOf(Constantes.MOVIMIENTO_CABALLO)
    }

    private fun getMovimientoAlfil() : IntArray {
        return intArrayOf(Constantes.MOVIMIENTO_DIAGONAL)
    }

    private fun getMovimientosTotal() : IntArray {
        return intArrayOf(Constantes.MOVIMIENTO_HORIZONTAL, Constantes.MOVIMIENTO_VERTICAL,
            Constantes.MOVIMIENTO_DIAGONAL)
    }

    // Creación de la parte visual del tablero. Ponemos como fondo del View la ficha que queremos.
    // Lo hacemos sabiendo las coordenadas usando el codigo c+fila+columna
    private fun pintarFichas() {
        // BLANCAS
        var divAux = binding.fila1
        // aprovechas el for para poner todos los peones de golpe
        for (casilla in divAux.children)
            casilla.setBackgroundResource(R.drawable.blancaspeon)
        binding.c00.setBackgroundResource(R.drawable.blancastorre)
        binding.c01.setBackgroundResource(R.drawable.blancascaballo)
        binding.c02.setBackgroundResource(R.drawable.blancasalfil)
        binding.c03.setBackgroundResource(R.drawable.blancasrey)
        binding.c04.setBackgroundResource(R.drawable.blancasreina)
        binding.c05.setBackgroundResource(R.drawable.blancasalfil)
        binding.c06.setBackgroundResource(R.drawable.blancascaballo)
        binding.c07.setBackgroundResource(R.drawable.blancastorre)

        divAux = binding.fila6
        for (casilla in divAux.children)
            casilla.setBackgroundResource(R.drawable.negraspeon)
        binding.c70.setBackgroundResource(R.drawable.negrastorre)
        binding.c71.setBackgroundResource(R.drawable.negrascaballo)
        binding.c72.setBackgroundResource(R.drawable.negrasalfil)
        binding.c73.setBackgroundResource(R.drawable.negrasrey)
        binding.c74.setBackgroundResource(R.drawable.negrasreina)
        binding.c75.setBackgroundResource(R.drawable.negrasalfil)
        binding.c76.setBackgroundResource(R.drawable.negrascaballo)
        binding.c77.setBackgroundResource(R.drawable.negrastorre)
    }

    // recorremos el tablero para encontrar las fichas sobre las que poder actuar
    private fun actualizarClicables() {
        // recorremos el tablero para obtener el primer []
        tablero.forEach { filas ->
            // recorremos para ya tener el segundo []
            filas.forEach { casilla ->
                val ficha = casilla.ficha
                // si no está vacía
                if (ficha != null)
                    // y corresponde al color del turno
                    if ((swTurnoBlanco && ficha.color == Constantes.BLANCAS)
                        || (!swTurnoBlanco && ficha.color == Constantes.NEGRAS))
                        clicado(casilla)
            }
        }
    }

    // la casilla pasada como parámetro la transformamos a la parte visual para indicar que va a
    // tener un listener para que haga de botón
    private fun clicado(casilla: Casilla) {
        val tag = getNumeroTag(casilla)
        val clicable = divPiezas.findViewWithTag<View>(tag)
        clicable.setOnClickListener {
            calcularPosibilidades(casilla)
        }
    }

    // cuando ya sabemos que casilla es la que quiere mover el usuario, vamos a determinar todas
    // las casillas a las que se podría mover dicha ficha
    private fun calcularPosibilidades(casilla: Casilla) {
        val opciones = arrayListOf<Casilla>()
        // podemos recogerlo así porque sabemos que la casilla seleccionada siempre tiene su ficha
        val ficha = casilla.ficha!!
        // recorremos todas las direcciones en las que se puede mover la ficha
        for (movimiento in ficha.movimientos)
            when (movimiento) {
                Constantes.MOVIMIENTO_VERTICAL ->
                    opciones.addAll(getMovimientosVerticales(casilla))
                Constantes.MOVIMIENTO_HORIZONTAL ->
                    opciones.addAll(getMovientosHorizontales(casilla))
                Constantes.MOVIMIENTO_CABALLO ->
                    opciones.addAll(getMovimientoCaballo(casilla))
                Constantes.MOVIMIENTO_DIAGONAL ->
                    opciones.addAll(getMovimientosDiagonales(casilla))
            }
        // aparte de la comprobación general, comprobamos si la ficha es el rey para controlar la
        // posibilidad de que quiera hacer un enroque
        if (ficha.isRey())
            opciones.addAll(isPosibleEnroque(casilla))

        // antes de marcar las nuevas opciones que tenemos, vacíamos el tablero de las anteriores
        // posibilidades
        limpiarOpciones()
        // comprobamos que tenga opciones de movimiento
        if (opciones.size > 0) {
            // dibujamos las opciones visualmente que tiene el usuario sobre dicha ficha
            pintarOpciones(opciones)
            // llamamos al método indicando la casilla que se mueve y las opciones que tiene
            opcionesFichaSeleccionada(casilla, opciones)
        } else
        // como para esa casilla no hay opciones, volvemos a llamar a la función que determina
        // las casillas que son clicables
            actualizarClicables()
    }

    // cuando sabemos cual es la ficha que queremos mover
    private fun opcionesFichaSeleccionada(casillaActual: Casilla, opciones: ArrayList<Casilla>){
        // en la casilla que queriamos mover, dejamos un listener para que si volvemos a pulsar
        // sobre ella limpie las opicones y vuelva a dejar elegir todas las fichas del color
        val fichaCancelada = divPiezas.findViewWithTag<View>(getNumeroTag(casillaActual))
        fichaCancelada.setOnClickListener {
            limpiarOpciones()
            actualizarClicables()
        }
        // recorremos todos los posibles destinos para la ficha
        for (casillaDestino in opciones) {
            // la pasamos de la lógica a lo visual a través del tag y dejamos un listener sobre ella
            val fichaSeleccionada = divPiezas.findViewWithTag<View>(getNumeroTag(casillaDestino))
            fichaSeleccionada.setOnClickListener {
                // en caso de confirmar a donde queremos ir, hacemos el movimiento de fichas
                moverFicha(casillaActual, casillaDestino)
                // limpiamos las opciones previas (tanto visual como listeners)
                limpiarOpciones()
                // volvemos a llamar a la asginación de posibles fichas clicables
                actualizarClicables()
            }
        }
    }

    // método para mover las fichas, necesitamos el origen del movimiento y el destino
    private fun moverFicha(casillaOrigen: Casilla, casillaDestino: Casilla) {
        controlEnroque(casillaOrigen)
        // la recogemos así porque sabemos que siempre va a ver una ficha en origen
        val fichaOrigen = casillaOrigen.ficha!!

        // PARTE VISUAL
        // transformamos la casilla en la parte visual y dejamos el hueco vacío
        val casillaVacia = divPiezas.findViewWithTag<View>(getNumeroTag(casillaOrigen))
        casillaVacia.setBackgroundResource(R.color.transparente)
        // transformamos la casilla destino, sabiendo en que turno estamos y la ficha sabemos que
        // aspecto tenemos que darle en el tablero
        val casillaAOcupar = divPiezas.findViewWithTag<View>(getNumeroTag(casillaDestino))
        if (swTurnoBlanco) {
            when {
                fichaOrigen.isPeon() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancaspeon)
                fichaOrigen.isCaballo() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancascaballo)
                fichaOrigen.isTorre() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancastorre)
                fichaOrigen.isAlfil() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancasalfil)
                fichaOrigen.isReina() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancasreina)
                fichaOrigen.isRey() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.blancasrey)
            }
        } else {
            when {
                fichaOrigen.isPeon() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negraspeon)
                fichaOrigen.isCaballo() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negrascaballo)
                fichaOrigen.isTorre() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negrastorre)
                fichaOrigen.isAlfil() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negrasalfil)
                fichaOrigen.isReina() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negrasreina)
                fichaOrigen.isRey() ->
                    casillaAOcupar.setBackgroundResource(R.drawable.negrasrey)
            }
        }

        // tenemos que comprobar si lo que desea hacer el jugador es un enroque. usamos el estado de
        // la variable ya que lo actualizamos cuando indicamos que una de las opciones es el enroque
        if (estadoEnRoque == Constantes.ENROQUE_PUEDE) {
            val fila = casillaDestino.fila
            val columna = casillaDestino.columna
            // separamos por color
            if (fichaOrigen.isBlanca()) {
                // esta es la posición destino de un enroque corte blanco
                if (fila == Constantes.TABLERO_INICIO && columna == 1) {
                    // acutalizamos la variable para indicar que se ha realizado un enroque
                    estadoEnRoque = Constantes.ENROQUE_SI
                    // como sabemos el tipo de enroque que estamos haciendo, cogemos la posición de
                    // la torre izquierda y la dejamos en su nueva posición
                    moverFicha(
                        tablero[Constantes.TABLERO_INICIO][0],
                        tablero[Constantes.TABLERO_INICIO][2])
                }
                // esta es la posicón en el tablero de un enroque largo blanco
                else if (fila == Constantes.TABLERO_INICIO && columna == 6) {
                    estadoEnRoque = Constantes.ENROQUE_SI
                    moverFicha(
                        tablero[Constantes.TABLERO_INICIO][7],
                        tablero[Constantes.TABLERO_INICIO][5])
                }
            } else {
                // en caso de que el destino sea un enroque negro corto
                if (fila == Constantes.TABLERO_FIN && columna == 1) {
                    estadoEnRoque = Constantes.ENROQUE_SI
                    moverFicha(
                        tablero[Constantes.TABLERO_FIN][0], tablero[Constantes.TABLERO_FIN][2])
                }
                // enroque negro largo
                else if (fila == Constantes.TABLERO_FIN && columna == 6) {
                    estadoEnRoque = Constantes.ENROQUE_SI
                    moverFicha(
                        tablero[Constantes.TABLERO_FIN][7], tablero[Constantes.TABLERO_FIN][5])
                }
            }
        }

        // PARTE LÓGICA DE PROGRAMACIÓN
        // quitamos la ficha de la casilla origen
        casillaOrigen.ficha = null
        // miramos a ver si en la casilla de destino había una ficha
        val fichaEnDestino = casillaDestino.ficha
        // como no es seguro que haya, tenemos que ver si es null
        if (fichaEnDestino != null)
            // cuando hay ficha es que se la va a comer
            comerFicha(fichaEnDestino)
        // actualizamos la ficha en el destino y dejamos la que había en origen
        casillaDestino.ficha = fichaOrigen

        // esta comprobación es para ver si un peón ha llegado al extremo opuesto de su tablero
        if (fichaOrigen.isPeon())
            if (fichaOrigen.isBlanca() && casillaDestino.fila == Constantes.TABLERO_FIN)
                empezarCoronacion(casillaDestino, true)
            else if (!fichaOrigen.isBlanca() && casillaDestino.fila == Constantes.TABLERO_INICIO)
                empezarCoronacion(casillaDestino, false)

        // tenemos está comprobación porque cuando hacemos un enroque, hacemos una segunda llamada a
        // este método y haría una doble actualización de turno y lo dejaría igual.
        // cuando no ha habido enroque
        if (estadoEnRoque != Constantes.ENROQUE_SI)
            actualizarTurno()
        // cuando ha habido enroque, vovelos a dejar como si no lo hubiera y en la siguiente pasada
        // por la función ya entra por el if anterior para cambiar el turno
        else
            estadoEnRoque = Constantes.ENROQUE_NO
    }

    // determinamos que hay que hacer cuando se ha comido alguna ficha
    // (la ficha que recibimos como parámetros es la ficha que han comido)
    private fun comerFicha(ficha: Ficha) {
        // lo primero que hacemos es saber si la ficha es el rey, porque en ese caso el juego habría
        // finalizado
        if (ficha.isRey())
            irAFinJuego(ficha.color)
        else {
            // recogemos las cajas que usamos para mostrar las fichas perdidas
            var cajaPerdidas = binding.divBlancasPerdidas
            // creamos una nueva view que es la que usaremos para guardar la ficha comida
            val nuevaView = View(this)
            // separación por colores
            if (ficha.isBlanca()) {
                // seguín la tipología de la ficha, vamos a asignar el fondo a la caja creada para
                // el aspecto visual
                when {
                    ficha.isPeon() -> nuevaView.setBackgroundResource(R.drawable.blancaspeon)
                    ficha.isCaballo()-> nuevaView.setBackgroundResource(R.drawable.blancascaballo)
                    ficha.isTorre()-> nuevaView.setBackgroundResource(R.drawable.blancastorre)
                    ficha.isAlfil()-> nuevaView.setBackgroundResource(R.drawable.blancasalfil)
                    ficha.isReina()-> nuevaView.setBackgroundResource(R.drawable.blancasreina)
                }
            } else {
                cajaPerdidas = binding.divNegrasPerdidas
                when {
                    ficha.isPeon() -> nuevaView.setBackgroundResource(R.drawable.negraspeon)
                    ficha.isCaballo()-> nuevaView.setBackgroundResource(R.drawable.negrascaballo)
                    ficha.isTorre()-> nuevaView.setBackgroundResource(R.drawable.negrastorre)
                    ficha.isAlfil()-> nuevaView.setBackgroundResource(R.drawable.negrasalfil)
                    ficha.isReina()-> nuevaView.setBackgroundResource(R.drawable.negrasreina)
                }
            }

            // a la caja de fichas comidas añadimos la nueva (es sólo visual)
            cajaPerdidas.addView(nuevaView)
            nuevaView.layoutParams.width = 100
            nuevaView.layoutParams.height= 100
        }
    }

    // en esta función lo que hacemos es controlar cuando hay movimiento de torres o del rey para
    // actualizar el estado de los posibles enroques
    private fun controlEnroque(casilla: Casilla) {
        val ficha = casilla.ficha!!
        val fila = casilla.fila
        val columna = casilla.columna
        // separamos por colores
        if (swTurnoBlanco)
            // en caso de que ya se haya movido el rey, no hay que comprobar nada más
            if (!swBlancasReyMov)
                // miramos que al menos alguna de las torres siga sin haberse movido
                if (!swBlancasTorreIzqMov || !swBlancasReyMov)
                    // separamos el movimiento entre torre y rey
                    if (ficha.isTorre() ) {
                        // comprobación de la torre izquierda cuando aun no se ha movido
                        if (!swBlancasTorreIzqMov && fila == Constantes.TABLERO_INICIO &&
                            columna == Constantes.TABLERO_INICIO)
                            swBlancasTorreIzqMov = true
                        // comprobación de la torre de la derecha
                        if (!swBlancasTorreDerMov && fila == Constantes.TABLERO_INICIO &&
                            columna == Constantes.TABLERO_FIN)
                            swBlancasTorreDerMov = true
                        // por último, indicamos que si ya se han movido las dos torres, no podrá
                        // haber enroque por lo que actualizamos que el rey no se puede mover
                        // (lo hacemos por eficiencia en el código)
                        if (swBlancasTorreIzqMov && swBlancasTorreDerMov)
                            swBlancasReyMov = true
                    }
                    // en caso de que el rey se mueva, actualizamos su estado
                    else if (ficha.isRey())
                        swBlancasReyMov = true
        else {
            if (!swNegrasReyMov)
                if (!swNegrasTorreDerMov || !swNegrasTorreIzqMov)
                    if (ficha.isTorre() ) {
                        if (!swNegrasTorreIzqMov && fila == Constantes.TABLERO_FIN &&
                            columna == Constantes.TABLERO_INICIO)
                            swNegrasTorreIzqMov = true
                        if (!swNegrasTorreDerMov && fila == Constantes.TABLERO_FIN &&
                            columna == Constantes.TABLERO_FIN)
                            swNegrasTorreDerMov = true
                        if (swNegrasTorreIzqMov && swNegrasTorreDerMov)
                            swNegrasReyMov = true
                    } else if (ficha.isRey() && !swNegrasReyMov)
                        swNegrasReyMov = true
        }
    }

    // está función nos va a devolver como posibles destinos para el movimiento cuando se cumplan
    // los requerimientos necesarios para hacer un enroque
    private fun isPosibleEnroque(casilla: Casilla): ArrayList<Casilla>{
        // lo primero que hacemos es volver a dejar la variable a estado de que no hay enroque
        estadoEnRoque = Constantes.ENROQUE_NO
        val opciones = arrayListOf<Casilla>()
        val ficha = casilla.ficha!!
        // separamos las fichas entre blancas y negras porque las posiciones para el enroque son
        // difernetes
        if (ficha.isBlanca()) {
            // solo seguimos comprobando cuando sepamos que el rey no se ha movido
            if (!swBlancasReyMov) {
                // comprobamos que la torre de la izquierda no se ha movido
                if (!swBlancasTorreIzqMov )
                    // comprobamos que las fichas entre el rey y la torre están vacíos
                    if (isOpcionVacia(tablero[0][1]) && isOpcionVacia(tablero[0][2])) {
                        // en caso de que cumpla los requisitos, añadimos a mano la opción segun el
                        // lado del tablero y también indicamos que hay un posible enroque con el
                        // cambio en la variable
                        opciones.add(tablero[0][1])
                        estadoEnRoque = Constantes.ENROQUE_PUEDE
                    }
                // comprobamos que la torre de la derecha no se ha movido
                if (!swBlancasTorreDerMov)
                    if (isOpcionVacia(tablero[0][4]) && isOpcionVacia(tablero[0][5])
                        && isOpcionVacia(tablero[0][6])) {
                        opciones.add(tablero[0][6])
                        estadoEnRoque = Constantes.ENROQUE_PUEDE
                    }
            }
        } else {
            if (!swNegrasReyMov) {
                if (!swNegrasTorreIzqMov)
                    if (isOpcionVacia(tablero[7][1]) && isOpcionVacia(tablero[7][2])) {
                        opciones.add(tablero[7][1])
                        estadoEnRoque = Constantes.ENROQUE_PUEDE
                    }
                if (!swNegrasTorreDerMov)
                    if (isOpcionVacia(tablero[7][4]) && isOpcionVacia(tablero[7][5])
                        && isOpcionVacia(tablero[7][6])) {
                        opciones.add(tablero[7][6])
                        estadoEnRoque = Constantes.ENROQUE_PUEDE
                    }
            }
        }

        return opciones
    }

    // lógica cuando el peón llega al final del tablero y se tiene que cambiar la ficha
    private fun empezarCoronacion(casilla: Casilla, swBlanca: Boolean) {
        // instanciamos el dialog y decimos que no se cancelable para que tenga que elegir la nueva
        // ficha con la que lo va a sustituir
        val emergente = mostrarOpcionesCoronacion(casilla, swBlanca)
        emergente.setCancelable(false)
        emergente.show()
    }

    // sabemos con que ficha quiere sustituir y también la casilla donde se produce el cambio
    private fun coronacion(i: Int, casilla: Casilla, swBlanca: Boolean) {
        // creamos la ficha para poner la nueva
        var ficha : Ficha? = null
        // cogemos la parte visual de la casilla para saber que fondo le corresponde
        val casillaReemplazo = divPiezas.findViewWithTag<View>(getNumeroTag(casilla))

        // Separamos por colores
        if (swBlanca) {
            // usando la i (la obtenemos del orden de las opciones del dialog)
            // determinamos que ficha será la nueva y también la actualizamos en lo visual
            when (i) {
                0 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.blancastorre)
                    ficha = Ficha(Constantes.BLANCAS, Constantes.FICHA_TORRE, getMovimientosTorre(),
                        Constantes.LONGITUD_LIBRE)
                }
                1 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.blancascaballo)
                    ficha = Ficha(Constantes.BLANCAS, Constantes.FICHA_CABALLO, getMovimientoCaballo(),
                        Constantes.LONGITUD_CORTO)
                }
                2 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.blancasalfil)
                    ficha = Ficha(Constantes.BLANCAS, Constantes.FICHA_ALFIL, getMovimientoAlfil(),
                        Constantes.LONGITUD_LIBRE)
                }
                3 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.blancasreina)
                    ficha = Ficha(Constantes.BLANCAS, Constantes.FICHA_REINA, getMovimientosTotal(),
                        Constantes.LONGITUD_LIBRE)
                }
            }
        } else  {
            when (i) {
                0 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.negrastorre)
                    ficha = Ficha(Constantes.NEGRAS, Constantes.FICHA_TORRE, getMovimientosTorre(),
                        Constantes.LONGITUD_LIBRE)
                }
                1 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.negrascaballo)
                    ficha = Ficha(Constantes.NEGRAS, Constantes.FICHA_CABALLO, getMovimientoCaballo(),
                        Constantes.LONGITUD_CORTO)
                }
                2 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.negrasalfil)
                    ficha = Ficha(Constantes.NEGRAS, Constantes.FICHA_ALFIL, getMovimientoAlfil(),
                        Constantes.LONGITUD_LIBRE)
                }
                3 -> {
                    casillaReemplazo.setBackgroundResource(R.drawable.negrasreina)
                    ficha = Ficha(Constantes.NEGRAS, Constantes.FICHA_REINA, getMovimientosTotal(),
                        Constantes.LONGITUD_LIBRE)
                }
            }
        }
        // actualizamos la ficha que había en la casilla con la nueva
        casilla.ficha = ficha!!
        actualizarClicables()
    }

    // creamos un alert dialog en el que cargamos las diferentes opciones que tiene el jugador para
    // sustituir al peón
    private fun mostrarOpcionesCoronacion(casilla: Casilla, swBlanca: Boolean): AlertDialog.Builder {
        val opciones = arrayOf("Torre", "Caballo", "Alfil", "Reina")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Por que ficha quieres sustituir al peón?")
        builder.setItems(opciones,
            { dialog, i ->
                coronacion(i, casilla, swBlanca)
            })
        builder.create()
        return builder
    }

    // llamamos a este método para obtener las distintas opciones que vamos a tener en el plano
    // horizontal del tablero
    private fun getMovientosHorizontales(casilla: Casilla): ArrayList<Casilla> {
        val opciones = arrayListOf<Casilla>()
        var posibleCasilla : Casilla
        val fichaOrigen = casilla.ficha!!
        val swOrigenBlanca = fichaOrigen.isBlanca()
        val fila = casilla.fila
        val columna = casilla.columna
        // es una comprobación única a izquierdas y derechas (solo el rey)
        if (fichaOrigen.isDistanciaCorta()) {
            // comprobamos que no nos saldríamos por la izquierda
            if (columna-1 >= Constantes.TABLERO_INICIO){
                posibleCasilla = tablero[fila][columna-1]
                // si donde queremos movernos está vacío o es fucha contraria, se añade como opción
                if (isOpcionVacia(posibleCasilla) ||
                    isFichaContraria(posibleCasilla, swOrigenBlanca))
                    opciones.add(posibleCasilla)
            }
            // comprobamos que no nos saldríamos por la derecha
            if (columna+1 <= Constantes.TABLERO_FIN) {
                posibleCasilla = tablero[fila][columna + 1]
                if (isOpcionVacia(posibleCasilla) ||
                    isFichaContraria(posibleCasilla, swOrigenBlanca))
                    opciones.add(posibleCasilla)
            }
        }
        // comprobaciones desde la ficha en la que nos encontramos hasta el final del tablero
        else {
            // Posibilidades hacia la izquierda de la ficha seleccionada
            // comprobamos que no nos salgamos del tablero
            if (columna-1 >= Constantes.TABLERO_INICIO) {
                // recorremos desde la posición a la izquierda de la ficha que queremos mover hasta
                // el finl del tablero
                for (i in columna-1 downTo Constantes.TABLERO_INICIO ) {
                    posibleCasilla = tablero[fila][i]
                    // si la casilla esta vacía la añadimos como opción de movimiento
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    // si la casilla es contraria, la añadimos como opción porque puede comerla y
                    // luego terminamos el bucle porque ya se ha interrumpido
                    else if (isFichaContraria(posibleCasilla,swOrigenBlanca)) {
                        opciones.add(posibleCasilla)
                        break
                    }
                    // si el destino es del mismo color no podemos mover a esa casilla y salimos del
                    // bucle porque ya está interrumpido
                    if (isMismoColor(posibleCasilla, swOrigenBlanca))
                        break
                }
            }
            // Posibilidades hacia la derecha de la ficha
            // comprobamos que no nos salgamos del tablero
            if (columna+1 <= Constantes.TABLERO_FIN) {
                // recorremos desde la posición a la derecha de la ficha que queremos mover hasta
                // el finl del tablero
                for (i in columna+1 .. Constantes.TABLERO_FIN) {
                    posibleCasilla = tablero[fila][i]
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    if (isFichaContraria(posibleCasilla,swOrigenBlanca)) {
                        opciones.add(posibleCasilla)
                        break
                    }
                    if (isMismoColor(posibleCasilla, swOrigenBlanca))
                        break
                }
            }
        }

        return opciones
    }

    // con este método vamos a devolver todas las casillas a las que nuestra ficha podría moverse en
    // el eje vertical (fila)
    private fun getMovimientosVerticales(casilla: Casilla) : ArrayList<Casilla>{
        val opciones = arrayListOf<Casilla>()
        var posibleCasilla : Casilla
        // sabemos que la casilla siempre va a tener una ficha asignada
        val fichaOrigen = casilla.ficha!!
        val swOrigenBlanca = fichaOrigen.isBlanca()
        val columna = casilla.columna
        val fila = casilla.fila

        // comprobaciones para las fichas de desplazamiento de una única distancia
        if (fichaOrigen.isDistanciaCorta()) {
            // necesitamos separar a los peones ya que estos solo van en una dirección
            if (fichaOrigen.isPeon()) {
                // Hacemos la comprobación del color para saber que las blancas van hacia abajo
                if (swOrigenBlanca) {
                    // en caso de que esté vacía el destino, lo añadimos como opción de movimiento
                    posibleCasilla = tablero[fila+1][columna]
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    // el peón desde la ficha inicial siempre puede moverse dos hacia adelante
                    if (fila == 1)
                    // comprobamos que la ficha de delante en el recorrido no esté ocupada
                        if (isOpcionVacia(tablero[fila+1][columna])) {
                            posibleCasilla = tablero[fila+2][columna]
                            if (isOpcionVacia(posibleCasilla))
                                opciones.add(posibleCasilla)
                        }

                    // COMPROBACIONES PARA COMER
                    // comprobamos que no nos estemos saliendo del tablero
                    if (columna-1 >= Constantes.TABLERO_INICIO) {
                        posibleCasilla = tablero[fila+1][columna-1]
                        if (isFichaContraria(posibleCasilla, swOrigenBlanca))
                            opciones.add(posibleCasilla)
                    }
                    if (columna+1 <= Constantes.TABLERO_FIN) {
                        posibleCasilla = tablero[fila+1][columna+1]
                        if (isFichaContraria(posibleCasilla, swOrigenBlanca))
                            opciones.add(posibleCasilla)
                    }
                }
                // comprobaciones para los peones negros, van hacia arriba
                else {
                    posibleCasilla = tablero[fila-1][columna]
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    if (fila == 6) {
                        if (isOpcionVacia(tablero[fila-1][columna])) {
                            posibleCasilla = tablero[fila-2][columna]
                            if (isOpcionVacia(posibleCasilla))
                                opciones.add(posibleCasilla)
                        }
                    }

                    if (columna-1 >= Constantes.TABLERO_INICIO) {
                        posibleCasilla = tablero[fila-1][columna-1]
                        if (isFichaContraria(posibleCasilla, swOrigenBlanca))
                            opciones.add(posibleCasilla)
                    }
                    if (columna+1 <= Constantes.TABLERO_FIN) {
                        posibleCasilla = tablero[fila-1][columna+1]
                        if (isFichaContraria(posibleCasilla, swOrigenBlanca))
                            opciones.add(posibleCasilla)
                    }
                }
            }
            // como distancia corta solo pueden ser peones y rey, este es el movimiento del rey,
            // cuya diferencia es que este si puede moverse para arriba y para abajo
            else {
                // comprobamos que no nos salgamos del tablero por arriba
                if (fila-1 >= Constantes.TABLERO_INICIO) {
                    posibleCasilla = tablero[fila-1][columna]
                    // tanto si está vacía o está ocupada por ficha de otro color, el movimiento
                    // será posible
                    if (isOpcionVacia(posibleCasilla) ||
                        isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
                // comprobamos que no nos salgamos del tablero por abajo
                if (fila+1 <= Constantes.TABLERO_FIN) {
                    posibleCasilla = tablero[fila+1][columna]
                    if (isOpcionVacia(posibleCasilla) ||
                        isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
            }

        }
        // esta es la parte del resto de fichas con movimiento "total"
        else {
            // Con este if comprobamos que no nos salgamos del tablero por abajo
            if (fila+1 <= Constantes.TABLERO_FIN) {
                // Con este for recorremos "hacia abajo" el tablero desde la siguiente posición de
                // nuestra ficha hasta el final del tablero
                for (i in fila+1 .. Constantes.TABLERO_FIN) {
                    posibleCasilla = tablero[i][columna]
                    // si está vacía podemos movernos ahí
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    // si el destino es una ficha de nuestro color, no podremos ponernos en ese
                    // casilla y además tenemos que salir del bucle porque de ahi en adelante no
                    // podríamos llegar
                    else if (isMismoColor(posibleCasilla, swOrigenBlanca))
                        break
                    // si el destino es una ficha contraria, la añadimos como opción porque podemos
                    // comernosla y luego salimos del bucle por no poder avanzar más
                    else if (isFichaContraria(posibleCasilla,swOrigenBlanca)) {
                        opciones.add(posibleCasilla)
                        break
                    }

                }
            }

            // con este if comprbamos que no nos salgamos del tablero por arriba
            if (fila-1 >= Constantes.TABLERO_INICIO) {
                // Con este for recorremos "hacia arriba" el tablero desde la posición anterior de
                // nuestra ficha hasta el inicio del tablero
                for (i in fila-1 downTo Constantes.TABLERO_INICIO) {
                    posibleCasilla = tablero[i][columna]
                    if (isOpcionVacia(posibleCasilla))
                        opciones.add(posibleCasilla)
                    else if (isMismoColor(posibleCasilla, swOrigenBlanca))
                        break
                    else if (isFichaContraria(posibleCasilla,swOrigenBlanca)) {
                        opciones.add(posibleCasilla)
                        break
                    }
                }
            }
        }

        return opciones
    }

    // usamos este método para obtener las opciones de movimientos en los desplazamientos diagonales
    private fun getMovimientosDiagonales(casilla: Casilla) : ArrayList<Casilla> {
        val opciones = arrayListOf<Casilla>()
        var posibleCasilla : Casilla
        val fichaOrigen = casilla.ficha!!
        val swOrigenBlanca = fichaOrigen.isBlanca()
        val columna = casilla.columna
        val fila = casilla.fila
        // cuando entremos por aquí va a ser siempre un movimiento único (es sólo el rey)
        if (fichaOrigen.isDistanciaCorta()) {
            // comprueba que no se salga por la izquierda
            if (columna-1 >= Constantes.TABLERO_INICIO) {
                // comprueba que no se salga por la izquierada
                if (fila-1 >= Constantes.TABLERO_INICIO) {
                    posibleCasilla = tablero[fila-1][columna-1]
                    // si está vacío o se puede comer, lo añadimos como opción
                    if (isOpcionVacia(posibleCasilla) ||
                        isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
                // comprueba que no se salga por la derecha
                if (fila+1 <= Constantes.TABLERO_FIN) {
                    posibleCasilla = tablero[fila+1][columna-1]
                    if (isOpcionVacia(posibleCasilla) || isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
            }
            // comprueba que no se salga por la derecha
            if (columna+1 <= Constantes.TABLERO_FIN) {
                if (fila-1 >= Constantes.TABLERO_INICIO) {
                    posibleCasilla = tablero[fila-1][columna+1]
                    if (isOpcionVacia(posibleCasilla) || isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
                if (fila+1 <= Constantes.TABLERO_FIN) {
                    posibleCasilla = tablero[fila+1][columna+1]
                    if (isOpcionVacia(posibleCasilla) || isFichaContraria(posibleCasilla, swOrigenBlanca))
                        opciones.add(posibleCasilla)
                }
            }
        }
        // entra por el else en los movimientos totales
        else {
            // EXPLICACIÓN AUX1 y AUX2
            // lo que estamos haciendo es obtener la distancia que hay entre la ficha que queremos
            // mover y los laterales del tablero. Cambiamos el orden de la resta según queramos
            // obtener la cuenta hacia arriba (aux1) o hacia los lados (aux2). Posteriormente
            // usaremos la función getMasBajo para saber cual de las dos cifras es más pequeña para
            // que sea el límite de movimientos en diagonal que vamos a hacer

            // opciones arriba a la izquierda
            var aux1 = fila-Constantes.TABLERO_INICIO
            var aux2 = columna-Constantes.TABLERO_INICIO
            for (i in 1 .. getMasBajo(aux1, aux2)) {
                posibleCasilla = tablero[fila-i][columna-i]
                // si está vacía la podemos añadir
                if (isOpcionVacia(posibleCasilla))
                    opciones.add(posibleCasilla)
                // si es del mismo color no es una opción y tenemos que salir del bucle porque
                // interrumpe el paso
                else if (isMismoColor(posibleCasilla,swOrigenBlanca))
                    break
                // si es una ficha del otro color, la añadimos como opción pero salimos del bucle
                // porque el recorrido queda interrumpido
                else if (isFichaContraria(posibleCasilla, swOrigenBlanca)){
                    opciones.add(posibleCasilla)
                    break
                }
            }

            // opciones arriba a la derecha
            aux2 = Constantes.TABLERO_FIN - columna
            for (i in 1 .. getMasBajo(aux1, aux2)) {
                posibleCasilla = tablero[fila-i][columna+i]
                if (isOpcionVacia(posibleCasilla))
                    opciones.add(posibleCasilla)
                else if (isMismoColor(posibleCasilla,swOrigenBlanca))
                    break
                else if (isFichaContraria(posibleCasilla, swOrigenBlanca)){
                    opciones.add(posibleCasilla)
                    break
                }
            }

            // opciones abajo a la izquierda
            aux1 = Constantes.TABLERO_FIN - fila
            aux2 = columna - Constantes.TABLERO_INICIO
            for (i in 1 .. getMasBajo(aux1, aux2)) {
                posibleCasilla = tablero[fila+i][columna-i]
                if (isOpcionVacia(posibleCasilla))
                    opciones.add(posibleCasilla)
                else if (isMismoColor(posibleCasilla,swOrigenBlanca))
                    break
                else if (isFichaContraria(posibleCasilla, swOrigenBlanca)){
                    opciones.add(posibleCasilla)
                    break
                }
            }

            // opciones abajo a la derecha
            aux2 = Constantes.TABLERO_FIN - columna
            for (i in 1 .. getMasBajo(aux1, aux2)) {
                posibleCasilla = tablero[fila+i][columna+i]
                if (isOpcionVacia(posibleCasilla))
                    opciones.add(posibleCasilla)
                else if (isMismoColor(posibleCasilla,swOrigenBlanca))
                    break
                else if (isFichaContraria(posibleCasilla, swOrigenBlanca)){
                    opciones.add(posibleCasilla)
                    break
                }
            }
        }

        return opciones
    }

    // devolvemos las opciones a las que se podría mover el caballo seleccionado. Tenemos en cuenta
    // que el movimiento del caballo siempre es dos movimientos en horizontal (columna) y uno en
    // vertical (fila) o dos verticales y uno horizontal. En un primer momento seleccionamos todas
    // las opciones donde se podría mover el caballo sin salir del tablero, y luego filtramos si el
    // movimiento es posible o no
    private fun getMovimientoCaballo(casilla: Casilla): ArrayList<Casilla> {
        val opciones = arrayListOf<Casilla>()
        val posiblesCasillas = arrayListOf<Casilla>()
        val fichaOrigen = casilla.ficha!!
        val swOrigenBlanca = fichaOrigen.isBlanca()
        val fila = casilla.fila
        val columna = casilla.columna

        // compruebo que no me salga por arriba
        if (fila-1 >= Constantes.TABLERO_INICIO) {
            // compruebo que no me salga por la izquierda
            if (columna-2 >= Constantes.TABLERO_INICIO)
                posiblesCasillas.add(tablero[fila-1][columna-2])
            // compruebo que no me salga por la derecha
            if (columna+2 <= Constantes.TABLERO_FIN)
                posiblesCasillas.add(tablero[fila-1][columna+2])
        }
        // compruebo que no me salga por arriba
        if (fila-2 >= Constantes.TABLERO_INICIO){
            // compruebo que no me salga por la izquierda
            if (columna-1 >= Constantes.TABLERO_INICIO)
                posiblesCasillas.add(tablero[fila-2][columna-1])
            // compruebo que no me salga por la derecha
            if (columna+1 <= Constantes.TABLERO_FIN)
                posiblesCasillas.add(tablero[fila-2][columna+1])
        }
        // compruebo que no me salga por la abajo
        if (fila+1 <= Constantes.TABLERO_FIN) {
            // compruebo que no me salga por la izquierda
            if (columna-2 >= Constantes.TABLERO_INICIO)
                posiblesCasillas.add(tablero[fila+1][columna-2])
            // compruebo que no me salga por la derecha
            if (columna+2 <= Constantes.TABLERO_FIN)
                posiblesCasillas.add(tablero[fila+1][columna+2])
        }
        // compruebo que no me salga por la abajo
        if (fila+2 <= Constantes.TABLERO_FIN) {
            // compruebo que no me salga por la izquierda
            if (columna-1 >= Constantes.TABLERO_INICIO)
                posiblesCasillas.add(tablero[fila+2][columna-1])
            // compruebo que no me salga por la derecha
            if (columna+1 <= Constantes.TABLERO_FIN)
                posiblesCasillas.add(tablero[fila+2][columna+1])
        }

        // En caso de que esté vacía o se pueda comer, la marcamos como posible
        for (posibleCasilla in posiblesCasillas)
            if (isOpcionVacia(posibleCasilla) || isFichaContraria(posibleCasilla, swOrigenBlanca))
                opciones.add(posibleCasilla)

        return opciones
    }

    // comprobamos si la ficha donde queremos movermos tiene alguan ficha
    private fun isOpcionVacia (posibleCasilla: Casilla) : Boolean {
        /* EL CODIGO DE ABAJO LO PROPONE EL PROGRAMA, TENIA ESTO
        val colorDestino = getColorDestino(posibleCasilla)
        if (colorDestino==null)
            return true
        return false*/
        getColorDestino(posibleCasilla) ?: return true
        return false
    }

    // sabiendo la casilla a la que nos queremos mover y el color de nuestra ficha, comprobamos que
    // el destino es de diferente color
    private fun isFichaContraria(posibleCasilla: Casilla, swOrigenBlanca: Boolean) : Boolean {
        val colorDestino = getColorDestino(posibleCasilla)
        if ((swOrigenBlanca && colorDestino == Constantes.NEGRAS)
            || (!swOrigenBlanca && colorDestino == Constantes.BLANCAS))
            return true
        return false
    }

    // sabiendo la casilla a la que nos queremos mover y el color de nuestra ficha, comprobamos que
    // el destino tiene el mismo color que nuestra ficha
    private fun isMismoColor(posibleCasilla: Casilla, swOrigenBlanca: Boolean): Boolean {
        val colorDestino = getColorDestino(posibleCasilla)
        if ((swOrigenBlanca && colorDestino == Constantes.BLANCAS)
            || (!swOrigenBlanca && colorDestino == Constantes.NEGRAS))
            return true
        return false
    }

    // dando dos opciones, devolvemos el número más bajo
    private fun getMasBajo(op1: Int, op2: Int): Int {
        return if (op1<= op2)
            op1
        else
            op2
    }

    // recorremos las opciones donde se podría mover la ficha y transformamos la casilla en la parte
    // visual a través del tag
    private fun pintarOpciones(opciones: ArrayList<Casilla>) {
        for (casilla in opciones) {
            val caja = divPosibles.findViewWithTag<View>(getNumeroTag(casilla))
            caja.setBackgroundResource(R.drawable.opcion)
        }
    }

    private fun limpiarOpciones() {
        // eliminamos el efecto visual visual, para ello recorremos todas las casillas del tablero y
        // quitamos el fondo que pueda tener
        val filasPosibles = divPosibles.children
        filasPosibles.forEach {
            val linea = it as LinearLayout
            val casillas = linea.children
            casillas.forEach {casilla->
                casilla.setBackgroundResource(R.color.transparente)
            }
        }
        // eliminamos los sitios donde podemos hacer click (es la forma contraria a definir el
        // setOnClickListener. Lo recogemos del otro div porque están en "capas" diferentes
        val filasPiezas = divPiezas.children
        filasPiezas.forEach {
            val linea = it as LinearLayout
            val casillas = linea.children
            casillas.forEach { casilla->
                casilla.isClickable = false
            }
        }
    }

    // devolvemos el color de la casilla destino, en caso de que esté vacía devolvemos null
    private fun getColorDestino(casilla: Casilla) : Int? {
        val ficha = casilla.ficha
        return if (ficha != null) {
            if (ficha.isBlanca())
                Constantes.BLANCAS
            else
                Constantes.NEGRAS
        } else
            null
    }

    // sabiendo la casilla que queremos tratar, lo convertimos en el código del tag para poder
    // usarlo desde la parte visual
    private fun getNumeroTag(casilla: Casilla): String {
        return "f${casilla.fila}${casilla.columna}"
    }

    // cambiamos el turno en la partida
    private fun actualizarTurno () {
        /* OJO ESTO, lo tenía puesto con ifs y me recomienda ponerlo así. Es lo contrario
        de lo que tenía en este momento
        if (swTurnoBlanco)
            swTurnoBlanco = false
        else
            swTurnoBlanco = true
        */
        swTurnoBlanco = !swTurnoBlanco

        // parte visual, lo que estaba blanco a negro y viceversa
        val caja = binding.tvTurno
        val banderinIzq = binding.vBanderinIzq
        val banderinDer = binding.vBanderiDer
        if (swTurnoBlanco ){
            caja.text = resources.getString(R.string.turnoBlancas)
            caja.setTextColor(resources.getColor(R.color.white, null))
            banderinIzq.setBackgroundResource(R.color.white)
            banderinDer.setBackgroundResource(R.color.white)
        }
        else {
            caja.text = resources.getString(R.string.turnoNegras)
            caja.setTextColor(resources.getColor(R.color.black, null))
            banderinIzq.setBackgroundResource(R.color.black)
            banderinDer.setBackgroundResource(R.color.black)
        }
    }

    private fun irAFinJuego (color: Int) {
        val intentResultado = Intent(this, ActResultado::class.java)
        intent.putExtra(Constantes.VICTORIA, color)
        startActivity(intentResultado)
    }

}