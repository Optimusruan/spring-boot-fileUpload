package controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@CrossOrigin("*")
public class FileUploadController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private final String ROOT = "/var/lib/tomcat8/data/";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public void upLoadFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedOutputStream stream = null;
        JSONObject resultJson = new JSONObject();
        Map<String, String> status = new HashMap<>();
        Map<String, String> respBody = new HashMap<>();
        List<String> upLoadMsg = new ArrayList<>();

        //遍历上传的文件数组
        for (MultipartFile file : files) {
            //拼接返回的数据字符串
            StringBuffer tempMsg = new StringBuffer();
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //保存时的文件名
            Random random = new Random();
            random.setSeed(file.getSize());
            String saveFileName = Integer.toString(Math.abs(random.nextInt()) % 100000) + Long.toString(Calendar.getInstance().getTime().getTime());

            //文件后缀名
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(ROOT + saveFileName + suffixName);
                    Files.write(path, bytes);
                    tempMsg.append("200|").append(saveFileName + suffixName).append("|success");
                } catch (Exception e) {
                    tempMsg.append("202|").append("|error");
                    System.out.println(e.toString());
                    stream = null;
                }
            } else {
                tempMsg.append("201|").append("|empty");
            }
            upLoadMsg.add(tempMsg.toString());
        }

        //封装返回的JSON
        respBody.put("listVal", upLoadMsg.toString());
        status.put("statusCode", "200");
        JSONObject temp = new JSONObject();
        temp.put("status", status);
        temp.put("respBody", respBody);
        resultJson.put("resp", temp.toString());
        response.getWriter().print(resultJson.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename, HttpServletResponse response) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            //如果是图片格式，直接返回图片，否则直接下载文件
            if (suffix.equals("png") || suffix.equals("jpeg") || suffix.equals("jpg") || suffix.equals("gif") || suffix.equals("bmp")) {

                RandomAccessFile f = new RandomAccessFile(ROOT + filename, "r");
                byte[] b = new byte[(int) f.length()];
                f.readFully(b);
                if (suffix.equals("jpg")) {
                    headers.add("content-type", "image/jpeg");
                } else {
                    headers.add("content-type", "image/" + suffix);
                }
                return new ResponseEntity<byte[]>(b, headers, HttpStatus.CREATED);
//                return ResponseEntity.ok().body(resourceLoader.getResource("file:" + Paths.get(ROOT, filename)).getFile());
            } else {
                File file = resourceLoader.getResource("file:" + Paths.get(ROOT, filename)).getFile();
                if (file.exists()) {
                    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                    headers.add("Content-Disposition", "attachment; filename=" + filename);
                    headers.add("Pragma", "no-cache");
                    headers.add("Expires", "0");
                    return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
}
