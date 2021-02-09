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
import com.erwintobing15.disposisi.model.PerjanjianKerjasamaModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TperjanjiankerjasamaAdapter extends RecyclerView.Adapter<TperjanjiankerjasamaAdapter.TperjanjiankerjasamaViewHolder> {

    private Context context;
    private List<PerjanjianKerjasamaModel> list;
    private TperjanjiankerjasamaAdapter.Listener listener;

    public TperjanjiankerjasamaAdapter(Context context, List<PerjanjianKerjasamaModel> list, TperjanjiankerjasamaAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TperjanjiankerjasamaAdapter.TperjanjiankerjasamaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TperjanjiankerjasamaAdapter.TperjanjiankerjasamaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TperjanjiankerjasamaAdapter.TperjanjiankerjasamaViewHolder tperjanjiankerjasamaViewHolder, int i) {
        PerjanjianKerjasamaModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"perjanjian_kerjasama/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tperjanjiankerjasamaViewHolder.circleImageView);

        tperjanjiankerjasamaViewHolder.tglDiterima.setText(model.getTgl_catat());
        tperjanjiankerjasamaViewHolder.isiSurat.setText(model.getIsi());
        tperjanjiankerjasamaViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tperjanjiankerjasamaViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tperjanjiankerjasamaViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TperjanjiankerjasamaViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TperjanjiankerjasamaViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTperjanjiankerjasamaLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTperjanjiankerjasamaClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<PerjanjianKerjasamaModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTperjanjiankerjasamaClick(String id);

        void onTperjanjiankerjasamaLongClick(String id);
    }

}
