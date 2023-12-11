package ra.restapi_uploadfile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ra.restapi_uploadfile.dto.response.FileInfoResponse;
import ra.restapi_uploadfile.service.FileUploadService;

import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("api/v1/file")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;
    @PostMapping("/upload")
    public ResponseEntity<List<FileInfoResponse>> uploadFile(@RequestBody MultipartFile[] files){
        fileUploadService.init();
        Map<String, Path> listPath = new HashMap<>();
        Arrays.asList(files).forEach(file->{
            Path path = fileUploadService.uploadFile(file);
            listPath.put(file.getOriginalFilename(),path);
        });
        List<FileInfoResponse> listFileInfo = new ArrayList<>();
        for (String key:listPath.keySet()) {
            String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,"getFile",
                    listPath.get(key).getFileName().toString()).build().toString();
            listFileInfo.add(new FileInfoResponse(key,url));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listFileInfo);
    }
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName){
        Resource file = fileUploadService.load(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+file.getFilename()+"\"").body(file);
    }
}
