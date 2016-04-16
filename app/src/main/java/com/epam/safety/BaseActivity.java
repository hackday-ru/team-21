package com.epam.safety;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initNavigationDrawer();
        onCreateLayout(savedInstanceState);

    }

    abstract int getLayoutResource();

    abstract void onCreateLayout(Bundle savedInstanceState);

    private void initNavigationDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Settings");
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Edit contacts");
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("Edit message");

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 0:
                                startActivity(MainActivity.class);
                                break;
                            case 1:
                                startActivity(SettingsActivity.class);
                                break;
                            case 2:
                                startActivity(EditContactsActivity.class);
                                break;
                            case 3:
                                startActivity(EditMessageActivity.class);
                                break;
                        }

                        return true;
                    }
                })
                .build();
    }

    private void startActivity(Class<?> clazz){
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
