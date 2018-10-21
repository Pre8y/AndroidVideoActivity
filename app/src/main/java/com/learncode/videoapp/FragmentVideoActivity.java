package com.learncode.videoapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

public class FragmentVideoActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "main_fragment";
    private static final String VIDEO_SAMPLE =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_video);
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(FRAGMENT_TAG);
        if(f == null) {
            fm.beginTransaction().add(new VideoFragment(), FRAGMENT_TAG).commit();
        }
    }
    public static class VideoFragment extends Fragment{
        private MediaPlayer mediaPlayer;
        private SurfaceView surfaceView;
        MediaController controller;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            createMediaPlayer();
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            surfaceView = (SurfaceView) getActivity().findViewById(R.id.surface);
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mediaPlayer.setDisplay(holder);
                    setSurfaceSize();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
                    // nothing to do
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mediaPlayer.setDisplay(null);
                }
            });
        }
        private void setSurfaceSize() {
            // get the dimensions of the video (only valid when surfaceView is set)
            float videoWidth = mediaPlayer.getVideoWidth();
            float videoHeight = mediaPlayer.getVideoHeight();

            // get the dimensions of the container (the surfaceView's parent in this case)
            View container = (View) surfaceView.getParent();
            float containerWidth = container.getWidth();
            float containerHeight = container.getHeight();

            // set dimensions to surfaceView's layout params (maintaining aspect ratio)
            android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
            lp.width = (int) containerWidth;
            lp.height = (int) ((videoHeight / videoWidth) * containerWidth);
            if(lp.height > containerHeight) {
                lp.width = (int) ((videoWidth / videoHeight) * containerHeight);
                lp.height = (int) containerHeight;
            }
            surfaceView.setLayoutParams(lp);
        }

        @Override
        public void onStart() {
            super.onStart();
            if(mediaPlayer == null) {
                createMediaPlayer();
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if(getActivity().isChangingConfigurations()) {
                Log.i(getTag(), "configuration is changing: keep playing");
            } else {
                destroyMediaPlayer();
            }
        }


        private void createMediaPlayer() {
            Log.i(getTag(), "createMediaPlayer");
            try {
                 controller = new MediaController(getActivity());
                getActivity().findViewById(R.id.buffering_textview).setVisibility(View.VISIBLE);
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getActivity(), Uri.parse(VIDEO_SAMPLE));
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        getActivity().findViewById(R.id.buffering_textview).setVisibility(View.GONE);
//                        controller.setMediaPlayer(mediaPlayer);
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void destroyMediaPlayer() {
            Log.i(getTag(), "destroyMediaPlayer");
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }


    }
}
