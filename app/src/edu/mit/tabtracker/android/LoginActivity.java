/**
 * 
 */
package edu.mit.tabtracker.android;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

/**
 * @author
 *
 */
public class LoginActivity extends Activity {
	
	public Facebook facebook;
	private static final String[] PERMISSIONS = new String[] {"read_friendlists", "email"};
	private static final String API_KEY = "213656698681478";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Session session = Session.restore(this);
        if (session == null) {
        	facebook = new Facebook(API_KEY);
        	facebook.authorize(this, PERMISSIONS, new AppLoginListener(facebook));
        } else {
        	startActivity(new Intent(this, UsersActivity.class));
        }
    }
	
	private class AppLoginListener implements DialogListener {
		
		private Facebook fb;

        public AppLoginListener(Facebook fb) {
            this.fb = fb;
        }

        public void onCancel() {
            Log.d("app", "login canceled");
        }

        public void onComplete(Bundle values) {
            new AsyncFacebookRunner(fb).request("me", 
            	new AsyncRequestListener() {
                	public void onComplete(JSONObject obj, final Object state) {
                		// save the session data
                		String uid = obj.optString("id");
                		String name = obj.optString("name");
                		new Session(fb, uid, name).save(LoginActivity.this);
                	}
            	}, null);
            
            new AsyncFacebookRunner(fb).request("me/friends", 
                	new AsyncRequestListener() {
                    	public void onComplete(JSONObject dataObj, final Object state) {
							JSONArray friends = dataObj.optJSONArray("data");
							String[] friendNames = new String[friends.length()];
							Integer[] friendIds = new Integer[friends.length()];
							for (int i=0; i<friends.length(); i++) {
							    friendNames[i] = friends.optJSONObject(i).optString("name");
							    friendIds[i] = friends.optJSONObject(i).optInt("id");
							}
							FriendsListSingleton.getInstance().store(friendIds, friendNames);
                    	}
                	}, null);
        }

        public void onError(DialogError e) {
            Log.d("app", "dialog error: " + e);               
        }

        public void onFacebookError(FacebookError e) {
            Log.d("app", "facebook error: " + e);
        }
		
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		facebook.authorizeCallback(requestCode, resultCode, data);
		
		super.onActivityResult(requestCode, resultCode, data);
        
		startActivity(new Intent(this, UsersActivity.class));
    }
	
}
