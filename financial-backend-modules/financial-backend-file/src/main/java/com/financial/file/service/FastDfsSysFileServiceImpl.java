package com.financial.file.service;

import jakarta.annotation.Resource;
import java.io.InputStream;
import com.alibaba.nacos.common.utils.IoUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.financial.common.core.utils.file.FileTypeUtils;

/**
 * FastDFS 文件存储
 *
 * @author xinyi
 */
@Service
public class FastDfsSysFileServiceImpl implements ISysFileService
{
    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Resource
    private FastFileStorageClient storageClient;

    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception
    {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = storageClient.uploadFile(inputStream, file.getSize(),
                FileTypeUtils.getExtension(file), null);
        IoUtils.closeQuietly(inputStream);
        return domain + "/" + storePath.getFullPath();
    }
}
