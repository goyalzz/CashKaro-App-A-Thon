package cashkaro.pronto.com.cashkaro.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cashkaro.pronto.com.cashkaro.R;
import cashkaro.pronto.com.cashkaro.controller.activities.HomeActivity;
import cashkaro.pronto.com.cashkaro.dto.HomePageListViewDto;
import cashkaro.pronto.com.cashkaro.dto.HomePageListViewType;

/**
 * Created by Ankush Goyal on 17/04/17.
 */

public class HomeScreenRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context _context;

    private List<HomePageListViewDto> _data;

    private ItemClick clickListener;

    public HomeScreenRecyclerAdapter(Context context, List<HomePageListViewDto> data) {
        this._context = context;
        this._data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewPagerViewHolder(LayoutInflater.from(_context).inflate(R.layout.sliding_images_layout, null));
            case 1:
                return new SingleViewHolder(LayoutInflater.from(_context).inflate(R.layout.list_item_grid, null));
            case 2:
                return new GridViewHolder(LayoutInflater.from(_context).inflate(R.layout.list_item_grid, null));
            default:
                return new BottomViewHolder(LayoutInflater.from(_context).inflate(R.layout.list_item_grid, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewPagerViewHolder)
            ((ViewPagerViewHolder) holder).onBindUi(this._data.get(position));
        if(holder instanceof SingleViewHolder)
            ((SingleViewHolder) holder).onBindUi(this._data.get(position));
        if(holder instanceof GridViewHolder)
            ((GridViewHolder) holder).onBindUi(this._data.get(position));
        if(holder instanceof BottomViewHolder)
            ((BottomViewHolder) holder).onBindUi(this._data.get(position));
    }

    private class ViewPagerViewHolder extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener {

        private ViewPager intro_images;
        private LinearLayout pager_indicator;
        private int dotsCount;
        private ImageView[] dots;
        private ViewPagerAdapter mAdapter;
        private TextView heading;

        private String[] mImageResources = {
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png",
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png",
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png",
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png",
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png",
                "http://asset6.ckassets.com/resources/image/slider_images/ck-storepage-v2/1x4/slide3.1492408550.png"
        };

        private ViewPagerViewHolder(View itemView) {
            super(itemView);
            intro_images = (ViewPager) itemView.findViewById(R.id.pager_introduction);

            pager_indicator = (LinearLayout) itemView.findViewById(R.id.viewPagerCountDots);

            mAdapter = new ViewPagerAdapter(_context, mImageResources);
            intro_images.setAdapter(mAdapter);
            intro_images.setCurrentItem(0);
            intro_images.setOnPageChangeListener(this);
            setUiPageViewController();
        }

        private void setUiPageViewController() {

            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(_context);
                dots[i].setImageDrawable(_context.getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }

            dots[0].setImageDrawable(_context.getResources().getDrawable(R.drawable.selecteditem_dot));
        }

        private void onBindUi(HomePageListViewDto data) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(_context.getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }
            dots[position].setImageDrawable(_context.getResources().getDrawable(R.drawable.selecteditem_dot));
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView heading;
        private TextView offer;
        private String amazonLogoUrl = "http://static.couponspy.in/picture/shop/630.png";
        private ImageView logo;

        private SingleViewHolder(View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.title_list_item_grid);
            offer = (TextView) itemView.findViewById(R.id.offer);
            logo = (ImageView) itemView.findViewById(R.id.home_list_logo1);
            CardView view = (CardView) itemView.findViewById(R.id.card_view1);
            view.setVisibility(View.INVISIBLE);
        }

        private void onBindUi(final HomePageListViewDto data) {
            Picasso.with(_context).load(amazonLogoUrl).into(logo);
            heading.setText(data.getTitle().get(0));
            offer.setText(data.getTitle().get(1));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null)
                        clickListener.onItemClickListener(data);
                }
            });
        }

    }

    private class GridViewHolder extends RecyclerView.ViewHolder {

        private TextView heading;
        private TextView offer, offer1;
        private String amazonLogoUrl = "http://static.couponspy.in/picture/shop/630.png";
        private String snapdeallogoUrl = "http://mediaindia.eu/wp-content/uploads/2016/12/snapdeal-new-logo-change.png?w=640";
        private ImageView logo, logo1;

        private GridViewHolder(View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.title_list_item_grid);
            offer = (TextView) itemView.findViewById(R.id.offer);
            offer1 = (TextView) itemView.findViewById(R.id.offer1);
            logo = (ImageView) itemView.findViewById(R.id.home_list_logo);
            logo1 = (ImageView) itemView.findViewById(R.id.home_list_logo1);
        }

        private void onBindUi(final HomePageListViewDto data) {
            Picasso.with(_context).load(amazonLogoUrl).into(logo);
            Picasso.with(_context).load(snapdeallogoUrl).into(logo1);
            heading.setText(data.getTitle().get(0));
            offer.setText(data.getTitle().get(1));
            offer1.setText(data.getTitle().get(2));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null)
                        clickListener.onItemClickListener(data);
                }
            });
        }

    }

    private class BottomViewHolder extends RecyclerView.ViewHolder {

        private TextView heading;

        private BottomViewHolder(View itemView) {
            super(itemView);
        }

        private void onBindUi(HomePageListViewDto data) {

        }

    }

    @Override
    public int getItemCount() {
        return this._data.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(this._data.get(position).getType().equals(HomePageListViewType.VIEW_PAGER))
            return 0;
        else if(this._data.get(position).getType().equals(HomePageListViewType.SINGLE_VIEW))
            return 1;
        else if(this._data.get(position).getType().equals(HomePageListViewType.GRID_VIEW))
            return 2;
        else
            return 3;
    }

    public void replace(List<HomePageListViewDto> data) {
        this._data.clear();
        this._data.addAll(data);
        notifyDataSetChanged();
    }

    public void add(HomePageListViewDto data) {
        this._data.add(data);
        notifyItemInserted(getItemCount() - 1);
    }

    public void add(List<HomePageListViewDto> data) {
        this._data.addAll(data);
        notifyItemInserted(getItemCount() - 1);
    }

    public void setOnItemClickListener(ItemClick clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClick {
        void onItemClickListener(HomePageListViewDto data);
    }

}
