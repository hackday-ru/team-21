package com.epam.safety;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;



public abstract class BaseActivity extends AppCompatActivity {

    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initNavigationDrawer();
        onCreateLayout(savedInstanceState);
    }

    abstract int getLayoutResource();

    abstract void onCreateLayout(Bundle savedInstanceState);

    private static final int POSITION_HOME = 0;
    private static final int POSITION_SETTINGS = 1;
    private static final int POSITION_EDIT_CONTACTS = 2;
    private static final int POSITION_EDIT_MESSAGE = 3;

    private static final String MENU_HOME = "Home";
    private static final String MENU_SETTINGS = "Settings";
    private static final String MENU_EDIT_CONTACTS = "Edit contacts";
    private static final String MENU_EDIT_MESSAGE = "Edit message";

    private void initNavigationDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(MENU_HOME).withIcon(FontAwesome.Icon.faw_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(MENU_SETTINGS).withIcon(FontAwesome.Icon.faw_cog);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(MENU_EDIT_CONTACTS).withIcon(FontAwesome.Icon.faw_users);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(MENU_EDIT_MESSAGE).withIcon(FontAwesome.Icon.faw_comment);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4
                )
                .withHeader(R.layout.drawer_header)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            switch (((Nameable) drawerItem).getName().getText()) {
                                case MENU_HOME:
                                    startActivityWithDelay(MainActivity.class);
                                    break;
                                case MENU_SETTINGS:
                                    startActivityWithDelay(SettingsActivity.class);
                                    break;
                                case MENU_EDIT_CONTACTS:
                                    startActivityWithDelay(EditContactsActivity.class);
                                    break;
                                case MENU_EDIT_MESSAGE:
                                    startActivityWithDelay(EditMessageActivity.class);
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .build();
    }



    private void setSelectionAtPosition(int position){
        result.setSelectionAtPosition(position);
    }

    public void setSettingsSelected(){
        setSelectionAtPosition(POSITION_SETTINGS);
    }

    public void setEditContactsSelected(){
        setSelectionAtPosition(POSITION_EDIT_CONTACTS);
    }

    public void setEditMessageSelected() {
        setSelectionAtPosition(POSITION_EDIT_MESSAGE);
    }

    private void startActivityWithDelay(final Class<?> clazz){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BaseActivity.this, clazz);
                startActivity(intent);
            }
        }, 250);


    }
}
