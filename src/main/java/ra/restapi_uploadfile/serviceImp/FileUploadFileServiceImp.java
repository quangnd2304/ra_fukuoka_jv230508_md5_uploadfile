package ra.restapi_uploadfile.serviceImp;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.restapi_uploadfile.service.FileUploadService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadFileServiceImp implements FileUploadService {
    private final Path PATH_ROOT = Paths.get("upload");
    @Override
    public void init() {
        try {
            if (!Files.exists(PATH_ROOT)) {
                Files.createDirectories(PATH_ROOT);
            }
        }catch (IOException ex){
            throw new RuntimeException("Cound not initial folder upload");
        }
    }

    @Override
    public Path uploadFile(MultipartFile multipartFile) {
        try {
            Files.copy(multipartFile.getInputStream(),PATH_ROOT.resolve(multipartFile.getOriginalFilename()));
            return PATH_ROOT.resolve(multipartFile.getOriginalFilename());

        }catch (Exception ex){
            throw new RuntimeException("Could not store file. Error:"+ex.getMessage());
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path file = PATH_ROOT.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()||resource.isReadable()){
                return resource;
            }else {
                throw new RuntimeException("Could not read file");
            }
        }catch (Exception ex){
            throw  new RuntimeException("Error: "+ex.getMessage());
        }
    }
}
