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
import com.erwintobing15.disposisi.model.SUMasukModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TsumasukAdapter extends RecyclerView.Adapter<TsumasukAdapter.TsumasukViewHolder>{

    private Context context;
    private List<SUMasukModel> list;
    private TsumasukAdapter.Listener listener;

    public TsumasukAdapter(Context context, List<SUMasukModel> list, TsumasukAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TsumasukAdapter.TsumasukViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TsumasukAdapter.TsumasukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TsumasukAdapter.TsumasukViewHolder tsmasukViewHolder, int i) {
        SUMasukModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"su_masuk/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tsmasukViewHolder.circleImageView);

        tsmasukViewHolder.tglCatat.setText(model.getTgl_catat());
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
                    listener.onTsumasukLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTsumasukClick(list.get(getAdapterPosition()).getId());
                }
            });
        }

    }

    public void replaceData(List<SUMasukModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTsumasukClick(String id);

        void onTsumasukLongClick(String id);
    }
}
