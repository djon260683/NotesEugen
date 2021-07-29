package ru.eugen.noteseugen

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment

import com.google.android.material.navigation.NavigationView

import ru.eugen.noteseugen.observe.Publisher
import ru.eugen.noteseugen.ui.DialogCustomFragment
import ru.eugen.noteseugen.ui.FragmentNotesList
import ru.eugen.noteseugen.ui.StartFragment

class MainActivity : AppCompatActivity() {
    lateinit var navigation: Navigation
    var publisher: Publisher = Publisher()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation = Navigation(supportFragmentManager)
        if (savedInstanceState == null) {
//            navigation.replaceFragment(FragmentNotesList.newInstance(), false)
            navigation.replaceFragment(StartFragment.newInstance(), false)
        }
        initView()
    }



    fun initView(): Unit {
        val toolbar: Toolbar = initToolBar()
        initDrawer(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun initToolBar(): Toolbar {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        return toolbar
    }

    fun initDrawer(toolbar: Toolbar): Unit {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.menu_drawer_open, R.string.mneu_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener {
            NavigationView.OnNavigationItemSelectedListener { item ->
                val id: Int = item.itemId
                if (navigateFragment(id)) {
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
             false
            }
            true
        }
    }


    override fun onCreateOptionsMenu (menu: Menu): Boolean  {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_app, menu)

        val search: MenuItem  = menu.findItem (R.id.action_search)
        val searchText: SearchView  = search.actionView as SearchView

        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean    {
        val id: Int  = item.itemId
        if (navigateFragment(id)) return true
        return super.onOptionsItemSelected(item)
    }

    fun navigateFragment(id: Int): Boolean   {
        when (id) {
             R.id.action_settings-> {
                 Toast.makeText(this, "Выбраны настройки", Toast.LENGTH_SHORT).show()
             }
             R . id . action_notes -> {
                 Toast.makeText(this, "Выбраны заметки", Toast.LENGTH_SHORT).show()
             }
             R.id.action_tasks -> {
                 Toast.makeText(this, "Выбраны задачи", Toast.LENGTH_SHORT).show()
             }
        }
        return false
    }
}

