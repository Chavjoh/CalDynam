package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.Constants;
import ch.hesso.master.caldynam.IntentTag;
import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.ui.adapter.FoodCategorySpinnerAdapter;
import ch.hesso.master.caldynam.util.DialogUtils;
import ch.hesso.master.caldynam.util.ImageUtils;

public class FoodAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String localPicturePath = "";
    private String localThumbPicturePath = "";
    private FoodCategorySpinnerAdapter spinnerAdapter;
    private Spinner spCategory;
    private EditText etTitle;
    private EditText etCalorie;
    private Button btnImage;
    private Button btnSubmit;
    private ImageView ivImage;
    private TableRow trImage;
    private RelativeLayout rlImagePreview;
    private boolean isSaved;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (IntentTag.fromValue(requestCode)) {
            case TAKE_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Bitmap thumb = saveImage(photo);

                    ivImage.setImageBitmap(ImageUtils.getRoundedCornerBitmap(thumb, 64));

                    trImage.setVisibility(View.INVISIBLE);
                    rlImagePreview.setVisibility(View.VISIBLE);

                } else {
                    localPicturePath = "";
                    localThumbPicturePath = "";
                }

                break;
        }
    }

    private Bitmap saveImage(Bitmap bitmap) {

        String dateString = (new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSSZ").format(new Date()));
        localPicturePath = dateString + Constants.IMAGE_FORMAT;
        localThumbPicturePath = dateString + Constants.THUMBNAIL_SUFFIX + Constants.IMAGE_FORMAT;

        File fullPictureFile = new File(getActivity().getFilesDir(), localPicturePath);
        File thumbPictureFile = new File(getActivity().getFilesDir(), localThumbPicturePath);

        Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);

        saveImage(fullPictureFile, bitmap);
        saveImage(thumbPictureFile, thumbBitmap);

        return thumbBitmap;
    }

    private void saveImage(File file, Bitmap image) {
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();
        } catch (IOException e) {
            Log.d(Constants.PROJECT_NAME, e.toString());
        }
    }

    private void deleteImage() {
        if (!localPicturePath.isEmpty() && !localThumbPicturePath.isEmpty()) {
            File fullPictureFile = new File(getActivity().getFilesDir(), localPicturePath);
            File thumbPictureFile = new File(getActivity().getFilesDir(), localThumbPicturePath);

            if (fullPictureFile.exists()) {
                fullPictureFile.delete();
            }

            if (thumbPictureFile.exists()) {
                thumbPictureFile.delete();
            }

            Log.d("Add food", "Clean temporary picture");
        }
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

        isSaved = false;

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).getAddButton().setVisibility(View.INVISIBLE);
    }

    public void findViews() {
        etTitle = (EditText) getView().findViewById(R.id.et_title);
        etCalorie = (EditText) getView().findViewById(R.id.et_calorie);
        spCategory = (Spinner) getView().findViewById(R.id.sp_category);
        btnImage = (Button) getView().findViewById(R.id.btn_image);
        ivImage = (ImageView) getView().findViewById(R.id.iv_image);
        trImage = (TableRow) getView().findViewById(R.id.tr_image);
        rlImagePreview = (RelativeLayout) getView().findViewById(R.id.rl_image_preview);
        btnSubmit = (Button) getView().findViewById(R.id.btn_submit);
    }

    public void initSpinner() {
        List<FoodCategory> listFoodCategory = FoodCategoryRepository.getAll(getActivity());
        spinnerAdapter = new FoodCategorySpinnerAdapter(getActivity(), listFoodCategory);
        spCategory.setAdapter(spinnerAdapter);
    }

    public void initImageButton() {

        btnImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IntentTag.TAKE_PICTURE.getValue());
            }

        });

        rlImagePreview.setVisibility(View.INVISIBLE);
    }

    public void initSubmitButton() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String title = etTitle.getText().toString();
                String calorie = etCalorie.getText().toString();
                FoodCategory category = (FoodCategory) spCategory.getSelectedItem();

                if (title.isEmpty() || calorie.isEmpty() || localThumbPicturePath.isEmpty()) {
                    DialogUtils.show(getActivity(), "Please enter the title, calorie number and select a picture.");
                } else {
                    // Save new food
                    Food food = new Food(null, title, category.getId(), Integer.parseInt(calorie), localThumbPicturePath, "");
                    FoodRepository.insertOrUpdate(getActivity(), food);

                    // Indicate we saved the food
                    isSaved = true;

                    // Go back
                    getFragmentManager().popBackStack();
                }

            }

        });
    }

    @Override
    public void onDetach() {

        if (!isSaved) {
            deleteImage();
        }

        super.onDetach();
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
