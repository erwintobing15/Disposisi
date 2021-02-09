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
import com.erwintobing15.disposisi.model.suratmasukundangan.SuratMasukUndanganModel;
import com.erwintobing15.disposisi.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsuratmasukundanganAdapter extends RecyclerView.Adapter<TsuratmasukundanganAdapter.TsumasukViewHolder>{

    private Context context;
    private List<SuratMasukUndanganModel> list;
    private TsuratmasukundanganAdapter.Listener listener;

    public TsuratmasukundanganAdapter(Context context, List<SuratMasukUndanganModel> list, TsuratmasukundanganAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratmasukundanganAdapter.TsumasukViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratmasukundanganAdapter.TsumasukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratmasukundanganAdapter.TsumasukViewHolder tsmasukViewHolder, int i) {
        SuratMasukUndanganModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_masuk_undangan/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsmasukViewHolder.circleImageView);

        tsmasukViewHolder.tglCatat.setText(DateUtil.formatDate(model.getTgl_catat()));
        tsmasukViewHolder.isiSurat.setText(model.getIsi());
        tsmasukViewHolder.asalSurat.setText(model.getAsal_surat());


        if (model.getFile().isEmpty()) {
            tsmasukViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsmasukViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsumasukViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglCatat;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView asalSurat;

        public TsumasukViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglCatat = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            asalSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratmasukundanganLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratmasukundanganClick(list.get(getAdapterPosition()).getId());
                }
            });
        }

    }

    public void replaceData(List<SuratMasukUndanganModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratmasukundanganClick(String id);

        void onTsuratmasukundanganLongClick(String id);
    }
}
