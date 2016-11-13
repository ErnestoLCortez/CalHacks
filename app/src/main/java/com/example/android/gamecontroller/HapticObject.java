package com.example.android.gamecontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import co.tanvas.haptics.service.adapter.HapticServiceAdapter;
import co.tanvas.haptics.service.app.HapticApplication;
import co.tanvas.haptics.service.model.HapticMaterial;
import co.tanvas.haptics.service.model.HapticSprite;
import co.tanvas.haptics.service.model.HapticTexture;
import co.tanvas.haptics.service.model.HapticView;

/**
 * Created by atomi on 11/12/2016.
 */

public class HapticObject {


    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;


    public HapticObject(AppCompatActivity parentActivity, int bitMapID){
        try {
            // Get the service adapter
            HapticServiceAdapter serviceAdapter = HapticApplication.getHapticServiceAdapter();

            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();

            // Set the orientation of the haptic view
            Display display = ((WindowManager)parentActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);
            mHapticView.setOrientation(orientation);

            // Retrieve texture data from the bitmap
            Bitmap hapticBitmap = BitmapFactory.decodeResource(parentActivity.getResources(), bitMapID);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);

            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);

            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);

            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);

            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }



    public void onWindowFocusChanged(AppCompatActivity parentActivity, boolean hasFocus, int containerID) {

        if (hasFocus) {
            try {
                // Set the size and position of the haptic sprite to correspond to the view we created
                View view = parentActivity.findViewById(containerID);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mHapticSprite.setSize(view.getWidth(), view.getHeight());
                mHapticSprite.setPosition(location[0], location[1]);
            } catch (Exception e) {
                Log.e(null, e.toString());
            }
        }
    }


    public void onDestroy() {
        try {
            mHapticView.deactivate();
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }




}
