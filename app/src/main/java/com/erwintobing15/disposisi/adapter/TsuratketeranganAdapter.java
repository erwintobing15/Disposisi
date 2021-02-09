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
import com.erwintobing15.disposisi.model.suratketerangan.SuratKeteranganModel;
import com.erwintobing15.disposisi.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsuratketeranganAdapter extends RecyclerView.Adapter<TsuratketeranganAdapter.TsuratketeranganViewHolder> {

    private Context context;
    private List<SuratKeteranganModel> list;
    private TsuratketeranganAdapter.Listener listener;

    public TsuratketeranganAdapter(Context context, List<SuratKeteranganModel> list, TsuratketeranganAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratketeranganAdapter.TsuratketeranganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratketeranganAdapter.TsuratketeranganViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratketeranganAdapter.TsuratketeranganViewHolder tsuratketeranganViewHolder, int i) {
        SuratKeteranganModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_keterangan/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsuratketeranganViewHolder.circleImageView);

        tsuratketeranganViewHolder.tglDiterima.setText(DateUtil.formatDate(model.getTgl_catat()));
        tsuratketeranganViewHolder.isiSurat.setText(model.getIsi());
        tsuratketeranganViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tsuratketeranganViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsuratketeranganViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsuratketeranganViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TsuratketeranganViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratketeranganLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratketeranganClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<SuratKeteranganModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratketeranganClick(String id);

        void onTsuratketeranganLongClick(String id);
    }

}
