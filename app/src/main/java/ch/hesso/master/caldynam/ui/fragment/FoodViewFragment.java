package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ch.hesso.master.caldynam.Constants;
import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.ui.adapter.FoodCategorySpinnerAdapter;
import ch.hesso.master.caldynam.util.DialogUtils;
import ch.hesso.master.caldynam.util.ImageUtils;

public class FoodViewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView tvName;
    private TextView tvCategory;
    private TextView tvCalorie;
    private ImageView ivImage;
    private Button btnDelete;
    private Food food;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FoodViewFragment.
     */
    public static FoodViewFragment newInstance(Long foodId) {
        FoodViewFragment fragment = new FoodViewFragment();
        Bundle args = new Bundle();
        args.putLong("id", foodId);
        fragment.setArguments(args);
        return fragment;
    }

    public FoodViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViews();
        showFood();
        initDeleteButton();
    }

    private void findViews() {
        tvName = (TextView) getView().findViewById(R.id.tv_name);
        tvCategory = (TextView) getView().findViewById(R.id.tv_category);
        tvCalorie = (TextView) getView().findViewById(R.id.tv_calorie);
        ivImage = (ImageView) getView().findViewById(R.id.iv_image);
        btnDelete = (Button) getView().findViewById(R.id.btn_delete);
    }

    private void showFood() {
        tvName.setText(food.getName());
        tvCategory.setText(food.getFoodCategory().getName());
        tvCalorie.setText(String.valueOf(food.getCalorie()));

        File imgFile = new File(getActivity().getFilesDir(), food.getImage());

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivImage.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bitmap, 64));
        }
    }

    private void initDeleteButton() {

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.delete_confirmation_title)
                        .setMessage(R.string.delete_confirmation_food)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FoodRepository.delete(getActivity(), food.getId());
                                File thumbImage = new File(getActivity().getFilesDir(), food.getImage());
                                File fullImage = new File(
                                        getActivity().getFilesDir(),
                                        food.getImage().replace(Constants.THUMBNAIL_SUFFIX, ""));

                                if (thumbImage.exists()) {
                                    thumbImage.delete();
                                }

                                if (fullImage.exists()) {
                                    fullImage.delete();
                                }

                                getFragmentManager().popBackStack();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }

        });
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

        Long id = getArguments().getLong("id", 0);
        food = FoodRepository.get(activity, id);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity)getActivity()).getAddButton().setVisibility(View.INVISIBLE);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }

}
