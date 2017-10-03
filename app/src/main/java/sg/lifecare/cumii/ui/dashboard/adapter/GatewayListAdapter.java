package sg.lifecare.cumii.ui.dashboard.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.lifecare.cumii.R;
import sg.lifecare.cumii.data.server.response.ConnectedDeviceResponse;
import sg.lifecare.cumii.util.DateUtils;

public class GatewayListAdapter extends RecyclerView.Adapter<GatewayListAdapter.DeviceViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private ArrayList<ConnectedDeviceResponse.Data> mGateways = new ArrayList<>();

    private OnItemClickListener mListener;

    public GatewayListAdapter(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_gateway, parent, false);


        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mGateways.size();
    }

    public void replaceGateways(List<ConnectedDeviceResponse.Data> gateways) {
        mGateways.clear();

        if (gateways != null) {
            mGateways.addAll(gateways);
        }

        notifyDataSetChanged();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_image)
        ImageView mDeviceImage;

        @BindView(R.id.name_text)
        TextView mNameText;

        @BindView(R.id.message_text)
        TextView mMessageText;

        @BindView(R.id.timestamp_text)
        TextView mTimestampText;

        DeviceViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            ConnectedDeviceResponse.Data gateway = mGateways.get(position);

            String image = gateway.getImageUrl();

            if (!TextUtils.isEmpty(image)) {
                Glide.with(itemView.getContext())
                        .load(image)
                        .into(mDeviceImage);
            }

            mNameText.setText(gateway.getName());

            if ("N".equalsIgnoreCase(gateway.getStatus())) {
                mMessageText.setVisibility(View.VISIBLE);
                mMessageText.setText(itemView.getContext().getString(R.string.label_online));
                mMessageText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.online));
            } else if ("F".equalsIgnoreCase(gateway.getStatus())) {
                mMessageText.setVisibility(View.VISIBLE);
                mMessageText.setText(itemView.getContext().getString(R.string.label_offline));
                mMessageText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.offline));
            } else {
                mMessageText.setVisibility(View.INVISIBLE);
            }

            if (gateway.getLastUpdate() != null) {
                mTimestampText.setText(DateUtils.getDisplayTimestamp(itemView.getContext(), gateway.getLastUpdate()));
            }

            if (mListener != null) {
                itemView.setOnClickListener(v -> mListener.onItemClick(position));
            }
        }
    }
}