package ru.eugen.noteseugen;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

class Navigation {
    val fragmentManager: FragmentManager

    constructor(fragmentManager: FragmentManager){
        this.fragmentManager = fragmentManager
    }

    fun replaceFragment(fragment:Fragment, useBackStack:Boolean):Unit {
        val fragmentTransaction:FragmentTransaction  = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.notes, fragment)
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    fun addFragment(fragment:Fragment, useBackStack:Boolean) {
        val fragmentTransaction:FragmentTransaction  = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.notes, fragment)
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}
