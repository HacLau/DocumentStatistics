package com.tqs.filemanager.ui.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("customNavigator")
class CustomNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) :
    FragmentNavigator(context, fragmentManager, containerId) {


    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val ft = fragmentManager.beginTransaction()
        var fragment = fragmentManager.primaryNavigationFragment
        if (fragment != null) {
            ft.remove(fragment)
        }
        val tag = destination.id.toString()
        fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            ft.replace(containerId,fragment,tag)
        } else {
            fragment = instantiateFragment(context, fragmentManager, destination.className, args)
            ft.replace(containerId, fragment, tag)
        }
        ft.setPrimaryNavigationFragment(fragment)
        ft.setReorderingAllowed(true)
        ft.commit()
        return destination
    }
}