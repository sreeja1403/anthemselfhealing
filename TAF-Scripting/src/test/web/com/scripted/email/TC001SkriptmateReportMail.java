package com.scripted.email;

import java.io.IOException;

import com.scripted.Email.Sendmail;

public class TC001SkriptmateReportMail {

public static void main(String[] args) {
		try {
			Sendmail.send("src/main/resources/Email/mail.properties", "target/cucumber.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
}

}


