package ru.eugen.noteseugen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.data.CardsSource;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private CardsSource cardsSource;
    private OnItemClickListener itemClickListener;
    private final Fragment fragment;
    private int menuPosition;


    public NotesAdapter(CardsSource cardsSource, Fragment fragment) {
        this.cardsSource = cardsSource;
        this.fragment = fragment;
    }
    public int getMenuPosition() {
        return menuPosition;
    }
    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  NotesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.setData(cardsSource.getCard(position));

    }

    @Override
    public int getItemCount() {
        return cardsSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note;
        private TextView dates;
        private TextView essence;
        private TextView date;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            essence = itemView.findViewById(R.id.essence);
            date = itemView.findViewById(R.id.date);

            registerContextMenu(itemView);

            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            note.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }
        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null){
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });
                fragment.registerForContextMenu(itemView);
            }
        }
        public void setData(Card card){
            note.setText(card.getNote());
            date.setText(new SimpleDateFormat("dd-MM-yy").format(card.getDate()));
            essence.setText(card.getEssence());
        }
    }
}
