package org.xidea.app.dao.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * #....
 * #....
 * field1	field2....
 * content1field1
 * 
 * content1field2
 * 
 * .....
 * 
 * content2field1
 * 
 * content2field2
 * </pre>
 * 
 * @author jindawei
 * 
 */
public class ContentHelper {
	public static final String KEY_SOURCE = "原文";
	public static final String KEY_TRANSLATED = "译文";
	public static final String CN = "零一二三四五六七八九十";

	public static void main(String[] args) {
		ContentHelper data = new ContentHelper();

		System.out.println(data.outline);
	}

	private List<Map<String, String>> content;
	private List<String> outline;
	
	/**
	 * @return the content
	 */
	public List<Map<String, String>> getContent() {
		return content;
	}

	/**
	 * @return the outline
	 */
	public List<String> getOutline() {
		return outline;
	}

	public ContentHelper(){

		InputStream in = ContentHelper.class
				.getResourceAsStream("default.data");
		try {
			try {
				readContent(new InputStreamReader(in, "UTF-8"));
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void readContent(Reader source)
			throws IOException {
		this.outline = new ArrayList<String>();
		this.content = new ArrayList<Map<String, String>>();
		
		BufferedReader in = new BufferedReader(source);
		String line;
		int index = 0;// header
		String[] fields = null;
		Map<String, String> current = new LinkedHashMap<String, String>();

		while ((line = in.readLine()) != null) {
			if (line.length() == 0) {
				while ((line = in.readLine()).length() != 0) {
					break;
				}
				index++;
			}
			if (fields == null) {
				if (!line.startsWith("#")) {
					fields = line.split("\t");
				}
				continue;
			}
			if (line.startsWith("#")) {
				this.outline.add(toChineseChapter(outline.size()) + line.substring(1));
				index = 0;
				if(current.size()>0){
				content.add(current);
				current = new LinkedHashMap<String, String>();
				}
				continue;
			}
			String key = fields[index];
			String previewValue = current.get(key);
			current.put(key, previewValue == null ? line : previewValue + '\n'
					+ line);
		}
		if (!current.isEmpty()) {
			content.add(current);
		}
	}
//
//	private List<String> readOutline(Reader source) throws IOException {
//		BufferedReader in = new BufferedReader(source);
//		List<String> result = new ArrayList<String>(81);
//		String line;
//		int c = 1;
//		while ((line = in.readLine()) != null) {
//			StringBuilder buf = new StringBuilder();
//			buf.append(toChineseChapter(c));
//			buf.append("\t").append(line);
//			result.add(buf.toString());
//			c++;
//		}
//		return result;
//	}

	private String toChineseChapter(int c) {
		StringBuilder buf = new StringBuilder("第");
		int c1 = c / 10;
		int c2 = c % 10;
		if (c1 >= 1) {
			if (c1 > 1) {
				buf.append(CN.charAt(c1));
			}
			buf.append(CN.charAt(10));
		}
		if (c2 > 0) {
			buf.append(CN.charAt(c2));
		}
		return buf.append("章 ").toString();
	}
}
