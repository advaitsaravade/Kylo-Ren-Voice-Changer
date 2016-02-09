package com.advait.s.emokylo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements FragmentChangeListener {
	
	private String[] nav_titles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
	private ActionBar actionbar;
	private RelativeLayout mDrawerHolderLayout;
	private int stuff_position = 0;
    
	private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    public static Bundle myBundle = new Bundle();
    private Fragment voicechangerfrag;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  actionbar = getSupportActionBar();
      
		nav_titles = getResources().getStringArray(R.array.nav_drawer_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.listview);
		mDrawerHolderLayout = (RelativeLayout) findViewById(R.id.left_drawer);
		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		 
      // nav drawer icons from resources
      navMenuIcons = getResources()
              .obtainTypedArray(R.array.nav_drawer_icons);
		ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
		 
      // adding nav drawer items to array
      // Home
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
      
		final CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), navDrawerItems);
		// Set the adapter for the list view
		mDrawerList.setAdapter(adapter);
		        
		        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, 
		        		R.string.drawer_closed){
		        	/** Called when a drawer has settled in a completely closed state. */
		            public void onDrawerClosed(View view) {
		                super.onDrawerClosed(view);
		                if(stuff_position != -1)
		                {
		                actionbar.setTitle(nav_titles[stuff_position]);
		                }
		                FragmentManager fragmentManager = getSupportFragmentManager();
		                List<Fragment> fragments = fragmentManager.getFragments();
		                for(Fragment fragment : fragments){
		                    if(fragment != null && fragment.isVisible())
		                    {
		                       // if(fragment.getTag().equals("Home"))
		                       // {
		                        //	adapter.notifyDataSetChanged();
		                        //}
		                    }
		                }
		            }

		            /** Called when a drawer has settled in a completely open state. */
		            public void onDrawerOpened(View drawerView) {
		                super.onDrawerOpened(drawerView);
		                actionbar.setTitle("MENU");
		            }
		        };
		 mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setupTheWindow(position);
			}
		});
		        
		        

		        // Set the drawer toggle as the DrawerListener
		        mDrawerLayout.setDrawerListener(mDrawerToggle);

		        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		        getSupportActionBar().setHomeButtonEnabled(true);
		        
		        if (savedInstanceState == null) {
		            // on first time display view for first nav item
		            setupTheWindow(1);
		        }
		     // Rate dialog
			    RateMeMaybe rmm = new RateMeMaybe(this);
			    rmm.setPromptMinimums(2, 0, 2, 0);
			    rmm.setDialogMessage("Although this app took just a couple of days to make, I did put in "
			                    +"a lot of heart and soul into it. The whole idea was to make something "
			                    +"that was funny, beautiful, and ad-free. If you've liked your experience "
			                    +"so far, I'd love to hear from you. Tell me about your ideas, suggestions, "
			                    +"and much more by leaving a review on the Google Play Store. Would you like to do so?");
			    rmm.setDialogTitle("Write to the developer");
			    rmm.setPositiveBtn("Sure");
			    rmm.setNegativeBtn("Never");
			    rmm.setNeutralBtn("Not now");
			    rmm.run();
	}
	protected void setupTheWindow(int position) {
		if(position != 0)
		{
		stuff_position = position-1;
		}
		Fragment fragment = null;
		switch(position)
		{
		case 1:
			fragment = new VoiceChangerFragment();
			break;
		case 2:
			fragment = new HistoryFragment();
			break;
		case 3:
			fragment = new HomeFragment();
			break;
		case 4:
			fragment = new SocialFragment();
			break;
		default:
			break;
		}
		if(fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
          fragmentManager.beginTransaction()
                  .replace(R.id.content_frame, fragment, nav_titles[position-1])
                  .addToBackStack(nav_titles[position-1])
                  .commit(); // Adding a tag here by nav_titles[pos]

          // update selected item and title, then close the drawer
          mDrawerList.setItemChecked(stuff_position, true);
          mDrawerList.setSelection(stuff_position);
          mDrawerLayout.closeDrawer(mDrawerHolderLayout);
          actionbar.setTitle(nav_titles[position-1]);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Made by a developer who thinks he is cool. Follow me @advait1911. Icon made by Michael Flarup.",  Toast.LENGTH_LONG).show();
		}
	}
	@Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      mDrawerToggle.onConfigurationChanged(newConfig);
  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        }
	        else if(item.getItemId() == R.id.share)
	        {
	        	doShare();
	        }
	        // Handle your other action bar items...

	        return super.onOptionsItemSelected(item);
	}
	private void doShare() {
		Intent shareint = new Intent(Intent.ACTION_SEND);
		shareint.setType("text/plain");
		shareint.putExtra(Intent.EXTRA_TEXT, "Try out this app, https://play.google.com/store/apps/details?id=com.advait.s.emokylo&hl=en");
		startActivity(shareint);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void replaceFragment(int fragmentNo) {
		nav_titles = getResources().getStringArray(R.array.nav_drawer_items);
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(nav_titles[fragmentNo]);
		if(fragment == null)
		{
			Log.e("nullfragment", "true");
		}
           FragmentManager fragmentManager = getSupportFragmentManager();
           fragmentManager.beginTransaction() 
                          .replace(R.id.content_frame, fragment)
                          .commit(); 
	}
}
