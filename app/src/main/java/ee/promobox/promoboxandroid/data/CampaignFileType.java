package ee.promobox.promoboxandroid.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignFileType {
    IMAGE(1), AUDIO(2), VIDEO(3), HTML(4), SWF(5), RTP(6),RSS(7);

    private int type;
    private boolean rss = false;

    CampaignFileType(int type) {
        this.type = type;
    }

    public boolean isRss() {
        return rss;
    }

    public void setRss(boolean rss) {
        this.rss = rss;
    }

    @JsonCreator
    public static CampaignFileType valueOf(int type) {
        for (CampaignFileType cType : CampaignFileType.values()) {
            if (cType.type == type) return cType;
        }

        return  CampaignFileType.IMAGE;
    }

    @JsonValue
    public int getTypeNumber() {
        return type;
    }


    public boolean isDownloadable() {
        return this != HTML && this != RTP;
    }
}
