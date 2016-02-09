package com.advait.s.emokylo;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> {

	private Context c;
	private int length = 0;
	private String filename = "hello";
	private String[] filenames;
	
	@Override
    public int getItemCount() {
          return length;
    }
    public HistoryAdapter(Context cnx) {
    	int itemNo = 0;
    	c = cnx;
    	String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren";
    	File f = new File(path);   
    	if(f != null)
    	{
    	File file[] = f.listFiles();
    	if(file != null)
    	{
    	filenames = new String[file.length];
    	for (int i=0; i < file.length; i++)
    	{
    		if(!file[i].getName().contains(".nomedia"))
    		{
    			filenames[itemNo] = file[i].getName().substring(0, file[i].getName().lastIndexOf('.'));
    			itemNo++;
    		}
    	}
    	length = itemNo;
    	}
    	else
    	{
    		length = 0;
    	}
    	}
    	else
    	{
    		length = 0;
    	}
	}
    @Override
    public void onBindViewHolder(final CustomViewHolder viewHolder, int position) {
    	viewHolder.holder.setTag(filenames[position]);
    	viewHolder.filename.setText(filenames[position]);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
 	   View itemView = null;
         itemView = LayoutInflater.
                     from(viewGroup.getContext()).
                     inflate(R.layout.history_list, viewGroup, false);
         return new CustomViewHolder(itemView);
    }
 	public static class CustomViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
 		
 		 public TextView filename;
 		 public RelativeLayout holder;

 	     public CustomViewHolder(View v) {
 	          super(v);
 	          holder = (RelativeLayout) v.findViewById(R.id.holder);
 	          filename = (TextView) v.findViewById(R.id.filename);
 	          holder.setOnClickListener(this);
 	      }

 		@Override
 		public void onClick(View v) {
 			int id = v.getId();
 			if(id == R.id.holder)
 			{
 				Intent intent = new Intent();
 		        String packageName = "com.advait.s.emokylo";
 		        String className = "com.advait.s.emokylo.PostRecordingActivity";
 		        intent.setClassName(packageName, className);
 		        intent.setClassName(packageName, className);
		        intent.putExtra("tweet", "nil");
		        intent.putExtra("filename", (String)v.getTag());
 		        v.getContext().startActivity(intent);
 			}
 		}
 	 }
}
