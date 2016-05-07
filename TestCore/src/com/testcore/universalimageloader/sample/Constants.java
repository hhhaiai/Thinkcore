/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.testcore.universalimageloader.sample;

import com.testcore.R;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class Constants {

	public static final String[] IMAGES = new String[] {
			// Heavy images
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",
			"http://www.haoyitong.com:88//Uploads//Treasure//t1.jpg",

			"http://www.theblaze.com/wp-content/uploads/2011/08/Apple.png",
			"http://1.bp.blogspot.com/-y-HQwQ4Kuu0/TdD9_iKIY7I/AAAAAAAAE88/3G4xiclDZD0/s1600/Twitter_Android.png",
			"http://3.bp.blogspot.com/-nAf4IMJGpc8/TdD9OGNUHHI/AAAAAAAAE8E/VM9yU_lIgZ4/s1600/Adobe%2BReader_Android.png",
			"http://cdn.geekwire.com/wp-content/uploads/2011/05/oovoo-android.png?7794fe",
			"http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
			"http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
			"http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
			"http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
			// Special cases
			"http://cdn.urbanislandz.com/wp-content/uploads/2011/10/MMSposter-large.jpg", // Very
																							// large
																							// image
			"http://www.ioncannon.net/wp-content/uploads/2011/06/test9.webp", // WebP
																				// image
			"http://4.bp.blogspot.com/-LEvwF87bbyU/Uicaskm-g6I/AAAAAAAAZ2c/V-WZZAvFg5I/s800/Pesto+Guacamole+500w+0268.jpg", // Image
																															// with
																															// "Mark has been invalidated"
																															// problem
			"file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image
																			// from
																			// SD
																			// card
																			// with
																			// encoded
																			// symbols
			"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
			"drawable://" + R.drawable.ic_launcher, // Image from drawables
			"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link
																							// with
																							// UTF-8
			"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image
																		// from
																		// HTTPS
			"http://bit.ly/soBiXr", // Redirect link
			"http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
			"", // Empty link
			"http://wrong.site.com/corruptedLink", // Wrong link
	};

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static class Extra {
		public static final String FRAGMENT_INDEX = "com.nostra13.example.universalimageloader.FRAGMENT_INDEX";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
