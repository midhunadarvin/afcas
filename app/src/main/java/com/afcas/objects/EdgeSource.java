package com.afcas.objects;

public enum EdgeSource {
    Principal("Principal"),
    Operation("Operation"),
    Resource("Resource");

    private final String text;

    /**
     * @param text
     */
    EdgeSource(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}