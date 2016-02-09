package com.advait.s.emokylo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
	
	List<twitter4j.Status> statuses;
	//private RecyclerView recList;
	private ProgressBar progressbar;
	private TextView progressbartxt;
	private Button saythistweet;
	private ViewPager viewPager;
	private MyPagerAdapter myPagerAdapter;
	//private RecyclerView.Adapter<TweetsAdapter.CustomViewHolder> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		//recList = (RecyclerView) rootView.findViewById(R.id.cardList);
	    //recList.setHasFixedSize(false);
	    progressbar = (ProgressBar) rootView.findViewById(R.id.spinner);
        progressbartxt  = (TextView) rootView.findViewById(R.id.spinnertxt);
        saythistweet = (Button) rootView.findViewById(R.id.saytweet);
        saythistweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(statuses != null && statuses.size() > 0)
				{
				int posi = viewPager.getCurrentItem(); // 0,1,2..
				MainActivity.myBundle.putString("tweet", statuses.get(posi).getText());
				FragmentChangeListener fc=(FragmentChangeListener)getActivity();
	            fc.replaceFragment(0);
				}
				else
				{
				Toast.makeText(getActivity().getApplicationContext(), "Hold on. The tweets aren\'t ready yet.", Toast.LENGTH_LONG).show();
				}
			}
		});
        progressbar.setVisibility(View.VISIBLE);
        progressbartxt.setVisibility(View.VISIBLE);
        //RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
	    //recList.setLayoutManager(llm);
        viewPager = (ViewPager) rootView.findViewById(R.id.myviewpager);
        if(InternetAvail.getInstance(getActivity().getApplicationContext()).isOnline())
	    {
		new DownloadTwitterTask().execute("KyloR3n");
	    }
	    else
	    {
	    progressbar.setVisibility(View.INVISIBLE);
		progressbartxt.setText("No internet");
	    }
        return rootView;
	}
	// Uses an AsyncTask to download a Twitter user's timeline
	private class DownloadTwitterTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;
			ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true)
	                .setOAuthConsumerKey("vJ8iwKeQC5olk5CUS3TLYlOqo")
	                .setOAuthConsumerSecret(
	                        "JNCxZfqUqUBuXy613KowAuBpLZrMjiWfY0XyIXOCpJmGxmPtIQ")
	                .setOAuthAccessToken(
	                        "127832518-Th8Q7E4F7R5dApQey4OEedxAIeRXIYJ1jHGnmojg")
	                .setOAuthAccessTokenSecret(
	                        "rGqTxb9xZcVvStxgsWFjc3V4QqyaVm9HOxwTSyUnSSwyz");
	        TwitterFactory tf = new TwitterFactory(cb.build());
	        Twitter twitter = tf.getInstance();
	        Paging p = new Paging();
	        p.setCount(30);
	        try {
	            String user;
	            user = "KyloR3n";
	            statuses = twitter.getUserTimeline(user, p);

	        } catch (TwitterException te) {
	            te.printStackTrace();
	        }
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			progressbar.setVisibility(View.GONE);
			progressbartxt.setVisibility(View.GONE);
			myPagerAdapter = new MyPagerAdapter(getActivity().getApplicationContext(), statuses);
	        viewPager.setAdapter(myPagerAdapter);
			//mAdapter = new TweetsAdapter(getActivity().getApplicationContext(), statuses); // RecyclerView.Adapter
    		//recList.setAdapter(mAdapter);
		}
	}
	private class MyPagerAdapter extends PagerAdapter{
		  
		  int NumberOfPages = 30;
		  private List<twitter4j.Status> status;
		  private Context c;

		  @Override
		  public int getCount() {
		   return NumberOfPages;
		  }
		  public MyPagerAdapter(Context cnx, List<twitter4j.Status> statuses) {
			  c = cnx;
			  status = statuses;
			  NumberOfPages = statuses.size();
		}
		  @Override
		  public boolean isViewFromObject(View view, Object object) {
		   return view == object;
		  }

		  @Override
		  public Object instantiateItem(ViewGroup container, int position) {
		   
			  LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      View v = (View) inflater.inflate(R.layout.tweets_card_layout, null);
		      Typeface type = Typeface.createFromAsset(c.getAssets(),"bold.ttf");
	          ImageView profilepic = (ImageView) v.findViewById(R.id.profilepic);
	          TextView name = (TextView) v.findViewById(R.id.name);
	          TextView username = (TextView) v.findViewById(R.id.username);
	          TextView tweet = (TextView) v.findViewById(R.id.tweet);
	          TextView datetime = (TextView) v.findViewById(R.id.datetime);
	          tweet.setMovementMethod(new ScrollingMovementMethod());
	            
	            name.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String url = "https://twitter.com/kylor3n?lang=en";
			   			Intent i = new Intent(Intent.ACTION_VIEW);
			   			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   			i.setData(Uri.parse(url));
			   			v.getContext().startActivity(i);
					}
				});
	            username.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String url = "https://twitter.com/kylor3n?lang=en";
			   			Intent i = new Intent(Intent.ACTION_VIEW);
			   			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   			i.setData(Uri.parse(url));
			   			v.getContext().startActivity(i);
					}
				});
	            profilepic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String url = "https://twitter.com/kylor3n?lang=en";
			   			Intent i = new Intent(Intent.ACTION_VIEW);
			   			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   			i.setData(Uri.parse(url));
			   			v.getContext().startActivity(i);
					}
				});
	            name.setTypeface(type);
		    	Picasso.with(c).load(status.get(position).getUser().getBiggerProfileImageURL())
				.into(profilepic);
		    	name.setText(status.get(position).getUser().getName());
		    	username.setText("@"+status.get(position).getUser().getScreenName());
		    	tweet.setText(status.get(position).getText());
		    	SimpleDateFormat df = new SimpleDateFormat("hh:mm aa - dd MMM yyy", Locale.ENGLISH);
		    	String reportDate = df.format(status.get(position).getCreatedAt());
		    	datetime.setText(reportDate);
		      container.addView(v);
		      return v;
		  }

		  @Override
		  public void destroyItem(ViewGroup container, int position, Object object) {
		   container.removeView((RelativeLayout)object);
		  }

		 }
}
