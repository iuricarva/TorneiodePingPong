package curso.android.com.processa.torneiodepingpong

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL



class wsAcessoPartida: AsyncTask<String, Void, String>()
{
    lateinit var finalString: String
    override fun doInBackground(vararg params: String?): String {
        val verbo = params[0]
        val servidor = params[1]
        var id: String?
        var tecnico1: String?
        var tecnico2: String?
        var placar: String?

        try{
            id = params[2]
        }catch (ex: Exception)
        {
            id = ""
        }

        try{
            tecnico1 = params[3]
            tecnico2 = params[4]
        }catch (ex: Exception)
        {
            tecnico1 = ""
            tecnico2 = ""
        }

        try{
            placar = params[5]
        }catch (ex: Exception)
        {
            placar = ""
        }

        when(verbo){
            "get" -> return getPartida(servidor)

            "post" -> return postPartida(servidor, id, tecnico1, tecnico2, placar)

            //"put" -> return putTecnico(servidor, id, nome)

            "delete" -> return deletePartida(servidor, id)

            else -> "false"
        }

        return "false"
    }

    fun getPartida(servidor: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Partidas?format=JSON")
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestMethod("GET")
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.connectTimeout = 10000
        urlConnection.connect()
        val inputStream = urlConnection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        var linha: String?
        val buffer = StringBuffer()

        linha = reader.readLine()

        while (linha != null) {
            buffer.append(linha)
            linha = reader.readLine()
        }


        if (buffer.isEmpty()) {
            return "false"
        }
        if (urlConnection != null) {
            urlConnection!!.disconnect()
        }
        if (reader != null) {
            try {
                reader.close()
            } catch (e: IOException) {
                Log.e("Erro", "Erro fechando o stream", e)
            }

        }
        return  buffer.toString()
    }

    fun postPartida(servidor: String?, id: String?, idTecnico1: String?, idTecnico2: String?, idPlacar: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Partidas?format=JSON")

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "POST"
        urlConnection.connectTimeout = 10000

        try {
            val partida = PartidasActivity.partidaWS(id!!, idTecnico1!!, idTecnico2!!, idPlacar!!, "983")
            val novoPartida = Gson().toJson(partida)

            urlConnection.doOutput = true
            urlConnection.outputStream.write(novoPartida.toByteArray())

            if (urlConnection.responseCode == 200) {
                val responseBody = urlConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                val reader = BufferedReader(responseBodyReader)
                val buffer = StringBuffer()
                var line = reader.readLine()

                while (line!= null) {
                    buffer.append(line)
                    line = reader.readLine()
                }


                finalString = buffer.toString()
            } else {
                finalString = urlConnection.responseCode.toString() + ""
            }

            return finalString.contains("true").toString()

        }catch (e: MalformedURLException) {
            return "Erro1: " + e.message
        } catch (e: IOException) {
            return "Erro2: " + e.message
        }

        return "false"
    }

    /*fun putTecnico(servidor: String?, id: String?, nome: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Tecnicos/$id?format=JSON")

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "PUT"
        urlConnection.connectTimeout = 10000

        try {
            val tec = TecnicosActivity.Tecnico(id!!, nome!!)
            val novoTecnico = Gson().toJson(tec)

            urlConnection.doOutput = true
            urlConnection.outputStream.write(novoTecnico.toByteArray())

            if (urlConnection.responseCode == 200) {
                val responseBody = urlConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                val reader = BufferedReader(responseBodyReader)
                val buffer = StringBuffer()
                var line = reader.readLine()

                while (line!= null) {
                    buffer.append(line)
                    line = reader.readLine()
                }


                finalString = buffer.toString()
            } else {
                finalString = urlConnection.responseCode.toString() + ""
            }

            return finalString.contains("true").toString()

        }catch (e: MalformedURLException) {
            return "Erro1: " + e.message
        } catch (e: IOException) {
            return "Erro2: " + e.message
        }

        return "false"
    }*/

    fun deletePartida(servidor: String?, id: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Partidas/$id")

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "DELETE"
        urlConnection.connectTimeout = 10000

        try {

            if (urlConnection.responseCode == 200) {
                val responseBody = urlConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                val reader = BufferedReader(responseBodyReader)
                val buffer = StringBuffer()
                var line = reader.readLine()

                while (line!= null) {
                    buffer.append(line)
                    line = reader.readLine()
                }


                finalString = buffer.toString()
            } else {
                finalString = urlConnection.responseCode.toString() + ""
            }

            return finalString.contains("true").toString()

        }catch (e: MalformedURLException) {
            return "Erro1: " + e.message
        } catch (e: IOException) {
            return "Erro2: " + e.message
        }

        return "false"
    }

}