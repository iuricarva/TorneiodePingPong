package curso.android.com.processa.torneiodepingpong

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import javax.security.auth.callback.Callback
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.Serializable


class TecnicosActivity : AppCompatActivity(), Callback , NavigationView.OnNavigationItemSelectedListener  {

    lateinit var listView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<tecnicoAdapter.ItemViewHolder>
    val listItens = arrayListOf<Tecnico>()
    lateinit var botaoAdd :FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tecnicos)

        carregaListView()

        botaoAdd = findViewById(R.id.btnAddTecnico) as FloatingActionButton
        botaoAdd.setOnClickListener{ abreEditTecnicos() }

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()
        carregaListView()
    }

    fun onCarregaListaTecnicos() {
        listItens.clear()
        var lista: String?

        lista = wsAcessoTecnico().execute("get", "172.27.1.46:90").get()

        val gson = Gson()
        val objectList = gson.fromJson(lista, Array<Tecnico>::class.java).asList()
        for (obj in objectList)
        {
            listItens.add(obj)
        }

        adapter.notifyDataSetChanged()
    }

    fun imprimirToast(item: Tecnico)
    {
        val intent = Intent(this, EditTecnicos::class.java)
        intent.putExtra("obj",item)
        startActivity(intent)
    }

    fun abreEditTecnicos()
    {
        startActivity( Intent(this, EditTecnicos::class.java))
    }

    fun carregaListView(){
        val layout = LinearLayoutManager(this)

        listView = findViewById(R.id.rvp)
        listView.layoutManager = layout

        adapter = tecnicoAdapter(listItens, this, { item: Tecnico -> imprimirToast(item)})
        listView.adapter = adapter

        onCarregaListaTecnicos()
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
                startActivity( Intent(this, TecnicosActivity::class.java))
                finish()
            }

            R.id.nav_partidas -> {
                startActivity( Intent(this, PartidasActivity::class.java))
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    data class Tecnico(var idTecnico: String, var nome: String): Serializable{
        override fun toString(): String {
            return nome
        }
    }
}
