package com.recyclerview.gridview.test.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.image.ImageLoad;
import com.recyclerview.gridview.test.base.recyclerview.adapter.BaseViewHolder;
import com.recyclerview.gridview.test.base.recyclerview.adapter.PullRefreshDelegate;

import java.util.List;

/**
 * Created by pc on 2017/3/3.
 */

public class InformationDelegate extends PullRefreshDelegate
{

    private Context mContext;

    public InformationDelegate(Context context, int dataCount) {
        super(R.layout.item_information_style, dataCount);
        this.mContext = context;
    }

    @Override
    public int getLayoutId(int viewType) {
        int id;
        switch (viewType) {
            case MyInformationAdapter.PROMOTION_IMAGE_ONE:
                id = R.layout.item_information_style;
                break;
            case MyInformationAdapter.RECYCLE_TYPE_FOOTER:
                id = com.app.frame.R.layout.item_footer;
                break;
            default:
                 id = R.layout.item_information_style2;
                break;
        }
        return id;
    }

    @Override
    public void initCustomView(BaseViewHolder holder, Object obj, final int position) {
        final List<InformationResult.DataBean.ListBean> data = (List<InformationResult.DataBean.ListBean>) obj;

        RelativeLayout imageRy = (RelativeLayout) holder.findViewById(R.id.imageRy);
        ImageView imageView1 = (ImageView) holder.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) holder.findViewById(R.id.image2);
        ImageView imageView0 = (ImageView) holder.findViewById(R.id.image0);
        TextView title = (TextView) holder.findViewById(R.id.title);
        TextView comeFrom = (TextView) holder.findViewById(R.id.comeFrom);
        TextView time = (TextView) holder.findViewById(R.id.time);
        if (data.get(position).getImgs().size() == 1) {
        } else {
            if (data.get(position).getImgs().size() == 0) {
                // why  null 指针
                imageRy.setVisibility(View.GONE);

            } else {
                imageRy.setVisibility(View.VISIBLE);
                if (data.get(position).getImgs().size() > 1) {
                    ImageLoad.loadImage(mContext, data.get(position).getImgs().get(0), R.drawable.default_img, imageView1);
                } else {
                    imageView1.setVisibility(View.VISIBLE);
                }

                if (data.get(position).getImgs().size() > 2) {
                    ImageLoad.loadImage(mContext, data.get(position).getImgs().get(0), R.drawable.default_img, imageView2);
                } else {
                    imageView2.setVisibility(View.VISIBLE);
                }
            }
        }
        if (data.get(position).getImgs().size() != 0) {
            ImageLoad.loadImage(mContext, data.get(position).getImgs().get(0), R.drawable.default_img, imageView0);
        }
        title.setText(data.get(position).getTitle());
        comeFrom.setText(  data.get(position).getSource());
        time.setText( Long.valueOf(data.get(position).getTime())+"");
    }
}
