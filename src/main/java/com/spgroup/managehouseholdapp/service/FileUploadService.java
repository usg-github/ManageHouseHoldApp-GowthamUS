package com.spgroup.managehouseholdapp.service;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import com.spgroup.managehouseholdapp.dao.FileUploadDAO;
import com.spgroup.managehouseholdapp.model.FileUpload;

@Service
public class FileUploadService {
	
	@Autowired
	private FileUploadDAO fileUploadDAO;

	public FileUpload store(MultipartFile file) throws IOException {
		

		//Convert bytes[] to BufferedImage, Resize image and back again BufferedImage to bytes[]
		byte[] imageArray = resizeImage(ImageIO.read(new ByteArrayInputStream(file.getBytes())),150,150);
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName(fileName);
		fileUpload.setType(file.getContentType());
		fileUpload.setData(imageArray);
		
		return fileUploadDAO.saveAndFlush(fileUpload);
	}

	
	byte[] resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    
	    graphics2D.dispose();
	    
	    
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, "jpg", baos);
		byte[] bytes = baos.toByteArray();
	    
	    return bytes;
	}
	public FileUpload getFile(String id) {
		return fileUploadDAO.findById(Long.parseLong(id)).get();
	}

}
