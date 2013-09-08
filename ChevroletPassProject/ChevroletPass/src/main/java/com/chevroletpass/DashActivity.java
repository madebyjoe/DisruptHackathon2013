package com.chevroletpass;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;

public class DashActivity extends FragmentActivity {

  private static DashActivity instance;

  public static Context getContext() {
    return instance;
  }

  public static DashActivity getInstance() {
    return instance;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dash_main);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.dash, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.remote:
        getSupportFragmentManager().beginTransaction().add(new RemoteFragment(), RemoteFragment.class.getSimpleName()).commit();
        return true;
      case R.id.action_settings:
        //TODO Settings
        return true;
      case R.id.insurance_claim:
        //TODO Daniel
        //open photo app section and make insurance claim
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


}
