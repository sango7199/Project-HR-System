package com.woosan.hr_system.resignation.service;

import com.woosan.hr_system.resignation.model.Resignation;
import com.woosan.hr_system.file.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResignationService {
    List<Resignation> getAllResignation();
    Resignation getResignation(String employeeId);
    String resignEmployee(String employeeId, Resignation resignation, MultipartFile[] resignationDocuments);
    String updateResignation(String employeeId, Resignation resignation, List<Integer> oldFileIdList, MultipartFile[] newFileArr);
    void deleteResignation(String employeeId);

    // == resignationFile 관련 코드 ==
    void uploadNewFiles(String employeeId, MultipartFile[] newFileArr);
    List<Integer> getFileIdList(String employeeId);
    List<File> getAllFileInfo(String employeeId);
}
