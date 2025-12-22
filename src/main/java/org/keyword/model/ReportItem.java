package org.keyword.model;

public class ReportItem {
    private String url;
    private String summary;

    public ReportItem(String url, String summary) {
        this.url = url;
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public String getSummary() {
        return summary;
    }
}
