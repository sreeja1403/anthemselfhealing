package com.scripted.reporting.selfhealing;

import com.scripted.jsonWriter.TestStep;

public class ReportMsgsTemplate {
	
	
	static String RECOM_MSG="";
	public StringBuffer HealSuccessWthRecommendations(String modelID, TestStep teststep)
	{
		
	StringBuffer recommBuffer = new StringBuffer();
	String imageStyle;
    String divStyleMain;
    String divStyleImage;
    if(teststep.getTechnology().equalsIgnoreCase("web"))
    {
    	imageStyle = "class='imgpopupsz'";
        divStyleMain = "class='col-sm-12 col-md-7";
        divStyleImage = "class='col-sm-12 col-sm-5";
    }
    else
    {
    	imageStyle = "style='max-width:200px;border:0.6pt solid #EDEFEF;'";
        divStyleMain = "class='col-sm-12 col-md-9";
        divStyleImage = "class='col-sm-12 col-sm-3";
    }
    
		return recommBuffer;
		
	}

}
