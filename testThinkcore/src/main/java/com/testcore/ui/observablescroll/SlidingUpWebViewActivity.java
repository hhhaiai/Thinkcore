/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.testcore.ui.observablescroll;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.testcore.R;

public class SlidingUpWebViewActivity extends SlidingUpBaseActivity<ObservableWebView> implements ObservableScrollViewCallbacks {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_slidingupwebview;
    }

    @Override
    protected ObservableWebView createScrollable() {
        ObservableWebView webView = (ObservableWebView) findViewById(R.id.scroll);
        webView.setScrollViewCallbacks(this);
        webView.loadUrl("file:///android_asset/lipsum.html");
        return webView;
    }
}
