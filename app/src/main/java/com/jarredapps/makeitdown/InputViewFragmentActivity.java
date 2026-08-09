package com.jarredapps.makeitdown;

import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.jarredapps.makeitdown.databinding.InputViewFragmentBinding;


public class InputViewFragmentActivity extends Fragment {
	
	private InputViewFragmentBinding binding;
	private SharedViewModel viewModel;

	// Deprecated
//	private Intent openFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//	private Intent saveFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);

	private final ActivityResultLauncher<String[]> openDocumentLauncher = registerForActivityResult(
			new ActivityResultContracts.OpenDocument(),
			uri -> {
				if (uri != null) {
					// Process the selected file URI
					String filePath = FilePathUtil.getPathFromUri(requireContext(), uri);
					String mdTextFile = FileUtil.readFile(filePath);

					binding.edittext1.setText(mdTextFile);

					new MaterialAlertDialogBuilder(requireContext())
							.setTitle("Compile this Markdown")
							.setMessage("It is recommended to compile your Markdown document to show the document's output...\nContinue?")
							.setPositiveButton("Yes", ((dialogInterface, i) -> {
								viewModel.sendText(binding.edittext1.getText().toString());
								Snackbar snackbar = Snackbar.make(binding.getRoot(), "Compiled Success", Snackbar.LENGTH_SHORT);
								snackbar.show();
							}))
							.setNegativeButton("No", null)
							.show();

					Snackbar snackbar = Snackbar.make(binding.getRoot(), "Opened: " + filePath, Snackbar.LENGTH_LONG);
					snackbar.show();
				}
			}
	);

	private final ActivityResultLauncher<String> saveDocumentLauncher = registerForActivityResult(
			new ActivityResultContracts.CreateDocument("text/markdown"), // Default MIME type
			uri -> {
				if (uri != null) {
					// Write data directly to the content URI
					String mdFileOutput = binding.edittext1.getText().toString();

					try {
						java.io.OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri);
						if (outputStream != null) {
							outputStream.write(mdFileOutput.getBytes());
							outputStream.close();

							Snackbar snackbar = Snackbar.make(binding.getRoot(), "Saved successfully", Snackbar.LENGTH_LONG);
							snackbar.show();
						}
					} catch (java.io.IOException e) {
						Snackbar snackbar = Snackbar.make(binding.getRoot(), "Save failed: " + e.getMessage(), Snackbar.LENGTH_LONG);
						snackbar.show();
						e.printStackTrace();
					}
				}
			}
	);

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
				openDocumentLauncher.launch(new String[]{ "text/markdown" });
			}
		});
		
		binding.materialbutton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final EditText editName = new EditText(requireContext());
				editName.setHint("Enter your file name... (no adding .md at the end)");
				if (editName.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) editName.getLayoutParams();

					// Convert target DP to pixels
					int marginInPx = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP,
							16, // Your target margin in DP
							getResources().getDisplayMetrics()
					);

					// Set margins: left, top, right, bottom (in pixels)
					params.setMargins(
							marginInPx + 3,
							marginInPx - 14,
							marginInPx - 14,
							marginInPx - 14
					);

					// Apply the updated parameters back to the view
					editName.setLayoutParams(params);
				}
				editName.setInputType(InputType.TYPE_CLASS_TEXT);

				new MaterialAlertDialogBuilder(requireContext())
						.setTitle("Save Markdown File as:")
						.setView(editName)
						.setCancelable(false)
						.setPositiveButton("Save", ((dialogInterface, i) -> {
							if (editName.getText().toString().isEmpty()) {
								Snackbar snackbar = Snackbar.make(binding.getRoot(), "File name cannot be empty", Snackbar.LENGTH_SHORT);
								snackbar.show();
							}
							else {
								saveDocumentLauncher.launch(editName.getText().toString() + ".md");
							}
						}))
						.setNegativeButton("Cancel", null)
						.show();
			}
		});
	}
	
	private void initializeLogic() {
		viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
	}
	
}
