package com.bonita.camerasample.widget;

import android.content.Context;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;

import org.opencv.android.JavaCameraView;

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
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        } else {
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
        }
        mCamera.setParameters(params);
    }

    /**
     * 화이트 밸런스 설정, 카메라 기본 값은 Auto
     *
     * @param wb_params : 화이트 밸런스 파라미터 입력, Camera.Paramters.WHITE_BALANCE_**** 사용
     */
    public void setWhiteBalance(String wb_params) {
        Parameters params = mCamera.getParameters();
        params.setWhiteBalance(wb_params);
        mCamera.setParameters(params);
    }
}