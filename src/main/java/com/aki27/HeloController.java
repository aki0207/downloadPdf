package com.aki27;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HeloController {

	@Autowired
	ResourceLoader resourceLoader;

	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("message", "Hello Springboot");
		return "index";
	}

	@RequestMapping("/download")
	public String download(HttpServletResponse response) {
		Resource resource = resourceLoader.getResource("classpath:static/file/no_1.pdf");
		byte[] fileContent = StreamToByte(resource);

		//ファイル書き込み
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + "downloadFile.pdf");
		response.setContentLength(fileContent.length);

		OutputSreamWrite(response, fileContent);

		return null;
	}
	/**
	 * ブラウザでPDF表示
	 * @param request
	 * @param httpSession
	 * @param response
	 * @return
	 */
	@RequestMapping("/preivew")
	protected String preivew(      
		    HttpServletRequest request,
		        HttpSession httpSession,
		    HttpServletResponse response) {
		    try {
		    	Resource resource = resourceLoader.getResource("classpath:static/file/no_1.pdf");
				byte[] fileContent = StreamToByte(resource);   
		        response.setDateHeader("Expires", -1);
		        response.setContentType("application/pdf");
		        response.setContentLength(fileContent.length);
		        response.getOutputStream().write(fileContent);
		    } catch (Exception ioe) {
		    } finally {
		    }
		    return null;
		}

	/**
	 * InputStream から　バイト文字列に変換
	 * @param filepath
	 * @return
	 */
	private byte[] StreamToByte(Resource resource) {

		int nRead;
		InputStream is = null;
		byte[] fileContent = new byte[16384];
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		//ファイルをバイト形式に変換
		try {
			is = new FileInputStream(resource.getFile().toString());

			while ((nRead = is.read(fileContent, 0, fileContent.length)) != -1) {
				buffer.write(fileContent, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();
		} catch (FileNotFoundException e) {
			e.getStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ダウンロードファイル書き込み
	 * @param response
	 * @param fileContent
	 */
	public void OutputSreamWrite(HttpServletResponse response, byte[] fileContent) {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(fileContent);
			os.flush();
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
}