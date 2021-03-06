package com.leochin.proxydecryptvediostream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private MediaPlayer mPlayer;
	private LocalProxy proxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		proxy = new LocalProxy();
		proxy.init();
		proxy.start();

		initMediaPlayer();
		
	}
	
	private void initMediaPlayer(){
		
		//Uri uri = Uri.parse("http://172.20.223.172/letitgo.mp3");
		String str = proxy.getUrl("Music/letitgo.mp3");
		Uri uri = Uri.parse(str);
		mPlayer = MediaPlayer.create(this, uri);
	}

	/**
	 * 播放音乐
	 * 
	 * @param view
	 */
	public void play(View view){
		
		mPlayer.start();
	}
	
	public void stop(View view){
		
		mPlayer.stop();
	}
	
	/**
	 * 加密文件测试
	 * @param view
	 */
	public void encrypt(View view){
		
		String rootPath = Environment.getExternalStorageDirectory().toString();
		String filePath = rootPath + "/sky.mp3";
		File inputFile = new File(filePath);
		File outputFile = new File(rootPath +"/s.mp3");
		
		if(inputFile.exists()){
			try {
				FileInputStream fis = new FileInputStream(inputFile);
				FileOutputStream fos = new FileOutputStream(outputFile);				
				
				byte[] buffer = new byte[5*1024];
				int len = 0;
				while((len = fis.read(buffer)) > 0){
					
					byte[] byteArray = Base64.encode(buffer).getBytes();
					
					Log.d("wenhao","write size = " + byteArray.length);
					fos.write(byteArray);
				}
				
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Toast.makeText(this, "encrypt file successful...", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "This is no encryt file...", Toast.LENGTH_LONG).show();
		}
		
	}
	
	/**
	 * 
	 * 解密文件测试
	 * 
	 * @param view
	 */
	public void decrypt(View view){
		
		String rootPath = Environment.getExternalStorageDirectory().toString();
		File inputFile = new File(rootPath + "/s.mp3");
		File outputFile = new File(rootPath + "/sky2.mp3");
		
		if(inputFile.exists()){
			try {
				FileInputStream fis = new FileInputStream(inputFile);
				FileOutputStream fos = new FileOutputStream(outputFile);				
				
				byte[] buffer = new byte[6828];
				int len = 0;
				while((len = fis.read(buffer)) > 0){
					
					byte[] byteArray = Base64.decode(new String(buffer,0,len));
					Log.d("wenhao",fos +":"+byteArray.length);
					fos.write(byteArray);
					//fos.write(Base64.encode(buffer).getBytes());
				}
				
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Toast.makeText(this, "decrypt file successful...", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "This is no decryt file...", Toast.LENGTH_LONG).show();
		}
		
	}

}
