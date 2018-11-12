package com.application.lumaque.bizlinked.helpers.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.ui.dialogs.DateFormatHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class Utils {
    private static final String REC_CONSTANT = "REC";

    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    public static boolean isEmptyOrNull(String string) {
        if (string == null)
            return true;

        return (string.trim().length() <= 0);
    }

    public static int getResourceId(Context context, String pVariableName, String pResourcename) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, context.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    public static BaseFragment getFragmentByName(Context context, String fragName) {
        return (BaseFragment) Fragment.instantiate(context, context.getPackageName() + ".fragments." + fragName);
    }


    public static void showToast(Context ctx, String msg, int type) {

        switch (type) {
            case AppConstant.TOAST_TYPES.INFO:
                Toasty.normal(ctx, msg, R.drawable.ic_alert).show();
                break;

            case AppConstant.TOAST_TYPES.SUCCESS:
                Toasty.success(ctx, msg).show();
                break;

            case AppConstant.TOAST_TYPES.ERROR:
                Toasty.error(ctx, msg).show();
                break;
        }
    }

//    public static void setImageFromGlide(Context ctx, final ImageView imageView, final ProgressBar progressBar, String url) {
//
//        if (!Utils.isEmptyOrNull(url)) {
//            Glide.with(ctx).load(url)
//                    .apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            Log.d("onError", "onError");
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            imageView.setVisibility(View.VISIBLE);
//                            if (progressBar != null)
//                                progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//                    }).into(imageView);
//        }
//    }

    //Check loaction enable or not
    public static boolean checkLocationPermission(Context context) {
        String permission[] = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return ContextCompat.checkSelfPermission(context, permission[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, permission[1]) == PackageManager.PERMISSION_GRANTED;
    }

    //To get distance between two points
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    //    Get Distance Between Two Points Android Native
    public static float getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] distance = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //To detect that location is on or not//
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public static void makeLinks(CheckBox checkBox, String[] links, ClickableSpan[] clickableSpans) {
        Spannable mySpannable = (Spannable) checkBox.getText();
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = checkBox.getText().toString().indexOf(link);
            mySpannable.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());
        checkBox.setText(mySpannable, CheckBox.BufferType.SPANNABLE);

    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSnackBar(Context ctx, View v, String text, int color) {

        Snackbar snackbar;
        snackbar = Snackbar.make(v, text, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(color);
        snackbar.show();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {

        //Setting the width and height of the Bitmap that will be returned                        //equal to the original Bitmap that needs round corners.
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Creating canvas with output Bitmap for drawing
        Canvas canvas = new Canvas(output);

        //Setting paint and rectangles.
        final int color = Color.BLACK;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        //SetXfermode applies PorterDuffXfermode to paint.
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public static boolean isEmptyValidate(Context ctx, View view, String hint, String text) {

        if (text.isEmpty()) {
            Utils.showSnackBar(ctx, view, hint + " required", ctx.getResources().getColor(R.color.grayColor));

            /*editText.setError("Empty field");
            editText.requestFocus();
            */
            return false;
        } else {
            return true;
        }

    }

    public static boolean isValidEmail(Context ctx, View view, String email, EditText editText) {
     /*   String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern;
        pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();*/
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            Utils.showSnackBar(ctx, view, "Email Not Valid", ctx.getResources().getColor(R.color.grayColor));

          /*  editText.setError("Email Not Valid");
        editText.requestFocus();
*/
        return false;
    }

    public static boolean isAcceptablePassword(Context ctx, View view, String password, EditText editText) {
        if (TextUtils.isEmpty(password)) {
            Utils.showSnackBar(ctx, view, "Please enter the password", ctx.getResources().getColor(R.color.grayColor));

            //  editText.setError("Please Enter Password");
            // editText.requestFocus();
            return false;
        }
        password = password.trim();
        int len = password.length();
        if (len < MIN_PASSWORD_LENGTH || len > MAX_PASSWORD_LENGTH) {
            //    Toast.makeText(getContext(), "wrong size, it must have at least 8 characters and less than 20.", Toast.LENGTH_SHORT).show();
            Utils.showSnackBar(ctx, view, "Password size must have at least 8 characters and less than 20", ctx.getResources().getColor(R.color.grayColor));

            //   editText.setError("Password size must have at least 8 characters and less than 20.");
            // editText.requestFocus();
            return false;
        }
        char[] aC = password.toCharArray();
        for (char c : aC) {
            if (Character.isUpperCase(c)) {
               /* PassWord.setError("password In uppercase");
                PassWord.requestFocus();*/
                //Toast.makeText(getContext(), "password Is uppercase", Toast.LENGTH_SHORT).show();

                //    System.out.println(c + " is uppercase.");

            } else if (Character.isLowerCase(c)) {
                //  System.out.println(c + " is lowercase.");
              /*  PassWord.setError("password In lowercase");
                PassWord.requestFocus();*/
                // Toast.makeText(getContext(), "password Is lowercase", Toast.LENGTH_SHORT).show();
            } else if (Character.isDigit(c)) {
             /*   PassWord.setError("Password is digit.");
                PassWord.requestFocus();*/
                // System.out.println(c + " is digit.");
            } else if (SPECIAL_CHARACTERS.indexOf(String.valueOf(c)) >= 0) {
                ///    System.out.println(c + " is valid symbol.");
              /*  editText.setError("password contains symbol");
                editText.requestFocus();
              */

                Utils.showSnackBar(ctx, view, "Password contains symbol", ContextCompat.getColor(ctx, R.color.grayColor));

                return false;
                // Toast.makeText(getContext(), "password contains symbol", Toast.LENGTH_SHORT).show();
            } else {
                Utils.showSnackBar(ctx, view, "Invalid character in the password", ctx.getResources().getColor(R.color.grayColor));

                /*   editText.setError("invalid character in the password");
                editText.requestFocus();
             */   //Toast.makeText(getContext(), "invalid character in the password", Toast.LENGTH_SHORT).show();
                //   System.out.println(c + " is an invalid character in the password.");
                return false;
            }
        }
        return true;
    }


    //    public static void setImageWithGlide(final Context ctx, final ImageView imageView, String url) {
//        //if (!Utils.isEmptyOrNull(url)) {
//
//            Glide.with(ctx)
//                .load(url)
//                .apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
//                .listener(new RequestListener<Drawable>() {
//
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
//                    Log.d("onError", ctx.getResources().getString(R.string.glide_image_error));
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
//                    return false;
//                }
//            }).into(imageView);
//
//
//        //}
//    }
//    public static void setImageWithGlide(final Context ctx, final ImageView imageView, String url) {
//        //if (!Utils.isEmptyOrNull(url)) {
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.noimage);
//        requestOptions.error(R.drawable.noimage);
//
//        Glide.with(ctx)
//                .setDefaultRequestOptions(requestOptions)
//                .load(url)
//                .transition(GenericTransitionOptions.with(R.anim.fade_in))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                        return false;
//                    }
//                })
//                .into(imageView);
//        // }
//    }

    public static Bitmap loadBitmapFromFile(String mBackgroundFilename) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inPurgeable = true;
        localOptions.inInputShareable = true;
        return BitmapFactory.decodeFile(mBackgroundFilename, localOptions);
    }

//    public static String saveBitmapToFile(Activity context, Bitmap bitmap) {
//        String filePath = "";
//        try {
//            File file = generateFile(context);
//            file.createNewFile();
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            filePath = file.getAbsolutePath();
//            return filePath;
//        } catch (IOException exception) {
//        }
//        return filePath;
//    }

//    private static File generateFile(Activity context) {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String md5 = String.valueOf(DigestUtils.md5(timestamp));
//        File localFile = getCacheDir(context, "img_cache");
//        if (!localFile.exists())
//            localFile.mkdirs();
//        return new File(localFile, md5);
//
//    }

    private static File getCacheDir(Context context, String dirName) {
        return new File(context.getCacheDir(), dirName);
    }

    public static Bitmap drawViewToBitmap(View view, int color) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(color);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static void shareWithImage(Context context, String description, Bitmap bitmap) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, description);
        String path = null;
        path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", null);
        Uri screenshotUri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "Share image via..."));
    }


    public static String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public static void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public static String getAudioFileName() {
        return new StringBuilder()
                .append(REC_CONSTANT)
                .append("-")
                .append(DateFormatHelper.getDateForAudioFileName()).toString();
    }

    public static void deleteFileForPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public static File createFileFromBitmap(Activity activity, Bitmap bitmap, String fileName) {

        File filesDir = activity.getFilesDir();
        File imageFile = new File(filesDir, fileName + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(activity.getClass().getSimpleName(), "Error writing bitmap", e);
        }


        return imageFile;


//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        byte[] bitmapdata = bos.toByteArray();
//        FileOutputStream fos = null;
//        File f = null;
//        try {
//            f = new File(activity.getCacheDir(), fileName);
//            f.createNewFile();
//            fos = new FileOutputStream(f);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return f;

    }


    public static Bitmap getScaledDownBitmap(Bitmap bitmap, int threshold, boolean isNecessaryToKeepOrig) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = width;
        int newHeight = height;

        if (width > height && width > threshold) {
            newWidth = threshold;
            newHeight = (int) (height * (float) newWidth / width);
        }

        if (width > height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        if (width < height && height > threshold) {
            newHeight = threshold;
            newWidth = (int) (width * (float) newHeight / height);
        }

        if (width < height && height <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        if (width == height && width > threshold) {
            newWidth = threshold;
            newHeight = newWidth;
        }

        if (width == height && width <= threshold) {
            //the bitmap is already smaller than our required dimension, no need to resize it
            return bitmap;
        }

        return getResizedBitmap(bitmap, newWidth, newHeight, isNecessaryToKeepOrig);
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, boolean isNecessaryToKeepOrig) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (!isNecessaryToKeepOrig) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    /**
     * Function creates a formatted string with numeric value
     * combined with currency , currency is shown in smaller font and
     * grey text color and numeric value is shown in app color
     * with larger font
     *
     * @param currency the value for currency
     * @param value    the numeric value
     * @param context
     * @return SpannableString
     */
    public static SpannableString formatCurrency(Context context, String currency, String value) {

        int textSizeCurrency = context.getResources().getDimensionPixelSize(R.dimen.s10);
        int textSizeNumericValue = context.getResources().getDimensionPixelSize(R.dimen.s12);

        SpannableString spannableString = new SpannableString(new StringBuilder(currency).append(" ").append(value));
        spannableString.setSpan(new AbsoluteSizeSpan(
                textSizeCurrency
        ), 0, currency.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannableString.setSpan(new AbsoluteSizeSpan(
                textSizeNumericValue
        ), currency.length() + 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        ;

        spannableString.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.appThemeColor)
        ), currency.length() + 1, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        ;

        return spannableString;

    }

}