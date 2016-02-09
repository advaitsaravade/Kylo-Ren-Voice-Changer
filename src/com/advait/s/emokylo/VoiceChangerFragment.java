package com.advait.s.emokylo;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceChangerFragment extends Fragment implements OnSeekBarChangeListener {
	
	byte[] bData = null;
	private static final int RECORDER_SAMPLERATE = 44100;
	 
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	 
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	 
	private AudioRecord recorder = null;
	 private Thread recordingThread = null;
	 private boolean isRecording = false;
	 
	 private Button button1_btn;
	 private ImageView button2_btn;
	 private ImageView button_cancel_btn;
	 private TextView rec_label;
	 private TextView introtxt;
	 private TextView header;
	 private LinearLayout buttonholder;
	 private View rv;
	 
	 private SeekBar seekBar;
	 private float progress = (float) 0.5;
	 private int waveSampling = 37000;
	 private String filename = "";

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_voicechanger, container, false);
		rv = rootView;
		SimpleDateFormat df = new SimpleDateFormat("hh-mm-ss-SSS aa - dd MMM yyy", Locale.ENGLISH);
		Date today = Calendar.getInstance().getTime();
		filename = df.format(today);
		setButtonHandlers(rootView);
		  enableButtons(false);

		  int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
		    RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
		  introtxt = (TextView) rootView.findViewById(R.id.introtext);
        return rootView;
	}
	@Override
	public void onResume() {
		String tweet = MainActivity.myBundle.getString("tweet");
		if(tweet != null)
		{
			MainActivity.myBundle.putString("tweet", null);
			if(introtxt != null)
			{
				introtxt.setText(tweet);
			}
		}
		else
		{
			introtxt.setText(getResources().getString(R.string.kylorenintro));
		}
		super.onResume();
	}
	private void setButtonHandlers(View rootView) {
		  ((Button) rootView.findViewById(R.id.button)).setOnClickListener(btnClick);
		  ((ImageView) rootView.findViewById(R.id.button2)).setOnClickListener(btnClick);
		  button1_btn = (Button) rootView.findViewById(R.id.button);
		  button2_btn = (ImageView) rootView.findViewById(R.id.button2);
		  button_cancel_btn = (ImageView) rootView.findViewById(R.id.cancelbutton);
		  button_cancel_btn.setOnClickListener(btnClick);
		  rec_label = (TextView) rootView.findViewById(R.id.recstat);
		  header = (TextView) rootView.findViewById(R.id.kyloname);
		  buttonholder= (LinearLayout) rootView.findViewById(R.id.postRecButHolder);
		  seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
		  seekBar.setOnSeekBarChangeListener(this);
		  seekBar.setMax(100);
		  seekBar.setProgress((int)Math.round(progress*100));
		  Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"starjedirounded.ttf");
		  header.setTypeface(type);
		 }

		 private void enableButton(int id, boolean isEnable) {
			 int visi = 0;
			 if(isEnable)
			 {
				 visi = View.VISIBLE;
			 }
			 else
			 {
				 visi = View.GONE;
			 }
			 if(id==R.id.postRecButHolder)
			 {
		((LinearLayout) rv.findViewById(id)).setVisibility(visi);
			 }
			 else
			 {
		  ((Button) rv.findViewById(id)).setVisibility(visi);
			 }
			 rec_label.setVisibility(visi);
		 }

		 private void enableButtons(boolean isRecording) {
		  enableButton(R.id.button, !isRecording);
		  enableButton(R.id.postRecButHolder, isRecording);
		 }

		 int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
		 int BytesPerElement = 2; // 2 bytes in 16bit format

		 private void startRecording() {

		  recorder = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER,
		    RECORDER_SAMPLERATE, RECORDER_CHANNELS,
		    RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

		  recorder.startRecording();
		   
		  isRecording = true;

		  recordingThread = new Thread(new Runnable() {

		   public void run() {

		    writeAudioDataToFile();

		   }
		  }, "AudioRecorder Thread");
		  recordingThread.start();
		 }

		        //Conversion of short to byte
		 private byte[] short2byte(short[] sData) {
		  int shortArrsize = sData.length;
		  byte[] bytes = new byte[shortArrsize * 2];

		  for (int i = 0; i < shortArrsize; i++) {
		   bytes[i * 2] = (byte) (sData[i] & 0x00FF);
		   bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
		   sData[i] = 0;
		  }
		  return bytes;
		 }

		 private void writeAudioDataToFile() {
		  // Write the output audio in byte
			 File file_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/");
		        if (!file_folder.exists())
		        {
		            file_folder.mkdirs();
		            File nomediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/.nomedia");
		            try {
						nomediaFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        if(filename.equals(""))
		        {
		        	SimpleDateFormat df = new SimpleDateFormat("hh-mm-ss-SSS aa - dd MMM yyy", Locale.ENGLISH);
		    		Date today = Calendar.getInstance().getTime();
		    		filename = df.format(today);
		        }
		  String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".pcm";
		  
		                short sData[] = new short[BufferElements2Rec];

		  FileOutputStream os = null;
		  try {
		   os = new FileOutputStream(filePath);
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		  }

		  while (isRecording) {
		   // gets the voice output from microphone to byte format
		   recorder.read(sData, 0, BufferElements2Rec);
		   System.out.println("Short wirting to file" + sData.toString());
		   try {
		    // writes the data to file from buffer stores the voice buffer
		    bData = short2byte(sData);

		    os.write(bData, 0, BufferElements2Rec * BytesPerElement);

		   } catch (IOException e) {
		    e.printStackTrace();
		   }
		  }

		  try {
		   os.close();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  finally
		  {
			  File f1 = new File(filePath);
			  File f2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".wav");
			  try {
				rawToWave(f1, f2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		 }
		 private void rawToWave(final File rawFile, final File waveFile) throws IOException {

			    byte[] rawData = new byte[(int) rawFile.length()];
			    DataInputStream input = null;
			    try {
			        input = new DataInputStream(new FileInputStream(rawFile));
			        input.read(rawData);
			    } finally {
			        if (input != null) {
			            input.close();
			        }
			    }

			    DataOutputStream output = null;
			    try {
			        output = new DataOutputStream(new FileOutputStream(waveFile));
			        // WAVE header
			        // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
			        writeString(output, "RIFF"); // chunk id
			        writeInt(output, 36 + rawData.length); // chunk size
			        writeString(output, "WAVE"); // format
			        writeString(output, "fmt "); // subchunk 1 id
			        writeInt(output, 16); // subchunk 1 size
			        writeShort(output, (short) 1); // audio format (1 = PCM)
			        writeShort(output, (short) 1); // number of channels
			        writeInt(output, waveSampling); // sample rate
			        writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
			        writeShort(output, (short) 2); // block align
			        writeShort(output, (short) 16); // bits per sample
			        writeString(output, "data"); // subchunk 2 id
			        writeInt(output, rawData.length); // subchunk 2 size
			        // Audio data (conversion big endian -> little endian)
			        short[] shorts = new short[rawData.length / 2];
			        ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
			        ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
			        for (short s : shorts) {
			            bytes.putShort(s);
			        }
			        output.write(fullyReadFileToBytes(rawFile));
			    } finally {
			        if (output != null) {
			            output.close();
			        }
			    }
			    // Adding echo
			    //Clone original Bytes 
			    byte [] bytesTemp = fullyReadFileToBytes(rawFile);
			    byte[] temp = bytesTemp.clone();
			    RandomAccessFile randomAccessFile = new RandomAccessFile(waveFile, "rw");
			    //seek to skip 44 bytes 
			    randomAccessFile.seek(44);
			    //Echo 
			    int N = RECORDER_SAMPLERATE / 8;
			    for (int n = N + 1; n < bytesTemp.length; n++) {
			       bytesTemp[n] = (byte) (temp[n] + .3 * temp[n - N]);
			    }
			    randomAccessFile.write(bytesTemp);
			    randomAccessFile.close();
			    rawFile.delete();
			    Intent intent = new Intent();
		        String packageName = "com.advait.s.emokylo";
		        String className = "com.advait.s.emokylo.PostRecordingActivity";
		        intent.setClassName(packageName, className);
		        intent.putExtra("tweet", "nil");
		        intent.putExtra("filename", filename);
		        getActivity().startActivity(intent);
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
			private void writeInt(final DataOutputStream output, final int value) throws IOException {
			    output.write(value >> 0);
			    output.write(value >> 8);
			    output.write(value >> 16);
			    output.write(value >> 24);
			}

			private void writeShort(final DataOutputStream output, final short value) throws IOException {
			    output.write(value >> 0);
			    output.write(value >> 8);
			}

			private void writeString(final DataOutputStream output, final String value) throws IOException {
			    for (int i = 0; i < value.length(); i++) {
			        output.write(value.charAt(i));
			    }
			}

		 private void stopRecording() {
		  // stops the recording activity
		  if (null != recorder) {
		   isRecording = false;

		  
		   recorder.stop();
		   recorder.release();

		   recorder = null;
		   recordingThread = null;
		  }
		 }

		 private View.OnClickListener btnClick = new View.OnClickListener() {
		  public void onClick(View v) {
		   switch (v.getId()) {
		   case R.id.button: {
		    enableButtons(true);
		    startRecording();
		    break;
		   }
		   case R.id.button2: {
		    enableButtons(false);
		    stopRecording();
		    break;
		   }
		   case R.id.cancelbutton: {
			   enableButtons(false);
			   stopRecording();
			   try
			   {
			   File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kylo Ren/"+filename+".pcm");
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
			   catch(Exception e)
			   {
				   
			   }
			   Toast.makeText(getContext(), "Kylo Ren morphing, cancelled.", Toast.LENGTH_LONG).show();
			   break;
		   }
		   }
		  }
		 };

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if(fromUser)
			{
				if(progress > 50)
				{
				waveSampling = 37000 + ((progress-50)*40);
				}
				else
				{
				waveSampling = 37000 - ((50-progress)*40);
				}
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
}