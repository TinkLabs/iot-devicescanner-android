package com.tinklabs.iot.devicescanner.business.batchscan.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.data.DeviceInfo
import kotlinx.android.synthetic.main.item_device.view.*

class ScanItemAdapter: RecyclerView.Adapter<ScanItemAdapter.ScanItemViewHolder>() {
    lateinit var onRemove: (info: DeviceInfo) -> Unit

    var items: MutableList<DeviceInfo> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanItemViewHolder =
        ScanItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ScanItemViewHolder, position: Int) {
        holder.data = items[position]
        holder.itemView.removeIcon.setOnClickListener {
            holder.data?.let {
                onRemove(it)
            }
        }
    }

    fun setRemoveClickListener(onRemove: (info: DeviceInfo) -> Unit) {
        this.onRemove = onRemove
    }

    class ScanItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var data: DeviceInfo? = null
            @SuppressLint("SetTextI18n")
            set(value) {
                field = value
                itemView.item_imei.text = "IMEI: ${value?.imei}"
                itemView.item_sn.text = "S/N: ${value?.snCode}"
            }
    }
}