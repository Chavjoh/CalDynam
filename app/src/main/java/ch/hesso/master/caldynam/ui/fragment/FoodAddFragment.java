package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.Constants;
import ch.hesso.master.caldynam.IntentTag;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.ui.adapter.FoodCategorySpinnerAdapter;
import ch.hesso.master.caldynam.ui.callback.ActivityResultCallback;
import ch.hesso.master.caldynam.ui.callback.ActivityResultManager;
import ch.hesso.master.caldynam.util.DialogUtils;

public class FoodAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String localPicturePath = "";
    private FoodCategorySpinnerAdapter spinnerAdapter;
    private Spinner spCategory;
    private EditText etTitle;
    private EditText etCalorie;
    private Button btnImage;
    private Button btnSubmit;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddFoodFragment.
     */
    public static FoodAddFragment newInstance() {
        FoodAddFragment fragment = new FoodAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FoodAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_add, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initSpinner();
        initImageButton();
        initSubmitButton();
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
    }

    public void findViews() {
        etTitle = (EditText) getView().findViewById(R.id.et_title);
        etCalorie = (EditText) getView().findViewById(R.id.et_calorie);
        spCategory = (Spinner) getView().findViewById(R.id.sp_category);
        btnImage = (Button) getView().findViewById(R.id.btn_image);
        btnSubmit = (Button) getView().findViewById(R.id.btn_submit);
    }

    public void initSpinner() {
        List<FoodCategory> listFoodCategory = FoodCategoryRepository.getAll(getActivity());
        FoodCategory[] array = listFoodCategory.toArray(new FoodCategory[listFoodCategory.size()]);
        spinnerAdapter = new FoodCategorySpinnerAdapter(getActivity(), array);
        spCategory.setAdapter(spinnerAdapter);
    }

    public void initImageButton() {
        final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + Constants.PROJECT_NAME + "/";
        File directory = new File(path);
        directory.mkdirs();

        ActivityResultCallback callback = new ActivityResultCallback() {
            @Override
            public void onResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == getActivity().RESULT_OK) {
                    btnImage.setVisibility(View.INVISIBLE);
                } else {
                    localPicturePath = "";
                }
            }
        };

        ActivityResultManager.registerCallback(IntentTag.TAKE_PICTURE, callback);

        btnImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                localPicturePath = path + (new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSSZ").format(new Date())) + ".jpg";

                File fileImage = new File(localPicturePath);
                try {
                    fileImage.createNewFile();
                } catch (IOException e) {
                    Log.d(Constants.PROJECT_NAME, e.toString());
                }

                Uri outputFileUri = Uri.fromFile(fileImage);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, IntentTag.TAKE_PICTURE.getValue());
            }

        });
    }

    public void initSubmitButton() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String calorie = etCalorie.getText().toString();
                FoodCategory category = (FoodCategory) spCategory.getSelectedItem();

                if (title.isEmpty() || calorie.isEmpty() || localPicturePath.isEmpty()) {
                    DialogUtils.show(getActivity(), "Please enter the title, calorie number and select a picture.");
                } else {
                    // Save new food
                    Food food = new Food(null, title, category.getId(), Integer.parseInt(calorie), localPicturePath, "");
                    FoodRepository.insertOrUpdate(getActivity(), food);

                    // Go back
                    getActivity().getFragmentManager().beginTransaction().remove(FoodAddFragment.this).commit();
                }

            }

        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        ActivityResultManager.unregisterCallback(IntentTag.TAKE_PICTURE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }

}
