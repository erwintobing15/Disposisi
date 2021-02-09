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
import com.erwintobing15.disposisi.model.suratperintah.SuratPerintahModel;
import com.erwintobing15.disposisi.util.DateUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsuratperintahAdapter extends RecyclerView.Adapter<TsuratperintahAdapter.TsuratperintahViewHolder> {

    private Context context;
    private List<SuratPerintahModel> list;
    private TsuratperintahAdapter.Listener listener;

    public TsuratperintahAdapter(Context context, List<SuratPerintahModel> list, TsuratperintahAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratperintahAdapter.TsuratperintahViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratperintahAdapter.TsuratperintahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratperintahAdapter.TsuratperintahViewHolder tsuratperintahViewHolder, int i) {
        SuratPerintahModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_perintah/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsuratperintahViewHolder.circleImageView);

        tsuratperintahViewHolder.tglDiterima.setText(DateUtil.formatDate(model.getTgl_catat()));
        tsuratperintahViewHolder.isiSurat.setText(model.getIsi());
        tsuratperintahViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tsuratperintahViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsuratperintahViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsuratperintahViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TsuratperintahViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratperintahLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratperintahClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<SuratPerintahModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratperintahClick(String id);

        void onTsuratperintahLongClick(String id);
    }
}
