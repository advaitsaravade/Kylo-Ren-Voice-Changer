package com.advait.s.emokylo;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryFragment extends Fragment {
	
	private RecyclerView recList;
	private TextView progressbartxt;
	private RecyclerView.Adapter<HistoryAdapter.CustomViewHolder> mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container, false);
		recList = (RecyclerView) rootView.findViewById(R.id.cardList);
	    recList.setHasFixedSize(false);
        progressbartxt  = (TextView) rootView.findViewById(R.id.spinnertxt);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren";
    	File f = new File(path);
    	if(f != null)
    	{
    	File file[] = f.listFiles();
    	if(file == null)
    	{
    		progressbartxt.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		progressbartxt.setVisibility(View.GONE);
    	}
    	}
    	else
    	{
    		progressbartxt.setVisibility(View.VISIBLE);
    	}
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
	    recList.setLayoutManager(llm);
	    mAdapter = new HistoryAdapter(getActivity().getApplicationContext()); // RecyclerView.Adapter
		recList.setAdapter(mAdapter);
        return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(recList != null && mAdapter != null)
		{
			mAdapter.notifyDataSetChanged();
		}
	}
}
