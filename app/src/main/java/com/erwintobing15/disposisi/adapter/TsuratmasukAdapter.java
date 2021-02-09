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
import com.erwintobing15.disposisi.model.suratmasuk.SuratMasukModel;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class TsuratmasukAdapter extends RecyclerView.Adapter<TsuratmasukAdapter.TsuratmasukViewHolder> {
    private Context context;
    private List<SuratMasukModel> list;
    private TsuratmasukAdapter.Listener listener;

    public TsuratmasukAdapter(Context context, List<SuratMasukModel> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsuratmasukAdapter.TsuratmasukViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsuratmasukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsuratmasukAdapter.TsuratmasukViewHolder tsuratmasukViewHolder, int i) {
        SuratMasukModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"surat_masuk/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsuratmasukViewHolder.circleImageView);

        tsuratmasukViewHolder.tglDiterima.setText(model.getTgl_diterima());
        tsuratmasukViewHolder.isiSurat.setText(model.getIsi());
        tsuratmasukViewHolder.asalSurat.setText(model.getAsal_surat());


        if (model.getFile().isEmpty()) {
            tsuratmasukViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tsuratmasukViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TsuratmasukViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView asalSurat;

        public TsuratmasukViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            asalSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTsuratmasukLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsuratmasukClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<SuratMasukModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsuratmasukClick(String id);

        void onTsuratmasukLongClick(String id);
    }

}
