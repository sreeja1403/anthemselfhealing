package com.scripted.testscripts;

import com.scripted.jsonParser.JsonEncryptor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scripted.jsonWriter.HealingInfo;


public class Reference {
	
	public static String readFile(String encyrpath) {
		String fileContent = null;
		try {
			File file = new File(encyrpath);
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			fileContent = new String(chars);
		} catch (Exception e) {
			System.out.println("Exception while reading file content :" + e);
		}

		return fileContent;
	}
	
	public static void decryptFile(String path) {
		try {
			String dec = JsonEncryptor.decryptPass(readFile(path));
			// System.out.println("Value after decryption :" + dec);
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(dec);
			writer.close();
			System.out.println("Decrypted file written back to file successfully");
		} catch (Exception e) {
			System.out.println("Exception while decrypting file : " + e);
		}
	}
	
	public static void encryptFile(String path) {
		try {
			String enc = JsonEncryptor.encryptPass(readFile(path));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(enc);
			writer.close();
			System.out.println("Encrypted file written back to file successfully");
		} catch (Exception e) {
			System.out.println("Exception while encrypting file :" + e);
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		//decryptFile("D:\\SelfHealingProj\\objectrepo\\SelfHealingProj.sky");
		encryptFile("D:\\SelfHealingProj\\objectrepo\\SelfHealingProj.sky");
		//encryptFile("C:\\Users\\u38119\\Desktop\\jsonfiles\\Mobile_Native.sky");

	}

}
