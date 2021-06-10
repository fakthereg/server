package com.myproject.server;

import com.myproject.server.models.File;
import com.myproject.server.repositories.FileRepository;
import com.myproject.server.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileRepository repository;
    private UserRepository userRepository;

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
    public List<File> getFilesByCategory(@PathVariable String category) {
        ArrayList<File> result = new ArrayList<>();
        for (File file : repository.findAll()) {
            if (file.getCategory().equals(category)) {
                result.add(file);
            }
        }
        return result;
    }


    @GetMapping("/downloadFile/{category}/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String category, @PathVariable(value = "filename") String filename, HttpServletRequest request) {
        // Load file as Resource
        Resource resource;
        File file = repository.findFileByFilename(filename);
        String outputFilename = file.getArtist() + " - " + file.getTitle() + ".mp3";
        String fileBasePath = "C:\\1\\" + category + "\\";
        System.out.println("Trying to download file at: " + fileBasePath + filename);
        Path path = Paths.get(fileBasePath + filename);
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(outputFilename, StandardCharsets.UTF_8)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        System.out.println("File found and sent");
 /*       return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
*/
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/addNewFile",
            consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<Resource> addNewFile(@RequestBody String string) {
        File file = new File();
        ObjectId objectId = ObjectId.get();
        String filename = "";
        String title = "";
        String artist = "";
        String category = "";
        try {
            JSONObject jsonObject = new JSONObject(string);
            filename = jsonObject.getString("filename");
            String[] s = filename.split("(?: - )");
            artist = s[0].trim();
            title = s[1].split("[.]")[0].trim();
            category = jsonObject.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        file.set_id(objectId);
        file.setFilename(filename);
        file.setArtist(artist);
        file.setCategory(category);
        file.setTitle(title);
        if (repository.findFileByFilename(file.getFilename()) == null && !file.getFilename().equals("")) {
            repository.save(file);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

 /*       @RequestMapping(value = "/", params = "id", method = RequestMethod.GET)
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
