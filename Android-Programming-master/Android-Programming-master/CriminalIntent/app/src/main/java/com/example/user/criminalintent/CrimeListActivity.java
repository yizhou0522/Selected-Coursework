package com.example.user.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by user on 2019/9/8.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
