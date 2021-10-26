package com.scripted.jsonWriter;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.fasterxml.jackson.databind.ObjectMapper;

public class HtmlData {

	

public String getHtmlData( String strJsonData ) {
	System.out.println("Inside html writer file");
    return jsonToHtml( new JSONObject( strJsonData ) );
}

private String jsonToHtml( Object obj ) {
    StringBuilder html = new StringBuilder( );
System.out.println("Inside json to html converter");
    try {
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject)obj;
            String[] keys = JSONObject.getNames( jsonObject );

            html.append("<div class=\"json_object\">");

            if (keys.length > 0) {
                for (String key : keys) {
                    // print the key and open a DIV
                    html.append("<div><span class=\"json_key\">")
                        .append(key).append("</span> : ");

                    Object val = jsonObject.get(key);
                    // recursive call
                    html.append( jsonToHtml( val ) );
                    // close the div
                    html.append("</div>");
                }
            }

            html.append("</div>");

        } else if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray)obj;
            for ( int i=0; i < array.length( ); i++) {
                // recursive call
                html.append( jsonToHtml( array.get(i) ) );                    
            }
        } else {
            // print the value
            html.append( obj );
        }                
    } catch (JSONException e) { e.printStackTrace(); }
  System.out.println(html.toString( ));
    return html.toString( );
}

}