package curso.android.com.processa.torneiodepingpong

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class EditTecnicos : AppCompatActivity() {

    lateinit var tecnico: TecnicosActivity.Tecnico
    lateinit var botaoDelete: FloatingActionButton
    lateinit var botaoSalvar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tecnicos)

        botaoDelete = findViewById(R.id.btnDeleteTecnico) as FloatingActionButton

        botaoSalvar = findViewById(R.id.btnSaveTecnico) as FloatingActionButton


        val intent = intent
        var retorno = intent.getSerializableExtra("obj") as TecnicosActivity.Tecnico?
        if (retorno != null) {

            tecnico = retorno

            var txtId = findViewById(R.id.idTecnico) as TextView
            var txtNome = findViewById(R.id.nomeTecnico) as TextView

            txtId.text = tecnico.idTecnico
            txtNome.text = tecnico.nome
            botaoSalvar.setOnClickListener { editTecnicos() }
            botaoDelete.setOnClickListener { deleteTecnicos() }
        } else {
            tecnico = TecnicosActivity.Tecnico("0", "")
            botaoSalvar.setOnClickListener { insertTecnicos() }
            botaoDelete.setOnClickListener { novoTecnico() }
        }


    }

    fun insertTecnicos() {

        var txtId = findViewById(R.id.idTecnico) as TextView
        var txtNome = findViewById(R.id.nomeTecnico) as TextView

        var id = txtId.text.toString()
        var nome = txtNome.text.toString()

        if (id.equals(""))
            id = "0"
        else
            id = txtId.text.toString()

        tecnico.idTecnico = id
        tecnico.nome = txtNome.text.toString()


        var retorno: String?

        retorno = wsAcessoTecnico().execute("post", "172.27.1.46:90", tecnico.idTecnico, tecnico.nome).get()

        if (retorno.equals("true")) {
            Toast.makeText(this, "Técnico inserido com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        } else if (retorno.equals("false")) {
            Toast.makeText(this, "Erro ao inserir técnico", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro de sistema. Contate o administrador", Toast.LENGTH_SHORT).show()
        }
    }

    fun editTecnicos() {

        var txtId = findViewById(R.id.idTecnico) as TextView
        var txtNome = findViewById(R.id.nomeTecnico) as TextView

        var id = txtId.text.toString()
        var nome = txtNome.text.toString()

        if (id.equals(""))
            id = "0"
        else
            id = txtId.text.toString()

        tecnico.idTecnico = id
        tecnico.nome = txtNome.text.toString()


        var retorno: String?

        retorno = wsAcessoTecnico().execute("put", "172.27.1.46:90", tecnico.idTecnico, tecnico.nome).get()

        if (retorno.equals("true")) {
            Toast.makeText(this, "Técnico modificado com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        } else if (retorno.equals("false")) {
            Toast.makeText(this, "Erro ao modificar técnico", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro de sistema. Contate o administrador", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteTecnicos() {
        var retorno: String?

        retorno = wsAcessoTecnico().execute("delete", "172.27.1.46:90", tecnico.idTecnico).get()

        if (retorno.equals("true")) {
            Toast.makeText(this, "Técnico deletado com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        } else if (retorno.equals("false")) {
            Toast.makeText(this, "Erro ao deletar técnico", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erro de sistema. Contate o administrador", Toast.LENGTH_SHORT).show()
        }
    }

    fun novoTecnico(){
        Toast.makeText(this, "Técnico Inexistente", Toast.LENGTH_SHORT).show()
    }




}
