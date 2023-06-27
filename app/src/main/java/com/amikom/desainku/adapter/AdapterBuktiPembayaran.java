package com.amikom.desainku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amikom.desainku.R;
import com.amikom.desainku.databinding.ItemRecyclerBuktipembayaranBinding;
import com.amikom.desainku.model.BuktiPembayaranModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterBuktiPembayaran extends RecyclerView.Adapter<AdapterBuktiPembayaran.ViewHolder> {


    private List<BuktiPembayaranModel> buktiPembayaranModels;

    private OnClickBuktiPembayaranCallback onClickBuktiPembayaranCallback;

    public AdapterBuktiPembayaran(List<BuktiPembayaranModel> buktiPembayaranModels) {
        this.buktiPembayaranModels = buktiPembayaranModels;
    }

    public void setOnClickBuktiPembayaranCallback(OnClickBuktiPembayaranCallback onClickBuktiPembayaranCallback) {
        this.onClickBuktiPembayaranCallback = onClickBuktiPembayaranCallback;
    }

    @NonNull
    @Override
    public AdapterBuktiPembayaran.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecyclerBuktipembayaranBinding itemRecyclerBuktipembayaranBinding = ItemRecyclerBuktipembayaranBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemRecyclerBuktipembayaranBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBuktiPembayaran.ViewHolder holder, int position) {
        BuktiPembayaranModel buktiPembayaranModel = buktiPembayaranModels.get(position);
        holder.binding.tvListMultiTitle.setText(buktiPembayaranModel.getIdPembayaran());
        holder.binding.tvListMultiSubtitle.setText(buktiPembayaranModel.getEmailPembayar());
        holder.binding.tvListMultiSubtitle1.setText(buktiPembayaranModel.getKeteranganPembayaran());
        holder.binding.tvPrice.setText(UtilitiesClass.formatRupiah(Integer.parseInt(buktiPembayaranModel.getPembayaran())));
        holder.binding.tvTime.setText(buktiPembayaranModel.getDateCreated());

        if(buktiPembayaranModel.getIsValid().equals("0")) {
            holder.binding.ivIndicator.setBackgroundColor(Color.RED);
        } else {
            holder.binding.ivIndicator.setBackgroundColor(Color.GREEN);
        }


        Picasso.get().load(buktiPembayaranModel.getImage()).into(holder.binding.ivListMultiImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBuktiPembayaranCallback.select(buktiPembayaranModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buktiPembayaranModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRecyclerBuktipembayaranBinding binding;

        public ViewHolder(@NonNull ItemRecyclerBuktipembayaranBinding itemRecyclerBuktipembayaranBinding) {
            super(itemRecyclerBuktipembayaranBinding.getRoot());
            this.binding = itemRecyclerBuktipembayaranBinding;
        }
    }

    public interface OnClickBuktiPembayaranCallback {
        void select(BuktiPembayaranModel buktiPembayaranModel);
    }
}
