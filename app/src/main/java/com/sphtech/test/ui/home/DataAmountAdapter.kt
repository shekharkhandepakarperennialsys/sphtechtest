package com.sphtech.test.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sphtech.shared.entities.RecordsData
import com.sphtech.test.databinding.DataAmountRowBinding

class DataAmountAdapter(
    val clickListener: ( year: String,
                         data: List<RecordsData>?) -> Unit
) :
    RecyclerView.Adapter<DataAmountAdapter.DataAmountViewHolder>() {

    private val recordDataList: HashMap<String, List<RecordsData>> = hashMapOf()
    private val mKeys: ArrayList<String> = arrayListOf()

    class DataAmountViewHolder(private val cardNumberRowBinding: DataAmountRowBinding) :
        RecyclerView.ViewHolder(cardNumberRowBinding.root) {
        fun bind(
            year: String,
            data: List<RecordsData>?
        ) {

            val sortedData = data?.sortedBy {
                it.volumeOfMobileData.toDouble()
            }

            cardNumberRowBinding.recordYear = year
            cardNumberRowBinding.consumption = "${data?.map { it.volumeOfMobileData.toDouble() }?.sum()}"

            cardNumberRowBinding.ivDecreasedVolume.visibility = when(sortedData?.equals(data)){
                true -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataAmountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataAmountRowBinding.inflate(layoutInflater, parent, false)
        return DataAmountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataAmountViewHolder, position: Int) {
        holder.bind(mKeys[position], recordDataList[mKeys[position]])
        holder.itemView.setOnClickListener { clickListener(mKeys[position], recordDataList[mKeys[position]]) }
    }

    override fun getItemCount() = recordDataList.size

    fun dataChanged(refreshedRecordDataList: MutableMap<String, List<RecordsData>>){
        recordDataList.clear()
        recordDataList.putAll(refreshedRecordDataList)
        mKeys.clear()
        mKeys.addAll(ArrayList(recordDataList.keys.toList()))
        mKeys.sortDescending()
        notifyDataSetChanged()
    }
}