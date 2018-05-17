package controller;

import org.json.JSONObject;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.*;

@Controller
@CrossOrigin("*")
@RequestMapping("/")
public class FileUploadController {

    private final String ROOT = "/var/lib/tomcat8/data/";
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public void upLoadFile(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        BufferedOutputStream stream = null;
        JSONObject resultJson = new JSONObject();
        Map<String, String> status = new HashMap<>();
        Map<String, String> respBody = new HashMap<>();
        List<String> upLoadMsg = new ArrayList<>();


        for (MultipartFile file : files) {
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
                    stream = new BufferedOutputStream(new FileOutputStream(new File(saveFileName + suffixName)));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;

                }
            } else {
                //文件为空
            }

        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{filename}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename,ResourceLoader resourceLoader) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
