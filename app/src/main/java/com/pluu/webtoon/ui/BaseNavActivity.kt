package com.pluu.webtoon.ui

import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.ServiceConst
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import java.util.*

/**
 * Base ActionBar Activity
 * Created by pluu on 2017-05-07.
 */
abstract class BaseNavActivity : AppCompatActivity() {

    // Primary toolbar and drawer toggle
    private val mActionBarToolbar: Toolbar? by lazy {
        val toolbar = findViewById(R.id.toolbar_actionbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar
    }

    // Navigation drawer:
    private val mDrawerLayout: DrawerLayout? by lazy {
        findViewById(R.id.drawer_layout) as DrawerLayout
    }

    private lateinit var mHandler: Handler

    // list of navdrawer items that were actually added to the navdrawer, in order
    private val mNavDrawerItems = ArrayList<NAV_ITEM>()

    // views that correspond to each navdrawer item, null if not yet created
    private var mNavDrawerItemViews: MutableList<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mHandler = Handler()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupNavDrawer()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mActionBarToolbar
    }

    protected var selfNavDrawerItem = NAV_ITEM.INVALID

    private fun setupNavDrawer() {
        mDrawerLayout?.setStatusBarBackgroundColor(
                ContextCompat.getColor(this, R.color.theme_primary_dark))

        mActionBarToolbar?.let {
            val mDrawerToggle = ActionBarDrawerToggle(
                    this, /* host Activity */
                    mDrawerLayout, /* DrawerLayout object */
                    mActionBarToolbar, /* nav drawer image to replace 'Up' caret */
                    R.string.app_name, /* "open drawer" description for accessibility */
                    R.string.app_name /* "close drawer" description for accessibility */
            )
            mDrawerToggle.isDrawerIndicatorEnabled = true
            mDrawerLayout?.addDrawerListener(mDrawerToggle)
            mDrawerToggle.syncState()
        }

        populateNavDrawer()
    }

    private fun populateNavDrawer() {
        mNavDrawerItems.clear()
        NAV_ITEM.values().filterTo(mNavDrawerItems) { it.isSelect }
        createNavDrawerItems()
    }

    private fun createNavDrawerItems() {
        val mDrawerItemsListContainer: ViewGroup? = findViewById(R.id.navdrawer_items_list) as ViewGroup
        mDrawerItemsListContainer?.let {
            mNavDrawerItemViews = mutableListOf<View>().apply {
                mDrawerItemsListContainer.removeAllViews()

                for ((index, value) in mNavDrawerItems.withIndex()) {
                    add(makeNavDrawerItem(value, mDrawerItemsListContainer))
                    mDrawerItemsListContainer.addView(get(index))
                }
            }
        }
    }

    private fun makeNavDrawerItem(item: NAV_ITEM, container: ViewGroup): View {
        val selected = selfNavDrawerItem === item
        val layoutToInflate = if (item === NAV_ITEM.SEPARATOR) {
            R.layout.navdrawer_separator
        } else {
            R.layout.navdrawer_item
        }
        val view = layoutInflater.inflate(layoutToInflate, container, false)

        if (isSeparator(item)) {
            // we are done
            return view
        }

        val iconView = view.findViewById(R.id.icon) as ImageView
        val titleView = view.findViewById(R.id.title) as TextView
        val idx = item.ordinal
        val iconId = ServiceConst.NAVDRAWER_ICON_RES_ID[idx]
        val titleId = ServiceConst.NAVDRAWER_TITLE_RES_ID[idx]

        // set icon and text
        iconView.visibility = if (iconId > 0) View.VISIBLE else View.GONE
        if (iconId > 0) {
            iconView.setImageResource(iconId)
        }
        titleView.text = getString(titleId)

        formatNavDrawerItem(view, item, selected)

        view.setOnClickListener { _ -> onNavDrawerItemClicked(item) }

        return view
    }

    private fun formatNavDrawerItem(view: View, item: NAV_ITEM, selected: Boolean) {
        if (isSeparator(item)) {
            // not applicable
            return
        }

        val iconView = view.findViewById(R.id.icon) as ImageView
        val titleView = view.findViewById(R.id.title) as TextView

        if (selected) {
            view.setBackgroundResource(R.drawable.selected_navdrawer_item_background)
        } else {
            view.setBackgroundResource(R.drawable.selector_nav_item)
        }

        // configure its appearance according to whether or not it's selected
        titleView.setTextColor(ContextCompat.getColor(this,
                if (selected) item.color else R.color.navdrawer_text_color))
        iconView.setColorFilter(ContextCompat.getColor(this,
                if (selected) item.bgColor else R.color.navdrawer_icon_tint))
    }

    private fun isSeparator(item: NAV_ITEM): Boolean {
        return item === NAV_ITEM.SEPARATOR
    }

    protected val isNavDrawerOpen: Boolean
        get() = mDrawerLayout?.isDrawerOpen(GravityCompat.START) ?: false

    protected fun closeNavDrawer() = mDrawerLayout?.closeDrawer(GravityCompat.START)

    private fun onNavDrawerItemClicked(item: NAV_ITEM) {
        if (item === selfNavDrawerItem) {
            mDrawerLayout?.closeDrawer(GravityCompat.START)
            return
        }

        // launch the target Activity after a short delay, to allow the close animation to play
        mHandler.postDelayed({ goToNavDrawerItem(item) }, NAVDRAWER_LAUNCH_DELAY.toLong())

        // change the active item on the list so the user can see the item changed
        setSelectedNavDrawerItem(item)

        mDrawerLayout?.closeDrawer(GravityCompat.START)
    }

    private fun goToNavDrawerItem(item: NAV_ITEM?) {
        if (item != null) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val tag = manager.findFragmentByTag(Const.MAIN_FRAG_TAG)
            transaction.remove(tag)
            transaction.replace(R.id.container,
                    MainFragment.newInstance(item), Const.MAIN_FRAG_TAG)
            transaction.commit()
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could
     * also be accomplished (perhaps more cleanly) with state-based layouts.
     * @param item Select Navi Item
     */
    private fun setSelectedNavDrawerItem(item: NAV_ITEM) {
        mNavDrawerItemViews?.apply {
            for ((index, value) in mNavDrawerItems.withIndex()) {
                if (index < mNavDrawerItems.size) {
                    formatNavDrawerItem(get(index), value, item === value)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isNavDrawerOpen) {
            closeNavDrawer()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        // delay to launch nav drawer item, to allow close animation to play
        private val NAVDRAWER_LAUNCH_DELAY = 250
    }

}
