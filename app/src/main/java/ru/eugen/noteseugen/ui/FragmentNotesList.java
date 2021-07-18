package ru.eugen.noteseugen.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import ru.eugen.noteseugen.data.CardsSource;
import ru.eugen.noteseugen.data.CardsSourceImpl;
import ru.eugen.noteseugen.data.CardsSourceResponse;
import ru.eugen.noteseugen.data.Note;
import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.observe.Observer;
import ru.eugen.noteseugen.observe.Publisher;

public class FragmentNotesList extends Fragment {
    private boolean isLandscape;
    public static boolean isInstance;
    private CardsSourceImpl cardsSourceImpl;
    private final String CARDS = "CARDS";
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
//
//    public static FragmentNotesList newInstanceNote(Note note) {
//        FragmentNotesList fragment = new FragmentNotesList();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        isInstance = true;
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            cardsSourceImpl = savedInstanceState.getParcelable(CARDS);
//        } else {
//            cardsSourceImpl = new CardsSourceImpl(getResources()).init();
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        initView(view);
        setHasOptionsMenu(true);
        cardsSourceImpl = new CardsSourceFirebaseImpl.init(new CardsSourceResponse() {
            @Override
            public void initialized(CardsSource cards) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(cardsSourceImpl);
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
//        switch (item.getItemId()) {
//            case R.id.action_add:
//                navigation = activity.getNavigation();
//                navigation.addFragment(CardFragment.newInstance(), true);
//                publisher.subscribe(new Observer() {
//                    @Override
//                    public void updateCard(Card card) {
//                        cardsSourceImpl.addCard(card);
//                        adapter.notifyItemInserted(cardsSourceImpl.size() - 1);
//                        moveToLastPosition = true;
//                    }
//                });
//                return true;
//            case R.id.action_clear:
//                cardsSourceImpl.clearCard();
//                adapter.notifyDataSetChanged();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
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

        if (moveToFirstPosition && cardsSourceImpl.size() > 0){
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }

//        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                note = new Note(getResources().getStringArray(R.array.notes)[position], position);
//                showFragment(note);
//            }
//        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        int position = adapter.getMenuPosition();
//        switch (item.getItemId()) {
//            case R.id.action_update:
//                navigation = activity.getNavigation();
//                navigation.addFragment(CardFragment.newInstance(cardsSourceImpl.getCard(position)), true);
//                publisher.subscribe(new Observer() {
//                    @Override
//                    public void updateCard(Card card) {
//                        cardsSourceImpl.updateCard(position, card);
//                        adapter.notifyItemChanged(position);
//                    }
//                });
//                return true;
//
//            case R.id.action_delete:
//                cardsSourceImpl.deleteCard(position);
//                adapter.notifyItemRemoved(position);
//                return true;
//        }
//        return super.onContextItemSelected(item);
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (getArguments() != null) {
//            note = getArguments().getParcelable(NOTE);
//        }
//        if (savedInstanceState != null) {
//            note = savedInstanceState.getParcelable(NOTE);
//        }
//        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
//        if (isLandscape == true && note == null) {
//            clearLandFragment();
//        }
//        if (isLandscape == true && note != null && isInstance == false) {
//            showLandFragment(note);
//        }
//        if (isLandscape == false && note != null) {
//            showPortFragment(note);
//        }
    }

//    private void clearLandFragment() {
//        FragmentLandNote details = FragmentLandNote.newInstanceClear();
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment, details);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CARDS, cardsSourceImpl);
        super.onSaveInstanceState(outState);
    }

//    private void showFragment(Note indexNote) {
//        if (isLandscape) {
//            showLandFragment(indexNote);
//        } else {
//            showPortFragment(indexNote);
//        }
//    }

//    private void showLandFragment(Note note) {
//        FragmentLandNote details = FragmentLandNote.newInstance(note);
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment, details);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();
//    }

//    private void showPortFragment(Note note) {
//        FragmentPortNote details = FragmentPortNote.newInstance(note);
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.notes, details);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.commit();
//    }

    private boolean onItemSelected(int menuItemId){
        switch (menuItemId){
            case R.id.action_add:
                navigation.addFragment(CardFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCard(Card card) {
                        cardsSourceImpl.addCard(card);
                        adapter.notifyItemInserted(cardsSourceImpl.size() - 1);
// это сигнал, чтобы вызванный метод onCreateView
// перепрыгнул на начало списка
                        moveToFirstPosition = true;
                    }
                });
                return true;

            case R.id.action_update:
                final int updatePosition = adapter.getMenuPosition();
                navigation.addFragment(CardFragment.newInstance(cardsSourceImpl.getCard(updatePosition)), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCard(Card card) {
                        cardsSourceImpl.updateCard(updatePosition, card);
                        adapter.notifyItemChanged(updatePosition);
                    }
                });
                return true;

            case R.id.action_delete:
                int deletePosition = adapter.getMenuPosition();
                cardsSourceImpl.deleteCard(deletePosition);
                adapter.notifyItemRemoved(deletePosition);
                return true;

            case R.id.action_clear:
                cardsSourceImpl.clearCard();
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }
}