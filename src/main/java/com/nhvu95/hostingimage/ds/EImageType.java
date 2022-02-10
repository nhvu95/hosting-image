package com.nhvu95.hostingimage.ds;

public enum EImageType {
	PNG(1),
	JPG(2),
	SVG(3);

    private final int value;
	
    EImageType(final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
    public String getStringValue() { 
    	switch(value) {
	    	case 1:
	    		return "png";
	    	case 2:
	    		return "jpg";
	    	case 3:
	    		return "svg";
	    	default:
	    		return "png";
    	}
    }
}
