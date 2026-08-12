package com.jarredapps.makeitdown;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jarredapps.makeitdown.databinding.OutputViewFragmentBinding;

import io.noties.markwon.Markwon;


public class OutputViewFragmentActivity extends Fragment {
	
	private OutputViewFragmentBinding binding;
	private SharedViewModel viewModel;
	private ScaleGestureDetector scaleGestureDetector;
	private float textSize = 18f;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		binding = OutputViewFragmentBinding.inflate(_inflater, _container, false);
		initialize(_savedInstanceState, binding.getRoot());
		initializeLogic();
		return binding.getRoot();
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());

		binding.textview1.setOnTouchListener(new View.OnTouchListener() {
			//@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Pass touch data to detector
				scaleGestureDetector.onTouchEvent(event);

				// If two fingers are touching, tell ViewPager not to steal the event
				if (event.getPointerCount() > 1) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
				}
				return true;
			}
		});
	}
	
	private void initializeLogic() {
		viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
		
		viewModel.getTextData().observe(getViewLifecycleOwner(), new Observer<String>() {
			@Override
			public void onChanged(String newText) {
				Markwon markwon = Markwon.create(requireContext());
				markwon.setMarkdown(binding.textview1, newText);
			}
		});            
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			textSize *= detector.getScaleFactor();

			// Constrain text sizing bounds
			textSize = Math.max(12.0f, Math.min(textSize, 100.0f));

			binding.textview1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			return true;
		}
	}
}
