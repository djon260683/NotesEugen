package ru.eugen.noteseugen.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.eugen.noteseugen.MainActivity;
import ru.eugen.noteseugen.Navigation;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.data.CardSourceFirebaseImpl;
import ru.eugen.noteseugen.data.CardsSource;
import ru.eugen.noteseugen.data.CardsSourceResponse;
import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.observe.Observer;
import ru.eugen.noteseugen.observe.Publisher;

public class FragmentNotesList extends Fragment {
    private CardsSource data;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private static final int MY_DEFAULT_DURATION = 1000;
    private MainActivity activity;

    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static FragmentNotesList newInstance() {
        FragmentNotesList fragment = new FragmentNotesList();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        initView(view);
        setHasOptionsMenu(true);
        data = new CardSourceFirebaseImpl().init(new CardsSourceResponse() {
            @Override
            public void initialized(CardsSource cards) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_noteslist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        if (moveToFirstPosition && data.size() > 0){
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putParcelable(CARDS, data);
        super.onSaveInstanceState(outState);
    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
            case R.id.action_add:
                navigation.replaceFragment(CardFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCard(Card card) {
                        data.addCard(card);
                        adapter.notifyItemInserted(data.size() - 1);
                        moveToFirstPosition = true;
                    }
                });
                return true;

            case R.id.action_update:
                final int updatePosition = adapter.getMenuPosition();
                navigation.replaceFragment(CardFragment.newInstance(data.getCard(updatePosition)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCard(Card card) {
                        data.updateCard(updatePosition, card);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;

            case R.id.action_delete:
                int deletePosition = adapter.getMenuPosition();
                data.deleteCard(deletePosition);
                adapter.notifyItemRemoved(deletePosition);
                Log.d("Log", "action_delete");
                return true;

            case R.id.action_clear:
                data.clearCard();
                adapter.notifyDataSetChanged();
                Log.d("Log", "action_clear");
                return true;
        }
        return false;
    }
}