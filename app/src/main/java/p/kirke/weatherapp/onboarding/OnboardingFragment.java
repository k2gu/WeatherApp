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

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import p.kirke.weatherapp.MainActivity;
import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.util.Const;

public class OnboardingFragment extends Fragment implements OnboardingView {

    @BindView(R.id.name_input_field)
    EditText nameInput;

    private OnboardingPresenter presenter;

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
        saveEnteredName(name);
        startPresenter();
    }

    private void saveEnteredName(String name) {
        if (!name.isEmpty()) {
            PreferencesSingleton.getSingletonInstance(getContext()).setName(name);
        }
    }

    private void startPresenter() {
        if (presenter == null) {
            presenter = new OnboardingPresenter(this, PreferencesSingleton.getSingletonInstance(getContext()),
                    new PermissionHandler(getActivity()));
        }
        presenter.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO check result
        presenter.onRequestPermission(requestCode, permissions, grantResults);
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        Activity activity = getActivity();
        if (activity != null) {
            getActivity().startActivityForResult(intent, Const.OPEN_GALLERY_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.OPEN_GALLERY_REQUEST_CODE) {
                MainActivity activity = ((MainActivity) getActivity());
                if (activity != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage == null) {
                        return;
                    }
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor == null) {
                        return;
                    }
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    PreferencesSingleton.getSingletonInstance(getContext()).setPrefPictureLocation(imgDecodableString);
                    ((MainActivity) getActivity()).replaceOnboardingFragment();
                }
            }
        }
    }
}
