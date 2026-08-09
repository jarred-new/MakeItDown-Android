package com.jarredapps.makeitdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.jarredapps.makeitdown.databinding.InputViewFragmentBinding;


public class InputViewFragmentActivity extends Fragment {
	
	private InputViewFragmentBinding binding;
	private SharedViewModel viewModel;
	
	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		binding = InputViewFragmentBinding.inflate(_inflater, _container, false);
		initialize(_savedInstanceState, binding.getRoot());
		initializeLogic();
		return binding.getRoot();
	}
	
	private void initialize(Bundle _savedInstanceState, View _view) {
		
		binding.materialbutton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				viewModel.sendText(binding.edittext1.getText().toString());
				Snackbar snackbar = Snackbar.make(binding.getRoot(), "Compiled Success", Snackbar.LENGTH_SHORT);
				snackbar.show();
			}
		});
		
		binding.materialbutton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		binding.materialbutton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
	}
	
	private void initializeLogic() {
		viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
	}
	
}
