package com.graduationwork.tracediary;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioRecordThread extends Thread {
    /* audio setting */
    private static final int SAMPLE_RATE = 44100;                                           /* 샘플 속도 */
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;            /* 채널 */
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;  /* 오디오 포맷 */

    static final String AUDIO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tracediary/audio";

    boolean isRecording = false;
    //private boolean isPlaying = false;

    AudioRecord audioRecord;            /* audio record */
    // AudioTrack audioTrack;              /* record 동작 확인 */

    int buffer_Size;

    String filename;

    private BufferedOutputStream BOStream;


    public void run() {
        audioInit();

        audioRecord.startRecording();
        isRecording = true;

        writeAudioDataToFile();
    }

    /* audio 초기 설정 */
    public void audioInit() {
          /* Audio 버퍼 크기 설정 */
        buffer_Size = AudioRecord.getMinBufferSize(SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);

         /* 하드웨어에서 지원x - 버퍼 크기 조절 */
        if (buffer_Size == AudioRecord.ERROR || buffer_Size == AudioRecord.ERROR_BAD_VALUE) {
            buffer_Size = SAMPLE_RATE * 2;
        }

         /* AudioRecord 인스턴스 생성 */
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, buffer_Size);

        /* AudioTrack 인스턴스 생성 */
        //audioTrack = new AudioTrack(AudioManager.MODE_IN_COMMUNICATION, SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, buffer_Size, AudioTrack.MODE_STREAM);
        // audioTrack.setPlaybackRate(SAMPLE_RATE);            /* 트랙의 재생 샘플 속도 설정 */
    }

    public void writeAudioDataToFile() {
        //byte[] buffer = new byte[buffer_Size];
        byte[] data = new byte[buffer_Size];

        File directory = new File(AUDIO_PATH);

        if (!directory.exists()) {
            directory.mkdir();
        }

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_kk_mm_ss");
        String datetime = sdf.format(date);

        filename = directory + "/" + date + ".pcm";
        File file = new File(filename);

    /* 덮어 쓰기 */
        if (file.exists()) {
            file.delete();
        }

    /* 스트림 */
        try {
            BOStream = new BufferedOutputStream(new FileOutputStream(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int read = 0;

        if (BOStream != null) {
            try {
                while (isRecording) {
                    read = audioRecord.read(data, 0, buffer_Size);

                    if (read != AudioRecord.ERROR_INVALID_OPERATION) {
                        BOStream.write(data);
                    }
                }

                BOStream.flush();       /* outputStream flush*/
                BOStream.close();       /*  */

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();         /* AudioRecord - 녹음 중지 */
            audioRecord.release();      /* AudioRecord - 리소스 해제 */
            audioRecord = null;
        }
    }
}
