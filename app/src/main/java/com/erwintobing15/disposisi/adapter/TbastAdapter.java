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
import com.erwintobing15.disposisi.model.bast.BastModel;
import com.erwintobing15.disposisi.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TbastAdapter extends RecyclerView.Adapter<TbastAdapter.TbastViewHolder> {

    private Context context;
    private List<BastModel> list;
    private TbastAdapter.Listener listener;

    public TbastAdapter(Context context, List<BastModel> list, TbastAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TbastAdapter.TbastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TbastAdapter.TbastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TbastAdapter.TbastViewHolder tbastViewHolder, int i) {
        BastModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"bast/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tbastViewHolder.circleImageView);

        tbastViewHolder.tglDiterima.setText(DateUtil.formatDate(model.getTgl_catat()));
        tbastViewHolder.isiSurat.setText(model.getIsi());
        tbastViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tbastViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tbastViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TbastViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TbastViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTbastLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTbastClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<BastModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTbastClick(String id);

        void onTbastLongClick(String id);
    }

}
