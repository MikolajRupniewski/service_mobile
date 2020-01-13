package pl.rupniewski.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pl.rupniewski.R;



public class FilterFragment extends Fragment {
    private Context context;
    private Spinner spinner;
    private Button confirm;
    private Button cancel;
    private String selectedCategory;
    private int distance;
    private String search;
    private SeekBar sbDistance;
    private TextView tvDistance;
    private EditText etSearch;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        context = root.getContext();
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        spinner = root.findViewById(R.id.spinner_category_filter);
        confirm = root.findViewById(R.id.btn_confirm_filter);
        sbDistance = root.findViewById(R.id.sb_distance_filter);
        tvDistance = root.findViewById(R.id.tv_distance_filter);
        etSearch = root.findViewById(R.id.et_search_filter);
        cancel = root.findViewById(R.id.btn_cancel_filter);
        sbDistance.setOnSeekBarChangeListener(handleDistance());
        confirm.setOnClickListener(handleConfirm());
        String[] categories = new String[]{"Fryzjer", "Manicure", "Hydraulik"};
        ArrayAdapter<String>adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cancel.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), homeFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .remove(FilterFragment.this)
                    .commit();
        });
    }

    private SeekBar.OnSeekBarChangeListener handleDistance() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDistance.setText(progress + " Km");
                distance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private View.OnClickListener handleConfirm() {
        return v -> {
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", selectedCategory);
            bundle.putInt("distance", distance);
            search = etSearch.getText().toString();
            bundle.putString("search", search);
            homeFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), homeFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .remove(this)
                    .commit();

        };
    }
}
