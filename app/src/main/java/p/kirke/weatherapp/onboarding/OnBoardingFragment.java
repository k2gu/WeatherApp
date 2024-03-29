package p.kirke.weatherapp.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.util.Const;

public class OnBoardingFragment extends Fragment implements OnboardingView {

    @BindView(R.id.name_input_field)
    EditText nameInput;

    private OnBoardingPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.select_avatar)
    void onClickSelectAvatar() {
        String name = nameInput.getText().toString();
        startPresenter(name);
    }

    private void startPresenter(String name) {
        if (presenter == null) {
            presenter = new OnBoardingPresenter(this, PreferencesSingleton.getSingletonInstance(getContext()),
                    new PermissionHandler(getActivity()));
        }
        presenter.start(name);
    }

    @Override
    public void onError(int message) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showError(message);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionResponse(requestCode, permissions, grantResults);
    }

    @Override
    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, Const.OPEN_GALLERY_REQUEST_CODE);
    }

    @Override
    public void openHomeFragment() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.replaceOnboardingFragment();
        }
    }

    @Override
    public void hideError() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.hideError();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.OPEN_GALLERY_REQUEST_CODE) {
            MainActivity activity = ((MainActivity) getActivity());
            Uri selectedImage = data.getData();
            if (activity != null && selectedImage != null) {
                String imageString = getImageDecodableStringFromUri(selectedImage);
                presenter.onImageResponse(imageString);
            }
        } else {
            presenter.onImageSelectionCancelled();
        }
    }

    private String getImageDecodableStringFromUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imageString = cursor.getString(columnIndex);
            cursor.close();
            return imageString;
        }
        return "";
    }
}
