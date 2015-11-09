package com.michael.jiang.earthquake.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.michael.jiang.earthquake.EarthQuakeListFragment;
import com.michael.jiang.earthquake.Quake;
import com.michael.jiang.earthquake.R;
import com.michael.jiang.earthquake.broadcast.EarthQuakeAlarmReceiver;
import com.michael.jiang.earthquake.database.EarthQuakeSQLiteHelper;
import com.michael.jiang.earthquake.preference.EarthQuakePreferenceFragment;
import com.michael.jiang.earthquake.provider.EarthQuakeProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * EarthQuakeUpdateService
 */
public class EarthQuakeUpdateService extends IntentService {
    public EarthQuakeUpdateService(){
        super("EarthQuakeUpdateService");
    }

    public EarthQuakeUpdateService(String name) {
        super(name);
    }

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    public static final int REQUEST_CODE=0;

    private boolean isAutoUpdateChecked=false;
    private int updateFreq=60;
    private int minMagnitude=3;

    private URL url;
    private URLConnection urlConnection;
    private String urlString;
    @Override
    public void onCreate() {
        super.onCreate();
        urlString = getString(R.string.feed_url);
        alarmManager = (AlarmManager) this.getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(EarthQuakeAlarmReceiver.ALARM_ACTION);
        alarmIntent=PendingIntent.getBroadcast(this,REQUEST_CODE,intent,0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getPrefsConfig();
        if(isAutoUpdateChecked){
            int alarmType=AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long nextTimeToRefresh= SystemClock.elapsedRealtime()+updateFreq*1000;
            alarmManager.setInexactRepeating(alarmType,nextTimeToRefresh,updateFreq*1000,alarmIntent);
        }else {
            alarmManager.cancel(alarmIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        refreshEarthquakes();
    }

    private void getPrefsConfig() {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        isAutoUpdateChecked = defaultPrefs.getBoolean(EarthQuakePreferenceFragment.PREF_AUTO_UPDATE, false);
        updateFreq = Integer.parseInt(defaultPrefs.getString(EarthQuakePreferenceFragment.PREF_FREQ,"60"));
        minMagnitude = Integer.parseInt(defaultPrefs.getString(EarthQuakePreferenceFragment.PREF_MIN_MAGNITUDE,"3"));
    }


    public void refreshEarthquakes()  {
        try {
            url = new URL(urlString);
            urlConnection=url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                InputStream inputStream=httpURLConnection.getInputStream();

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document parse = documentBuilder.parse(inputStream);
                Element documentElement = parse.getDocumentElement();

                NodeList entryList = documentElement.getElementsByTagName("entry");
                if(entryList!=null&&entryList.getLength()>0){
                    for(int i=0;i<entryList.getLength();i++) {
                        Element entry = (Element) entryList.item(i);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element g = (Element) entry.getElementsByTagName("georss:point").item(0);
                        Element when = (Element) entry.getElementsByTagName("updated").item(0);
                        Element link = (Element) entry.getElementsByTagName("link").item(0);

                        String details=title.getFirstChild().getNodeValue();
                        String hostName="http://earthquake.usgs.gov";
                        String linkString = hostName + link.getAttribute("href");

                        String point="0 0";
                        Location location = new Location("dummyGPS");
                        if(g!=null){
                            try{
                                point= g.getFirstChild().getNodeValue();
                                String[] locationPoint = point.split(" ");
                                location.setLatitude(Double.parseDouble(locationPoint[0]));
                                location.setLongitude(Double.parseDouble(locationPoint[1]));}
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        String dateStr = when.getFirstChild().getNodeValue();
                        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                        Date quakeDate=new GregorianCalendar(0,0,0).getTime();
                        quakeDate = dateFormat.parse(dateStr);

                        String magnitudeString=details.split(" ")[1];
                        int end=magnitudeString.length()-1;
                        double magnitude=0.0;
                        try{
                            magnitude= Double.parseDouble(magnitudeString.substring(0, end));
                        }catch (Exception err){
                            magnitude=0.0;
                            err.printStackTrace();
                        }

                        //details=details.split(",")[1].trim();
                        final Quake quake = new Quake(quakeDate, details, magnitude, location, linkString);
                        addNewQuake(quake);
                    }
                }

            }
        } catch (IOException | ParserConfigurationException | SAXException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void addNewQuake(Quake quake){
        ContentResolver resolver=this.getApplicationContext().getContentResolver();
        String selection= EarthQuakeSQLiteHelper.KEY_DATE+" = "+quake.getDate().getTime();
       Cursor cursor= resolver.query(EarthQuakeProvider.CONTENT_URIS, null, selection, null, null);
        if(cursor==null||cursor.getCount()==0){
            ContentValues contentValues=new ContentValues();
            contentValues.put(EarthQuakeSQLiteHelper.KEY_DATE,quake.getDate().getTime());
            contentValues.put(EarthQuakeSQLiteHelper.KEY_DETAILS,quake.getDetails());
            contentValues.put(EarthQuakeSQLiteHelper.KEY_MAGNITUDE,quake.getMagnitude());
            resolver.insert(EarthQuakeProvider.CONTENT_URIS, contentValues);
        }
        cursor.close();
    }


}
