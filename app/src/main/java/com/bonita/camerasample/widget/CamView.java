package com.bonita.camerasample.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * OpenCV 카메라뷰 클래스
 *
 * @author KYI
 */
public class CamView extends JavaCameraView {

    public CamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * 해상도 확인 함수
     *
     * @return 가능한 해상도 리스트 반환
     */
    public List<Size> getResolutionList() {
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    /**
     * 입력한 사이즈로 카메라 해상도 설정
     * 파라미터 : width(가로 사이즈), height(세로 사이즈)
     */
    public void setResolution(int width, int height) {
        disconnectCamera();
        mMaxHeight = height;
        mMaxWidth = width;
        connectCamera(getWidth(), getHeight());
    }

    /**
     * 플래시 온/오프
     *
     * @param b : true 불 켬, false 불 끔.
     */
    public void setFlashMode(boolean b) {
        Parameters params = mCamera.getParameters();
        if (b) {
            // FLASH_MODE_TORCH: preview, auto-focus, snapshot 동안 on
            // FLASH_MODE_ON: snapshot 동안 on
            //params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            params.setFlashMode(Parameters.FLASH_MODE_ON);
        } else {
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        mCamera.setParameters(params);
    }

    // 2022/08/12 사진 찍기 루틴 추가
    public void takePicture(String filename) {
        Parameters params = mCamera.getParameters();
        params.setRotation(90); // 사진 이미지 정보를 90도 회전
        mCamera.setParameters(params);

        final Object lock = new Object();

        mCamera.takePicture(
                new Camera.ShutterCallback() {
                    public void onShutter() {
                        // 시스템 셧터 음이 출력되므로 필요 없음
                    }
                },
                null, // raw PictureCallback
                new Camera.PictureCallback() {
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            FileOutputStream os = new FileOutputStream(filename);
                            os.write(data);
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            synchronized (lock) {
                                lock.notify();
                            }
                        }
                    }
                });

        // 이미지 저장이 완료될 때까지 wait
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mCamera.startPreview();
    }
}