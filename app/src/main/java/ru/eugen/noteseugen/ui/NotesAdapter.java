package ru.eugen.noteseugen.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.eugen.noteseugen.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{
    private String[] dataSource;

    public NotesAdapter(String[] dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull  NotesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(dataSource[position]);
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
