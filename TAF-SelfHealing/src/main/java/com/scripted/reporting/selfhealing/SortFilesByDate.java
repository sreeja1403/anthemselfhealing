package com.scripted.reporting.selfhealing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;

public class SortFilesByDate {
	
	public SortFilesByDate(){
		
	}
	
	 private static void printFiles(File[] files) {
	      for (File file : files) {
	          long m = getFileCreationEpoch(file);
	          Instant instant = Instant.ofEpochMilli(m);
	          LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	          System.out.println(date+" - "+file.getName());
	      }
	  }

	  public static File[]  sortFilesByDateCreated (File[] files) {
	      Arrays.sort(files, new Comparator<File>() {
	          public int compare (File f1, File f2) {
	              long l1 = getFileCreationEpoch(f1);
	              long l2 = getFileCreationEpoch(f2);
	              return Long.valueOf(l1).compareTo(l2);
	          }
	      });
		return files;
	  }

	  public static long getFileCreationEpoch (File file) {
	      try {
	          BasicFileAttributes attr = Files.readAttributes(file.toPath(),
	                  BasicFileAttributes.class);
	          return attr.creationTime()
	                     .toInstant().toEpochMilli();
	      } catch (IOException e) {
	          throw new RuntimeException(file.getAbsolutePath(), e);
	      }
	  }
}
