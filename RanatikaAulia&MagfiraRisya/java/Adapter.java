package rpa.mobile.forecastingweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter {

    private final Context ctx;
    private List<Weather> cuaca;

    public Adapter(Context ctx, List<Weather> cuaca) {
        this.ctx = ctx;
        this.cuaca = cuaca;
    }

    // Class View Holder
    class VHWeather extends RecyclerView.ViewHolder{

        public TextView tvtanggal, tvDailyPred;
        public ImageView ikon;

        public VHWeather(@NonNull View itemView) {
            super(itemView);
            this.tvtanggal = itemView.findViewById(R.id.tvDate);
            this.tvDailyPred = itemView.findViewById(R.id.tvDaily);
            this.ikon = itemView.findViewById(R.id.imgIcon);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHWeather(
                LayoutInflater.from(this.ctx).inflate(R.layout.daily_update, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Weather w = this.cuaca.get(position);
        VHWeather vh = (VHWeather) holder;
        vh.tvtanggal.setText(w.getTanggal());
        vh.tvDailyPred.setText(w.getPrediction());
        vh.ikon.setImageResource(w.getGambar());
    }

    @Override
    public int getItemCount() {
        return this.cuaca.size();
    }
}