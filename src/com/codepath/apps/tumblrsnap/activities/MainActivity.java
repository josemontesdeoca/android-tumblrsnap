
package com.codepath.apps.tumblrsnap.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tumblrsnap.R;
import com.codepath.apps.tumblrsnap.TumblrSnapApp;
import com.codepath.apps.tumblrsnap.fragments.LoginFragment;
import com.codepath.apps.tumblrsnap.fragments.LoginFragment.OnLoginHandler;
import com.codepath.apps.tumblrsnap.fragments.PhotosFragment;
import com.codepath.apps.tumblrsnap.models.User;

public class MainActivity extends FragmentActivity implements OnLoginHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (User.currentUser() != null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public void onLogin() {
        updateFragments();
    }

    public void onLogoutMenu(MenuItem menuItem) {
        TumblrSnapApp.getClient().clearAccessToken();
        User.setCurrentUser(null);
        updateFragments();
    }

    private void updateFragments() {
        if (User.currentUser() == null) {
            showLoginFragment();
        } else {
            showPhotosFragment();
        }
        invalidateOptionsMenu();
    }

    private void showPhotosFragment() {
        showFragment(PhotosFragment.class);
    }

    private void showLoginFragment() {
        showFragment(LoginFragment.class);
    }

    @SuppressWarnings("rawtypes")
    private void showFragment(Class activeFragmentClass) {
        Class[] fragmentClasses = new Class[] {
                LoginFragment.class,
                PhotosFragment.class
        };
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction transaction = mgr.beginTransaction();
        try {
            for (Class klass : fragmentClasses) {
                Fragment fragment = mgr.findFragmentByTag(klass.getName());
                if (klass == activeFragmentClass) {
                    if (fragment != null) {
                        transaction.show(fragment);
                    } else {
                        transaction.add(R.id.frmContent, (Fragment) klass.newInstance(),
                                klass.getName());
                    }
                } else {
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transaction.commit();
    }
}
