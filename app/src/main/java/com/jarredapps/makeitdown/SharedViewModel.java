package com.jarredapps.makeitdown;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> textData = new MutableLiveData<>();

    // Call this from Fragment 1 to send data
    public void sendText(String text) {
        textData.setValue(text);
    }

    // Observe this in Fragment 2 to receive data
    public LiveData<String> getTextData() {
        return textData;
    }
}
