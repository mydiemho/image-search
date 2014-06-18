package myho.gridimagesearch;

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
        View view = inflater.inflate(R.layout.fragment_search_filters, container);

        setUpImageSizesSpinner(view);
        setUpImageColorsSpinner(view);
        setUpImageTypeSpinner(view);

        etSite = (EditText) view.findViewById(R.id.etSite);

        btnApply = (Button) view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);

        String title = getArguments().getString("title", "Select Filters");
        getDialog().setTitle(title);
//        getDialog().setCanceledOnTouchOutside(true);

        return view;
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

    private void setUpImageTypeSpinner(View view) {
        spinnerImageTypes = (Spinner) view.findViewById(R.id.spinnerImageTypes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> imageTypesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        imageTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerImageTypes.setAdapter(imageTypesAdapter);

        spinnerImageColors.setSelection(-1);
    }

    private void setUpImageColorsSpinner(View view) {
        spinnerImageColors = (Spinner) view.findViewById(R.id.spinnerImageColors);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> imageColorsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_colors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        imageColorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerImageColors.setAdapter(imageColorsAdapter);
    }

    private void setUpImageSizesSpinner(View view) {
        spinnerImageSizes = (Spinner) view.findViewById(R.id.spinnerImageSizes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> imageSizesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.image_sizes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        imageSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerImageSizes.setAdapter(imageSizesAdapter);
    }
}
