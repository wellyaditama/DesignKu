package com.amikom.desainku.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amikom.desainku.databinding.ItemRecyclerBookingBinding;
import com.amikom.desainku.model.DesignBookingModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterBookingService extends RecyclerView.Adapter<AdapterBookingService.ViewHolder> implements Filterable {

    private List<DesignBookingModel> designBookingModels;
    private List<DesignBookingModel> designBookingModelsFull;

    private OnClickBookingCallback onClickBookingCallback;

    public AdapterBookingService(List<DesignBookingModel> bookingModelList) {
        this.designBookingModels = bookingModelList;
        designBookingModelsFull = new ArrayList<>(designBookingModels);
    }

    public void setOnClickBookingCallback(OnClickBookingCallback onClickBookingCallback) {
        this.onClickBookingCallback = onClickBookingCallback;
    }



    @NonNull
    @Override
    public AdapterBookingService.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecyclerBookingBinding itemRecyclerBookingBinding = ItemRecyclerBookingBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemRecyclerBookingBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBookingService.ViewHolder holder, int position) {
        DesignBookingModel designBookingModel = designBookingModels.get(position);

        holder.itemRecyclerBookingBinding.tvListMultiTitle.setText(designBookingModel.getNamaPemesan());
        holder.itemRecyclerBookingBinding.tvListMultiSubtitle.setText(designBookingModel.getEmailPemesan());
        holder.itemRecyclerBookingBinding.tvListMultiSubtitle1.setText(designBookingModel.getKeterangan());

        if(designBookingModel.getStatusPembayaran().equals("1")) {
            holder.itemRecyclerBookingBinding.tvPrice.setText(UtilitiesClass.statusPembayaran[0]);
        } else {
            holder.itemRecyclerBookingBinding.tvPrice.setText(UtilitiesClass.statusPembayaran[1]);
        }

        if(designBookingModel.getStatusPengerjaan().equals("1")) {
            holder.itemRecyclerBookingBinding.tvTime.setText(UtilitiesClass.statusPengerjaan[0]);
        } else if(designBookingModel.getStatusPengerjaan().equals("2")) {
            holder.itemRecyclerBookingBinding.tvTime.setText(UtilitiesClass.statusPengerjaan[1]);
        } else if(designBookingModel.getStatusPengerjaan().equals("3")) {
            holder.itemRecyclerBookingBinding.tvTime.setText(UtilitiesClass.statusPengerjaan[2]);
        } else {
            holder.itemRecyclerBookingBinding.tvTime.setText(UtilitiesClass.statusPengerjaan[3]);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBookingCallback.select(designBookingModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return designBookingModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRecyclerBookingBinding itemRecyclerBookingBinding;
        public ViewHolder(@NonNull ItemRecyclerBookingBinding itemView) {
            super(itemView.getRoot());
            this.itemRecyclerBookingBinding = itemView;
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List <DesignBookingModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(designBookingModelsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(DesignBookingModel designBookingModel : designBookingModelsFull) {
                    if(designBookingModel.getNamaPemesan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(designBookingModel);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            designBookingModels.clear();
            designBookingModels.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnClickBookingCallback {
        void select(DesignBookingModel bookingModel);
    }
}
