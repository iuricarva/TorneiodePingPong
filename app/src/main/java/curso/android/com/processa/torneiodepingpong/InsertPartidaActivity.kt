package curso.android.com.processa.torneiodepingpong

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.support.design.widget.FloatingActionButton
import android.widget.*
import curso.android.com.processa.torneiodepingpong.DataBase.DataBasePingPing


class InsertPartidaActivity : AppCompatActivity() {

    lateinit var Partida: PartidasActivity.Partida
    lateinit var botaoSalvar: FloatingActionButton
    lateinit var botaoDelete: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_partida)

        botaoSalvar = findViewById(R.id.btnSavePartida) as FloatingActionButton
        botaoDelete = findViewById(R.id.btnDeletePartida) as FloatingActionButton



        val listTecnicos = arrayListOf<TecnicosActivity.Tecnico>()

        val list = DataBasePingPing(this).getTecnicos()

        for (obj in list) {
            listTecnicos.add(obj)
        }

        val spinner1 = findViewById(R.id.spTecnico1) as Spinner
        val spinner2 = findViewById(R.id.spTecnico2) as Spinner

        val btnMaisUm = findViewById(R.id.btnMais1) as Button
        val btnMenosUm = findViewById(R.id.btnMenos1) as Button

        val btnMaisDois = findViewById(R.id.btnMais2) as Button
        val btnMenosDois = findViewById(R.id.btnMenos2) as Button

        val txtPontos1 = findViewById(R.id.ptTecnico1) as TextView
        val txtPontos2 = findViewById(R.id.ptTecnico2) as TextView

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTecnicos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listTecnicos)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner1.setAdapter(adapter)
        spinner2.setAdapter(adapter2)

        btnMaisUm.setOnClickListener { mais(txtPontos1) }
        btnMaisDois.setOnClickListener { mais(txtPontos2) }

        btnMenosUm.setOnClickListener { menos(txtPontos1) }
        btnMenosDois.setOnClickListener { menos(txtPontos2) }

        val intent = intent
        var retorno = intent.getSerializableExtra("idPartida") as String?
        if (retorno != null) {

            Partida = DataBasePingPing(this).getPartida(retorno)

            var pontos1 = findViewById(R.id.ptTecnico1) as TextView
            var pontos2 = findViewById(R.id.ptTecnico2) as TextView

            pontos1.text = Partida.placar.pontuacaoTecnico1.toString()
            pontos2.text = Partida.placar.pontuacaoTecnico2.toString()

            spinner1.setSelection(listTecnicos.indexOf(Partida.tecnico1))
            spinner2.setSelection(listTecnicos.indexOf(Partida.tecnico2))

            //botaoSalvar.setOnClickListener { editTecnicos() }
            botaoDelete.setOnClickListener { deletePartida(retorno) }

        } else {
            Partida = PartidasActivity.Partida("0",TecnicosActivity.Tecnico("0",""),
                TecnicosActivity.Tecnico("0",""), PartidasActivity.Placar("0", 0, 0))

            botaoSalvar.setOnClickListener {
                salvarPartida(
                    spinner1,
                    spinner2,
                    txtPontos1.text.toString(),
                    txtPontos2.text.toString()
                )
            }
            botaoDelete.setOnClickListener { novaPartida() }
        }


    }


    fun mais(view: TextView) {
        var texto = view.text.toString()
        if (texto.equals("")) {
            view.text = "1"
        } else {
            texto = (texto.toInt() + 1).toString()
            view.text = texto
        }
    }

    fun menos(view: TextView) {
        var texto = view.text.toString()
        if (texto.equals("")) {
            view.text = "0"
        } else{
            if (texto.toInt() > 0) {
                texto = (texto.toInt() - 1).toString()
                view.text = texto
            }
        }
    }

    fun salvarPartida(spinner1: Spinner, spinner2: Spinner, ptsTecnico1: String, ptsTecnico2: String) {
        val idTecnico1 = (spinner1.getSelectedItem() as TecnicosActivity.Tecnico).idTecnico
        val idTecnico2 = (spinner2.getSelectedItem() as TecnicosActivity.Tecnico).idTecnico

        if (!idTecnico1.equals(idTecnico2)) {
            var idPlacar = DataBasePingPing(this).insertPlacarGetId(ptsTecnico1, ptsTecnico2)

            if (idPlacar > 0) {
                var partida = PartidasActivity.partidaWS(
                    "",
                    idTecnico1,
                    idTecnico2,
                    idPlacar.toString(),
                    ""

                )
                if (DataBasePingPing(this).insertPartidaSemId(partida)){
                    Toast.makeText(this, "Partida salva com sucesso.", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this, "Falha ao inserir partida.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Falha ao inserir partida.", Toast.LENGTH_LONG).show()
            }


        } else {
            Toast.makeText(this, "Técnicos iguais. Selecione outros técnicos.", Toast.LENGTH_LONG).show()
        }
    }

    fun novaPartida(){
        Toast.makeText(this, "Partida Inexistente", Toast.LENGTH_SHORT).show()
    }

    fun deletePartida(id: String)
    {
        DataBasePingPing(this).deletePartidaLogico(id.toInt())
        Toast.makeText(this, "Partida deletada com sucesso.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
