package ru.eugen.noteseugen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.data.CardsSource;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private CardsSource dataSource;
    private OnItemClickListener itemClickListener;
    private final Fragment fragment;


    public NotesAdapter(CardsSource dataSource, Fragment fragment) {
        this.dataSource = dataSource;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  NotesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.setData(dataSource.getCard(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note;
        private TextView date;
        private TextView essence;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
            essence = itemView.findViewById(R.id.essence);

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
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }
        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null){
                fragment.registerForContextMenu(itemView);
            }
        }
        public void setData(Card cardData){
            note.setText(cardData.getNote());
            date.setText(cardData.getDate());
            essence.setText(cardData.getEssence());
        }
    }
}
