package fr.ifpen.synergreen.service;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import fr.ifpen.synergreen.config.ApplicationProperties;
import fr.ifpen.synergreen.domain.SynergreenException;
import fr.ifpen.synergreen.service.util.FileUtils;
import org.elasticsearch.common.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
@Transactional
public class FileService {
    private final Logger log = LoggerFactory.getLogger(FileService.class);

    private final ApplicationProperties applicationProperties;

    @Inject
    public FileService(ApplicationProperties applicationProperties) {
        super();
        this.applicationProperties = applicationProperties;
    }

    public File uploadFichier(MultipartFile uploadedFile) throws SynergreenException {
        return uploadFile(applicationProperties.getRepertoireUpload(), uploadedFile);
    }

    public File uploadFile(String uploadPath, MultipartFile uploadedFile) throws SynergreenException {
        if (uploadedFile == null) {
            log.error("Erreur à l'enregistrement du fichier (file null)");
        }
        try {
            String fileName = FileUtils.nomUpload(uploadedFile.getOriginalFilename());
            log.debug(Paths.get(uploadPath, fileName).toString());
            File file = Paths.get(uploadPath, fileName).toFile();
            FileCopyUtils.copy(uploadedFile.getBytes(), file);
            return file;
        } catch (IOException e) {
            log.error("Erreur à l'enregistrement du fichier {}", uploadedFile.getOriginalFilename(), e);
            log.error("Erreur à l'enregistrement du fichier " + uploadedFile.getOriginalFilename());
            return null;
        }
    }


    public void readUploadedFile(String uploadPath, String fileName, HttpServletResponse response) throws SynergreenException {
        File file = Paths.get(uploadPath, fileName).toFile();
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType(FileUtils.contentType(fileName) + "; name=\"" + fileName + "\"");
            byte data[] = Files.toByteArray(file);
            response.setContentLength(data.length);
            FileCopyUtils.copy(data, response.getOutputStream());
        } catch (IOException e) {
            log.error("Erreur à la lecture du fichier {}", file.getName(), e);
        }
    }

    public void deleteFichier(String fichier) {
        deleteFile(applicationProperties.getRepertoireUpload(), fichier);
    }

    public void deleteFile(String uploadPath, String fileName) throws SynergreenException {
        if (Strings.isNullOrEmpty(fileName)) return;
        File file = Paths.get(uploadPath, fileName).toFile();
        if (!file.delete()) {
            log.error("Le fichier {} n'a pas pu être supprimé sur le disque.", file.getAbsolutePath());
        }
    }
}
