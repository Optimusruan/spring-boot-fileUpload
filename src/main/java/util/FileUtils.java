package util;

import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    //文件类型
    /*-----------------------------目前可以识别的类型----------------------------*/
    public final static Map<String, String> FILE_TYPE_MAP= new HashMap<String, String>(){{
        put("jpg", "FFD8FF"); //JPEG
        put("png", "89504E47"); //PNG
        put("gif", "47494638"); //GIF
        put("tif", "49492A00"); //TIFF
        put("bmp", "424D"); //Windows Bitmap
        put("dwg", "41433130"); //CAD
        put("html", "68746D6C3E"); //HTML
        put("rtf", "7B5C727466"); //Rich Text Format
        put("xml", "3C3F786D6C");
        put("zip", "504B0304");
        put("rar", "52617221");
        put("psd", "38425053"); //PhotoShop
        put("eml", "44656C69766572792D646174653A"); //Email [thorough only]
        put("dbx", "CFAD12FEC5FD746F"); //Outlook Express
        put("pst", "2142444E"); //Outlook
        put("office", "D0CF11E0"); //office类型，包括doc、xls和ppt
        put("mdb", "000100005374616E64617264204A"); //MS Access
        put("wpd", "FF575043"); //WordPerfect
        put("eps", "252150532D41646F6265");
        put("ps", "252150532D41646F6265");
        put("pdf", "255044462D312E"); //Adobe Acrobat
        put("qdf", "AC9EBD8F"); //Quicken
        put("pwl", "E3828596"); //Windows Password
        put("wav", "57415645"); //Wave
        put("avi", "41564920");
        put("ram", "2E7261FD"); //Real Audio
        put("rm", "2E524D46"); //Real Media
        put("mpg", "000001BA"); //
        put("mov", "6D6F6F76"); //Quicktime
        put("asf", "3026B2758E66CF11"); //Windows Media
        put("mid", "4D546864"); //MIDI (mid)
    }};



}
