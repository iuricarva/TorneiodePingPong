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

class wsAcessoTecnico: AsyncTask<String, Void, String>()
{
    lateinit var finalString: String
    override fun doInBackground(vararg params: String?): String {
        val verbo = params[0]
        val servidor = params[1]
        var id: String?
        var nome: String?

        try{
            id = params[2]
        }catch (ex: Exception)
        {
            id = ""
        }

        try{
            nome = params[3]
        }catch (ex: Exception)
        {
            nome = ""
        }

        when(verbo){
            "get" -> return getTecnico(servidor)

            "post" -> return postTecnico(servidor, id, nome)

            "put" -> return putTecnico(servidor, id, nome)

            "delete" -> return deleteTecnico(servidor, id)

            else -> "false"
        }

        return "false"
    }

    fun getTecnico(servidor: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Tecnicos?format=JSON")
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

    fun postTecnico(servidor: String?, id: String?, nome: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Tecnicos?format=JSON")

        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "POST"
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
    }

    fun putTecnico(servidor: String?, id: String?, nome: String?): String{

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
    }

    fun deleteTecnico(servidor: String?, id: String?): String{

        val destino: String

        if (servidor == null) {
            destino = "localhost"
        }else
            destino = servidor

        val url = URL("http://$destino/TorneioPingPong/Api/Tecnicos/$id")

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