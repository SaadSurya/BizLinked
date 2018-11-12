package com.application.lumaque.bizlinked.fragments.baseClass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.OptionMenuModal;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.common.KeyboardHelper;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment implements Validator.ValidationListener {

    public static final String TAG = BaseFragment.class.getSimpleName();

    public abstract void onCustomBackPressed();

    public BasePreferenceHelper preferenceHelper;
    protected BaseActivity activityReference;
    public boolean isLoadingRegisteration = false;
    public boolean isLoading = false;
    Validator validator;
    Unbinder unbinder;
    View rootView;

    //Abstract Methods
    protected abstract int getMainLayout();
    protected abstract void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState, View rootView);
    //boolean isOptionsMenuShow,boolean isNavigationMenuShow,
   // protected abstract void setBottomBarLayout(BottomTabLayout bottomTabLayout, BottomBarNavigationLayout bottomBarNavigationLayout);
    //Override Methods
    public void onValidationSuccess() {}
    public void onValidationFail() {}


    public View getContainerLayout(){
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getBaseActivity() != null) {
            preferenceHelper = getBaseActivity().prefHelper;
            activityReference = getBaseActivity();
        }
    }

    public void validateFields() {

        if(activityReference != null)
            KeyboardHelper.hideSoftKeyboard(activityReference, activityReference.getWindow().getDecorView());

        validator.validate();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(getMainLayout(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        onFragmentViewReady(inflater, container, savedInstanceState, rootView);
        validator = new Validator(this);
        validator.setValidationListener(this);
        setTitleBarListener();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityReference = getBaseActivity();
        preferenceHelper = getBaseActivity().prefHelper;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
        if (getBaseActivity().getWindow() != null)
            if (getBaseActivity().getWindow().getDecorView() != null)
                KeyboardHelper.hideSoftKeyboard(getBaseActivity(), getBaseActivity()
                        .getWindow().getDecorView());

    }

    protected BaseActivity getBaseActivity() {

        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        onValidationSuccess();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
//        for (ValidationError error : errors) {
//            View view = error.getView();
//            String message = error.getCollatedErrorMessage(activityReference);
        ValidationError error = errors.get(0);
        View view = error.getView();
        String message = "";
        message = error.getFailedRules().get(0).getMessage(activityReference);

        // Display error messages ;)
        if (view instanceof EditText) {
            ((EditText) view).setError(message);
            AnimationHelpers.animation(Techniques.RubberBand, 300, view);
            view.requestFocus();
        } else {
            Utils.showSnackBar(activityReference, view, message, ContextCompat.getColor(activityReference, R.color.grayColor));
        }
        onValidationFail();
    }



    public void loadingStarted() {
        isLoading = true;
        if (getBaseActivity() != null)
            getBaseActivity().onLoadingStarted();
    }

    public void loadingFinished() {
        isLoading = false;
        if (getBaseActivity() != null)
            getBaseActivity().onLoadingFinished();

    }


// todo to be in child fragment as abstract method
    private void setTitleBarListener() {

        final ArrayList<OptionMenuModal> optionList = new ArrayList<>();
        optionList.add(new OptionMenuModal(1, "Change Password", "ChangePasswordFragment"));
        optionList.add(new OptionMenuModal(2, "Logout", "Logout"));

        if (rootView.findViewById(R.id.ivTitleBarMenu) != null) {

            rootView.findViewById(R.id.ivTitleBarMenu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popup = new PopupMenu(getContext(), v);
                    //Inflating the Popup using xml file

                    for (int index = 0; index < optionList.size(); index++) {
                        popup.getMenu().add(0, optionList.get(index).getId(), index, optionList.get(index).getName());
                    }
                    //popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            try {
                                if (item.getItemId() == 2) {
                                   // logoutUser();

                                } else {
                                    openRequiredFragment(optionList, item);
                                }

                            } catch (Exception e) {
                                Utils.showToast(getContext(), getContext().getString(R.string.will_be_implemented), AppConstant.TOAST_TYPES.INFO);
                                e.printStackTrace();
                            }
                            return true;
                        }
                    });

                    popup.show();//showing popup menu

                }
            });
        }
    }


    private void openRequiredFragment(ArrayList<OptionMenuModal> optionList, MenuItem item) {
        for (OptionMenuModal object : optionList) {
            if (object.getId() == item.getItemId()) {
                BaseFragment className = Utils.getFragmentByName(getBaseActivity(), object.getClassName());
                getBaseActivity().addSupportFragment(className, AppConstant.TRANSITION_TYPES.SLIDE,true);
            }
        }
    }

}
