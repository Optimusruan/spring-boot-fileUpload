package service;

import dao.FilesRepository;
import entity.FilesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilesService {

    @Autowired
    private FilesRepository filesRepository;

    public List<FilesEntity> isExistFile(String md5) throws Exception{
        return filesRepository.findByFileMd5(md5);
    }

    public boolean saveFileInfo(FilesEntity filesEntity) {
        boolean result = false;
        if (filesEntity != null) {
            try {
                filesRepository.save(filesEntity);
                result = true;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return result;
    }
}
