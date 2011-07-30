package edu.mit.tabtracker.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

abstract class AsyncRequestListener implements RequestListener {

    public void onComplete(String response, final Object state) {
        try {
            JSONObject obj = Util.parseJson(response);
            onComplete(obj, state);
        } catch (JSONException e) {
            e.printStackTrace();
            onFail(e.getMessage());
            Log.e("facebook-stream", "JSON Error:" + e.getMessage());
        } catch (FacebookError e) {
        	onFail(e.getMessage());
            Log.e("facebook-stream", "Facebook Error:" + e.getMessage());
        }

    }

    public abstract void onComplete(JSONObject obj, final Object state);
    public abstract void onFail(String errorMessage);

    public void onFacebookError(FacebookError e, final Object state) {
        Log.e("stream", "Facebook Error:" + e.getMessage());
    }

    public void onFileNotFoundException(FileNotFoundException e,
                                        final Object state) {
        Log.e("stream", "Resource not found:" + e.getMessage());      
    }

    public void onIOException(IOException e, final Object state) {
        Log.e("stream", "Network Error:" + e.getMessage());      
    }

    public void onMalformedURLException(MalformedURLException e,
                                        final Object state) {
        Log.e("stream", "Invalid URL:" + e.getMessage());            
    }

}