/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.qihoo360.replugin.fresco.plugin;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author RePlugin Team
 */
public class MainActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "https://img1.doubanio.com/lpic/s3743777.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("GGL","ClassLoader:"+this.getClass().getClassLoader().getClass().getSimpleName());
        Log.e("GGL","ClassLoader:"+this.getClassLoader().getClass().getSimpleName());
//        RePlugin.registerHookingClass("com.facebook.drawee.view.SimpleDraweeView", RePlugin.createComponentName("com.qihoo360.replugin.fresco.host", "com.facebook.drawee.view.SimpleDraweeView"), null);
        setContentView(R.layout.activity_main);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.image);
        Uri uri = Uri.parse(IMAGE_URL);
        simpleDraweeView.setImageURI(uri);
    }
}