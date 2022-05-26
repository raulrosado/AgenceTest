package com.serproteam.agencetest.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.serproteam.agencetest.R

class ReplaceFragment {
    fun replace(
        fragment: Fragment,
        fragmentTransaction: FragmentTransaction
    ) {
        fragmentTransaction.replace(R.id.contenedorFragment, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit()
    }
}