package com.application.lumaque.bizlinked.activities.baseClass;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.lumaque.bizlinked.BizLinkApplication;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.RegistrationActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.customViews.BadgeDrawerArrowDrawable;
import com.application.lumaque.bizlinked.data_models.OptionMenuModal;
import com.application.lumaque.bizlinked.data_models.bizlinked.FireBaseDataMode;
import com.application.lumaque.bizlinked.fireBase.BackgroundService;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.CustomerFragmentTabs;
import com.application.lumaque.bizlinked.fragments.bizlinked.ProductListFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.SupplierFragmentTabs;
import com.application.lumaque.bizlinked.fragments.bizlinked.ViewProfileFragment;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.placeHelper.GooglePlaceHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.ui.dialogs.DialogFactory;
import com.application.lumaque.bizlinked.listener.LocationListener;
import com.application.lumaque.bizlinked.listener.MediaTypePicker;
import com.application.lumaque.bizlinked.listener.OnActivityResultInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yovenny.videocompress.MediaController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

import static com.google.android.gms.location.LocationSettingsRequest.Builder;


public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {



    public Toolbar toolbar;

    TextView supplier,customer;






    private ActionBarDrawerToggle toggle;
    private BadgeDrawerArrowDrawable badgeDrawable;


    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int CAMERA_PIC_REQUEST = 110;
    private static final int LOCATION_REQUEST_CODE = 1100;
    public BasePreferenceHelper prefHelper;
    public static final String KEY_FRAG_FIRST = "firstFrag";
    protected Context context;
    MediaTypePicker mediaPickerListener;
    ArrayList<String> photoPaths;
    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed_Videos/";
    public static final String TEMP_DIR = "/Temp/";
    private boolean loading = false;
    LocationListener locationListener;
    ProgressBar progressBar;
    FrameLayout progressBarContainer;
    private Location mLastLocation;
    //For Location
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    //    int UPDATE_INTERVAL = 10000;
    int UPDATE_INTERVAL = 1000;
    final int FASTEST_INTERVAL = 500;

    TextView Username;
    de.hdodenhof.circleimageview.CircleImageView    profileImage;


    //For Places
    OnActivityResultInterface onActivityResultInterface;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    //For Location
    //Abstract Methods
    public abstract int getMainLayoutId();

    public abstract int getFragmentFrameLayoutId();

    protected abstract void onViewReady();

    public BaseFragment baseFragment;


    protected ViewGroup getMainView() {
        return (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
    }


   /* public BottomTabLayout getBottomBarOptionsMenu() {
        if (getMainView() != null && getMainView().findViewById(R.id.bottomBarCustomView) != null) {
            return getMainView().findViewById(R.id.bottomBarCustomView);
        }
        return null;
    }


    public BottomBarNavigationLayout getBottomBarNavigationMenu() {
        if (getMainView() != null && getMainView().findViewById(R.id.bottomBarNavigationCustomView) != null) {
            return getMainView().findViewById(R.id.bottomBarNavigationCustomView);
        }
        return null;
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getMainLayoutId());
        ButterKnife.bind(this);
        context = BaseActivity.this;
        prefHelper = new BasePreferenceHelper(context);
        setUpDrowerMenu();
        if (getMainView() != null && getMainView().findViewById(R.id.progressBarContainer) != null && getMainView().findViewById(R.id.progressBar) != null) {
            progressBarContainer = findViewById(R.id.progressBarContainer);
            progressBar = findViewById(R.id.progressBar);
        }

        onViewReady();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(getFragmentFrameLayoutId());
              //  Toast.makeText(BaseActivity.this,"before frag",Toast.LENGTH_LONG).show();
                if (currentFragment != null) {
               //     Toast.makeText(BaseActivity.this,currentFragment.getClass().getSimpleName(),Toast.LENGTH_LONG).show();
                    Log.e("fragment=", currentFragment.getClass().getSimpleName());
                    baseFragment = (BaseFragment) currentFragment;
                }else {
                    onBackPressed();
                }
            }
        });


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
    }


    public void setFireBase(){
      //  addMenuBadge(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("notifications").child("comp_"+prefHelper.getCompanyProfile().getCompanyID());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                ArrayList<FireBaseDataMode> list = new ArrayList<FireBaseDataMode>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(FireBaseDataMode.class));
                }
                if (list.size() != 0){
                    badgeDrawable.setEnabled(true);
                    badgeDrawable.setText("");

                    addMenuBadge(list.get(list.size()-1).getText().toLowerCase().contains("supplier"));

                }

              //  dataSnapshot.getRef().removeValue();


                //    isForeground("com.application.lumaque.bizlinked");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

public void addMenuBadge(boolean isSupplier){


        if(!isSupplier) {
            supplier.setGravity(Gravity.CENTER_VERTICAL);
            supplier.setTypeface(null, Typeface.BOLD);
            supplier.setTextColor(getResources().getColor(R.color.red));
            supplier.setText("1");

        }else {

            customer.setGravity(Gravity.CENTER_VERTICAL);
            customer.setTypeface(null, Typeface.BOLD);
            customer.setTextColor(getResources().getColor(R.color.red));
            customer.setText("1");

        }

}
    public void removeReadFireBaseNotification(){


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("notifications").child("comp_"+prefHelper.getCompanyProfile().getCompanyID());






    }

    public void setUpDrowerMenu(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);



        if(toolbar!= null){
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
           final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
             toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    // Do whatever you want here
                }
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // Do whatever you want here
                }
            };




            badgeDrawable = new BadgeDrawerArrowDrawable(getSupportActionBar().getThemedContext());
            badgeDrawable.setEnabled(false);
            toggle.setDrawerArrowDrawable(badgeDrawable);


            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerLayout = navigationView.getHeaderView(0);
             profileImage = (de.hdodenhof.circleimageview.CircleImageView) headerLayout.findViewById(R.id.profile_image);

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewProfileFragment viewprofilefragment = new ViewProfileFragment();
                    viewprofilefragment.setViewID(String.valueOf(prefHelper.getCompanyProfile().getCompanyID()),"self");
                    addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE,true);


                    drawer.closeDrawer(GravityCompat.START);
                }
            });


             Username = headerLayout.findViewById(R.id.userName);

            Glide.with(context).load(AppConstant.ServerAPICalls.GET_MEDIA_FILE+prefHelper.getCompanyProfile().getCompanyID())
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).placeholder(R.drawable.profile))
                    .into(profileImage);
            Username.setText(prefHelper.getCompanyProfile().getCompanyName());

            supplier=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_supplier));
            customer=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                    findItem(R.id.nav_customers));






















        }
    }




    public void updateDrawer(){

        Glide.with(context).load(AppConstant.ServerAPICalls.GET_MEDIA_FILE+prefHelper.getCompanyProfile().getCompanyID())
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                .into(profileImage);
        Username.setText(prefHelper.getCompanyProfile().getCompanyName());
    }
    private void openRequiredFragment(ArrayList<OptionMenuModal> optionList, MenuItem item) {
        for (OptionMenuModal object : optionList) {
            if (object.getId() == item.getItemId()) {
                BaseFragment className = Utils.getFragmentByName(context, object.getClassName());
                addSupportFragment(className, AppConstant.TRANSITION_TYPES.SLIDE,true);
            }
        }
    }

    private void logoutUser() {
        DialogFactory.createMessageDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                prefHelper.removeLoginPreference();
               // clearBackstack();

                StopBackgroundService(BackgroundService.class);
/*

                BizLinkApplication.setFireBaseReffID("");
                BizLinkApplication.setLoginStatus(false);
*/

                changeActivity(RegistrationActivity.class, true);
            }
        }, getString(R.string.logout)).show();
    }


    public <T> void changeActivity(Class<T> cls, boolean isActivityFinish) {
        Intent intent = new Intent(this, cls);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        startActivity(intent);
        if (isActivityFinish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }

    }

    public <T> void StartBackgroundService(Class<T> cls) {



        Intent intent = new Intent(this, cls);
        intent.putExtra("msgID", "comp_"+prefHelper.getCompanyProfile().getCompanyID());
        intent.putExtra("islogin", prefHelper.getLoginStatus());
       // startService(new Intent(SelectSigningFragment.this,BackgroundService.class));
        startService(intent);


    }

  public <T> void StopBackgroundService(Class<T> cls) {



        Intent intent = new Intent(this, cls);
       // startService(new Intent(SelectSigningFragment.this,BackgroundService.class));
      stopService(intent);


    }


    /* public <T> void changeActivity(Class<T> cls, boolean isActivityFinish) {

        Intent intent = new Intent(this, cls);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

      *//*  startActivity(intent);
        if (isActivityFinish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.finish();
        }
*//*
    }
*/
    protected <T> void changeActivity(Class<T> cls, Bundle data) {
        Intent resultIntent = new Intent(this, cls);
        if (data != null)
            resultIntent.putExtras(data);
        startActivity(resultIntent);
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    public void addSupportFragment(BaseFragment frag, int transition,boolean addToStack) {



        baseFragment = frag;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction =manager.beginTransaction();

        if (transition == AppConstant.TRANSITION_TYPES.FADE)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        else if (transition == AppConstant.TRANSITION_TYPES.SLIDE)
            transaction.setCustomAnimations(R.anim.anim_enter, 0);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);




        transaction.replace(getFragmentFrameLayoutId(), frag, frag.getClass().getName());
        if(addToStack){
        transaction.addToBackStack(frag.getClass().getName()).commit();// AllowingStateLoss();
             }
             else
             transaction.commit();
    }

    public void addSupportFragmentWithContainerView(Fragment frag, int layoutId) {


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(layoutId, frag, frag.getClass().getName());

            transaction.commit();


//       transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ?
//                KEY_FRAG_FIRST : null).commit();// AllowingStateLoss();
    }


    public void addSupportFragmentWithOutBackStack(BaseFragment frag, String tag) {

        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(getFragmentFrameLayoutId(), frag, tag);
        transaction.commit();
    }

    public void realAddSupportFragment(BaseFragment frag, String tag) {

        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(getFragmentFrameLayoutId(), frag, tag);

        transaction.addToBackStack(
                getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                        : null).commit();// AllowingStateLoss();

    }

    public void addSupportFragmentReplace(BaseFragment frag, String tag) {
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(getFragmentFrameLayoutId(), frag, tag);
        transaction.commit();

    }

    public void addSupportFragmentRemove(BaseFragment frag, String tag) {

        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(getFragmentFrameLayoutId(), frag, tag);
        transaction
                .addToBackStack(null).commit();// AllowingStateLoss();

    }


    public void clearBackstack() {

        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                0);
        getSupportFragmentManager().popBackStack(entry.getId(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();

    }
    public void removeFragment( String tag) {


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag(tag)).commit();




    }

    public void addSupportFragmentWithData(BaseFragment frag, String tag, Bundle args) {

        frag.setArguments(args);
        baseFragment = frag;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(getFragmentFrameLayoutId(), frag, tag);
        transaction.addToBackStack(null).commit();// AllowingStateLoss();

    }

