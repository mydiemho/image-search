package myho.gridimagesearch.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import myho.gridimagesearch.R;
import myho.gridimagesearch.models.FilterInfo;

import static android.view.View.OnClickListener;

public class SearchFiltersDialog extends DialogFragment implements OnClickListener {

    public interface SearchFiltersDialogListener {
        void onFinishSelectFilters(FilterInfo filterInfo);
    }

    private Spinner spinnerImageSizes;
    private Spinner spinnerImageColors;
    private Spinner spinnerImageTypes;
    private EditText etSite;
    private Button btnApply;

    private FilterInfo filterInfo;

    public SearchFiltersDialog() {
        // empty constructor required
    }

    public static SearchFiltersDialog newInstance(String title) {
        SearchFiltersDialog searchFiltersDialog = new SearchFiltersDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        searchFiltersDialog.setArguments(args);
        return searchFiltersDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        filterInfo = (FilterInfo) getArguments().getSerializable("filterInfo");

        View view = inflater.inflate(R.layout.fragment_search_filters, container);

        setUpViews(view);
        setUpFilters();

        btnApply = (Button) view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        String title = getArguments().getString("title", "Select Filters");
        getDialog().setTitle(title);

        return view;
    }

    private void setUpViews(View view) {
        spinnerImageTypes = (Spinner) view.findViewById(R.id.spinnerImageTypes);
        spinnerImageColors = (Spinner) view.findViewById(R.id.spinnerImageColors);
        spinnerImageSizes = (Spinner) view.findViewById(R.id.spinnerImageSizes);
        etSite = (EditText) view.findViewById(R.id.etSite);
    }

    @Override
    public void onClick(View v) {

        if (v == btnApply) {
            // Return image size selection to activity
            SearchFiltersDialogListener listener = (SearchFiltersDialogListener) getActivity();

            FilterInfo filterInfo = new FilterInfo(
                    spinnerImageSizes.getSelectedItem().toString(),
                    spinnerImageColors.getSelectedItem().toString(),
                    spinnerImageTypes.getSelectedItem().toString(),
                    etSite.getText().toString()
            );

            Log.i(getActivity().toString(), "filterInfo: " + filterInfo);
            listener.onFinishSelectFilters(filterInfo);
            dismiss();
        }
    }

    private void setUpFilters() {
        if(filterInfo != null) {
            setUpImageColorsSpinner();
            setUpImageSizesSpinner();
            setUpImageTypeSpinner();
            etSite.setText(filterInfo.getSite());
        }
    }

    private void setUpImageTypeSpinner() {
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter) spinnerImageTypes.getAdapter();
        int spinnerImageSizesPosition = arrayAdapter.getPosition(filterInfo.getImageType());
        spinnerImageTypes.setSelection(spinnerImageSizesPosition);
    }

    private void setUpImageColorsSpinner() {
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter) spinnerImageColors.getAdapter();
        int spinnerImageColorsPosition = arrayAdapter.getPosition(filterInfo.getImageColor());
        spinnerImageColors.setSelection(spinnerImageColorsPosition);
    }

    private void setUpImageSizesSpinner() {
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter) spinnerImageSizes.getAdapter();
        int spinnerImageSizesPosition = arrayAdapter.getPosition(filterInfo.getImageSize());
        spinnerImageSizes.setSelection(spinnerImageSizesPosition);
    }
}
