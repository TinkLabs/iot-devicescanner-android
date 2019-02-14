package com.tinklabs.iot.devicescanner.business.singlescan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.fragment_single_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleScanFragment : Fragment() {
    private val viewModel by viewModel<SingleScanViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.valid.observe(this, Observer {
            if (it) {
                decodeComponent.enableScanning(false)
                ConfirmDialog(context!!)
                    .cancelable(false)
                    .title("Confirm to Upload?")
                    .content("IMEI: ${viewModel.deviceInfo.value?.imei}\n S/N: ${viewModel.deviceInfo.value?.snCode}")
                    .confirmText(R.string.upload)
                    .onConfirm(View.OnClickListener {
                        viewModel.upload()
                        decodeComponent.enableScanning(true)
                    })
                    .onCancel(View.OnClickListener {
                        decodeComponent.enableScanning(true)
                    })
                    .show()
            } else {

            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_scan, container, false)
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
        decodeComponent.dispose()
    }
}