package com.main.test4.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.main.test4.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

// Class containing several static methods that are used in different parts of the application
public class Utils {

    // Formatting double values into € currency format
    public static String formatDoubleToPrice(double value) {
        return String.format("%.2f €", value);
    }

    // Converts an InputStream into a plain string
    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    // Function to download a file from a remote URL using httpsURLConnection
    public static InputStream downloadFileFromURL(String strURL) throws IOException {
        URL url = new URL(strURL);
        HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            return httpURLConnection.getInputStream();
        }

        return null;
    }

    // Function to store an InputSteam inside a local file
    public static boolean saveInputStreamToFile(Context context, InputStream inputStream, String fileName) {
        try {
            File file = new File(getApplicationDataDirectory(context), fileName);
            file.getParentFile().mkdirs();

            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=inputStream.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            inputStream.close();

            return true;
        } catch (Exception exc) {
            Log.e(context.getString(R.string.app_name), "Error while saving inputStream to file: " + exc.getMessage());

            return false;
        }
    }

    // Function to open an image file from device file system
    public static Bitmap getImageFromApplicationData(Context context, String imageName) {
        try {
            File file = new File(getApplicationDataDirectory(context), imageName);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (IOException exc) {
            Log.e(context.getString(R.string.app_name), "Error while trying to get course image: " + exc.getMessage());
            return null;
        }
    }

    // Getting private application data direcory
    public static File getApplicationDataDirectory(Context context) {
        File appDataDIr = context.getDir("AppData", Context.MODE_PRIVATE);
        if (!appDataDIr.exists()) {
            appDataDIr.mkdir();
        }
        return appDataDIr;
    }

    // Function to return the name of the JSON file that contains courses information
    public static String getAppDataFileName(Context context) {
        return context.getString(R.string.config_courses_json_file);
    }

    // Reading content of application data JSON file
    public static String getAppDataFileContent(Context context) {
        String json = null;

        File jsonFile = new File(getApplicationDataDirectory(context), getAppDataFileName(context));
        if (jsonFile.exists()) {
            try {
                FileInputStream fin = new FileInputStream(jsonFile);
                json = convertStreamToString(fin);
                fin.close();
            } catch (Exception exc) {
                Log.e(context.getString(R.string.app_name), "Error while reading app data file: " + exc.getMessage());
            }
        }

        return json;
    }

    // Saving application data file in local file system
    public static void setAppDataFileContent(Context context, String data) {
        try {
            File file = new File(getApplicationDataDirectory(context), getAppDataFileName(context));
            OutputStream out = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException exc) {
            Log.e(context.getString(R.string.app_name), "Error while saving app data file: " + exc.getMessage());
        }
    }
}
