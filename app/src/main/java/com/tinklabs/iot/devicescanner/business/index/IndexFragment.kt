package com.tinklabs.iot.devicescanner.business.index

import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.index.adapter.StatusAdapter
import com.tinklabs.iot.devicescanner.ext.toast
import kotlinx.android.synthetic.main.index_layout.*
import kotlinx.android.synthetic.main.index_layout.view.*


class IndexFragment : Fragment() {

    companion object {
        const val STATUS_BUNDLE_KEY = "status"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.index_layout, container, false)
        view.SingleScan.setOnClickListener {
            navigateTo(R.id.action_to_single_scan)
        }
        view.BatchScan.setOnClickListener {
            navigateTo(R.id.action_to_batch_scan)
        }
        setHasOptionsMenu(true)

        view.recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        val adapter = StatusAdapter()
        adapter.setCompletedListener {
            tvStatus.setText(it)
            tvStatus.setSelection(it.length) //
        }
        view.recyclerView.adapter = adapter

        return view
    }

    /**
     * navigate to fragment
     */
    private fun navigateTo(@IdRes resId: Int) {
        val status: String = tvStatus.text.toString()
        if (status.isEmpty()) {
            toast("please input or select status you will upload devices")
            return
        }
        findNavController().navigate(resId, bundleOf(STATUS_BUNDLE_KEY to status))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.setting, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                findNavController().navigate(R.id.action_to_setting)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}