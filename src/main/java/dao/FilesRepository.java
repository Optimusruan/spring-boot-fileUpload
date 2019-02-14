package dao;

import entity.FilesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FilesRepository extends CrudRepository<FilesEntity,Long> {
    List<FilesEntity> findByFileMd5(String md5);

}
