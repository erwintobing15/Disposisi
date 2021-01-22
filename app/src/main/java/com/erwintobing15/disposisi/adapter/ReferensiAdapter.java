package com.erwintobing15.disposisi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.model.ReferensiModel;
import com.erwintobing15.disposisi.model.SuratMasukModel;

import java.util.List;

public class ReferensiAdapter extends RecyclerView.Adapter<ReferensiAdapter.ReferensiViewHolder> {

    private Context context;
    private List<ReferensiModel> list;
    private ReferensiAdapter.Listener listener;

    public ReferensiAdapter(Context context, List<ReferensiModel> list, ReferensiAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReferensiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_referensi, null);
        return new ReferensiAdapter.ReferensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferensiViewHolder referensiViewHolder, int position) {
        ReferensiModel model = list.get(position);

        referensiViewHolder.kode.setText(model.getKode());
        referensiViewHolder.nama.setText(model.getNama());
        referensiViewHolder.uraian.setText(model.getUraian());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReferensiViewHolder extends RecyclerView.ViewHolder {

        private TextView kode;
        private TextView nama;
        private TextView uraian;

        public ReferensiViewHolder(@NonNull View itemView) {
            super(itemView);

            kode = itemView.findViewById(R.id.tv_kode);
            nama = itemView.findViewById(R.id.tv_nama);
            uraian = itemView.findViewById(R.id.tv_uraian);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReferensiClick(list.get(getAdapterPosition()).getId());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onReferensiLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });
        }
    }

    public void replaceData(List<ReferensiModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onReferensiClick(String id);

        void onReferensiLongClick(String id);
    }
}
