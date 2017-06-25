package com.cockpunchers.commutator2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cockpunchers.commutator2.RideChatFragment;
import com.cockpunchers.commutator2.RidesCreatedFragment;
import com.cockpunchers.commutator2.RidersFragment;
//import com.cockpunchers.commutator2.SearchRideFragment;

public class TabsPagerAdapter2 extends FragmentPagerAdapter{

	public TabsPagerAdapter2(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) 
		{
		case 0:
			// Create new ride fragment activity
			return new RidesCreatedFragment();
		case 1:
			// Search for exisiting ride fragment activity
			return new RidersFragment();
			
			
		case 2:
			// History fragment activity
			return new RideChatFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	
}
