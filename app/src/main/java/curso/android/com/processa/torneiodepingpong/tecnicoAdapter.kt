package curso.android.com.processa.torneiodepingpong

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_tecnico.view.*

class tecnicoAdapter(val list: ArrayList<TecnicosActivity.Tecnico>, val context: Context, val clickListener: (TecnicosActivity.Tecnico) -> Unit): RecyclerView.Adapter<tecnicoAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder?.id?.text = list[position].idTecnico
        holder?.nome?.text = list[position].nome
        holder.bind(list[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_tecnico, parent, false)
        val ivh = ItemViewHolder(v)

        return ivh
    }



    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val id = view.findViewById(R.id.txtIdTecnico) as TextView
        val nome = view.findViewById(R.id.txtNomeTecnico) as TextView

        fun bind(part: TecnicosActivity.Tecnico, clickListener: (TecnicosActivity.Tecnico) -> Unit) {
            itemView.txtIdTecnico.text = part.idTecnico
            itemView.txtNomeTecnico.text = part.nome
            itemView.setOnClickListener { clickListener(part)}
        }
    }


    override fun getItemCount(): Int = list.size

    fun getTecnico(index: Int): TecnicosActivity.Tecnico
    {
        return list[index]
    }

}