//    public void addAndShowDialogFragment(DialogFragment dialog) {
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        dialog.show(transaction, "tag");
//
//    }
//
//    public void prepareAndShowDialog(DialogFragment frag, String TAG) {
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        Fragment prev = getSupportFragmentManager().findFragmentByTag(TAG);
//
//        if (prev != null)
//            transaction.remove(prev);
//
//        transaction.addToBackStack(null);
//
//        frag.show(transaction, TAG);
//    }
public void onPageBack() {
    if (!loading) {


        super.onBackPressed();

    } else {
        Utils.showToast(context, context.getString(R.string.please_wait_data_is_loading), AppConstant.TOAST_TYPES.INFO);
    }
}


    @Override
    public void onBackPressed() {

      /*  if (!loading) {
            int a = getSupportFragmentManager().getFragments().size() -1 ;
            if (!(getSupportFragmentManager().getFragments().get(a) instanceof HomeFragment))
                baseFragment.onCustomBackPressed();
            else
                finish();
        } else {
            Utils.showToast(context, context.getString(R.string.please_wait_data_is_loading), AppConstant.TOAST_TYPES.INFO);
        }*/
  if (!loading) {

                baseFragment.onCustomBackPressed();

        } else {
            Utils.showToast(context, context.getString(R.string.please_wait_data_is_loading), AppConstant.TOAST_TYPES.INFO);
        }


    }

    public BaseFragment getLastAddedSuppFragment() {
        return baseFragment;
    }

    public void emptyBackStack() {
        popBackStackTillEntry(0);
    }

    /**
     * @param entryIndex is the index of fragment to be popped to, for example the
     *                   first fragment will have a index 0;
     */
    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    /**
     * Function to clear fragment back stack until
     * the fragment with given tag is reached
     *
     * @param tag Tag provided to fragment
     */
    public void clearStackTillFragment(String tag) {
        getSupportFragmentManager().popBackStack(tag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Fragment isFragmentPresent(String tag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        if (frag != null) {
            return frag;
        } else
            return null;
    }


//    @Override
//    public void onValidationSucceeded() {
////        ValidationHelpers.resetAllErrors(getMainContainerView());
//        onValidationSuccess();
//
//    }


//    @Override
//    public void onValidationFailed(List<ValidationError> errors) {
//        ValidationError error = errors.get(0);
//        View view = error.getView();
//        String message = "";
//        message = error.getFailedRules().get(0).getMessage(BaseActivity.this);
//        //ArrayList<CustomTextInputLayout> customInputLayout = ValidationHelpers.traverseAllCustomInputLayout(getMainContainerView());
//        if (view instanceof EditText) {
//             for (int layoutViewIndex = 0; layoutViewIndex < customInputLayout.size(); layoutViewIndex++) {
//                CustomTextInputLayout customTextInputLayout = (CustomTextInputLayout) customInputLayout.get(layoutViewIndex);
//                EditText editText = ValidationHelpers.getEditText(customTextInputLayout);
//
//                if (editText != null) {
//                    if (editText.getId() == view.getId()) {
//
//                        AnimationHelpers.customAnimation(Techniques.RubberBand, 300, customTextInputLayout);
//                        editText.requestFocus();
//
//                        if (!customTextInputLayout.isErrorEnable())
//                            customTextInputLayout.setError(message);
//                        else
//                            customTextInputLayout.setErrorMessage(message);
//                    } else {
//                        customTextInputLayout.errorEnable(false);
//                    }
//                }
//            }
//        } else {
//            Utils.showSnackBar(BaseActivity.this, view, message, ContextCompat.getColor(BaseActivity.this, R.color.grayColor));
//        }
//        onValidationFail();
//    }


    //
    //@Override
    public void onLoadingStarted() {

        if (progressBarContainer != null) {
            progressBarContainer.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            loading = true;

        }

    }

    //@Override
    public void onLoadingFinished() {

        if (progressBarContainer != null) {
            progressBarContainer.setVisibility(View.GONE);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            loading = false;
        }

    }


    public void openImagePicker(final MediaTypePicker listener) {

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(AppConstant.SELECT_IMAGE_COUNT)
                                //.setSelectedFiles(photoPaths)
                                //.setActivityTheme(R.style.AppTheme)
                                .enableVideoPicker(false)
                                .enableCameraSupport(true)
                                .showGifs(false)
                                .enableSelectAll(false)
                                .showFolderView(false)
                                .enableImagePicker(true)
                                .withOrientation(Orientation.UNSPECIFIED)
                                .pickPhoto(BaseActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();

                    }
                }).check();

    }


    public void openCameraPicker(final MediaTypePicker listener) {

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }).check();


    }

    public void openMediaPicker(final MediaTypePicker listener) {

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(AppConstant.SELECT_IMAGE_COUNT)
                                //.setSelectedFiles(photoPaths)
                                //.setActivityTheme(R.style.Theme_Holo_Light)
                                .enableVideoPicker(false)
                                .enableCameraSupport(true)
                                .showFolderView(false)
                                .showGifs(false)
                                .enableSelectAll(false)
                                .enableImagePicker(true)
                                .withOrientation(Orientation.UNSPECIFIED)
                                .pickPhoto(BaseActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, co ntext.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    public void openFilePicker(final MediaTypePicker listener) {


        final String fileTypes[] = {".jpg", ".jpeg", ".mp4"};
        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(AppConstant.SELECT_MAX_FILE_COUNT)
                                .setSelectedFiles(photoPaths)
                                //.setActivityTheme(R.style.NormalTheme)
                                .enableVideoPicker(true)
                                .enableCameraSupport(true)
                                .showGifs(false)
                                .enableSelectAll(false)
                                .enableImagePicker(true)
                                .withOrientation(Orientation.UNSPECIFIED)
                                .addFileSupport("File", fileTypes)
                                .pickPhoto(BaseActivity.this);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    public void openDocPicker(final MediaTypePicker listener) {

        TedPermission.with(this)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        mediaPickerListener = listener;
                        FilePickerBuilder.getInstance()
                                .setMaxCount(AppConstant.SELECT_DOC_FILE_COUNT)
                                //.setSelectedFiles(filePaths)
                                //.setActivityTheme(R.style.NormalTheme)
                                .pickFile(BaseActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    new AsyncTaskRunner().execute(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {

                    ArrayList<String> docFiles = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
                    ArrayList<File> files = new ArrayList<>();
                    for (int index = 0; index < docFiles.size(); index++) {
                        File file = new File(docFiles.get(index));
                        files.add(file);
                    }
                    mediaPickerListener.onDocClicked(files);
                }
                break;
            case CAMERA_PIC_REQUEST:
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    ArrayList<String> cameraPic = new ArrayList<>();
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(this, (Bitmap) data.getExtras().get("data"));
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    cameraPic.add(getRealPathFromURI(tempUri));

                    new AsyncTaskRunner().execute(cameraPic);
                }
                break;


            case LOCATION_REQUEST_CODE:
                if (data != null && data.getExtras() != null) {
                    if (resultCode == Activity.RESULT_OK) {

                        if (locationListener != null)
                            onLoadingStarted();

                        displayLocation();
                    } else {
                        if (locationListener != null)
                            locationListener.onNoActionPerformed();
                    }
                }

                break;

            //For Places API
            case GooglePlaceHelper.REQUEST_CODE_AUTOCOMPLETE:
                onActivityResultInterface.onActivityResultInterface(requestCode, resultCode, data);
                break;

            default:
                break;
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private class AsyncTaskRunner extends AsyncTask<ArrayList<String>, ArrayList<File>, ArrayList<File>> {

        ProgressDialog progressDialog;

        @Override
        protected ArrayList<File> doInBackground(ArrayList<String>... params) {

            ArrayList<File> compressedAndVideoImageFileList = new ArrayList<>();

            for (int index = 0; index < params[0].size(); index++) {

                File file = new File(params[0].get(index));

                if (file.toString().endsWith(".jpg") ||
                        file.toString().endsWith(".jpeg") ||
                        file.toString().endsWith(".png") ||
                        file.toString().endsWith(".gif")) {
                    try {
                        File compressedImageFile = new Compressor(BaseActivity.this).compressToFile(file, "compressed_" + file.getName());
                        compressedAndVideoImageFileList.add(compressedImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {


                    if (!file.toString().endsWith(".3gp")) {
                        createCompressDir();
                        String compressVideoPath = Environment.getExternalStorageDirectory()
                                + File.separator
                                + APP_DIR
                                + COMPRESSED_VIDEOS_DIR
                                + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";

                        boolean isCompressSuccess = MediaController.getInstance().convertVideo(file.getAbsolutePath(), compressVideoPath);

                        if (isCompressSuccess) {
                            compressedAndVideoImageFileList.add(new File(compressVideoPath));
                        } else {
                            compressedAndVideoImageFileList.add(file);
                        }

                    } else {
                        compressedAndVideoImageFileList.add(file);
                    }
                }
            }
            return compressedAndVideoImageFileList;
        }


        @Override
        protected void onPostExecute(ArrayList<File> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            mediaPickerListener.onPhotoClicked(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BaseActivity.this,
                    context.getString(R.string.app_name),
                    context.getString(R.string.compressing_please_wait));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }


    }

    private void createCompressDir() {
        File filedir = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        filedir = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
        filedir = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }

    }


    // Location Work
    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(2);

    }

    @Override
    public void onConnected(Bundle bundle) {
        settingRequest();
    }

    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Builder builder = new Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        displayLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(BaseActivity.this, LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    public void notify(LocationListener loc) {
        this.locationListener = loc;
    }

    private void displayLocation() {

        //if (TedPermission.isGranted(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        setLastLocation(mLastLocation);

        if (mLastLocation != null) {
            //Toast.makeText(this, String.valueOf(mLastLocation.getLatitude()) + " , " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
            Location lastLocation = new Location("");
            lastLocation.setLatitude(mLastLocation.getLatitude());
            lastLocation.setLongitude(mLastLocation.getLongitude());
            setLastLocation(lastLocation);
            Log.e("Current Location", mLastLocation.getLatitude() + " , " + mLastLocation.getLongitude());

            if (locationListener != null)
                locationListener.onLocationReceived();

        } else {

            if (locationListener != null)
                locationListener.onNoLocationReceived();
            /*if there is no last known location. Which means the device has no data for the loction currently.
             * So we will get the current location.
             * For this we'll implement Location Listener and override onLocationChanged*/
            Log.i("Current Location", "No data for location found");

            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, BaseActivity.this);
        }
        //}


    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(BaseActivity.this, 1000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }


//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(new ResultCallback<Status>() {
//
//                @Override
//                public void onResult(Status status) {
//
//                    mGoogleApiClient.disconnect();
//                }
//            });
//
//        }


    }

    public void getCurrentLocation() {
        TedPermission.with(this)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        getGoogleLocation();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        //Utils.showToast(context, context.getString(R.string.permission_denied));
                        Toasty.warning(context, context.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                        if (locationListener != null)
                            locationListener.onNoActionPerformed();

                    }
                }).check();
    }

    public void getGoogleLocation() {
//        if(mLastLocation==null) {


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
       /* }
        else {
            Log.e(" Location_lat",mLastLocation.getLatitude()+" Location_long"+mLastLocation.getLongitude());
        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        setLastLocation(mLastLocation);
        displayLocation();
    }

    //For Places API
    public void setOnActivityResultInterface(OnActivityResultInterface activityResultInterface) {
        this.onActivityResultInterface = activityResultInterface;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String s = "";
        if(baseFragment !=null)
            s =  baseFragment.getClass().getName();
        switch (id) {
          /*  case R.id.nav_profile:
                ProfileTabsFragment ProfileTabsFragment = new ProfileTabsFragment();
                addSupportFragment(ProfileTabsFragment, AppConstant.TRANSITION_TYPES.SLIDE,true);

                break;*/
           /* case R.id.nav_biz_links:
                LinksTabsFragment LinksTabsFragment = new LinksTabsFragment();
                addSupportFragment(LinksTabsFragment, AppConstant.TRANSITION_TYPES.SLIDE,false);
                break;*/

                case R.id.nav_customers:
                    // TODO: 10/1/2018 change to supplier frag
                    badgeDrawable.setEnabled(false);
                    supplier.setText("");
                    customer.setText("");
                    if(!s .contains("CustomerFragmentTabs")){


                    CustomerFragmentTabs customerFragment = new CustomerFragmentTabs();
                addSupportFragment(customerFragment, AppConstant.TRANSITION_TYPES.SLIDE,false);
                    }
                  //  popBackStackTillEntry(1);
                break;

                case R.id.nav_supplier:
                    // TODO: 10/1/2018 change to customer frag
                    badgeDrawable.setEnabled(false);
                    supplier.setText("");
                    customer.setText("");
                    if(!s .contains("SupplierFragmentTabs")) {


                        SupplierFragmentTabs supplierFragment = new SupplierFragmentTabs();
                        addSupportFragment(supplierFragment, AppConstant.TRANSITION_TYPES.SLIDE, false);
                    }
                   // popBackStackTillEntry(1);
                break;

   case R.id.nav_product:






       Bundle bundle = new Bundle();
       bundle.putString(ProductListFragment.companyId,  String.valueOf(prefHelper.getCompanyProfile().getCompanyID()));
       bundle.putString(ProductListFragment.productCategoryId, "");
       ProductListFragment ProductListFragment = new ProductListFragment();
       ProductListFragment.setArguments(bundle);
       addSupportFragment(ProductListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);

                   // popBackStackTillEntry(1);
                break;


            case R.id.nav_logout:
                logoutUser();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}