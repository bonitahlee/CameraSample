package com.bonita.camerasample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonita.camerasample.databinding.ActivityCameraBinding

/**
 * Camera Activity
 *
 * @author bonita
 * @date 2021-11-17
 */
class MainActivity : AppCompatActivity() {

    // Camera view 의 fragment
    private lateinit var cameraFragment: CameraFragment

    // View binding
    private lateinit var viewBinding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // 옵션에 따른 button text 설정
        bindButton()

        onReplaceFragment(CameraFragment.TAG)
    }

    /**
     * 프래그먼트 교체
     */
    private fun onReplaceFragment(a_fragmentTag: String) {
        cameraFragment = CameraFragment()

        // 프래그먼트 교체
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(viewBinding.cameraFrame.id, cameraFragment, a_fragmentTag)
        fragmentTransaction.commit()
    }

    /**
     * Button 설정
     */
    private fun bindButton() {
        // 캡쳐버튼 설정
        viewBinding.captureButton.run {
            setOnClickListener { cameraFragment.doCapture() }
        }

        // 플래시 버튼 설정
        viewBinding.flashButton.run {
            setOnClickListener { cameraFragment.flashOn() }
        }
    }
}