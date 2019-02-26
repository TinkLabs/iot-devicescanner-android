package com.tinklabs.iot.devicescanner.business.index

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.index.adapter.StatusAdapter
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.index_layout.view.*


class IndexFragment : Fragment() {

    companion object {
        const val STATUS_BUNDLE_KEY = "status"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.index_layout, container, false)
        setHasOptionsMenu(true)

        view.recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        val adapter = StatusAdapter()
        adapter.setCompletedListener { status ->
            ConfirmDialog(context!!)
                .cancelable(false)
                .title(getString(R.string.tips))
                .content("Status: $status \nDo you sure?")
                .cancelTextColor(R.color.red_700)
                .confirmText(R.string.confirm)
                .onConfirm(View.OnClickListener {
                    navigateTo(status)
                })
                .show()
        }
        view.recyclerView.adapter = adapter

        return view
    }

    /**
     * navigate to fragment
     */
    private fun navigateTo(status:String) {
        findNavController().navigate(R.id.action_to_batch_scan, bundleOf(STATUS_BUNDLE_KEY to status))
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