package com.advait.s.emokylo;

import java.text.SimpleDateFormat;
import java.util.List;

import twitter4j.Status;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.CustomViewHolder> {

	private Context c;
	private int length = 0;
	private List<Status> status;
	
	@Override
    public int getItemCount() {
          return length;
    }
    public TweetsAdapter(Context cnx, List<Status> statuses) {
    	length = statuses.size();
    	c = cnx;
    	status = statuses;
    }
    @Override
    public void onBindViewHolder(final CustomViewHolder viewHolder, int position) {
    	/*WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x;
    	int height = size.y;
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
    	viewHolder.cardview.getLayoutParams();
    	params.width = width;
    	viewHolder.cardview.setLayoutParams(params);*/
    	Typeface type = Typeface.createFromAsset(c.getAssets(),"bold.ttf");
    	viewHolder.name.setTypeface(type);
    	Picasso.with(c).load(status.get(position).getUser().getBiggerProfileImageURL())
		.into(viewHolder.profilepic);
    	viewHolder.name.setText(status.get(position).getUser().getName());
    	viewHolder.username.setText("@"+status.get(position).getUser().getScreenName());
    	viewHolder.tweet.setText(status.get(position).getText());
    	SimpleDateFormat df = new SimpleDateFormat("hh:mm aa - dd MMM yyy");
    	String reportDate = df.format(status.get(position).getCreatedAt());
    	viewHolder.datetime.setText(reportDate);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
 	   View itemView = null;
         itemView = LayoutInflater.
                     from(viewGroup.getContext()).
                     inflate(R.layout.tweets_card_layout, viewGroup, false);
         WindowManager windowManager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
         int width = windowManager.getDefaultDisplay().getWidth();
         itemView.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.MATCH_PARENT));
         return new CustomViewHolder(itemView);
    }
    public static class CustomViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    	
    public CardView cardview;
   	 public ImageView profilepic;
   	 public TextView name;
   	 public TextView username;
   	 public TextView tweet;
   	 public TextView datetime;

       public CustomViewHolder(View v) {
            super(v); 
            cardview = (CardView) v.findViewById(R.id.card_view);
            profilepic = (ImageView) v.findViewById(R.id.profilepic);
            name = (TextView) v.findViewById(R.id.name);
            username = (TextView) v.findViewById(R.id.username);
            tweet = (TextView) v.findViewById(R.id.tweet);
            datetime = (TextView) v.findViewById(R.id.datetime);
            tweet.setMovementMethod(new ScrollingMovementMethod());
            
            name.setOnClickListener(this);
            username.setOnClickListener(this);
            profilepic.setOnClickListener(this);
        }

   	@Override
   	public void onClick(View v) {
   		int id = v.getId();
   		if(id == R.id.profilepic || id == R.id.username || id == R.id.name)
   		{
   			String url = "https://twitter.com/kylor3n?lang=en";
   			Intent i = new Intent(Intent.ACTION_VIEW);
   			i.setData(Uri.parse(url));
   			v.getContext().startActivity(i);
   		}
   	}
   }
}