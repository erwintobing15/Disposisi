package com.erwintobing15.disposisi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.erwintobing15.disposisi.R;
import com.erwintobing15.disposisi.config.Constants;
import com.erwintobing15.disposisi.model.SptjmModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsptjmAdapter extends RecyclerView.Adapter<TsptjmAdapter.TsptjmViewHolder> {

    private Context context;
    private List<SptjmModel> list;
    private TsptjmAdapter.Listener listener;

    public TsptjmAdapter(Context context, List<SptjmModel> list, TsptjmAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsptjmAdapter.TsptjmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsptjmAdapter.TsptjmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsptjmAdapter.TsptjmViewHolder tsptjmViewHolder, int i) {
        SptjmModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"sptjm/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsptjmViewHolder.circleImageView);

        tsptjmViewHolder.tglDiterima.setText(model.getTgl_catat());
        tsptjmViewHolder.isiSurat.setText(model.getIsi());
        tsptjmViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tsptjmViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsptjmViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsptjmViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TsptjmViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsptjmLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsptjmClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<SptjmModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsptjmClick(String id);

        void onTsptjmLongClick(String id);
    }

}
