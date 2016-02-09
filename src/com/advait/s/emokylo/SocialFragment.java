package com.advait.s.emokylo;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocialFragment extends Fragment {
	
	private RecyclerView recList;
	private ProgressBar progressbar;
	private TextView progressbartxt;
	List<twitter4j.Status> statuses = null;
	private RecyclerView.Adapter<SocialTweetsAdapter.CustomViewHolder> mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_social, container, false);
		recList = (RecyclerView) rootView.findViewById(R.id.cardList);
	    recList.setHasFixedSize(false);
	    progressbar = (ProgressBar) rootView.findViewById(R.id.spinner);
        progressbartxt  = (TextView) rootView.findViewById(R.id.spinnertxt);
        progressbar.setVisibility(View.VISIBLE);
        progressbartxt.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	    recList.setLayoutManager(llm);
	    if(InternetAvail.getInstance(getActivity().getApplicationContext()).isOnline())
	    {
		new DownloadSocialTwitterTask().execute("#kylorenvoiceapp");
	    }
	    else
	    {
	    progressbar.setVisibility(View.INVISIBLE);
		progressbartxt.setText("No internet");
	    }
        return rootView;
	}
	// Uses an AsyncTask to download a Twitter user's timeline
		private class DownloadSocialTwitterTask extends AsyncTask<String, Void, String> {

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
		        Query query = new Query("#kylorenvoiceapp");
		        query.setCount(40);
		        try {
		            QueryResult result3 = twitter.search(query);
		            statuses = result3.getTweets();

		        } catch (TwitterException te) {
		            te.printStackTrace();
		        }
		        if(statuses == null || statuses.size() < 1)
		        {
		        	Query query2 = new Query("#kyloren");
			        query2.setCount(40);
			        try {
			            QueryResult result3 = twitter.search(query2);
			            statuses = result3.getTweets();

			        } catch (TwitterException te) {
			            te.printStackTrace();
			        }
		        }
				return result;
			}
			@Override
			protected void onPostExecute(String result) {
				progressbar.setVisibility(View.GONE);
				progressbartxt.setVisibility(View.GONE);
				mAdapter = new SocialTweetsAdapter(getActivity().getApplicationContext(), statuses); // RecyclerView.Adapter
	    		recList.setAdapter(mAdapter);
			}
		}
}
