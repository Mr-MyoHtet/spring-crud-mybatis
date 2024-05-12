package com.mybatiscrud.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class FolderRefreshService {
	private final String folderPath = "src/main/resources/static/images/";

	public void refreshFolder() {
		// Implement logic to refresh the folder
		// For example, if you want to reload files from disk:
		File folder = new File(folderPath);
		// Perform operations like reloading files, updating metadata, etc.
		// ...
	}
}
