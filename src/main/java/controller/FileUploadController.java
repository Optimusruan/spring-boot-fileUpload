package controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public void upLoadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        BufferedOutputStream stream = null;
        JSONObject resultJson = new JSONObject();
        Map<String, String> status = new HashMap<>();
        Map<String, String> respBody = new HashMap<>();
        List<String> upLoadMsg = new ArrayList<>();


        for (MultipartFile file : files) {
            StringBuffer tempMsg = new StringBuffer();
            //原始文件名
            String originalFilename = file.getOriginalFilename();

            //保存时的文件名
            Random random = new Random();
            random.setSeed(file.getSize());
            String saveFileName = Integer.toString(Math.abs(random.nextInt()) % 100000) + Long.toString(Calendar.getInstance().getTime().getTime());

            //文件后缀名
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    File saveFile = new File(ROOT + saveFileName + suffixName);
                    stream = new BufferedOutputStream(new FileOutputStream(saveFile));
                    stream.write(bytes);
                    stream.close();
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
        respBody.put("listVal", upLoadMsg.toString());
        status.put("statusCode", "200");
        JSONObject temp = new JSONObject();
        temp.put("status", status);
        temp.put("respBody", respBody);
        resultJson.put("resp", temp.toString());
        response.getWriter().print(resultJson.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{filename}.{suffix}}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename, @PathVariable String suffix) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
}
