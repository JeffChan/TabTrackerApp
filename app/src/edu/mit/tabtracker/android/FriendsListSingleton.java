package edu.mit.tabtracker.android;

public class FriendsListSingleton {
	private static final FriendsListSingleton instance = new FriendsListSingleton();
	private int[] friendIds;
	private String[] friendNames;
	 
    // Private constructor prevents instantiation from other classes
    private FriendsListSingleton() {
    	
    }
 
    public static FriendsListSingleton getInstance() {
        return instance;
    }
    
    public void store(int[] ids, String[] names) {
    	friendIds = ids;
    	friendNames = names;
    }
    
    public int[] getFriendIds() {
    	return friendIds;
    }
    
    public String[] getFriendNames() {
    	return friendNames;
    }
}
