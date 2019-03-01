package com.tinklabs.iot.devicescanner.business.index

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.business.MainViewModel
import com.tinklabs.iot.devicescanner.business.index.IndexViewModel.Companion.STATUS_COMPLETE
import com.tinklabs.iot.devicescanner.business.index.IndexViewModel.Companion.STATUS_EMPTY
import com.tinklabs.iot.devicescanner.business.index.IndexViewModel.Companion.STATUS_ERROR
import com.tinklabs.iot.devicescanner.business.index.IndexViewModel.Companion.STATUS_LOADING
import com.tinklabs.iot.devicescanner.business.index.adapter.StatusAdapter
import com.tinklabs.iot.devicescanner.widget.ConfirmDialog
import kotlinx.android.synthetic.main.index_layout.*
import kotlinx.android.synthetic.main.index_layout.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class IndexFragment : Fragment() {
    companion object {
        const val STATUS_BUNDLE_KEY = "status"
    }

    private val viewModel by viewModel<IndexViewModel>()
    private val mainViewModel:MainViewModel by sharedViewModel()

    private val adapter = StatusAdapter()

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.index_layout, container, false)
        setHasOptionsMenu(true)

        view.recyclerView.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.status.observe(this, Observer {
            when(it) {
                STATUS_EMPTY -> stateLayout.showEmpty()
                STATUS_LOADING -> stateLayout.showLoading()
                STATUS_ERROR -> stateLayout.showError()
                STATUS_COMPLETE -> stateLayout.showContent()
            }
        })

        viewModel.stateData.observe(this, Observer {
            adapter.setData(it)
        })

        mainViewModel.isSignedIn.observe(this, Observer {
            Timber.d("sign in status: $it")
            if (it) viewModel.loadStatus() // sign successful should get status list.
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
    }

    /**
     * navigate to fragment
     */
    private fun navigateTo(status: String) {
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