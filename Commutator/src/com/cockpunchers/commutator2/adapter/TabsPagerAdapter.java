package com.cockpunchers.commutator2.adapter;

import com.cockpunchers.commutator2.CreateRideFragment;
import com.cockpunchers.commutator2.SearchRideFragment;
import com.cockpunchers.commutator2.RideHistoryFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Search for exisiting ride fragment activity
			return new SearchRideFragment();
		case 1:
			//Create new ride fragment activity
			return new CreateRideFragment();
			case 2:
			// History fragment activity
			return new RideHistoryFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	
}
