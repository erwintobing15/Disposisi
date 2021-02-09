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
import com.erwintobing15.disposisi.model.agendamou.AgendaMouModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TagendamouAdapter extends RecyclerView.Adapter<TagendamouAdapter.TagendamouViewHolder> {

    private Context context;
    private List<AgendaMouModel> list;
    private TagendamouAdapter.Listener listener;

    public TagendamouAdapter(Context context, List<AgendaMouModel> list, TagendamouAdapter.Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagendamouAdapter.TagendamouViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi_surat, null);
        return new TagendamouAdapter.TagendamouViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagendamouAdapter.TagendamouViewHolder tagendamouViewHolder, int i) {
        AgendaMouModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+"mou/"+model.getFile())
                .apply(new RequestOptions().error(R.drawable.doc))
                .into(tagendamouViewHolder.circleImageView);

        tagendamouViewHolder.tglDiterima.setText(model.getTgl_catat());
        tagendamouViewHolder.isiSurat.setText(model.getIsi());
        tagendamouViewHolder.tujuanSurat.setText(model.getTujuan());


        if (model.getFile().isEmpty()) {
            tagendamouViewHolder.namaFile.setText("Tidak ada file");
        } else {
            tagendamouViewHolder.namaFile.setText(model.getFile());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TagendamouViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView tglDiterima;
        private TextView namaFile;
        private TextView isiSurat;
        private TextView tujuanSurat;

        public TagendamouViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_trans_surat);
            tglDiterima = itemView.findViewById(R.id.tv_tgl_catat);
            namaFile = itemView.findViewById(R.id.tv_nama_file);
            isiSurat = itemView.findViewById(R.id.tv_isi);
            tujuanSurat = itemView.findViewById(R.id.tv_tujuan_or_asal);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onTagendamouLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTagendamouClick(list.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public void replaceData(List<AgendaMouModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onTagendamouClick(String id);

        void onTagendamouLongClick(String id);
    }

}
