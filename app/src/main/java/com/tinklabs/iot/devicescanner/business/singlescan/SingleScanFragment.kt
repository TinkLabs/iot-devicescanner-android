package com.tinklabs.iot.devicescanner.business.singlescan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tinklabs.iot.devicescanner.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SingleScanFragment : Fragment() {
    private val viewModel by viewModel<SingleScanViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.valid.observe(this, Observer {
            if (it) {
                Timber.d("%s, %s", viewModel.deviceInfo.value?.imei, viewModel.deviceInfo.value?.snCode)
                viewModel.upload()
            } else {

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_single_scan, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
    }
}