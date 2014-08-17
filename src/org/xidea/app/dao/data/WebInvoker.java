package org.xidea.app.dao.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

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
				if (message.startsWith("js-invoke://")) {
					// TODO: 解析通讯协议，获得要调用的对象的方法和调用参数，完成调用厚返回数据
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
