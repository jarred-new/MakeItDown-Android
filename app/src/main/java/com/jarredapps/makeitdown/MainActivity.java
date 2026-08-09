package com.jarredapps.makeitdown;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer; // Import this class explicitly
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jarredapps.makeitdown.databinding.MainBinding;


public class MainActivity extends AppCompatActivity {
	
	private MainBinding binding;
	public String outputStr = "";
	
	private FragAdaptFragmentAdapter fragAdapt;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = MainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		binding.Toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
		setSupportActionBar(binding.Toolbar);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
//		binding.Toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View _v) {
//				onBackPressed();
//			}
//		});
		fragAdapt = new FragAdaptFragmentAdapter(getApplicationContext(), getSupportFragmentManager());
	}
	
	private void initializeLogic() {
		fragAdapt.setTabCount(2);
		binding.viewpager1.setAdapter(fragAdapt);
		binding.tablayout1.setupWithViewPager(binding.viewpager1);
		binding.tablayout1.setInlineLabel(true);
		binding.tablayout1.setTabTextColors(SketchwareUtil.getMaterialColor(MainActivity.this, R.attr.colorSecondaryContainer), SketchwareUtil.getMaterialColor(MainActivity.this, R.attr.colorOnPrimary));
		binding.tablayout1.setTabRippleColor(new android.content.res.ColorStateList(new int[][]{
				new int[]{android.R.attr.state_pressed}},
				new int[]{getResources().getColor(R.color.colorControlHighlight)})
		);
		binding.tablayout1.setSelectedTabIndicatorColor(SketchwareUtil.getMaterialColor(MainActivity.this, R.attr.colorPrimaryContainer));
		binding.tablayout1.setSelectedTabIndicatorHeight(3);
	}
	
	public class FragAdaptFragmentAdapter extends FragmentStatePagerAdapter {
		// This class is deprecated, you should migrate to ViewPager2:
		// https://developer.android.com/reference/androidx/viewpager2/widget/ViewPager2
		Context context;
		int tabCount;
		
		public FragAdaptFragmentAdapter(Context context, FragmentManager manager) {
			super(manager);
			this.context = context;
		}
		
		public void setTabCount(int tabCount) {
			this.tabCount = tabCount;
		}
		
		@Override
		public int getCount() {
			return tabCount;
		}
		
		@Override
		public CharSequence getPageTitle(int _position) {
			if (_position == 0) {
				return "Input";
			}
			if (_position == 1) {
				return "Output";
			}
			return "";
		}
		
		
		@Override
		public Fragment getItem(int _position) {
			if (_position == 0) {
				return new InputViewFragmentActivity();
			}
			if (_position == 1) {
				return new OutputViewFragmentActivity();
			}
			return new Fragment();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	

}
