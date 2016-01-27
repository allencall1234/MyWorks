package com.xj.dms.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xj.dms.common.MyApp;
import com.xj.dms.common.SlideShowView;
/**
 * 电网信息界面
 * @author qinfan
 *
 * 2015-11-6
 */
public class PowerNetworkInfoFragment extends Fragment {
	private View view;
	private TextView activePower,reactivePower,hotPower;
	private SlideShowView slideShowView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				R.layout.power_network_info_list_detail, 
				container, 
				false
		);
		init();
		return view;
	}

	private void init(){
		activePower = (TextView) view.findViewById(R.id.tv_usable_work);
		reactivePower = (TextView) view.findViewById(R.id.tv_no_usable_work);
		hotPower = (TextView) view.findViewById(R.id.tv_hot_power);

		activePower.setText(MyApp.netPowerBean.getActivePower());
		reactivePower.setText(MyApp.netPowerBean.getReactivePower());
		hotPower.setText(MyApp.netPowerBean.getHotPower());
		slideShowView = (SlideShowView) view.findViewById(R.id.slideshowView);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		slideShowView.stopPlay();
	}
}
