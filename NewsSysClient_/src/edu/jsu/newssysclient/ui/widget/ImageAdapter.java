/*
 * Copyright (C) 2011 Patrik ï¿½kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jsu.newssysclient.ui.widget;

import edu.jsu.newssysclient.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Í¼Æ¬ÊÓÆµÆ÷
 * @author zuo
 *
 */
public class ImageAdapter extends BaseAdapter {
	Context mContext;

	private LayoutInflater mInflater;
	private static final int[] ids = { R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4 };
	/*private static final int[] ids = { R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
			R.drawable.gingerbread, R.drawable.honeycomb, R.drawable.icecream };*/

	public ImageAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.image_item, null);
		}
		((ImageView) convertView.findViewById(R.id.imgView)).setImageResource(ids[position%ids.length]);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "nihaoya", 0).show();
			}
		});
		return convertView;
	}

}
