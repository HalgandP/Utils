import android.util.Log;

public class ExceptionUtil extends Exception
{
    private String TAG;
    private String errorMessage;

    public ExceptionUtil(String TAG, ErrorType errorType, String identifier, ExceptionUtil e) {
        super(e);
        this.TAG = TAG;
        this.errorMessage = getErrorMessage(errorType, identifier, e.getMessage());
    }

    public void log() {
        Log.e(TAG, errorMessage);
        //Crashlytics.logException(this);
    }

    private String getErrorMessage(ErrorType errorType, String identifier, String e)
    {
        switch (errorType)
        {
            case TYPE1: return "... " + identifier + " error: "+ e;
            case TYPE2: return "... " + identifier + " error: "+ e;
        }

        return null;
    }

    public enum ErrorType {
        TYPE1,
        TYPE2
    }
}