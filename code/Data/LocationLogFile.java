package com.graduationwork.tracediary.Data;

import android.os.Environment;

import com.graduationwork.tracediary.LocationDTO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import static com.graduationwork.tracediary.Adapter.LogRecyclerViewAdapter.logLocationDTOs;

public class LocationLogFile {
    static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tracediary/LocationLog/";
    static final String FILENAME = "locationLog.txt";

    FileWriter fileWriter = null;
    FileReader fileReader = null;

    BufferedWriter bufferedWriter = null;
    BufferedReader bufferedReader = null;

    /* txt 파일에 저장 */
    public void writeFile (String log, String date) {
        try {
            File directory = new File (PATH);

            if (!directory.exists()) {
                directory.mkdir();
            }

            /* 이어 쓰기 */
            fileWriter = new FileWriter(PATH + date + " " + FILENAME, true);
            bufferedWriter = new BufferedWriter(fileWriter);

            if (log != null) {
                bufferedWriter.write(log);
                bufferedWriter.write("\r\n");

                bufferedWriter.flush();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* txt 파일 읽기 */
    public void readFile(String selected_date) {
        try{
            fileReader = new FileReader(PATH + selected_date + " " + FILENAME);
            bufferedReader = new BufferedReader(fileReader);

            String logLine;

            logLocationDTOs.clear();

            while ((logLine = bufferedReader.readLine()) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(logLine, "/");

                String _date_ = stringTokenizer.nextToken();
                String _time_ = stringTokenizer.nextToken();
                String _place_ = stringTokenizer.nextToken();
                Double _lati_ = Double.valueOf(stringTokenizer.nextToken());
                Double _longi_ = Double.valueOf(stringTokenizer.nextToken());
                Float _accu_ = Float.valueOf(stringTokenizer.nextToken());


                logLocationDTOs.add(new LocationDTO(_date_, _time_, _place_, _lati_, _longi_, _accu_));
            }

            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (fileReader != null) {
                fileReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

