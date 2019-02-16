package com.tinklabs.iot.devicescanner.business.singlescan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.index.IndexFragment
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.fragment_single_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SingleScanFragment : Fragment() {
    private val viewModel by viewModel<SingleScanViewModel>()

    private lateinit var status: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        status = arguments?.getString(IndexFragment.STATUS_BUNDLE_KEY) ?: ""
    }

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
                        Timber.d(status) // debug log
                        viewModel.upload(status)
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