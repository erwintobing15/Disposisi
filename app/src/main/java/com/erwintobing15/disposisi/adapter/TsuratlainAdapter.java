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
import com.erwintobing15.disposisi.model.SuratLainModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsuratlainAdapter extends RecyclerView.Adapter<TsuratlainAdapter.TsuratlainViewHolder> {

    private Context context;
    private List<SuratLainModel> list;
    private TsuratlainAdapter.Listener listener;

    public TsuratlainAdapter(Context context, List<SuratLainModel> list, TsuratlainAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratlainAdapter.TsuratlainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratlainAdapter.TsuratlainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratlainAdapter.TsuratlainViewHolder tsuratlainViewHolder, int i) {
        SuratLainModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_lain/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsuratlainViewHolder.circleImageView);

        tsuratlainViewHolder.tglDiterima.setText(model.getTgl_catat());
        tsuratlainViewHolder.isiSurat.setText(model.getIsi());
        tsuratlainViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tsuratlainViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsuratlainViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsuratlainViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TsuratlainViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratlainLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratlainClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<SuratLainModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratlainClick(String id);

        void onTsuratlainLongClick(String id);
    }

}
