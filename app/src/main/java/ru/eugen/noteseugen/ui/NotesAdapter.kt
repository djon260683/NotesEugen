package ru.eugen.noteseugen.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.eugen.noteseugen.R
import ru.eugen.noteseugen.data.Card
import ru.eugen.noteseugen.data.CardsSource
import ru.eugen.noteseugen.ui.NotesAdapter.ViewHolder
import java.text.SimpleDateFormat

class NotesAdapter : RecyclerView.Adapter<ViewHolder> {
    private lateinit var data: CardsSource
    private lateinit var itemClickListener: OnItemClickListener

    var fragment: Fragment
    var menuPosition: Int


    constructor(fragment: Fragment) {
        this.fragment = fragment
        menuPosition = 0
    }

    fun setDataSource(cardsSource: CardsSource): Unit {
        this.data = cardsSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setData(data.getCard(position))
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    interface OnItemClickListener : AdapterView.OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

//    fun SetOnItemClickListener(itemClickListener: AdapterView.OnItemClickListener)  {
//        this.itemClickListener = itemClickListener
//    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        val note: TextView
        val essence: TextView
        val date: TextView

        constructor (itemView: View) : super(itemView) {

            note = itemView.findViewById(R.id.note)
            essence = itemView.findViewById(R.id.essence)
            date = itemView.findViewById(R.id.date)

            registerContextMenu(itemView)

            note.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition())
                    }
                }
            });
            note.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(view: View): Boolean {
                    menuPosition = getLayoutPosition()
                    itemView.showContextMenu(10f, 10f)
                    return true
                }
            });
        }

        fun registerContextMenu(itemView: View) {
            if (fragment != null) {

                itemView.setOnLongClickListener(object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        menuPosition = getLayoutPosition()
                        return false
                    }
                });
                fragment.registerForContextMenu(itemView)
            }
        }

        fun setData(card: Card) {
            note.text = card.note
            essence.text = card.essence
            date.text = SimpleDateFormat("dd-MM-yy").format(card.date)
        }
    }
}
