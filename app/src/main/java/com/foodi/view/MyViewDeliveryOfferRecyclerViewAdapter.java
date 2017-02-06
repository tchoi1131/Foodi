package com.foodi.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.view.ViewDeliveryOfferFragment.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
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

    public MyViewDeliveryOfferRecyclerViewAdapter(List<String> keys, List<DeliveryOffer> items, OnListFragmentInteractionListener listener) {
        mKeys = keys;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_del_offer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mKey=mKeys.get(position);
        holder.mItem = mValues.get(position);
        holder.mDriverNameView.setText(mValues.get(position).getdriverName());
        holder.mOfferPriceView.setText(Double.toString(mValues.get(position).getOfferPrice()));
        SimpleDateFormat dateTimeformat = new SimpleDateFormat("hh:mm aa");
        holder.mEstimatedDeliveryTimeView.setText(dateTimeformat.format(mValues.get(position).getEstimatedDeliveryTime()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onViewDeliveryOfferFragmentInteraction(holder.mKey,holder.mItem,DeliveryOffer.DELIVERY_Offer_STATUS_CUSTOMER_CONFIRMED);
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
        public DeliveryOffer mItem;
        public String mKey;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDriverNameView = (TextView) view.findViewById(R.id.driver_name);
            mOfferPriceView = (TextView) view.findViewById(R.id.offer_price);
            mEstimatedDeliveryTimeView = (TextView) view.findViewById(R.id.est_del_time);
        }
    }
}
