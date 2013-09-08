package com.chevroletpass;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chevroletpass.api.Config;
import com.chevroletpass.api.Fixtures.MockUserService;
import com.chevroletpass.api.IUserService;
import com.chevroletpass.api.UserService;

public class DashActivity extends FragmentActivity {

  private static DashActivity instance;

  public static Context getContext() {
    return instance;
  }

  public static DashActivity getInstance() {
    return instance;
  }

  private Button mTestButton;
  private IUserService userService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dash_main);

    if(Config.isTestMode){
      userService = new MockUserService();
    }else{
      userService = new UserService();
    }

    this.mTestButton = (Button) this.findViewById(R.id.test_btn);
    this.mTestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        
      }
    });
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
