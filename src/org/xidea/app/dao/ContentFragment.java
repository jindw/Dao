package org.xidea.app.dao;

import java.util.List;
import java.util.Map;

import org.xidea.app.dao.data.ContentHelper;
import org.xidea.app.dao.data.WebInvoker;

import dalvik.system.VMRuntime;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;

public class ContentFragment extends Fragment {
	public static final String CHAPTER_INDEX = "chapter_index";
	public static final String CHAPTER_TITLE = "chapter_title";
	private static final List<Map<String, String>> result = new ContentHelper().readDefaultContent();
	private int chapterIndex;
	private CharSequence chapterTitle;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ContentFragment newInstance(int sectionNumber,String sectionTitle) {
		ContentFragment fragment = new ContentFragment();
		Bundle args = new Bundle();
		args.putInt(CHAPTER_INDEX, sectionNumber);
		args.putString(CHAPTER_TITLE, sectionTitle);
		fragment.setArguments(args);
		return fragment;
	}
	public ContentFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.chapterIndex = getArguments().getInt(CHAPTER_INDEX, 0);
		this.chapterTitle = getArguments().getString(CHAPTER_TITLE);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.content_fragment,
				container, false);
		Map<String, String> content = result.get(chapterIndex);
		StringBuilder buf = new StringBuilder();
		for(Map.Entry<String, String> entry : content.entrySet()){
			boolean isFirst = buf.length()==0;
			buf.append(isFirst?"<section id='original'>":"<section>");
			String value = entry.getValue().replace("\n", "<br/>");
			buf.append("<em>").append(entry.getKey()).append("</em>\n").append("<p>").append(value).append("</p>\n");
			buf.append("</section>");
		}
		this.getActivity().setTitle(chapterTitle);

//		// Show the dummy content as text in a TextView.
		
		View contentView = rootView.findViewById(R.id.content);
		//System.out.println(android.os.SystemClock.currentThreadTimeMillis());
		//System.out.println(android.os.SystemClock.uptimeMillis());
		//System.out.println(System.currentTimeMillis()-android.os.SystemClock.currentThreadTimeMillis());
		String body = buf.toString();
		if(contentView instanceof WebView){
			WebView webview = (WebView)contentView;
			WebInvoker.setup(webview);
			//file:///android_asset/
			String html = "<html><head><script src='file:///android_asset/html/init.js'></script>" +
					"<link rel='stylesheet' type='text/css' href='file:///android_asset/html/default.css' /></head>" +
					"<body style='font-size:20px'>"+body+"</body></html>";
			webview.loadDataWithBaseURL("file:///android_asset/html/",html, "text/html", "utf-8",null);
		}else{
			((TextView) contentView).setText(Html.fromHtml(body));
		}

		initMedia(); 

		return rootView;
	}
	private MediaPlayer.OnSeekCompleteListener seekToPlay = new MediaPlayer.OnSeekCompleteListener() {
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			System.out.println("seek:"+mp.getCurrentPosition());
			//mp.start();
		}
	};
	private void initMedia() {
		Main activity = (Main)this.getActivity();
		MediaController controller = activity.mediaController;
		MediaPlayer mediaMusic = MediaPlayer.create(activity, R.raw.cp1);
		mediaMusic.setOnSeekCompleteListener(seekToPlay);
		mediaMusic.start();
		mediaMusic.pause();
		mediaMusic.seekTo(3000);
		controller.show();
		
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		//menu..getMenuInflater().inflate(R.menu.main_activity, menu);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
}
