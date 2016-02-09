package com.advait.s.emokylo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class PostRecordingActivity extends ActionBarActivity implements OnClickListener, OnSeekBarChangeListener {
	
	String filename = "";
	boolean tweet = false;
	private ImageView heroimg;
	private ImageView playpausebut;
	private SeekBar seekbar;
	private EditText filename_edt;
	private CardView delete;
	private CardView share;
	private CardView done;
	private MediaPlayer mediaPlayer;
	private Handler mHandler = new Handler();
	private boolean seekMoving = false;
	private boolean deleted = false;
	private Switch epicmusic;
	private boolean useEpicMusic = true;
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_recording);
		Intent intent = getIntent();
		if(intent!=null)
		{
			if(intent.getStringExtra("filename") != null)
			{
				filename = intent.getStringExtra("filename");
			}
			if(intent.getStringExtra("tweet") != null && !intent.getStringExtra("tweet").equals("nil"))
			{
				filename = intent.getStringExtra("tweet");
				tweet = true;
			}
		}
		heroimg = (ImageView) findViewById(R.id.kylohero);
		playpausebut = (ImageView) findViewById(R.id.playpausebut);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		seekbar.setOnSeekBarChangeListener(this);
		filename_edt = (EditText) findViewById(R.id.filename);
		delete = (CardView) findViewById(R.id.delete);
		share = (CardView) findViewById(R.id.others_share);
		done = (CardView) findViewById(R.id.done);
		epicmusic = (Switch) findViewById(R.id.epicmusic);
		try {
			createTwoVersions();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File audio = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav");
		Uri myUri = Uri.fromFile(audio); // initialize Uri here
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), myUri);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = null;
				seekbar.setProgress(0);
				playpausebut.setImageResource(R.drawable.ic_play_arrow_white_48dp);
			}
		});
		PostRecordingActivity.this.runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
		        if(mediaPlayer != null && !seekMoving && mediaPlayer.isPlaying()){
		            int mCurrentPosition = mediaPlayer.getCurrentPosition();
		            seekbar.setProgress(mCurrentPosition);
		        }
		        mHandler.postDelayed(this, 50);
		    }
		});
		seekbar.setMax(mediaPlayer.getDuration());
		playpausebut.setOnClickListener(this);
		delete.setOnClickListener(this);
		share.setOnClickListener(this);
		done.setOnClickListener(this);
		
		filename_edt.setText(filename);
		
		Random randomGenerator = new Random();
	    int rint = randomGenerator.nextInt(8);
	    if(rint == 0)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero);
	    }
	    else if(rint == 1)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero2);
	    }
	    else if(rint == 2)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero3);
	    }
	    else if(rint == 3)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero4);
	    }
	    else if(rint == 4)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero5);
	    }
	    else if(rint == 5)
	    {
	    	heroimg.setImageResource(R.drawable.kylohero6);
	    }
	    else
	    {
	    	heroimg.setImageResource(R.drawable.kylohero7);
	    }
	    epicmusic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(mediaPlayer!=null && mediaPlayer.isPlaying())
				{
					mediaPlayer.reset();
					mediaPlayer.release();
					mediaPlayer = null;
					seekbar.setProgress(0);
					playpausebut.setImageResource(R.drawable.ic_play_arrow_white_48dp);
				}
				if(isChecked) // Add epic background music
				{
					useEpicMusic = true;
				}
				else // Remove epic background music
				{
					useEpicMusic = false;
				}
			}
		});
	}

	private void createTwoVersions() throws IOException {
		// have two versions of the same file
		// Morph second version
		File orig = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".wav");
		File morp = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav");
		copy(orig, morp);
	    InputStream is = getResources().openRawResource(R.raw.emokylotheme);
	    byte [] bytesTemp2 = fullyReadFileToBytes(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav"));
	    byte [] sample2 = convertInputStreamToByteArray(is);
	    byte[] temp2 = bytesTemp2.clone();
	    RandomAccessFile randomAccessFile2 = new RandomAccessFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav"), "rw");
	    //seek to skip 44 bytes 
	    randomAccessFile2.seek(44);
	    //Echo
	    for (int n = 0; n < bytesTemp2.length; n++) {
	       bytesTemp2[n] = (byte) ((temp2[n] + (sample2[n])));
	    } 
	    randomAccessFile2.write(bytesTemp2);
	    randomAccessFile2.close();
	}

	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);
	 
	    // Transfer bytes from in to out 
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    } 
	    in.close();
	    out.close();
	} 
	
	public byte[] convertInputStreamToByteArray(InputStream inputStream)
	 {
	 byte[] bytes= null;
	 
	 try
	 {
	 ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 
	 byte data[] = new byte[1024];
	 int count;
	 
	 while ((count = inputStream.read(data)) != -1)
	 {
	 bos.write(data, 0, count);
	 }
	 
	bos.flush();
	 bos.close();
	 inputStream.close();
	 
	bytes = bos.toByteArray();
	 }
	 catch (IOException e)
	 {
	 e.printStackTrace();
	 }
	 return bytes;
	 }
	
	byte[] fullyReadFileToBytes(File f) throws IOException {
	    int size = (int) f.length();
	    byte bytes[] = new byte[size];
	    byte tmpBuff[] = new byte[size];
	    FileInputStream fis= new FileInputStream(f);
	    try { 
	 
	        int read = fis.read(bytes, 0, size);
	        if (read < size) {
	            int remain = size - read;
	            while (remain > 0) {
	                read = fis.read(tmpBuff, 0, remain);
	                System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
	                remain -= read;
	            } 
	        } 
	    }  catch (IOException e){
	        throw e;
	    } finally { 
	        fis.close();
	    } 
	 
	    return bytes;
	} 
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.others_share)
		{
			File audio = null;
			if(useEpicMusic)
			{
			audio = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav");
			}
			else
			{
			audio = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".wav");
			}
			Intent intent = new Intent(Intent.ACTION_SEND).setType("audio/*");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(audio)); 
			startActivity(Intent.createChooser(intent, "Share your Kylo"));
			this.finish();
		}
		else if(id == R.id.delete)
		{
			deleted = true;
			Toast.makeText(getApplicationContext(), "Recording deleted!", Toast.LENGTH_SHORT).show();
			   try
			   {
			   File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav");
			   File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".wav");
			   if(file1.exists())
			   {
				   file1.delete();
			   }
			   if(file2.exists())
			   {
				   file2.delete();
			   }
			   }
			   catch(Exception e){}
			   this.finish();
		}
		else if(id == R.id.done)
		{
			onBackPressed();
		}
		else if(id == R.id.playpausebut)
		{
			// toggle playback
			if(mediaPlayer==null)
			{
				File audio = null;
				if(useEpicMusic)
				{
				audio = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+"_morphed.wav");
				}
				else
				{
				audio = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".wav");
				}
				Uri myUri = Uri.fromFile(audio); // initialize Uri here
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try {
					mediaPlayer.setDataSource(getApplicationContext(), myUri);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mediaPlayer.reset();
						mediaPlayer.release();
						mediaPlayer = null;
						seekbar.setProgress(0);
						playpausebut.setImageResource(R.drawable.ic_play_arrow_white_48dp);
					}
				});
				PostRecordingActivity.this.runOnUiThread(new Runnable() {

				    @Override
				    public void run() {
				        if(mediaPlayer != null && !seekMoving && mediaPlayer.isPlaying()){
				            int mCurrentPosition = mediaPlayer.getCurrentPosition();
				            seekbar.setProgress(mCurrentPosition);
				        }
				        mHandler.postDelayed(this, 50);
				    }
				});
				mediaPlayer.start();
				playpausebut.setImageResource(R.drawable.ic_pause_white_48dp);
			}
			else if(mediaPlayer != null && !mediaPlayer.isPlaying())
			{
				mediaPlayer.start();
				playpausebut.setImageResource(R.drawable.ic_pause_white_48dp);
			}
			else if(mediaPlayer != null && mediaPlayer.isPlaying())
			{
				mediaPlayer.pause();
				playpausebut.setImageResource(R.drawable.ic_play_arrow_white_48dp);
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(mediaPlayer!=null && fromUser)
		{
			mediaPlayer.seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		seekMoving = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		seekMoving = false;
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mediaPlayer != null && mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			playpausebut.setImageResource(R.drawable.ic_play_arrow_white_48dp);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!deleted)
		{
		Toast.makeText(getApplicationContext(), "Recording saved!", Toast.LENGTH_SHORT).show();
		File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/");
		File from = null;
		File delete = null;
		if(useEpicMusic)
		{
        from = new File(directory, filename+"_morphed.wav");
        delete = new File(directory, filename+".wav");
		}
		else
		{
		from = new File(directory, filename+".wav");
		delete = new File(directory, filename+"_morphed.wav");
		}
		delete.delete();
        File to = new File(directory, filename_edt.getText().toString()+".wav");
        from.renameTo(to);
        filename = filename_edt.getText().toString();
		}
	}
}
