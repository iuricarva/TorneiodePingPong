package curso.android.com.processa.torneiodepingpong

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_partida.view.*

class partidaAdapter(val list: ArrayList<PartidasActivity.Partida>, val context: Context, val clickListener: (PartidasActivity.Partida) -> Unit): RecyclerView.Adapter<partidaAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.id?.text = list[position].idPartida
        holder?.nomeTecnico1?.text = list[position].tecnico1.nome
        holder?.nomeTecnico2?.text = list[position].tecnico2.nome
        holder?.placarTecnico1?.text = list[position].placar.pontuacaoTecnico1.toString()
        holder?.placarTecnico2?.text = list[position].placar.pontuacaoTecnico2.toString()
        holder.bind(list[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_partida, parent, false)
        val ivh = ItemViewHolder(v)

        return ivh
    }



    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val id = view.findViewById(R.id.txtIdPartida) as TextView
        val nomeTecnico1 = view.findViewById(R.id.txtTecnico1) as TextView
        val nomeTecnico2 = view.findViewById(R.id.txtTecnico2) as TextView
        val placarTecnico1 = view.findViewById(R.id.txtPlacar1) as TextView
        val placarTecnico2 = view.findViewById(R.id.txtPlacar2) as TextView

        fun bind(part: PartidasActivity.Partida, clickListener: (PartidasActivity.Partida) -> Unit) {
            itemView.txtIdPartida.text = part.idPartida
            itemView.setOnClickListener { clickListener(part)}
        }
    }


    override fun getItemCount(): Int = list.size

    fun getPartida(index: Int): PartidasActivity.Partida
    {
        return list[index]
    }

}