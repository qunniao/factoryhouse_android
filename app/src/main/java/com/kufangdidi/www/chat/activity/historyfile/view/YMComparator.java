package com.kufangdidi.www.chat.activity.historyfile.view;


import com.kufangdidi.www.chat.entity.FileItem;

import java.util.Comparator;

public class YMComparator implements Comparator<FileItem> {

	@Override
	public int compare(FileItem o1, FileItem o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}
