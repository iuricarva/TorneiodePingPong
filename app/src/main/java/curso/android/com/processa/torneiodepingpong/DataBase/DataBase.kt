package curso.android.com.processa.torneiodepingpong.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import curso.android.com.processa.torneiodepingpong.PartidasActivity
import curso.android.com.processa.torneiodepingpong.TecnicosActivity

const val dataBaseName = "TorneioDePingPong"
const val dataBaseVersion = 1




class DataBasePingPing (context: Context): SQLiteOpenHelper(context, dataBaseName, null, dataBaseVersion) {

    val PARTIDA = "partida"
    val PARTIDA_ID = "idPartida"
    val PARTIDA_PLACAR_ID = "idPlacar"
    val PARTIDA_TECNICO_1_ID = "idTecnico1"
    val PARTIDA_TECNICO_2_ID = "idTecnico2"
    val PARTIDA_INSERIDA = "inserida"
    val PARTIDA_DELETADA = "deletada"

    val TECNICO = "tecnico"
    val TECNICO_ID = "idTecnico"
    val TECNICO_NOME = "nomeTecnico"

    val PLACAR = "placar"
    val PLACAR_ID = "idPlacar"
    val PONTUACAO_TECNICO_1 = "pontuacaoTecnico1"
    val PONTUACAO_TECNICO_2 = "pontuacaoTecnico2"


    private val sqlCreatePartidas = "CREATE TABLE $PARTIDA " +
            "($PARTIDA_ID integer primary key autoincrement," +
            " $PARTIDA_PLACAR_ID integer not null," +
            " $PARTIDA_TECNICO_1_ID integer not null," +
            " $PARTIDA_TECNICO_2_ID  integer not null," +
            " $PARTIDA_INSERIDA integer not null ," +
            " $PARTIDA_DELETADA integer not null);"

    private val sqlCreateTecnicos = "CREATE TABLE $TECNICO " +
            "($TECNICO_ID integer primary key autoincrement," +
            " $TECNICO_NOME text not null );"

