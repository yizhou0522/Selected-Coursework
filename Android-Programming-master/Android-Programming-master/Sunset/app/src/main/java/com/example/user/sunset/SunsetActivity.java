package com.example.user.sunset;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SunsetActivity extends SingleFragmentActivity {

   protected Fragment createFragment()
   {
       return SunsetFragment.newInstance();
   }
}
