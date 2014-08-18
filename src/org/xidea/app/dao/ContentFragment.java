package org.xidea.app.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import android.widget.TextView;

public class ContentFragment extends Fragment {
	public static final String CHAPTER_INDEX = "chapter_index";
	private int chapterIndex;
	private CharSequence chapterTitle;
	MediaHelper media = new MediaHelper();

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ContentFragment newInstance(int sectionNumber,
			String sectionTitle) {
		ContentFragment fragment = new ContentFragment();
		Bundle args = new Bundle();
		args.putInt(CHAPTER_INDEX, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public void changeTo(int chapterIndex) {
		this.chapterIndex = chapterIndex;
		getArguments().putInt(CHAPTER_INDEX, chapterIndex);

		View contentView = this.getView().findViewById(R.id.content);
		WebView webview = (WebView) contentView;

		String body = loadContent(chapterIndex);
		media.setChapterIndex(chapterIndex);
		try {
			webview.loadUrl("javascript:document.body.innerHTML=decodeURIComponent('"+URLEncoder.encode(body, "UTF-8")+"'.replace(/\\+/g,' '))");
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	public ContentFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.chapterIndex = getArguments().getInt(CHAPTER_INDEX, -1);
		if (this.chapterIndex >= 0) {
			this.chapterTitle = Main.instance.getOutline().get(chapterIndex);
		} else {
			// TODO:
			// init default content
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.content_fragment, container,
				false);
		String body = loadContent(chapterIndex);
		
		this.getActivity().setTitle(chapterTitle);

		View contentView = rootView.findViewById(R.id.content);
		WebView webview = (WebView) contentView;
		WebInvoker.setup(webview);
		// file:///android_asset/
		String html = "<html><head><script src='file:///android_asset/html/init.js'></script>"
						+ "<link rel='stylesheet' type='text/css' href='file:///android_asset/html/default.css' /></head>"
						+ "<body style='font-size:20px'>" + body + "</body></html>";
		webview.loadDataWithBaseURL("file:///android_asset/html/", html,
				"text/html", "utf-8", null);
		
		media.setChapterIndex(chapterIndex);
		return rootView;
	}

	private static String loadContent(int chapterIndex) {
		Map<String, String> content = Main.instance.getChapterContent(chapterIndex);
		StringBuilder buf = new StringBuilder();
		for (Map.Entry<String, String> entry : content.entrySet()) {
			if (buf.length() == 0) {
				buf.append("<section id='original'>"
						+ genChapter(entry.getKey(), entry.getValue())
						+ "</section>");
			} else if (!ConfigController.isSourceOnly()) {
				buf.append("<section>"
						+ genChapter(entry.getKey(), entry.getValue())
						+ "</section>");
			}
		}

		return buf.toString();

	}

	private static String genChapter(String title, String value) {
		value = value.replace("\n", "<br/>");
		return "<em>" + title + "</em>\n<p>" + value + "</p>\n";
	}

	private MediaPlayer.OnSeekCompleteListener seekToPlay = new MediaPlayer.OnSeekCompleteListener() {
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			System.out.println("seek:" + mp.getCurrentPosition());
			mp.start();
		}
	};
	
	public void onPause() {
		super.onPause();
		this.media.pause();
	}

	public void onResume() {
		super.onResume();
		this.media.resume();
	}


}
