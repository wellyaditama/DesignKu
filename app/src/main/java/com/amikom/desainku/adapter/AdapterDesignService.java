package com.amikom.desainku.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amikom.desainku.databinding.ItemRecyclerBinding;
import com.amikom.desainku.model.DesignServiceModel;
import com.amikom.desainku.utility.UtilitiesClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterDesignService extends RecyclerView.Adapter<AdapterDesignService.ViewHolder> implements Filterable {

    private List<DesignServiceModel> serviceModelList;
    private List<DesignServiceModel> serviceModelListFull;

    private OnClickServiceCallback onClickServiceCallback;

    public AdapterDesignService(List<DesignServiceModel> serviceModelList) {
        this.serviceModelList = serviceModelList;
        serviceModelListFull = new ArrayList<>(serviceModelList);
    }

    public void setOnClickServiceCallback(OnClickServiceCallback onClickServiceCallback) {
        this.onClickServiceCallback = onClickServiceCallback;
    }



    @NonNull
    @Override
    public AdapterDesignService.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecyclerBinding itemRecyclerBinding = ItemRecyclerBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(itemRecyclerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDesignService.ViewHolder holder, int position) {
        DesignServiceModel designServiceModel = serviceModelList.get(position);
        holder.itemRecyclerBinding.tvListMultiTitle.setText(designServiceModel.getNamaJasa());
        holder.itemRecyclerBinding.tvListMultiSubtitle.setText(designServiceModel.getKeterangan());
        holder.itemRecyclerBinding.tvPrice.setText(UtilitiesClass.formatRupiah(designServiceModel.getHarga()));
        holder.itemRecyclerBinding.tvTime.setText(String.valueOf(designServiceModel.getLamapengerjaan()) + " hari");

        Picasso.get().load(designServiceModel.getGambar()).into(holder.itemRecyclerBinding.ivListMultiImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickServiceCallback.select(designServiceModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List <DesignServiceModel> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(serviceModelListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DesignServiceModel designServiceModel : serviceModelListFull) {
                    if(designServiceModel.getNamaJasa().toLowerCase().contains(filterPattern)) {
                        filteredList.add(designServiceModel);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            serviceModelList.clear();
            serviceModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRecyclerBinding itemRecyclerBinding;
        public ViewHolder(@NonNull ItemRecyclerBinding itemView) {
            super(itemView.getRoot());
            this.itemRecyclerBinding = itemView;
        }
    }

    public interface OnClickServiceCallback {
        void select(DesignServiceModel serviceModel);
    }
}
