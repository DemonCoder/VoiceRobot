package com.itgrape.robot;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class VideoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);

		if (!LibsChecker.checkVitamioLibs(this)) {
			return;
		}
		VideoView vv = (VideoView) findViewById(R.id.vv);
		vv.setVideoURI(Uri
				.parse("http://movie.ks.js.cn/flv/other/2014/06/20-2.flv"));
		vv.start();
		vv.setMediaController(new MediaController(this));
	}
}
