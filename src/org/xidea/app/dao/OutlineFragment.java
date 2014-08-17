//package org.xidea.app.dao;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.ListFragment;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import org.xidea.app.dao.data.ContentHelper;
//
//public class OutlineFragment extends ListFragment {
//	private static final String STATE_SELECTED_CHAPTER_POSITION = "selected_chapter_position";
//
//	private ChapterSelectedListener callbacks;
//
//	private int selectedPosition = ListView.INVALID_POSITION;
//
//	private List<String> outline=new ContentHelper().readDefaultOutline();
//
//	public interface ChapterSelectedListener {
//		public void onChapterSelected(int id,String title);
//	}
//
//	public OutlineFragment() {
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setListAdapter(new ArrayAdapter<String>(getActivity(),
//				//R.layout.outline_item,
//				android.R.layout.simple_list_item_activated_2
//				,
//				android.R.id.text1, outline));
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		if (savedInstanceState != null
//				&& savedInstanceState.containsKey(STATE_SELECTED_CHAPTER_POSITION)) {
//			setActivatedPosition(savedInstanceState
//					.getInt(STATE_SELECTED_CHAPTER_POSITION));
//		}
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		if (!(activity instanceof ChapterSelectedListener)) {
//			throw new IllegalStateException(
//					"Activity must implement fragment's callbacks.");
//		}
//		callbacks = (ChapterSelectedListener) activity;
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		callbacks = null;
//	}
//
//	@Override
//	public void onListItemClick(ListView listView, View view, int position,
//			long id) {
//		super.onListItemClick(listView, view, position, id);
//		if(callbacks!=null){
//			callbacks.onChapterSelected(position,outline.get(position));
//		}
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		if (selectedPosition != ListView.INVALID_POSITION) {
//			outState.putInt(STATE_SELECTED_CHAPTER_POSITION, selectedPosition);
//		}
//	}
//	
//
//	public void setActivateOnItemClick(boolean activateOnItemClick) {
//		getListView().setChoiceMode(
//				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
//						: ListView.CHOICE_MODE_NONE);
//	}
//
//	private void setActivatedPosition(int position) {
//		if (position == ListView.INVALID_POSITION) {
//			getListView().setItemChecked(selectedPosition, false);
//		} else {
//			getListView().setItemChecked(position, true);
//		}
//
//		selectedPosition = position;
//	}
//}
