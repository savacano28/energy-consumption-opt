package fr.ifpen.synergreen.service.util;

import com.google.common.base.Strings;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FileUtils {
    // cf: https://stackoverflow.com/questions/4212861/what-is-a-correct-mime-type-for-docx-pptx-etc
    //
    // .doc      application/msword
    // .dot      application/msword
    //
    // .docx     application/vnd.openxmlformats-officedocument.wordprocessingml.document
    // .dotx     application/vnd.openxmlformats-officedocument.wordprocessingml.template
    // .docm     application/vnd.ms-word.document.macroEnabled.12
    // .dotm     application/vnd.ms-word.template.macroEnabled.12
    //
    // .xls      application/vnd.ms-excel
    // .xlt      application/vnd.ms-excel
    // .xla      application/vnd.ms-excel
    //
    // .xlsx     application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
    // .xltx     application/vnd.openxmlformats-officedocument.spreadsheetml.template
    // .xlsm     application/vnd.ms-excel.sheet.macroEnabled.12
    // .xltm     application/vnd.ms-excel.template.macroEnabled.12
    // .xlam     application/vnd.ms-excel.addin.macroEnabled.12
    // .xlsb     application/vnd.ms-excel.sheet.binary.macroEnabled.12
    private static final String CONTENT_TYPE_OLD_WORD_FILES = "application/msword";
    private static final String CONTENT_TYPE_DOCX_FILE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String CONTENT_TYPE_DOTX_FILE = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
    private static final String CONTENT_TYPE_DOCM_FILE = "application/vnd.ms-word.document.macroEnabled.12";
    private static final String CONTENT_TYPE_DOTM_FILE = "application/vnd.ms-word.template.macroEnabled.12";
    private static final String CONTENT_TYPE_OLD_EXCEL_FILES = "application/vnd.ms-excel";
    private static final String CONTENT_TYPE_XLSX_FILE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE_XLTX_FILE = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
    private static final String CONTENT_TYPE_XLSM_FILE = "application/vnd.ms-excel.sheet.macroEnabled.12";
    private static final String CONTENT_TYPE_XLTM_FILE = "application/vnd.ms-excel.template.macroEnabled.12";
    private static final String CONTENT_TYPE_XLAM_FILE = "application/vnd.ms-excel.addin.macroEnabled.12";
    private static final String CONTENT_TYPE_XLSB_FILE = "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
    private static final Map<String, String> CONTENT_TYPES;
    private static DateTimeFormatter UPLOAD_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS", Locale.FRANCE).withZone(ZoneId.systemDefault());

    static {
        CONTENT_TYPES = new HashMap<>();
        CONTENT_TYPES.put("doc", CONTENT_TYPE_OLD_WORD_FILES);
        CONTENT_TYPES.put("dot", CONTENT_TYPE_OLD_WORD_FILES);
        CONTENT_TYPES.put("docx", CONTENT_TYPE_DOCX_FILE);
        CONTENT_TYPES.put("dotx", CONTENT_TYPE_DOTX_FILE);
        CONTENT_TYPES.put("docm", CONTENT_TYPE_DOCM_FILE);
        CONTENT_TYPES.put("dotm", CONTENT_TYPE_DOTM_FILE);
        CONTENT_TYPES.put("xls", CONTENT_TYPE_OLD_EXCEL_FILES);
        CONTENT_TYPES.put("xlt", CONTENT_TYPE_OLD_EXCEL_FILES);
        CONTENT_TYPES.put("xla", CONTENT_TYPE_OLD_EXCEL_FILES);
        CONTENT_TYPES.put("xlsx", CONTENT_TYPE_XLSX_FILE);
        CONTENT_TYPES.put("xltx", CONTENT_TYPE_XLTX_FILE);
        CONTENT_TYPES.put("xlsm", CONTENT_TYPE_XLSM_FILE);
        CONTENT_TYPES.put("xltm", CONTENT_TYPE_XLTM_FILE);
        CONTENT_TYPES.put("xlam", CONTENT_TYPE_XLAM_FILE);
        CONTENT_TYPES.put("xlsb", CONTENT_TYPE_XLSB_FILE);
        CONTENT_TYPES.put("3g2 ", "video/3gpp2");
        CONTENT_TYPES.put("3gp ", "video/3gpp");
        CONTENT_TYPES.put("7z ", "application/x-7z-compressed");
        CONTENT_TYPES.put("aac ", "audio/aac");
        CONTENT_TYPES.put("abw ", "application/x-abiword");
        CONTENT_TYPES.put("avi ", "video/x-msvideo");
        CONTENT_TYPES.put("azw ", "application/vnd.amazon.ebook");
        CONTENT_TYPES.put("bz ", "application/x-bzip");
        CONTENT_TYPES.put("bz2 ", "application/x-bzip2");
        CONTENT_TYPES.put("csh ", "application/x-csh");
        CONTENT_TYPES.put("css ", "text/css");
        CONTENT_TYPES.put("csv ", "text/csv");
        CONTENT_TYPES.put("eot ", "application/vnd.ms-fontobject");
        CONTENT_TYPES.put("epub ", "application/epub+zip");
        CONTENT_TYPES.put("gif ", "image/gif");
        CONTENT_TYPES.put("html ", "text/html");
        CONTENT_TYPES.put("ico ", "image/x-icon");
        CONTENT_TYPES.put("ics ", "text/calendar");
        CONTENT_TYPES.put("jar ", "application/java-archive");
        CONTENT_TYPES.put("jpeg", "image/jpeg");
        CONTENT_TYPES.put("jpg ", "image/jpeg");
        CONTENT_TYPES.put("js ", "application/javascript");
        CONTENT_TYPES.put("json ", "application/json");
        CONTENT_TYPES.put("mid", "audio/midi");
        CONTENT_TYPES.put("midi ", "audio/midi");
        CONTENT_TYPES.put("mpeg ", "video/mpeg");
        CONTENT_TYPES.put("mpkg ", "application/vnd.apple.installer+xml");
        CONTENT_TYPES.put("odp ", "application/vnd.oasis.opendocument.presentation");
        CONTENT_TYPES.put("ods ", "application/vnd.oasis.opendocument.spreadsheet");
        CONTENT_TYPES.put("odt ", "application/vnd.oasis.opendocument.text");
        CONTENT_TYPES.put("oga ", "audio/ogg");
        CONTENT_TYPES.put("ogv ", "video/ogg");
        CONTENT_TYPES.put("ogx ", "application/ogg");
        CONTENT_TYPES.put("otf ", "font/otf");
        CONTENT_TYPES.put("pdf ", "application/pdf");
        CONTENT_TYPES.put("png ", "image/png");
        CONTENT_TYPES.put("ppt ", "application/vnd.ms-powerpoint");
        CONTENT_TYPES.put("rar ", "application/x-rar-compressed");
        CONTENT_TYPES.put("rtf ", "application/rtf");
        CONTENT_TYPES.put("sh ", "application/x-sh");
        CONTENT_TYPES.put("svg ", "image/svg+xml");
        CONTENT_TYPES.put("swf ", "application/x-shockwave-flash");
        CONTENT_TYPES.put("tar ", "application/x-tar");
        CONTENT_TYPES.put("tif", "image/tiff");
        CONTENT_TYPES.put("tiff ", "image/tiff");
        CONTENT_TYPES.put("ts ", "application/typescript");
        CONTENT_TYPES.put("ttf ", "font/ttf");
        CONTENT_TYPES.put("vsd ", "application/vnd.visio");
        CONTENT_TYPES.put("wav ", "audio/x-wav");
        CONTENT_TYPES.put("weba ", "audio/webm");
        CONTENT_TYPES.put("webm ", "video/webm");
        CONTENT_TYPES.put("webp ", "image/webp");
        CONTENT_TYPES.put("woff ", "font/woff");
        CONTENT_TYPES.put("woff2 ", "font/woff2");
        CONTENT_TYPES.put("xhtml ", "application/xhtml+xml");
        CONTENT_TYPES.put("xml ", "application/xml");
        CONTENT_TYPES.put("xul ", "application/vnd.mozilla.xul+xml");
        CONTENT_TYPES.put("zip ", "application/zip");
    }

    private FileUtils() {
    }

    public static String nomUpload(String nomFichier) {
        if (nomFichier == null) return null;
        return StringFormatUtils.normalize(nom(nomFichier)) + "-" + UPLOAD_FORMAT.format(Instant.now()) + "." + extension(nomFichier);
    }

    public static String nomExport(String libelle, String nomFichier) {
        String ext = extension(nomFichier);
        if (Strings.isNullOrEmpty(ext)) ext = "xxx";
        return StringFormatUtils.normalize(libelle) + "-" + DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()) + "." + ext;
    }

    public static String nom(String nomFichier) {
        if (nomFichier == null) return null;
        int pos = nomFichier.lastIndexOf('.');
        return pos >= 0 ? nomFichier.substring(0, pos) : nomFichier;
    }

    public static String extension(String nomFichier) {
        if (nomFichier == null) return null;
        int pos = nomFichier.lastIndexOf('.');
        return pos >= 0 ? nomFichier.substring(pos + 1).toLowerCase(Locale.FRANCE) : null;
    }

    public static String contentType(String nomFichier) {
        String extension = extension(nomFichier);
        if (extension != null && CONTENT_TYPES.containsKey(extension)) {
            return CONTENT_TYPES.get(extension);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
