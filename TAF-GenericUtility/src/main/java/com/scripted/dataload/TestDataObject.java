package com.scripted.dataload;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestDataObject {

	private LinkedHashMap<String, Map<String, String>> tableData;
	private Map<String, String> mapData;
	private Boolean hasMapData = false;
	private Boolean hasTableData = false;

	public Map<String, String> getMapData() {
		return mapData;
	}

	public void setMapData(Map<String, String> mapData) {
		this.mapData = mapData;
		hasMapData = true;
	}

	public LinkedHashMap<String, Map<String, String>> getTableData() {
		return tableData;
	}

	public void setTableData(LinkedHashMap<String, Map<String, String>> tableData) {
		this.tableData = tableData;
		hasTableData = true;
	}
	
	public Boolean getHasMapData() {
		return hasMapData;
	}

	public Boolean getHasTableData() {
		return hasTableData;
	}
}
