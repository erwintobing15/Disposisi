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
import com.erwintobing15.disposisi.model.SuratPengantarKeluarModel;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter view class for transaksi surat pengantar keluar (Tspkeluar)
 * date         : 08 Jan 2021 00:10 am
 * modified     : 08 Feb 2021 07:25 am
 */

public class TsuratpengantarkeluarAdapter extends RecyclerView.Adapter<TsuratpengantarkeluarAdapter.TspkeluarViewHolder> {

    private Context context;
    private List<SuratPengantarKeluarModel> list;
    private TsuratpengantarkeluarAdapter.Listener listener;

    public TsuratpengantarkeluarAdapter(Context context, List<SuratPengantarKeluarModel> list, TsuratpengantarkeluarAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratpengantarkeluarAdapter.TspkeluarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratpengantarkeluarAdapter.TspkeluarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratpengantarkeluarAdapter.TspkeluarViewHolder tspkeluarViewHolder, int i) {
        SuratPengantarKeluarModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_pengantar_keluar/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tspkeluarViewHolder.circleImageView);

        tspkeluarViewHolder.tglCatat.setText(model.getTgl_catat());
        tspkeluarViewHolder.isiSurat.setText(model.getIsi());
        tspkeluarViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tspkeluarViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tspkeluarViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TspkeluarViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglCatat;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TspkeluarViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglCatat = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratpengantarkeluarLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratpengantarkeluarClick(list.get(getAdapterPosition()).getId());
                }
            });
        }

    }

    public void replaceData(List<SuratPengantarKeluarModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratpengantarkeluarClick(String id);

        void onTsuratpengantarkeluarLongClick(String id);
    }
}
