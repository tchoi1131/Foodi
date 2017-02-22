package com.foodi.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CpuUsageInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.view.ViewDeliveryOfferFragment.OnListFragmentInteractionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyViewDeliveryOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyViewDeliveryOfferRecyclerViewAdapter.ViewHolder> {

    private final List<String> mKeys;
    private final List<DeliveryOffer> mValues;
    private final OnListFragmentInteractionListener mListener;

    private SimpleDateFormat storedTimeFormat;
    private SimpleDateFormat displayTimeFormat;

    public MyViewDeliveryOfferRecyclerViewAdapter(List<String> keys, List<DeliveryOffer> items, OnListFragmentInteractionListener listener) {
        mKeys = keys;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_del_offer, parent, false);
        storedTimeFormat = new SimpleDateFormat(view.getResources().getString(R.string.stored_date_format));
        displayTimeFormat = new SimpleDateFormat(view.getResources().getString(R.string.display_time_format));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mKey=mKeys.get(position);
        holder.mItem = mValues.get(position);
        holder.mDriverNameView.setText(mValues.get(position).getDriverName());
        holder.mOfferPriceView.setText(Double.toString(mValues.get(position).getOfferPrice()));
        Calendar estDelvieryCal = Calendar.getInstance();

        try {
            estDelvieryCal.setTime(storedTimeFormat.parse(mValues.get(position).getEstimatedDeliveryTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mEstimatedDeliveryTimeView.setText(displayTimeFormat.format(estDelvieryCal.getTime()));
        holder.mOfferStatusView.setText(mValues.get(position).getOfferStatus());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onViewDeliveryOfferFragmentInteraction(holder.mKey,holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDriverNameView;
        public final TextView mOfferPriceView;
        public final TextView mEstimatedDeliveryTimeView;
        public final TextView mOfferStatusView;
        public DeliveryOffer mItem;
        public String mKey;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDriverNameView = (TextView) view.findViewById(R.id.driver_name);
            mOfferPriceView = (TextView) view.findViewById(R.id.offer_price);
            mEstimatedDeliveryTimeView = (TextView) view.findViewById(R.id.est_del_time);
            mOfferStatusView = (TextView) view.findViewById(R.id.offer_status);
        }
    }
}
