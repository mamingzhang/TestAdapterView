package com.example.testadapterview;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MainActivity extends Activity
{
	
	private ImageWallAdapterView mCustomAdapterView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCustomAdapterView = (ImageWallAdapterView) findViewById(R.id.customAdapterView);
		mCustomAdapterView.setAdapter(mListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private ListAdapter mListAdapter = new BaseAdapter()
	{
		private int[] imageIds = new int[]{R.drawable.a, R.drawable.s, R.drawable.d, R.drawable.f,
				R.drawable.g, R.drawable.h, R.drawable.j, R.drawable.k,
				R.drawable.l, R.drawable.z, R.drawable.x};
		
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return imageIds.length;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return imageIds[position];
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			
			ViewHolder viewHolder = null;
			
			if(convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.customadapterview_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.imageView.setImageResource(imageIds[position]);
			viewHolder.textView.setText("Image"+position);
			
			return convertView;
		}
		
		
	};
	
	private static class ViewHolder 
	{
		public ImageView imageView = null;
		public TextView textView = null;
	}
}
