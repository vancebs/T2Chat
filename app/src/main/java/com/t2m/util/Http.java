package com.t2m.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Http {
    public static final String TAG = Http.class.getSimpleName();

    private ExecutorService mExecutorService;

    public interface OnReadLineListener {
        void onRead(String line);
    }

    public interface OnReadLineJsonListener {
        void onRead(JSONObject line);
    }

    public Http() {
        mExecutorService = Executors.newFixedThreadPool(1);
    }

    public Future<String> get(String url) {
        return mExecutorService.submit(() -> getImpl(url));
    }

    public Future<JSONObject> getJson(String url) {
        return mExecutorService.submit(() -> {
            try {
                return new JSONObject(getImpl(url));
            } catch (JSONException e) {
                return null;
            }
        });
    }

    public Future<String> post(String url, String data) {
        return mExecutorService.submit(() -> postImpl(url, data));
    }

    public Future<JSONObject> postJson(String url, String data) {
        return mExecutorService.submit(() -> {
            try {
                return new JSONObject(postImpl(url, data));
            } catch (JSONException e) {
                return null;
            }
        });
    }

    public Future<Boolean> getAsync(String url, OnReadLineListener listener) {
        return mExecutorService.submit(() -> getAsyncImpl(url, listener));
    }

    public Future<Boolean> getJsonAsync(String url, OnReadLineJsonListener listener) {
        if (listener == null) {
            Log.e(TAG, "getJsonAsync()# listener should not be null");
            return mExecutorService.submit(() -> false);
        }

        return getAsync(url, (line) -> {
            try {
                listener.onRead(new JSONObject(line));
            } catch (JSONException e) {
                Log.e(TAG, "getJsonAsync()# convert string line to json failed.", e);
                listener.onRead(null);
            }
        });
    }

    public Future<Boolean> postAsync(String url, String data, OnReadLineListener listener) {
        return mExecutorService.submit(() -> postAsyncImpl(url, data, listener));
    }

    public Future<Boolean> postAsyncJson(String url, String data, OnReadLineJsonListener listener) {
        if (listener == null) {
            Log.e(TAG, "postAsyncJson()# listener should not be null");
            return mExecutorService.submit(() -> false);
        }

        return postAsync(url, data, (line) -> {
            try {
                listener.onRead(new JSONObject(line));
            } catch (JSONException e) {
                Log.e(TAG, "postAsyncJson()# convert string line to json failed.", e);
                listener.onRead(null);
            }
        });
    }

    private static String getImpl(String url) {
        HttpURLConnection connection = null;

        try {
            URL _url = new URL(url);

            // create connection
            connection = createConnection(_url, "GET");

            // connect
            connection.connect();

            // check response
            int code = connection.getResponseCode();
            if (code != 200) {
                Log.e(TAG, "getImpl()# connect failed. code: " + code);
                return null;
            }

            // get response
            StringBuilder msg = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    msg.append(line).append("\n");
                }
            }

            return msg.toString();
        } catch (IOException e) {
            Log.e(TAG, "getImpl()# get failed.", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String postImpl(String url, String data) {
        HttpURLConnection connection = null;

        try {
            URL _url = new URL(url);

            // create connection
            connection = createConnection(_url, "POST");

            // connect
            connection.connect();

            // send data
            try (OutputStream out = connection.getOutputStream()) {
                out.write(data.getBytes());
                out.flush();
            }

            // check response
            int code = connection.getResponseCode();
            if (code != 200) {
                Log.e(TAG, "postImpl()# connect failed. code: " + code);
                return null;
            }

            // get response
            StringBuilder msg = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    msg.append(line).append("\n");
                }
            }

            return msg.toString();
        } catch (IOException e) {
            Log.e(TAG, "postImpl()# get failed.", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static boolean getAsyncImpl(String url, OnReadLineListener listener) {
        if (listener == null) {
            Log.e(TAG, "getImplAsync()# listener should not be null");
            return false;
        }

        HttpURLConnection connection = null;

        try {
            URL _url = new URL(url);

            // create connection
            connection = createConnection(_url, "GET");

            // connect
            connection.connect();

            // check response
            int code = connection.getResponseCode();
            if (code != 200) {
                Log.e(TAG, "getImplAsync()# connect failed. code: " + code);
                return false;
            }

            // get response
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    listener.onRead(line);
                }
            }

            return true;
        } catch (IOException e) {
            Log.e(TAG, "getImplAsync()# get failed.", e);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static boolean postAsyncImpl(String url, String data, OnReadLineListener listener) {
        if (listener == null) {
            Log.e(TAG, "postImplAsync()# listener should not be null");
            return false;
        }

        HttpURLConnection connection = null;

        try {
            URL _url = new URL(url);

            // create connection
            connection = createConnection(_url, "POST");

            // connect
            connection.connect();

            // send data
            try (OutputStream out = connection.getOutputStream()) {
                out.write(data.getBytes());
                out.flush();
            }

            // check response
            int code = connection.getResponseCode();
            if (code != 200) {
                Log.e(TAG, "postImplAsync()# connect failed. code: " + code);
                return false;
            }

            // get response
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    listener.onRead(line);
                }
            }

            return true;
        } catch (IOException e) {
            Log.e(TAG, "postImplAsync()# get failed.", e);
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static HttpURLConnection createConnection(URL url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput("POST".equals(method));
        connection.setDoInput(true);
        connection.setRequestMethod(method);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(3000);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        return connection;
    }
}
