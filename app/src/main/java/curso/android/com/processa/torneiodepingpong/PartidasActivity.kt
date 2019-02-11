package curso.android.com.processa.torneiodepingpong

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import curso.android.com.processa.torneiodepingpong.DataBase.DataBasePingPing
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.Serializable
import java.lang.Exception
import java.nio.file.Files.exists



class PartidasActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var listView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<partidaAdapter.ItemViewHolder>
    val listItens = arrayListOf<Partida>()
    lateinit var botaoAddPartida :FloatingActionButton
    lateinit var dbFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partidas)

        dbFile = this.getDatabasePath("TorneioDePingPong")
        if (dbFile.exists()) {
            atualizaBanco()
        } else {
            criaBanco()
        }


        carregaListView()

        botaoAddPartida  = findViewById(R.id.btnAddPartida) as FloatingActionButton
        botaoAddPartida .setOnClickListener{ inserirPartida() }

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()
        carregaListView()
    }

    fun onCarregaListaPartidas() {
        listItens.clear()

        val objectList = DataBasePingPing(this).getAllPartidasAtivas()
        for(obj in objectList){
            listItens.add(obj)
        }

        adapter.notifyDataSetChanged()
    }

    fun imprimirToast(item: PartidasActivity.Partida)
    {
        val intent = Intent(this, InsertPartidaActivity::class.java)
        intent.putExtra("idPartida",item.idPartida)
        startActivity(intent)
    }

    fun inserirPartida()
    {
        startActivity( Intent(this, InsertPartidaActivity::class.java))
    }

    fun carregaListView() {
        val layout = LinearLayoutManager(this)

        listView = findViewById(R.id.rvPartida)
        listView.layoutManager = layout

        adapter = partidaAdapter(listItens, this, { item: PartidasActivity.Partida -> imprimirToast(item)})
        //adapter = partidaAdapter(listItens, this)

        listView.adapter = adapter

        onCarregaListaPartidas()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_tecnicos -> {
                startActivity(Intent(this, TecnicosActivity::class.java))
                finish()
            }

            R.id.nav_partidas -> {
                startActivity(Intent(this, PartidasActivity::class.java))
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun criaBanco() {
        var listaTecnicos: String?
        var listaPlacares: String?
        var listaPartidas: String?

        listaTecnicos = wsAcessoTecnico().execute("get", "172.27.1.46:90").get()
        listaPlacares = wsAcessoPlacar().execute("get", "172.27.1.46:90").get()
        listaPartidas = wsAcessoPartida().execute("get", "172.27.1.46:90").get()

        val gson1 = Gson()
        val gson2 = Gson()
        val gson3 = Gson()

        try {
            val list1 = gson1.fromJson(listaPlacares, Array<Placar>::class.java).asList()

            for (obj in list1) {
                DataBasePingPing(this).insertPlacar(obj)
            }


            val list2 = gson2.fromJson(listaTecnicos, Array<TecnicosActivity.Tecnico>::class.java).asList()

            for (obj in list2) {
                DataBasePingPing(this).insertTecnico(obj)
            }

            val list3 = gson3.fromJson(listaPartidas, Array<partidaWS>::class.java).asList()

            for (obj in list3) {
                DataBasePingPing(this).insertPartida(obj)
            }


        } catch (ex: Exception) {
            Toast.makeText(this, "Erro ao criar banco de dados", Toast.LENGTH_LONG).show()
            finish()
        }


    }

    fun atualizaBanco() {
        var listaTecnicos: String?
        var listaPlacares: String?
        var listaPartidas: String?

        listaTecnicos = wsAcessoTecnico().execute("get", "172.27.1.46:90").get()
        listaPlacares = wsAcessoPlacar().execute("get", "172.27.1.46:90").get()
        listaPartidas = wsAcessoPartida().execute("get", "172.27.1.46:90").get()

        val gson1 = Gson()
        val gson2 = Gson()
        val gson3 = Gson()

        try {

            DataBasePingPing(this).atualizaBanco()

            val list1 = gson1.fromJson(listaPlacares, Array<Placar>::class.java).asList()

            for (obj in list1) {
                DataBasePingPing(this).insertPlacar(obj)
            }


            val list2 = gson2.fromJson(listaTecnicos, Array<TecnicosActivity.Tecnico>::class.java).asList()

            for (obj in list2) {
                DataBasePingPing(this).insertTecnico(obj)
            }

            val list3 = gson3.fromJson(listaPartidas, Array<partidaWS>::class.java).asList()

            for (obj in list3) {
                DataBasePingPing(this).insertPartida(obj)
            }


        } catch (ex: Exception) {
            Toast.makeText(this, "Erro ao criar banco de dados", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        var lista = DataBasePingPing(this).getAllPartidasInseridas()

        var listaDeletada = DataBasePingPing(this).getAllPartidasDeletadas()

        for(obj in lista){
            var idPlacar = wsAcessoPlacar().execute("post", "172.27.1.46:90", obj.placar.idPlacar, obj.placar.pontuacaoTecnico1.toString(), obj.placar.pontuacaoTecnico2.toString()).get()
            idPlacar = idPlacar.replace("<int xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "")
            idPlacar = idPlacar.replace("</int>", "")

            var intId = idPlacar.toInt()

            if(intId > 0) {
                wsAcessoPartida().execute(
                    "post",
                    "172.27.1.46:90",
                    obj.idPartida,
                    obj.tecnico1.idTecnico,
                    obj.tecnico2.idTecnico,
                    obj.placar.idPlacar
                ).get()
            }else{
                Toast.makeText(this, "Erro ao sincronizar partidas.", Toast.LENGTH_LONG).show()
            }
        }

        for(delete in listaDeletada){
            var retorno: String?

            retorno = wsAcessoPlacar().execute("delete", "172.27.1.46:90", delete.placar.idPlacar).get()

            if (retorno.equals("false")) {
                break
            }

            retorno = wsAcessoPartida().execute("delete", "172.27.1.46:90", delete.idPartida).get()

            if (retorno.equals("false")) {
                break
            }
            Toast.makeText(this, "Erro ao sincronizar partidas.", Toast.LENGTH_LONG).show()
        }

        Toast.makeText(this, "Partidas sincronizadas com sucesso", Toast.LENGTH_LONG).show()
    }

    data class Partida(
        var idPartida: String,
        var tecnico1: TecnicosActivity.Tecnico,
        var tecnico2: TecnicosActivity.Tecnico,
        var placar: Placar
    ) : Serializable

    data class Placar(var idPlacar: String, var pontuacaoTecnico1: Int, var pontuacaoTecnico2: Int) : Serializable

    class partidaWS(
        var idPartida: String,
        var idTecnico1: String,
        var idTecnico2: String,
        var idPlacar: String,
        var idTorneio: String
    ) : Serializable
}
