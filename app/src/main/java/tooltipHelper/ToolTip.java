package tooltipHelper;


import android.content.Context;

import android.graphics.Rect;

import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

import com.hp.myapplication.R;


public class ToolTip extends PopupWindows implements OnDismissListener {

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;
    private View mRootView;
    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private LayoutInflater mInflater;
    private OnDismissListener mDismissListener;
    private TextView mToolTipText;
    private View mMainView;
    private View mGreyBackgroundView;
    private VisualTip mVisualTip;
    private int mAnimStyle;

    private boolean shouldUpdate;

    /**
     * @param context    Context
     * @param mainView The main view of the activity in which we'll add a grey layout background
     * @param visualTip The text that will be displayed (String ressource identifier)
     */
    public ToolTip(Context context, View mainView, VisualTip visualTip, boolean shouldUpdateDataBase) {
        super(context);

        mMainView = mainView;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mVisualTip = visualTip;

        this.shouldUpdate = shouldUpdateDataBase;

        buildUi();

        mAnimStyle = ANIM_AUTO;

        this.setOnDismissListener(this);
    }


    public void buildUi() {
        mRootView = mInflater.inflate(R.layout.layout_popupwindows_horizontal, null);

        mArrowDown = mRootView.findViewById(R.id.arrow_down);
        mArrowUp = mRootView.findViewById(R.id.arrow_up);

        LinearLayout mContainer = mRootView.findViewById(R.id.container);

        mToolTipText = mRootView.findViewById(R.id.tv_tooltip);
        this.buildText();

        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // Show a background with grey transparent color
        this.mGreyBackgroundView = mInflater.inflate(R.layout.layout_visualtip_grey_background, (ViewGroup) mMainView, false);
        ((ViewGroup) mMainView).addView(mGreyBackgroundView);

        setContentView(mRootView);
    }

    // Set the text from the VisualTip
    private void buildText()
    {
        // TODO
        String toolTipText = "";
        mToolTipText.setText(toolTipText);
    }

    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }


    // Show Popup  automatically positioned, on top or bottom of anchor view.
    public void show (View anchor) {
        preShow();

        int xPos, yPos, arrowPos;

        // Get the position in the screen of the anchor
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getMeasuredWidth(), location[1] + anchor.getMeasuredHeight());

        // Get the size of the pop up
        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int rootHeight = mRootView.getMeasuredHeight();
        int rootWidth = mRootView.getMeasuredWidth();

        // Retrieve the size screen width and height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Value relatives to the anchor
        int anchorLeft = anchorRect.left;
        int anchorRight = anchorRect.right;
        int anchorBottom = anchorRect.bottom;
        int anchorTop = anchorRect.top;
        int anchorWidth = anchor.getMeasuredWidth();
        int anchorCenterX = anchorRect.centerX();

        // Because we're using a ViewPager in the AnswersView, we have to retrieve the values of the view by this way else the value returned from the getLocationOnScreen are wrong
        boolean isFromViewPager = false;
//        isFromViewPager =mVisualTip.getViewEnumTip().equals(VisualTip.VisualTipViewEnum.VIEW_PAGER);
        if (isFromViewPager) {
            anchorLeft = (int) anchor.getX();
            anchorRight = anchorLeft + anchorWidth;
            anchorTop = (int) anchor.getY();
            anchorBottom = anchorTop + anchor.getMeasuredHeight();
            anchorCenterX = anchorWidth / 2;
        }


        // Anchor is on the Right or Left size of the screen
        if (anchorLeft >= (screenWidth / 2) || anchorRight <= (screenWidth / 2) || anchorWidth < screenWidth/2)
        {
            // We don't want the pop up to be fixed to the left side of the anchor, reduce x with the anchorWidth
            xPos = anchorLeft - anchorWidth;

            // In case the anchor was already on the very left size of the screen, set it to 0
            if (xPos < 0){
                xPos = 0;
            }
            // In case the popup at x is larger than the screen width, reduce x with the width difference
            else if ((xPos + rootWidth) > screenWidth) {
                xPos = xPos - (xPos + rootWidth - screenWidth);
            }

            // The x of the arrow will be set from the start of the popUp to the center of the anchor. We have to substract the xPos to be correctly set
            arrowPos = anchorCenterX - xPos;
        }
        // Anchor takes all the screen width
        else
        {
            // Calculate the difference between the anchor width and the pop up width
            int widthDiff = 0;

            // The anchor is larger than the popUp width, calculate the difference so we can center the popUp
            if (anchorWidth > rootWidth) {
                widthDiff = (anchorWidth - rootWidth) / 2;
            }

            // The popUp will be centered from the Anchor
            xPos = anchorLeft + widthDiff;

            // The x of the arrow will be set from the start of the popUp to the center of the anchor. We have to substract the xPos to be correctly set
            arrowPos = anchorCenterX - xPos;
        }

        int dyBottom = screenHeight - anchorBottom;
        boolean onTop = anchorTop > dyBottom;

        if (onTop) {
            yPos = anchorRect.top - rootHeight + mArrowDown.getMeasuredHeight();
        }
        else {
            yPos = anchorRect.bottom;
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

        setAnimationStyle(screenWidth, anchorCenterX, onTop);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRootView.setElevation(5);
        }

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    /**
     * Set animation style
     *
     * @param screenWidth screen width
     * @param requestedX distance from left edge
     * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top of anchor view
     * 		  and vice versa
     */
    private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
        int arrowPos = requestedX - mArrowUp.getMeasuredWidth()/2;

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_REFLECT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth/4) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth/4 && arrowPos < 3 * (screenWidth/4)) {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                } else {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                }

                break;
        }
    }

    /**
     * Show arrow
     *
     * @param whichArrow arrow type resource id
     * @param requestedX distance from left screen
     */
    private void showArrow(int whichArrow, int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        int leftMargin = (requestedX > arrowWidth / 2) ? requestedX - arrowWidth / 2 : requestedX;

//        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();
//        param.leftMargin = leftMargin;

        showArrow.setX(leftMargin);

        hideArrow.setVisibility(View.GONE);
    }


    public void setOnDismissListener(ToolTip.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {

        // Update on local, and if the visual.shouldBeSaved on dataBase
        if (shouldUpdate) {
            // Do something

            // If this visual tip need a special action to be updated on dataBase, it will not be updated before when calling updateVisualTip
            // because the shouldBeSaved value is false
            // When the special action is done (like clicking the skip button) the visualTip will be updated and won't be updated anymore
            mVisualTip.setShouldBeSaved(true);
        }

        if (mDismissListener != null) {
            mDismissListener.onToolTipDismissed();
        }

        // Remove the mGreyBackgroundView
        if (this.mGreyBackgroundView != null) {
            ViewGroup parent = (ViewGroup) this.mGreyBackgroundView.getParent();
            parent.removeView(this.mGreyBackgroundView);
        }
    }

    public interface OnDismissListener {
        void onToolTipDismissed();
    }

}
