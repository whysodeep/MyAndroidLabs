package algonquin.cst2355.rama0110.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> editString = new MutableLiveData<>();
    private String editStringValue;

    private MutableLiveData<Boolean> checkboxState = new MutableLiveData<>();
    private MutableLiveData<Boolean> switchState = new MutableLiveData<>();
    private MutableLiveData<Integer> radioButtonState = new MutableLiveData<>();

    public void setEditTextValue(String editString) {
        this.editStringValue = editString;
    }

    public MutableLiveData<Boolean> setCheckboxState() {
        return checkboxState;
    }

    public MutableLiveData<Boolean> setSwitchState() {
        return switchState;
    }

    public MutableLiveData<Integer> setRadioButtonState() {
        return radioButtonState;
    }
}