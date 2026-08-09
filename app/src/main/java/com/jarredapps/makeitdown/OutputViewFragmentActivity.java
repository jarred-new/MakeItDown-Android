package com.jarredapps.makeitdown;

import android.os.Bundle;
import android.view.LayoutInflater;
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
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		binding = OutputViewFragmentBinding.inflate(_inflater, _container, false);
		initialize(_savedInstanceState, binding.getRoot());
		initializeLogic();
		return binding.getRoot();
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
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
	
}
