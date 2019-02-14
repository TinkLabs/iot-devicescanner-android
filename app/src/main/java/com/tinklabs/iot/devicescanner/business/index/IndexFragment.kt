package com.tinklabs.iot.devicescanner.business.index

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tinklabs.iot.devicescanner.R
import kotlinx.android.synthetic.main.index_layout.view.*

class IndexFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.index_layout, container, false)
        view.SingleScan.setOnClickListener {
            findNavController().navigate(R.id.action_to_single_scan)
        }
        view.BatchScan.setOnClickListener {
            findNavController().navigate(R.id.action_to_batch_scan)
        }
        setHasOptionsMenu(true)
        return view
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