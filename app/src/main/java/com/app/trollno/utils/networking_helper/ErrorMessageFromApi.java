package com.app.trollno.utils.networking_helper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class ErrorMessageFromApi {

    public static String errorMessageFromApi(ResponseBody responseBody) {
        String userMessage = "";
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            userMessage = jsonObject.getString("message");
            Log.d(TAG_LOG, "response.errorBody() " + userMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userMessage;
    }
}
