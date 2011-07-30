package edu.mit.tabtracker.android;

abstract class AppSessionListener {
	
	public abstract void onFetchFriendsComplete(int[] friendIds, String[] names);
	
}
