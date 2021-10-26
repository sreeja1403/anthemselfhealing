package com.scripted.jsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ModifyData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String verify, putData = null;
		File file = new File("C:/Users/u38119/Desktop/testreport-jsoup/SelfHealingReport.html");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "", oldtext = "";
            while((line = reader.readLine()) != null)
                {
                oldtext += line + "\r\n";
            }
            reader.close();
            // replace a word in a file
            String newtext = oldtext.replaceAll("NoSuchElement_modify", "0");
            FileWriter writer = new FileWriter("C:/Users/u38119/Desktop/testreport-jsoup/SelfHealingReport.html");
            writer.write(newtext);writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
