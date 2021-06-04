package com.myproject.server.repositories;

import com.myproject.server.models.File;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "file", path = "file")
public interface FileRepository extends MongoRepository<File, String>{
    File findBy_id(ObjectId _id);
    File findFileByTitle(String title);
    File findFileByFilename(String filename);
    List<File> findFilesByCategory(String category);
}
