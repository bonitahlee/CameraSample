package com.bonita.camerasample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonita.camerasample.databinding.FragmentCameraBinding
import org.opencv.android.OpenCVLoader
import java.io.File

/**
 * 캡쳐 수행
 */
class CameraFragment : Fragment() {

    companion object {
        const val TAG = "CameraFragment"
    }

    // View binding
    private var _viewBinding: FragmentCameraBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(a_inflater: LayoutInflater, a_container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentCameraBinding.inflate(a_inflater, a_container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewBinding.cameraMain.disableView()
        _viewBinding = null
    }

    /**
     * 사진 찍기 및 저장
     */
    fun doCapture() {
        val saveFile = File("/storage/emulated/0/OCR/image.jpg")
        viewBinding.cameraMain.takePicture(saveFile.path)
    }

    /**
     * 플래시 켜기
     */
    fun flashOn() {
        viewBinding.cameraMain.setFlashMode(true)
    }

    /**
     * Bind Camera
     */
    private fun bindCamera() {
        OpenCVLoader.initDebug()

        // Open CV View enable 처리
        viewBinding.cameraMain.run {
            // 2022/08/12 camMat 을 사용하지 않으므로 삭제 함
            // 사진 찍기 루틴 변경: doCapture() 참조.
            // 화이트 밸런스 설정: JavaCameraView.initializeCamera()로 이동
/*
            setCvCameraViewListener(object : CameraBridgeViewBase.CvCameraViewListener2 {
                override fun onCameraViewStarted(width: Int, height: Int) {
                    // 화이트 밸런스 설정
                    setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT)
                }

                override fun onCameraViewStopped() {
                }

                override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
                    camMat = inputFrame.rgba()
                    return camMat!!
                }
            })
*/

            // 포커스 가지 않도록
            isFocusable = false

            // 카메라뷰 활성화
            enableView()
        }
    }
}