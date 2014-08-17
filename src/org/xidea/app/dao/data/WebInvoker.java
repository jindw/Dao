package org.xidea.app.dao.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import org.xidea.app.dao.Main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class WebInvoker {
	@SuppressLint("NewApi")
	public static void setup(WebView webview) {
		WebSettings setting = webview.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setAllowFileAccess(true);
		setting.setAllowFileAccessFromFileURLs(true);

		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				view.setVerticalScrollBarEnabled(false);
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, final JsPromptResult result) {
				if (message!=null && message.startsWith("js-invoke://")) {
					// TODO: 解析通讯协议，获得要调用的对象的方法和调用参数，完成调用厚返回数据
					if(defaultValue != null && defaultValue .startsWith("event:fling@")){
						String json = defaultValue.substring(defaultValue.indexOf('@')+1);
						try {
							json = URLDecoder.decode(json, "UTF-8");
							String xy = json.substring(1,json.length()-1);
							int p = xy.indexOf(',');
							int x = Integer.parseInt(xy.substring(0,p));
							int y = Integer.parseInt(xy.substring(p+1));
							if(Math.abs(x)>Math.abs(y)+50){
								Main.instance.jump(x>0?1:-1);
							}
							
							
						} catch (UnsupportedEncodingException e) {
						}
					}
					result.confirm("");
					return true;
				} else {
					final android.widget.EditText input = new EditText(view
							.getContext());
					input.setText(defaultValue);
					new AlertDialog.Builder(view.getContext())
							.setTitle(message)
							.setView(input)
							// .setMessage(defaultValue)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											result.confirm(input.getText()
													.toString());
										}
									})
							.setNegativeButton(android.R.string.cancel,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											result.cancel();
										}
									}).create().show();

					return true;
				}
			}

			public boolean onConsoleMessage(ConsoleMessage msg) {
				Log.e(msg.sourceId() + "@" + msg.lineNumber(), msg.message());
				return false;
			}
		});
	}

}
