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

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import ru.eugen.noteseugen.MainActivity;
import ru.eugen.noteseugen.Navigation;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.data.CardsSource;
import ru.eugen.noteseugen.data.CardsSourceImpl;
import ru.eugen.noteseugen.data.Note;
import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.observe.Observer;
import ru.eugen.noteseugen.observe.Publisher;

public class FragmentNotesList extends Fragment {
    public static final String NOTE = "NOTE";
    private Note note;
    private boolean isLandscape;
    public static boolean isInstance;
    private CardsSource cardsSource;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private static final int MY_DEFAULT_DURATION = 1000;

    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToLastPosition;

    public static FragmentNotesList newInstance() {
        FragmentNotesList fragment = new FragmentNotesList();
        isInstance = false;
        return fragment;
    }

    public static FragmentNotesList newInstanceNote(Note note) {
        FragmentNotesList fragment = new FragmentNotesList();
        Bundle args = new Bundle();
        args.putParcelable(NOTE, note);
        fragment.setArguments(args);
        isInstance = true;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardsSource = new CardsSourceImpl(getResources()).init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
//        cardsSource = new CardsSourceImpl(getResources()).init();
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
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
        switch (item.getItemId()) {
            case R.id.action_add:
//                cardsSource.addCard(new Card("Заголовок " + cardsSource.size(), "Дата " + cardsSource.size(), "Описание " + cardsSource.size(), Calendar.getInstance().getTime()));
//                adapter.notifyItemInserted(cardsSource.size() - 1);
//                recyclerView.scrollToPosition(cardsSource.size() - 1);
//                return true;

                navigation.addFragment(CardFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateCard(Card card) {
                        cardsSource.addCard(card);
                        adapter.notifyItemInserted(cardsSource.size() - 1);
                        moveToLastPosition = true;
                    }
                });
            case R.id.action_clear:
                cardsSource.clearCard();
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        cardsSource = new CardsSourceImpl(getResources()).init();
        initRecyclerView();
    }


    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(cardsSource, this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);
        if (moveToLastPosition) {
            recyclerView.smoothScrollToPosition(cardsSource.size() - 1);
            moveToLastPosition = false;
        }

        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                note = new Note(getResources().getStringArray(R.array.notes)[position], position);
                showFragment(note);
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        switch (item.getItemId()) {
//            case R.id.action_update:
//                cardsSource.updateCard(position, new Card("New " + cardsSource.getCard(position).getNote(),
//                        "New " + cardsSource.getCard(position).getDate(),
//                        "New " + cardsSource.getCard(position).getEssence(), Calendar.getInstance().getTime()));
//                adapter.notifyItemChanged(position);


            cardsSource.updateCard(position,
                    new Card("Кадр " + position,
                            cardsSource.getCard(position).getDescription(),
                            cardsSource.getCard(position).getPicture(),
                            false));
            adapter.notifyItemChanged(position);
            navigation.addFragment(CardFragment.newInstance(data.getCardData(position)),true);

            publisher.subscribe(new Observer() {
                @Override
                public void updateCardData(CardData cardData) {
                    data.updateCardData(position, cardData);
                    adapter.notifyItemChanged(position);
                }
            });
            return true;

            case R.id.action_delete:
                cardsSource.deleteCard(position);
                adapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE);
        }
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(NOTE);
        }
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape == true && note == null) {
            clearLandFragment();
        }
        if (isLandscape == true && note != null && isInstance == false) {
            showLandFragment(note);
        }
        if (isLandscape == false && note != null) {
            showPortFragment(note);
        }
    }

    private void clearLandFragment() {
        FragmentLandNote details = FragmentLandNote.newInstanceClear();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void showFragment(Note indexNote) {
        if (isLandscape) {
            showLandFragment(indexNote);
        } else {
            showPortFragment(indexNote);
        }
    }

    private void showLandFragment(Note note) {
        FragmentLandNote details = FragmentLandNote.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortFragment(Note note) {
        FragmentPortNote details = FragmentPortNote.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.notes, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}