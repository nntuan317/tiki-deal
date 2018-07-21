package vn.tiki.android.androidhometest;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.tiki.android.androidhometest.data.api.response.Deal;
import vn.tiki.android.androidhometest.util.CommonUtils;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private List<Deal> data;

    public DealAdapter(List<Deal> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DealViewHolder holder, final int position) {
        Deal deal = data.get(position);
        holder.nameTv.setText(deal.getProductName());
        holder.priceTv.setText(deal.getProductPrice() + " $");

        GlideApp.with(holder.itemView.getContext())
                .load(Uri.parse(deal.getProductThumbnail()))
                .thumbnail(0.1f)
                .fitCenter()
                .into(holder.pictureIv);

        if (holder.timer != null) {
            holder.timer.cancel();
        }

        long endTime = deal.getEndDate().getTime();
        long now = System.currentTimeMillis();
        long timeRemain = endTime - now;
        if (timeRemain > 0) {
            holder.timer = new CountDownTimer(timeRemain, 1000) {
                @Override
                public void onTick(long time) {
                    holder.timeRemainTv.setText(CommonUtils.getDuration(time));
                }

                @Override
                public void onFinish() {
                    int position = holder.getAdapterPosition();
                    removeItem(position);
                }
            }.start();
        } else {
            holder.timeRemainTv.setText("00:00:00");
            removeItem(position);
        }
    }

    private void removeItem(final int position) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (position == -1) {
                    return;
                }
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureIv;
        TextView nameTv;
        TextView priceTv;
        TextView timeRemainTv;
        CountDownTimer timer;

        public DealViewHolder(View itemView) {
            super(itemView);
            pictureIv = itemView.findViewById(R.id.image_item);
            nameTv = itemView.findViewById(R.id.text_name);
            priceTv = itemView.findViewById(R.id.text_price);
            timeRemainTv = itemView.findViewById(R.id.text_time_remain);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
