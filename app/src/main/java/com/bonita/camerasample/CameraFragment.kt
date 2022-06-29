package com.bonita.camerasample

import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonita.camerasample.databinding.FragmentCameraBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
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

    // 카메라 프리뷰 관련
    private var camMat: Mat? = null

    override fun onCreateView(a_inflater: LayoutInflater, a_container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentCameraBinding.inflate(a_inflater, a_container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindCamera(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewBinding.cameraMain.disableView()
        _viewBinding = null
    }

    fun doCapture() {
        val copyMat = Mat()
        val saveFile = File("/storage/emulated/0/OCR/image.jpg")

        // preview camMat 을 최대 size 로 resize 하여 copy
        val pictureSizes = viewBinding.cameraMain.parameters.supportedPictureSizes[0]
        val scaleSize = Size(pictureSizes.width.toDouble(), pictureSizes.height.toDouble())
        Imgproc.resize(camMat, copyMat, scaleSize)

        // 90도 회전하는 기능 추가..
        val newMat = Mat()
        rotateImage(copyMat, newMat, (90).toDouble())

        Imgcodecs.imwrite(saveFile.path, newMat)
    }

    fun flashOn() {
        viewBinding.cameraMain.setFlashMode(true)
    }

    private fun bindCamera(a_view: View) {
        OpenCVLoader.initDebug()

        // Open CV View enable 처리
        viewBinding.cameraMain.run {
            setCvCameraViewListener(object : CameraBridgeViewBase.CvCameraViewListener2 {
                override fun onCameraViewStarted(width: Int, height: Int) {
                    // 화이트 밸런스 설정
                    setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT)
                }

                override fun onCameraViewStopped() {/* Nothing to do */
                }

                override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
                    camMat = inputFrame.rgba()
                    return camMat!!
                }
            })

            // 포커스 가지 않도록
            isFocusable = false

            // 카메라뷰 활성화
            enableView()
        }
    }

    /**
     * 카메라 사진 회전 하는 함수.
     */
    private fun rotateImage(src: Mat, dst: Mat, a_angle: Double) {
        var angle = a_angle
        angle %= 360.0
        if (angle == 0.0) {
            src.copyTo(dst)
        } else if (angle == 90.0 || angle == -270.0) {
            Core.transpose(src, dst)
            Core.flip(dst, dst, 1)
        } else if (angle == 180.0 || angle == -180.0) {
            Core.flip(src, dst, -1)
        } else if (angle == 270.0 || angle == -90.0) {
            Core.transpose(src, dst)
            Core.flip(dst, dst, 0)
        } else {
            val rotMat = Imgproc.getRotationMatrix2D(
                Point(src.cols() / 2.0, src.rows() / 2.0), angle, 1.0
            )
            Imgproc.warpAffine(src, dst, rotMat, src.size())
        }
    }
}