    private val sqlCreatePlacares = "CREATE TABLE $PLACAR " +
            "($PLACAR_ID integer primary key autoincrement," +
            " $PONTUACAO_TECNICO_1 integer not null," +
            " $PONTUACAO_TECNICO_2 integer not null );"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(sqlCreatePartidas)
        db?.execSQL(sqlCreateTecnicos)
        db?.execSQL(sqlCreatePlacares)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $PARTIDA")
        db?.execSQL("DROP TABLE IF EXISTS $TECNICO")
        db?.execSQL("DROP TABLE IF EXISTS $PLACAR")
        onCreate(db)
    }

    fun atualizaBanco(){
        onUpgrade(this.writableDatabase, 1, 1)
    }
    fun insertPartida(partida: PartidasActivity.partidaWS): Boolean {
        val db = this.writableDatabase
        return try {
            val insertPartida = "INSERT INTO ${this.PARTIDA} " +
                    "(${this.PARTIDA_ID} ," +
                    "${this.PARTIDA_PLACAR_ID} ," +
                    "${this.PARTIDA_TECNICO_1_ID} ," +
                    "${this.PARTIDA_TECNICO_2_ID}," +
                    "${this.PARTIDA_INSERIDA}," +
                    "${this.PARTIDA_DELETADA}) " +
                    "VALUES (${partida.idPartida}," +
                    "${partida.idPlacar}," +
                    "${partida.idTecnico1}," +
                    "${partida.idTecnico2}," +
                    "0," +
                    "0)"

            db.execSQL(insertPartida)
            db.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun insertPartidaSemId(partida: PartidasActivity.partidaWS): Boolean {
        val db = this.writableDatabase
        return try {
            val insertPartida = "INSERT INTO ${this.PARTIDA} " +
                    "(${this.PARTIDA_PLACAR_ID} ," +
                    "${this.PARTIDA_TECNICO_1_ID} ," +
                    "${this.PARTIDA_TECNICO_2_ID}, " +
                    "${this.PARTIDA_INSERIDA}," +
                    "${this.PARTIDA_DELETADA}) " +
                    "VALUES (${partida.idPlacar}," +
                    "${partida.idTecnico1}," +
                    "${partida.idTecnico2}," +
                    "1," +
                    "0)"

            db.execSQL(insertPartida)
            db.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun insertTecnico(tecnico: TecnicosActivity.Tecnico): Boolean {
       val db = this.writableDatabase
       return try {
           val insertTecnico = "INSERT INTO ${this.TECNICO} " +
                   "(${this.TECNICO_ID}, " +
                   "${this.TECNICO_NOME}) " +
                   "VALUES (${tecnico.idTecnico}," +
                   "'${tecnico.nome}')"

           db.execSQL(insertTecnico)
           db.close()
           true
       } catch (e: Exception) {
           false
       }
   }

    fun insertPlacar(placar: PartidasActivity.Placar): Boolean {
        val db = this.writableDatabase
        return try {
            val insertPlacar = "INSERT INTO ${this.PLACAR} " +
                    "(${this.PLACAR_ID}, " +
                    "${this.PONTUACAO_TECNICO_1}," +
                    "${this.PONTUACAO_TECNICO_2}) " +
                    "VALUES (${placar.idPlacar}," +
                    "${placar.pontuacaoTecnico1}," +
                    "${placar.pontuacaoTecnico2})"

            db.execSQL(insertPlacar)
            db.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun insertPlacarGetId(ptsTecnico1: String, ptsTecnico2: String): Int{

        val db = this.writableDatabase
        val values = ContentValues()
        var resultado: Long = 0

        try {
            values.put(this.PONTUACAO_TECNICO_1, ptsTecnico1)
            values.put(this.PONTUACAO_TECNICO_2, ptsTecnico2)

            // inserir no banco
            resultado = db.insert(this.PLACAR, null, values)

        } catch (e: SQLiteException) {
            e.printStackTrace()
        } finally {
            db.close()
            return resultado.toInt()
        }
    }

    fun getPartida(id: String?): PartidasActivity.Partida {
        val retorno: PartidasActivity.Partida

        val db = this.writableDatabase
        val get = "SELECT * FROM ${this.PARTIDA} WHERE ${this.PARTIDA_ID} = $id"
        val cursor: Cursor = db.rawQuery(get, null)
        lateinit var partidaBanco: PartidasActivity.partidaWS


        while (cursor.moveToNext()) {
            partidaBanco = PartidasActivity.partidaWS(
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_1_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_2_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_PLACAR_ID)),
                "983"
            )
        }
        cursor.close()
        db.close()

        retorno = PartidasActivity.Partida(partidaBanco!!.idPartida, getTecnico(partidaBanco.idTecnico1.toInt()), getTecnico(partidaBanco.idTecnico2.toInt()), getPlacar(partidaBanco!!.idPlacar.toInt()))

        return retorno
    }

    /*fun getAllPartidas(): List<PartidasActivity.Partida> {
        val listaRetorno = arrayListOf<PartidasActivity.Partida>()
        val listaPartida = arrayListOf<PartidasActivity.partidaWS>()
        val db = this.writableDatabase
        val getAll = "SELECT * FROM ${this.PARTIDA} "
        val cursor: Cursor = db.rawQuery(getAll, null)


        while (cursor.moveToNext()) {
            val partida = PartidasActivity.partidaWS(
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_1_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_2_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_PLACAR_ID)),
                "983"
            )

            listaPartida.add(partida)
        }
        cursor.close()
        db.close()

        for(obj in listaPartida){

            var novaPartida = PartidasActivity.Partida(obj.idPartida, getTecnico(obj.idTecnico1.toInt()), getTecnico(obj.idTecnico2.toInt()), getPlacar(obj.idPlacar.toInt()))
            listaRetorno.add(novaPartida)
        }

        return listaRetorno
    }*/

    fun getAllPartidasAtivas(): List<PartidasActivity.Partida> {
       val listaRetorno = arrayListOf<PartidasActivity.Partida>()
       val listaPartida = arrayListOf<PartidasActivity.partidaWS>()
       val db = this.writableDatabase
       val getAll = "SELECT * FROM ${this.PARTIDA} WHERE ${this.PARTIDA_DELETADA} = 0 "
       val cursor: Cursor = db.rawQuery(getAll, null)


       while (cursor.moveToNext()) {
           val partida = PartidasActivity.partidaWS(
               cursor.getString(cursor.getColumnIndex(this.PARTIDA_ID)),
               cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_1_ID)),
               cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_2_ID)),
               cursor.getString(cursor.getColumnIndex(this.PARTIDA_PLACAR_ID)),
               "983"
           )

           listaPartida.add(partida)
       }
       cursor.close()
       db.close()

       for(obj in listaPartida){

           var novoPlacar = getPlacar(obj.idPlacar.toInt())
           if(novoPlacar == null)
               novoPlacar = PartidasActivity.Placar(obj.idPlacar, 0, 0)

           var novaPartida = PartidasActivity.Partida(obj.idPartida, getTecnico(obj.idTecnico1.toInt()), getTecnico(obj.idTecnico2.toInt()), novoPlacar)
           listaRetorno.add(novaPartida)
       }

       return listaRetorno
   }

    fun getAllPartidasInseridas(): List<PartidasActivity.Partida> {
        val listaRetorno = arrayListOf<PartidasActivity.Partida>()
        val listaPartida = arrayListOf<PartidasActivity.partidaWS>()
        val db = this.writableDatabase
        val getAll = "SELECT * FROM ${this.PARTIDA} WHERE ${this.PARTIDA_INSERIDA} = 1"
        val cursor: Cursor = db.rawQuery(getAll, null)


        while (cursor.moveToNext()) {
            val partida = PartidasActivity.partidaWS(
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_1_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_2_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_PLACAR_ID)),
                "983"
            )

            listaPartida.add(partida)
        }
        cursor.close()
        db.close()

        for(obj in listaPartida){

            var novaPartida = PartidasActivity.Partida(obj.idPartida, getTecnico(obj.idTecnico1.toInt()), getTecnico(obj.idTecnico2.toInt()), getPlacar(obj.idPlacar.toInt()))
            listaRetorno.add(novaPartida)
        }

        return listaRetorno
    }

    fun getAllPartidasDeletadas(): List<PartidasActivity.Partida> {
        val listaRetorno = arrayListOf<PartidasActivity.Partida>()
        val listaPartida = arrayListOf<PartidasActivity.partidaWS>()
        val db = this.writableDatabase
        val getAll = "SELECT * FROM ${this.PARTIDA} WHERE ${this.PARTIDA_DELETADA} = 1"
        val cursor: Cursor = db.rawQuery(getAll, null)


        while (cursor.moveToNext()) {
            val partida = PartidasActivity.partidaWS(
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_1_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_TECNICO_2_ID)),
                cursor.getString(cursor.getColumnIndex(this.PARTIDA_PLACAR_ID)),
                "983"
            )

            listaPartida.add(partida)
        }
        cursor.close()
        db.close()

        for(obj in listaPartida){

            var novaPartida = PartidasActivity.Partida(obj.idPartida, getTecnico(obj.idTecnico1.toInt()), getTecnico(obj.idTecnico2.toInt()), getPlacar(obj.idPlacar.toInt()))
            listaRetorno.add(novaPartida)
        }

        return listaRetorno
    }

    fun deletePartidaLogico(Id : Int):Boolean{
        val db = this.writableDatabase

        return try {
           /* val deletePlacar = "DELETE FROM ${this.PLACAR} WHERE ${this.PLACAR_ID} = (SELECT ${this.PARTIDA_PLACAR_ID} " +
                    "FROM ${this.PARTIDA} WHERE ${this.PARTIDA_ID} = $Id)"*/

            val deletePartida = "UPDATE ${this.PARTIDA} SET ${this.PARTIDA_DELETADA} = 1 WHERE ${this.PARTIDA_ID} = $Id"

            //db.execSQL(deletePlacar)
            db.execSQL(deletePartida)
            db.close()
            true
        } catch (e: Exception) {
            false
        }

    }

    fun getPlacar(id: Int): PartidasActivity.Placar {
        lateinit var placar: PartidasActivity.Placar
        val db = this.writableDatabase
        val get = "SELECT * FROM ${this.PLACAR} WHERE ${this.PLACAR_ID} = $id "

        val cursor: Cursor = db.rawQuery(get, null)

        if(cursor.moveToFirst()) {
            placar = PartidasActivity.Placar(
                cursor.getString(cursor.getColumnIndex(this.PLACAR_ID)),
                cursor.getInt(cursor.getColumnIndex(this.PONTUACAO_TECNICO_1)),
                cursor.getInt(cursor.getColumnIndex(this.PONTUACAO_TECNICO_2))
            )
        }
        cursor.close()
        return placar
    }

    fun getTecnico(id: Int): TecnicosActivity.Tecnico {
        lateinit var tecnico: TecnicosActivity.Tecnico
        val db = this.writableDatabase
        val get = "SELECT * FROM ${this.TECNICO} WHERE ${this.TECNICO_ID} = $id"

       val cursor: Cursor = db.rawQuery(get, null)

        if(cursor.moveToFirst()) {
            tecnico = TecnicosActivity.Tecnico(
                cursor.getString(cursor.getColumnIndex(this.TECNICO_ID)),
                cursor.getString(cursor.getColumnIndex(this.TECNICO_NOME))
            )
        }

        cursor.close()
        db.close()
        return tecnico
    }


    fun getTecnicos(): List<TecnicosActivity.Tecnico> {
        val listaTecnicos = arrayListOf<TecnicosActivity.Tecnico>()
        val db = this.writableDatabase
            val getAll = "SELECT * FROM ${this.TECNICO} "
        val cursor: Cursor = db.rawQuery(getAll, null)


        while (cursor.moveToNext()) {
            val tecnico = TecnicosActivity.Tecnico(
                cursor.getString(cursor.getColumnIndex(this.TECNICO_ID)),
                cursor.getString(cursor.getColumnIndex(this.TECNICO_NOME))            )

            listaTecnicos.add(tecnico)
        }

        return listaTecnicos
    }

}