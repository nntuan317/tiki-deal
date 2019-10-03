package vn.tiki.android.androidhometest

import android.net.Uri
import android.os.CountDownTimer
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.util.CommonUtils

class DealAdapter(private val data: MutableList<Deal>) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater
                .from(parent.context).inflate(R.layout.item_deal, parent, false)
        return DealViewHolder(view)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = data[position]
        holder.nameTv.text = deal.productName
        holder.priceTv.text = deal.productPrice.toString() + " $"

        GlideApp.with(holder.itemView.context)
                .load(Uri.parse(deal.productThumbnail))
                .thumbnail(0.1f)
                .fitCenter()
                .into(holder.pictureIv)

        if (holder.timer != null) {
            holder.timer!!.cancel()
        }

        val endTime = deal.endDate.time
        val now = System.currentTimeMillis()
        val timeRemain = endTime - now
        if (timeRemain > 0) {
            holder.timer = object : CountDownTimer(timeRemain, 1000) {
                override fun onTick(time: Long) {
                    holder.timeRemainTv.text = CommonUtils.getDuration(time)
                }

                override fun onFinish() {
                    val position = holder.adapterPosition
                    removeItem(position)
                }
            }.start()
        } else {
            holder.timeRemainTv.text = "00:00:00"
            removeItem(position)
        }
    }

    private fun removeItem(position: Int) {
        Handler().post(Runnable {
            if (position == -1) {
                return@Runnable
            }
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pictureIv: ImageView
        var nameTv: TextView
        var priceTv: TextView
        var timeRemainTv: TextView
        var timer: CountDownTimer? = null

        init {
            pictureIv = itemView.findViewById(R.id.image_item)
            nameTv = itemView.findViewById(R.id.text_name)
            priceTv = itemView.findViewById(R.id.text_price)
            timeRemainTv = itemView.findViewById(R.id.text_time_remain)
            itemView.setOnClickListener { }
        }
    }
}
