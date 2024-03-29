package com.tinklabs.iot.devicescanner.business.index.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.tinklabs.iot.devicescanner.R
import kotlinx.android.synthetic.main.item_status.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class StatusAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onCompleted: (status: String) -> Unit

    fun setCompletedListener(onCompleted: (status: String) -> Unit) {
        this.onCompleted = onCompleted
    }

    companion object {
        const val EMPTY_VIEW_TYPE = 0x002
        const val STATUS_VIEW_TYPE = 0x003
    }

    private var dataSet: List<String> = emptyList()

    //private var dataSet: MutableList<StatusItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (EMPTY_VIEW_TYPE == viewType) EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.empty_view,
                parent,
                false
            )
        )
        else StatusViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false))
    }

    override fun getItemCount(): Int = if (dataSet.isEmpty()) 1 else dataSet.size

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (EMPTY_VIEW_TYPE == viewType) {
            // empty view, also add click to refresh
        } else {
            holder as StatusViewHolder
            holder.data = dataSet[position]
            holder.itemView.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    holder.data?.let {
                        Timber.d(holder.data ?: "")
                        onCompleted(holder.data ?: "")
                    }
                }
        }

    }

    fun setData(data: List<String>) {
        dataSet = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = if (dataSet.isEmpty()) EMPTY_VIEW_TYPE else STATUS_VIEW_TYPE


    /**
     * Status Item View
     */
    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data: String? = null
            set(value) {
                field = value
                itemView.itemTvStatus.text = field ?: ""
            }
    }

    /**
     * Empty View for recyclerView
     */
    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}