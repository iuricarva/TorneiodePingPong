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

class wsAcessoPlacar: AsyncTask<String, Void, String>()
{
    lateinit var finalString: String

    override fun doInBackground(vararg params: String?): String {
        val verbo = params[0]
        val servidor = params[1]
        var id: String?
        var ptsTecnico1: String?
        var ptsTecnico2: String?
        /*var nome: String?*/

        try{
            id = params[2]
        }catch (ex: Exception){
            id = ""
        }

        try{
            ptsTecnico1 = params[3]
            ptsTecnico2 = params[4]
        }catch (ex: Exception){
            ptsTecnico1 = ""
            ptsTecnico2 = ""
        }

        when(verbo){
            "get" -> return getPlacar(servidor)

            "post" -> return postPlacar(servidor, id, ptsTecnico1, ptsTecnico2)

            //"put" -> return putTecnico(servidor, id, nome)

            "delete" -> return deletePlacar(servidor, id)

            else -> "false"
        }

        return "false"
    }

    fun getPlacar(servidor: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Placares?format=JSON")
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

    fun postPlacar(servidor: String?,id: String?, pontosTecnico1: String?, pontosTecnico2: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Placar?format=JSON")

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "POST"
        urlConnection.connectTimeout = 10000

        try {
            val placar = PartidasActivity.Placar(id!!, pontosTecnico1!!.toInt(), pontosTecnico2!!.toInt())
            val novoPlacar = Gson().toJson(placar)

            urlConnection.doOutput = true
            urlConnection.outputStream.write(novoPlacar.toByteArray())

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

            return finalString

        }catch (e: MalformedURLException) {
            return "-1"
        } catch (e: IOException) {
            return "-1"
        }

        return "-1"
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

    fun deletePlacar(servidor: String?, id: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Placares/$id")

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