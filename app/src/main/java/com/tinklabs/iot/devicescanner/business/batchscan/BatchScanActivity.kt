package com.tinklabs.iot.devicescanner.business.batchscan

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.batchscan.adapter.ScanItemAdapter
import com.tinklabs.iot.devicescanner.business.index.IndexFragment
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.activity_batch_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BatchScanActivity : AppCompatActivity() {

    private lateinit var status: String
    private val viewModel by viewModel<BatchScanViewModel> { parametersOf(this) }

    private val mAdapter = ScanItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_scan)

        status = intent.getStringExtra(IndexFragment.STATUS_BUNDLE_KEY)
        initView()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onResume()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onPause()
    }

    private fun initView() {
        toolbar.setNavigationOnClickListener { this.finish() }

        mAdapter.setRemoveClickListener {
            viewModel.removeItem(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this).apply { orientation = RecyclerView.VERTICAL }
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
            ConfirmDialog(this)
                .cancelable(false)
                .title(R.string.tips)
                .content("Confirm to upload these information?")
                .confirmText(R.string.upload)
                .onConfirm(View.OnClickListener {
                    viewModel.upload(status) {
                        decodeComponent.enableScanning(true)
                    }
                })
                .onCancel(View.OnClickListener {
                    decodeComponent.enableScanning(true)
                })
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyView()
    }
}