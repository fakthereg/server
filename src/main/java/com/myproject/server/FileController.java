package com.myproject.server;

import com.myproject.server.models.File;
import com.myproject.server.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<File> getAllFiles() {
        return repository.findAll();
    }
/*
    @RequestMapping(value = "/getCategories", method = RequestMethod.GET)
    public HashSet<String> getAllCategories() {
        List<File> files = repository.findAll();
        ArrayList<String> names = new ArrayList<>();
        for (File file : files) {
            names.add(file.getCategory());
        }
        return new HashSet<>(names);
    }
*/

    @RequestMapping(value = "/{category}/", method = RequestMethod.GET)
    public List<File> getFilesByCategory(@PathVariable String category){
        ArrayList<File> result = new ArrayList<>();
        for (File file : repository.findAll()){
            if(file.getCategory().equals(category)){
                result.add(file);
            }
        }
        return result;
    }


    @GetMapping("/downloadFile/{category}/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String category, @PathVariable(value = "filename") String filename, HttpServletRequest request) {
        // Load file as Resource
        Resource resource;

        String fileBasePath = "C:\\1\\" + category + "\\";
        System.out.println("Trying to download file at: " + fileBasePath + filename);
        Path path = Paths.get(fileBasePath + filename);
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        System.out.println("File found and sent");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    /*
        @RequestMapping(value = "/", params = "id", method = RequestMethod.GET)
        public File getFileById(@RequestParam("id") ObjectId id) {
            return repository.findBy_id(id);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
        public void modifyFilerById(@PathVariable("id") ObjectId id, @Valid @RequestBody File file) {
            file.set_id(id);
            repository.save(file);
        }

        @RequestMapping(value = "/", params = "songName", method = RequestMethod.GET)
        public File getFileByName(@RequestParam("name") String name) {
            return repository.findFileByName(name);
        }

        @RequestMapping(value = "/", params = "fileName", method = RequestMethod.GET)
        public File getFileByFilename(@RequestParam("filename") String filename) {
            return repository.findFileByFilename(filename);
        }

        @RequestMapping(value = "/", method = RequestMethod.POST)
        public File createFile(@Valid @RequestBody File file) {
            file.set_id(ObjectId.get());
            repository.save(file);
            return file;
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
        public void deleteFile(@PathVariable ObjectId id) {
            repository.delete(repository.findBy_id(id));
        }
    */
}
