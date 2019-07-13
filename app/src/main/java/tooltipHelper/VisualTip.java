package tooltipHelper;

public class VisualTip {

    // Correspond to the view the visual tip should be displayed
    private VisualTipViewEnum viewEnumTip;
    // Value by default is set to true, if the user already saw this tips, the value is false in his profile_data
    private boolean shouldDisplay;
    private boolean shouldSave;
    // Correspond to the String ressource id
    private int textStringId;

    public VisualTip(VisualTipViewEnum viewEnumTip, int textStringId, boolean shouldBeSaved) {
        this.viewEnumTip = viewEnumTip;
        this.textStringId = textStringId;
        this.shouldDisplay = true;
        this.shouldSave = shouldBeSaved;
    }

    public VisualTip(VisualTipViewEnum viewEnumTip, boolean shouldBeSaved) {
        this.viewEnumTip = viewEnumTip;
        this.shouldDisplay = true;
        this.shouldSave = shouldBeSaved;
    }


    public VisualTipViewEnum getViewEnumTip() {
        return viewEnumTip;
    }

    public boolean shouldBeDisplayed() {
        return shouldDisplay;
    }

    public int getTextStringId() {
        return textStringId;
    }

    public void setShouldBeDisplayed(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
    }

    public void setShouldBeSaved(boolean shouldSave) { this.shouldSave = shouldSave;}

    public boolean shouldBeSaved() { return shouldSave;}

    public enum VisualTipViewEnum {

        // SINGLE QUESTION
        TYPE1("path");

        private final String enumStatusString;

        VisualTipViewEnum(final String enumStatusString) {
            this.enumStatusString = enumStatusString;
        }

        @Override
        public String toString(){
            return enumStatusString;
        }

        public static VisualTipViewEnum valueFor(String statusString){
            for(VisualTipViewEnum fname : values()) {
                if(fname.enumStatusString.equals(statusString)){
                    return fname;
                }
            }
            return null;
        }
    }
}
