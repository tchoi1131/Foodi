package com.foodi.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.foodi.foodi.R;
import com.foodi.model.DeliveryOffer;
import com.foodi.model.SysConfig;
import com.foodi.view.ViewDeliveryOfferFragment.OnListFragmentInteractionListener;
import java.text.ParseException;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DeliveryOffer} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyViewDeliveryOfferRecyclerViewAdapter extends RecyclerView.Adapter<MyViewDeliveryOfferRecyclerViewAdapter.ViewHolder> {

    private final List<String> mKeys;                           //Delivery offer keys
    private final List<DeliveryOffer> mDelivaryOffers;          //Delivery offer objects
    private final OnListFragmentInteractionListener mListener;  //listener to communicate with MainMenuActivity

    public MyViewDeliveryOfferRecyclerViewAdapter(List<String> keys, List<DeliveryOffer> items, OnListFragmentInteractionListener listener) {
        mKeys = keys;
        mDelivaryOffers = items;
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
        holder.mItem = mDelivaryOffers.get(position);
        holder.mDriverNameView.setText(mDelivaryOffers.get(position).getDriverName());
        holder.mOfferPriceView.setText(Double.toString(mDelivaryOffers.get(position).getOfferPrice()));

        try {
            holder.mEstimatedDeliveryTimeView.setText(SysConfig.getDisplayTime(mDelivaryOffers.get(position).getEstimatedDeliveryTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mOfferStatusView.setText(mDelivaryOffers.get(position).getOfferStatus());

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
        return mDelivaryOffers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDriverNameView;              //TextView to store Driver Name
        public final TextView mOfferPriceView;              //TextView to store offer price
        public final TextView mEstimatedDeliveryTimeView;   //TextView to store Estimated Delivery Time
        public final TextView mOfferStatusView;             //TextView to store Offer status
        public DeliveryOffer mItem;                         //DeliveryOffer Object
        public String mKey;                                 //key of the offer

        public ViewHolder(View view) {
            super(view);
            //setup UI references
            mView = view;
            mDriverNameView = (TextView) view.findViewById(R.id.driver_name);
            mOfferPriceView = (TextView) view.findViewById(R.id.offer_price);
            mEstimatedDeliveryTimeView = (TextView) view.findViewById(R.id.est_del_time);
            mOfferStatusView = (TextView) view.findViewById(R.id.offer_status);
        }
    }
}
