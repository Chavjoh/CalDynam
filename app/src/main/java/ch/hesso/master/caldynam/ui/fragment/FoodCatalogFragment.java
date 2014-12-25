package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.ui.adapter.FoodAdapter;
import ch.hesso.master.caldynam.ui.adapter.FoodCategorySpinnerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodCatalogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodCatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FoodCatalogFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View mAddButton;
    private FoodAdapter foodAdapter;
    private ListView lvFood;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FoodCatalog.
     */
    public static FoodCatalogFragment newInstance() {
        FoodCatalogFragment fragment = new FoodCatalogFragment();
        return fragment;
    }

    public FoodCatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_catalog, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        ((MainActivity) activity).onSectionAttached(R.string.section_food_catalog);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAddButton = ((MainActivity) getActivity()).getAddButton();

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = FoodAddFragment.newInstance();
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });

        findViews();
        initListView();
    }

    public void findViews() {
        lvFood = (ListView) getActivity().findViewById(R.id.lv_food);
    }

    public void initListView() {
        foodAdapter = new FoodAdapter(getActivity(), new ArrayList<Food>());
        lvFood.setAdapter(foodAdapter);
        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food = (Food) lvFood.getItemAtPosition(position);
                Fragment fragment = FoodViewFragment.newInstance(food.getId());
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).getAddButton().setVisibility(View.VISIBLE);

        List<Food> listFood = FoodRepository.getAll(getActivity());
        foodAdapter.setData(listFood);
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html">
     * Communicating with Other Fragments</a> for more information.
     * </p>
     */
    public interface OnFragmentInteractionListener {

    }

}
