package com.spgroup.managehouseholdapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spgroup.managehouseholdapp.model.FileUpload;
import com.spgroup.managehouseholdapp.service.FileUploadService;

@RestController
@RequestMapping("/file")
public class FileUploadController {
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/upload")
	public FileUpload uploadFile(@RequestParam("file") MultipartFile file) {
		FileUpload fileUpload = new FileUpload();
		try {
			fileUpload = fileUploadService.store(file);
			fileUpload.setMessage("Uploaded the file successfully: " + file.getOriginalFilename()); 
		} catch (Exception e) {
			fileUpload.setMessage("Could not upload the file: " + file.getOriginalFilename() + "!");
		}
		return fileUpload;
	}
	
  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    FileUpload fileUpload = fileUploadService.getFile(id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUpload.getName() + "\"")
        .body(fileUpload.getData());
  }

}
