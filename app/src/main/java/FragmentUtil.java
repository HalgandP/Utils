
import android.app.Activity;
import android.os.Build;

import android.transition.Fade;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hp.myapplication.R;


public class FragmentUtil {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;

    public enum AnimationType {
        RIGHT_IN_LEFT_OUT,
        LEFT_IN_RIGHT_OUT,
        FADE_IN
    }

    // Use this method to display a Fragment like a visual Tip
    public static void performTransition(@NonNull Activity activity, @NonNull FrameLayout container, @NonNull Fragment fragmentToPopulate, @Nullable AnimationType animationType)
    {
        doTransition(activity, container.getId(), fragmentToPopulate, animationType);
    }


    // Use this method to display a Fragment like a visual Tip (with the id directly)
    public static void performTransition(@NonNull Activity activity, int containerId,@NonNull Fragment fragmentToPopulate,@Nullable AnimationType animationType)
    {
        doTransition(activity, containerId, fragmentToPopulate, animationType);
    }



    private static void doTransition(@NonNull Activity activity,int containerId,@NonNull Fragment fragmentToPopulate,@Nullable AnimationType animationType)
    {
        try
        {
            if (activity.isDestroyed() || activity.isFinishing()) {
                //Crashlytics.log(Log.DEBUG, FragmentUtil.class.getSimpleName(), "Cannot display Fragment, activity is destroyed or finishing.");
                return;
            }

            FragmentManager fragmentManager;
            if (activity instanceof AppCompatActivity) {
                fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            } else if (activity instanceof FragmentActivity) {
                fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            } else {
                //Crashlytics.logException(new Exception("Cannot display Fragment, the activity is not an instance of AppCompatActivity or FragmentActivity"));
                return;
            }


            Fragment previousFragment = fragmentManager.findFragmentById(containerId);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (animationType != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 1. Exit for Previous Fragment
                if (previousFragment != null && !animationType.equals(AnimationType.FADE_IN)) {
                    setCustomAnimation(fragmentTransaction, animationType);
                } else {
                    // 3. Enter Transition for New Fragment
                    Fade enterFade = new Fade();
                    enterFade.setStartDelay(0);
                    enterFade.setDuration(FADE_DEFAULT_TIME);
                    fragmentToPopulate.setEnterTransition(enterFade);
                }
            }

            fragmentTransaction.replace(containerId, fragmentToPopulate);
            fragmentTransaction.commit();
        }
        catch (ExceptionUtil e)
        {
            e.printStackTrace();
            //Crashlytics.logException(e);
        }
    }

    public static void removeFragment(@NonNull Activity activity,@NonNull Fragment fragmentToRemove, boolean isAnimated)
    {
        try
        {
            if (activity == null) {
                //Crashlytics.logException(new Exception("Cannot remove Fragment, activity is null."));
                return;
            }
            if (activity.isDestroyed() || activity.isFinishing()) {
                //Crashlytics.log(Log.DEBUG, FragmentUtil.class.getSimpleName(), "Cannot display Fragment, activity is destroyed or finishing.");
                return;
            }


            FragmentManager fragmentManager;
            if (activity instanceof AppCompatActivity) {
                fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            } else if (activity instanceof FragmentActivity) {
                fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            } else {
                //Crashlytics.logException(new Exception("Cannot remove Fragment, the activity is not an instance of AppCompatActivity or FragmentActivity"));
                return;
            }


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isAnimated) {
                Fade exitFade = new Fade();
                exitFade.setDuration(FADE_DEFAULT_TIME);
                fragmentToRemove.setExitTransition(exitFade);
            }

            fragmentTransaction.remove(fragmentToRemove).commit();
        }
        catch (ExceptionUtil e)
        {
            e.printStackTrace();
            //Crashlytics.logException(e);
        }
    }

    private static void setCustomAnimation(FragmentTransaction fragmentTransaction, AnimationType animationType)
    {
        switch (animationType)
        {
            case RIGHT_IN_LEFT_OUT:
                fragmentTransaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
                break;

            case LEFT_IN_RIGHT_OUT:
                fragmentTransaction.setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right);
                break;
        }
    }
}
