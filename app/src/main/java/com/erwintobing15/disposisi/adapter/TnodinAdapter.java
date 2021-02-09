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
import com.erwintobing15.disposisi.model.nodin.NodinModel;
import com.erwintobing15.disposisi.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TnodinAdapter extends RecyclerView.Adapter<TnodinAdapter.TnodinViewHolder> {

    private Context context;
    private List<NodinModel> list;
    private TnodinAdapter.Listener listener;

    public TnodinAdapter(Context context, List<NodinModel> list, TnodinAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TnodinAdapter.TnodinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TnodinAdapter.TnodinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TnodinAdapter.TnodinViewHolder tnodinViewHolder, int i) {
        NodinModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"nodin/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tnodinViewHolder.circleImageView);

        tnodinViewHolder.tglDiterima.setText(DateUtil.formatDate(model.getTgl_catat()));
        tnodinViewHolder.isiSurat.setText(model.getIsi());
        tnodinViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tnodinViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tnodinViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TnodinViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TnodinViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTnodinLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTnodinClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<NodinModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTnodinClick(String id);

        void onTnodinLongClick(String id);
    }

}
