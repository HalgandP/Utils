package recyclerViewHelpers;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private boolean isListGoingUp = false;
    private boolean isUserScrolling = false;
    private boolean isUserScrolled = false;

    private OnScrollListener onScrollListener;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager, OnScrollListener onScrollListener)
    {
        this.layoutManager = layoutManager;
        this.onScrollListener = onScrollListener;
    }

    public abstract static class OnScrollListener
    {
        public void onScrollingUp(){}

        public void onBottomReached(){}
    }


    @Override
    public void onScrollStateChanged(RecyclerView rv, int newState) {
        super.onScrollStateChanged(rv, newState);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        // User is scrolling
        if(RecyclerView.SCROLL_STATE_DRAGGING ==newState)
        {
            this.isUserScrolling = true;
        }
        // User finished scrolling
        else if(RecyclerView.SCROLL_STATE_IDLE ==newState)
        {
            isUserScrolling = false;
            // Check if user scrolled down
            if(!isListGoingUp && isUserScrolled)
            {
                // Check if he reached the bottom
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && lastVisibleItemPosition >= totalItemCount - 1){

                    if (this.onScrollListener != null)
                    {
                        this.onScrollListener.onBottomReached();
                        isUserScrolled = false;
                    }
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (isUserScrolling) {

            isUserScrolled = true;
            // User finger is moving up but the list is going down
            if (dy > 0) {
                isListGoingUp = false;
            }
            // User finger is moving down but the list is going up
            else {
                isListGoingUp = true;
                if (this.onScrollListener != null)
                {
                    this.onScrollListener.onScrollingUp();
                }
            }
        }
    }
}
