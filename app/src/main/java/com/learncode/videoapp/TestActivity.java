package com.learncode.videoapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "test_fragment";
    private static final String VIDEO_SAMPLE =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment==null){
            getSupportFragmentManager().beginTransaction().add(R.id.content, new TestFragment(), TAG).commit();
        }
    }
    public static class TestFragment extends Fragment{
        VideoView videoView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test,
                    container, false);
            if(videoView==null) {
                videoView = (VideoView) view.findViewById(R.id.videoview);
                MediaController controller = new MediaController(getContext());
                controller.setAnchorView(videoView);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.start();
                    }
                });
                videoView.setVideoURI(Uri.parse(VIDEO_SAMPLE));


                videoView.setMediaController(controller);
            }
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);


        }

        @Override
        public void onStart() {
            super.onStart();
            if(videoView!=null && !videoView.isPlaying()){
                videoView.start();
            }

        }

        @Override
        public void onStop() {
            super.onStop();
            if(getActivity().isChangingConfigurations()) {
                Log.i(getTag(), "configuration is changing: keep playing");
            } else {
                videoView.stopPlayback();
            }
        }

        @Override
        public void onPause() {
            super.onPause();
        }
    }
}
