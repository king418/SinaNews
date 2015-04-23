package com.king.utils;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * Basic LRU Memory cache.
 * 
 * @author Trey Robinson
 *
 */
public class MemoryCache extends LruCache<String, Bitmap> implements ImageCache{
	
	private final String TAG = this.getClass().getSimpleName();
	
	public MemoryCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
}
