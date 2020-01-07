package xyz.belvi.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intelia.datapoint.models.SmsDataPoint
import com.intelia.loansdk.R
import kotlinx.android.synthetic.main.alert_item.view.*

open class AlertRecyclerAdapter(val smsList: MutableList<SmsDataPoint>) : RecyclerView.Adapter<AlertRecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false))
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(smsList[position])
    }

    fun update(smsList: MutableList<SmsDataPoint>) {
        this.smsList.clear()
        this.smsList.addAll(smsList)
        notifyDataSetChanged()
    }

    class Holder(iteview: View) : RecyclerView.ViewHolder(iteview) {
        fun bind(sms: SmsDataPoint) {
//            itemView.title.text = sms.number
//            itemView.body.text = sms.body
        }
    }
}