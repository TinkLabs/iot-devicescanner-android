package com.tinklabs.iot.devicescanner.business.batchscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.batchscan.adapter.ScanItemAdapter
import com.tinklabs.iot.devicescanner.business.index.IndexFragment
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.fragment_batch_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BatchScanFragment : Fragment() {

    private lateinit var status: String
    private val viewModel by viewModel<BatchScanViewModel>()

    private val mAdapter = ScanItemAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_batch_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        status = arguments?.getString(IndexFragment.STATUS_BUNDLE_KEY) ?: ""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter.setRemoveClickListener {
            viewModel.removeItem(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        recyclerView.adapter = mAdapter

        viewModel.items.observe(this, Observer {
            mAdapter.items = it
            fab.isEnabled = it.size != 0
            textView_label.text = getString(R.string.scan_result_batch_message, viewModel.items.value?.size)
        })
        viewModel.valid.observe(this, Observer {
            if (!it) {
                scan_result.text =
                        "Not valid scan result: \n ${viewModel.deviceInfo.value?.imei}\n${viewModel.deviceInfo.value?.snCode}"
                /*Answers.getInstance()
                    .logCustom(CustomEvent("Scan")
                        .putCustomAttribute("VALID", "NO"))*/
            } else {
                scan_result.text = ""
            }
        })

        fab.setOnClickListener {
            decodeComponent.enableScanning(false)
            ConfirmDialog(context!!)
                .cancelable(false)
                .title(R.string.tips)
                .content("Confirm to upload these information?")
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
        }
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