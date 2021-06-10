package com.myproject.server;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import com.myproject.server.models.File;
import com.myproject.server.repositories.FileRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @Autowired
    private FileRepository repository;

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("category") String category,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(String.format("c://1//%s//%s", category, file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                File mFile = new File();
                String artist = file.getOriginalFilename().split("(?: - )")[0].trim();
                String title = file.getOriginalFilename().split("(?: - )")[0].split("[.]")[0].trim();
                mFile.set_id(ObjectId.get());
                mFile.setFilename(file.getOriginalFilename());
                mFile.setCategory(category);
                mFile.setTitle(title);
                mFile.setArtist(artist);
                repository.save(mFile);
                return "Вы удачно загрузили " + file.getOriginalFilename() + " в " + category + "категорию ! ";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + file.getOriginalFilename() + " потому что файл пустой.";
        }
    }

}

