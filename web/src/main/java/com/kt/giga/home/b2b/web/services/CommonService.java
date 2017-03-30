package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.FileInfo;
import com.kt.giga.home.b2b.entity.FileManager;
import com.kt.giga.home.b2b.mapper.FileInfoMapper;
import com.kt.giga.home.b2b.repository.FileManagerRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by junypooh on 2017-02-08.
 * <pre>
 * com.kt.giga.home.b2b.web.services.CommonService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-02-08 오후 4:56
 */
@Service
public class CommonService implements ICommonService {

    @Autowired
    private StandardPasswordEncoder standardPasswordEncoder;

    @Value("${server.session.timeout:1000000}")
    private Long sessionTimeOut;

    @Value("${upload.file.path}")
    private String fileUploadRootPath;

    @Value("${upload.file.thumbnailLogoWidth:192}")
    private int thumbnailLogoWidth;

    @Value("${upload.file.thumbnailLogoHeight:68}")
    private int thumbnailLogoHeight;

    @Value("${upload.file.thumbnailBannerWidth:260}")
    private int thumbnailBannerWidth;

    @Value("${upload.file.thumbnailBannerHeight:260}")
    private int thumbnailBannerHeight;

    @Value("${upload.file.thumbnailSubBannerWidth:206}")
    private int thumbnailSubBannerWidth;

    @Value("${upload.file.thumbnailSubBannerHeight:260}")
    private int thumbnailSubBannerHeight;

    @Autowired
    private FileManagerRepository fileManagerRepository;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    private static final String[] allowedFileExts =
            new String[]{"JPG", "PNG", "GIF", "JPEG"
            };

    /*private static final String[] allowedFileExts =
            new String[]{ "PDF", "HWP", "TXT", "PPTX", "PPT", "XLSX",
                    "XLS", "DOCX", "DOC", "JPG", "PNG", "GIF", "MP4", "MOV", "JPEG"
                    , "AVI", "MP3", "WAV", "WMA", "ZIP", "RAR"
            };*/

    private static final String[] convertThumnailExts =
            new String[]{"JPG", "PNG", "GIF", "JPEG"
            };

    private static final long DEFAULT_EXPIRE_TIME = 604800000L;


    @Override
    public boolean doesPasswordMatch(String rawPassWord, String encoded) {

        // 현재비밀번호 매칭 확인
        return standardPasswordEncoder.matches(rawPassWord, encoded);
    }

    @Override
    public Long getSessionTimeOut() {
        return this.sessionTimeOut;
    }

    @Override
    public FileInfo uploadFile(MultipartFile multipartFile, String appendPath, String type) throws Exception {

        if("LOGO".equals(type)) {
            return this.uploadFile(multipartFile, appendPath, this.thumbnailLogoWidth, this.thumbnailLogoHeight);
        } else if("BANNER".equals(type)) {
            return this.uploadFile(multipartFile, appendPath, this.thumbnailBannerWidth, this.thumbnailBannerHeight);
        } else if("SUBBANNER".equals(type)) {
            return this.uploadFile(multipartFile, appendPath, this.thumbnailSubBannerWidth, this.thumbnailSubBannerHeight);
        } else {
            return null;
        }
    }

    @Override
    public FileInfo uploadFile(MultipartFile multipartFile, String appendPath, int thumbnailWidth, int thumbnailHeight) throws Exception {

        String fileStorePath = fileUploadRootPath;
        if (StringUtils.isNotEmpty(appendPath))
            fileStorePath += "/" + appendPath + "/";

        File saveFolder = new File(fileStorePath);

        if (!saveFolder.exists() || saveFolder.isFile()) {
            saveFolder.mkdirs();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isNotBlank(originalFilename)) {

            int    index   = originalFilename.lastIndexOf(".");
            String fileExt = StringUtils.upperCase(originalFilename.substring(index + 1));
            String newName = UUID.randomUUID().toString();
//            String newName = LocalTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            long size = multipartFile.getSize();

            if (!ArrayUtils.contains(allowedFileExts, fileExt))
                throw new Exception("허용되지 않는 확장자입니다");

            if (ArrayUtils.contains(convertThumnailExts, fileExt)) {
                BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
                if (bufferedImage.getWidth() > thumbnailWidth || bufferedImage.getHeight() > thumbnailHeight) {
                    String savePathString = fileStorePath + "thumbnail/";
                    File   thumbnail      = new File(savePathString + newName);
                    thumbnail.getParentFile().mkdirs();
                    Thumbnails.of(multipartFile.getInputStream())
                              .width(thumbnailWidth)
                              .height(thumbnailHeight)
                              .outputFormat("png")
                              .toFile(thumbnail);
                }
            }

            multipartFile.transferTo(new File(fileStorePath + File.separator + newName));

            FileManager fileManager = new FileManager();
            fileManager.setFileName(newName);
            fileManager.setFilePath(fileStorePath);
            fileManager.setFileSize(size);
            fileManager.setFileExt(fileExt);
            fileManager.setOriFileName(originalFilename);

            fileManagerRepository.saveAndFlush(fileManager);

            return fileInfoMapper.toFileInfo(fileManager);
        } else {
            return null;
        }
    }

    @Override
    public void downloadFile(Long fileSeq, boolean origin, HttpServletRequest request, HttpServletResponse response, String type) throws Exception {

        if ("LOGO".equals(type)) {
            this.downloadFile(fileSeq, origin, request, response, this.thumbnailLogoWidth, this.thumbnailLogoHeight);
        } else if("BANNER".equals(type)) {
            this.downloadFile(fileSeq, origin, request, response, this.thumbnailBannerWidth, this.thumbnailBannerHeight);
        } else if("SUBBANNER".equals(type)) {
            this.downloadFile(fileSeq, origin, request, response, this.thumbnailSubBannerWidth, this.thumbnailSubBannerHeight);
        }

    }

    @Override
    public void downloadFile(Long fileSeq, boolean origin, HttpServletRequest request, HttpServletResponse response, int thumbnailWidth, int thumbnailHeight) throws Exception {


        FileManager fileManager = fileManagerRepository.findOneByFileSeq(fileSeq);
        FileInfo    fileInfo    = fileInfoMapper.toFileInfo(fileManager);

        if (fileInfo == null)
            return;

        String fileExt = StringUtils.upperCase(fileInfo.getFileExt());

        File uFile = new File(fileInfo.getFilePath() + fileInfo.getFileName());

        if (!origin && ArrayUtils.contains(convertThumnailExts, fileExt)) {

            File thumbFile = new File(fileInfo.getFilePath() + "thumbnail/", fileInfo.getFileName() + ".png");

            if (!thumbFile.exists()) {

                String savePathString = fileInfo.getFilePath() + "thumbnail/";
                File   thumbnail      = new File(savePathString + fileInfo.getFileName());
                thumbnail.getParentFile().mkdirs();
                BufferedImage bufferedImage = ImageIO.read(uFile);
                if (bufferedImage.getWidth() > thumbnailWidth || bufferedImage.getHeight() > thumbnailHeight) {
                    Thumbnails.of(uFile)
                              .width(thumbnailWidth)
                              .height(thumbnailHeight)
                              .outputFormat("png")
                              .toFile(thumbnail);
                    uFile = new File(fileInfo.getFilePath() + "thumbnail/", fileInfo.getFileName() + ".png");
                }
            } else {
                uFile = new File(fileInfo.getFilePath() + "thumbnail/", fileInfo.getFileName() + ".png");
            }
        }

        int fSize = (int) uFile.length();

        long expires = System.currentTimeMillis() + DEFAULT_EXPIRE_TIME;

        long   length       = uFile.length();
        long   lastModified = uFile.lastModified();
        String eTag         = fileInfo.getFileName() + "_" + length + "_" + lastModified;

        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && matches(ifNoneMatch, eTag)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            response.setHeader("ETag", eTag); // Required in 304.
            response.setDateHeader("Expires", expires); // Postpone cache with 1 week.
            return;
        }

        // If-Modified-Since header should be greater than LastModified. If so, then return 304.
        // This header is ignored if any If-None-Match header is specified.
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            response.setHeader("ETag", eTag); // Required in 304.
            response.setDateHeader("Expires", expires); // Postpone cache with 1 week.
            return;
        }

        if (fSize > 0) {
            String mimeType = "application/unknown";
            if (fileExt.equals("PDF")) {
                mimeType = "application/pdf";
            } else if (fileExt.equals("HWP")) {
                mimeType = "application/x-hwp";
            } else if (fileExt.equals("TXT")) {
                mimeType = "text/plain";
            } else if (fileExt.equals("PPTX") || fileExt.equals("PPT")) {
                mimeType = "application/vnd.ms-powerpoint";
            } else if (fileExt.equals("XLSX") || fileExt.equals("XLS")) {
                mimeType = "application/vnd.ms-excel";
            } else if (fileExt.equals("DOCX") || fileExt.equals("DOC")) {
                mimeType = "application/msword";
            } else if (fileExt.equals("JPG") || fileExt.equals("JPEG")) {
                mimeType = "image/jpeg";
            } else if (fileExt.equals("PNG")) {
                mimeType = "image/png";
            } else if (fileExt.equals("GIF")) {
                mimeType = "image/gif";
            } else if (fileExt.equals("MP4")) {
                mimeType = "video/mp4";
            } else if (fileExt.equals("MOV")) {
                mimeType = "application/pdf";
            } else if (fileExt.equals("AVI")) {
                mimeType = "video/msvideo";
            } else if (fileExt.equals("MP3")) {
                mimeType = "audio/mpeg";
            } else if (fileExt.equals("WAV")) {
                mimeType = "audio/wav";
            } else if (fileExt.equals("WMA")) {
                mimeType = "audio/x-ms-wma";
            } else if (fileExt.equals("ZIP")) {
                mimeType = "application/zip";
            } else if (fileExt.equals("RAR")) {
                mimeType = "application/x-rar-compressed";
            }
            //String mimetype = "application/x-msdownload";

            //response.setBufferSize(fSize);	// OutOfMemeory 발생
            response.setBufferSize(10240);
            response.setContentType(mimeType);

            response.setHeader("Cache-Control", "public,max-age=86400");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", eTag);
            response.setDateHeader("Last-Modified", lastModified);
            response.setDateHeader("Expires", expires);
            //response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
            setDisposition(fileInfo.getOriFileName(), request, response);
            response.setContentLength(fSize);

            /*
             * FileCopyUtils.copy(in, response.getOutputStream());
             * in.close();
             * response.getOutputStream().flush();
             * response.getOutputStream().close();
             */
            BufferedInputStream  in  = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(new FileInputStream(uFile));
                out = new BufferedOutputStream(response.getOutputStream());

                FileCopyUtils.copy(in, out);
                out.flush();
            } catch (IOException ex) {
                // 다음 Exception 무시 처리
                // Connection reset by peer: socket write error
                //EgovBasicLogger.ignore("IO Exception", ex);
            } finally {
                close(in, out);
            }

        } else {
            response.setContentType("application/octet-stream");

            PrintWriter printwriter = response.getWriter();

            printwriter.println("<html>");
            printwriter.println("<br><br><br><h2>Could not get file name:<br>" + fileInfo.getOriFileName() + "</h2>");
            printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
            printwriter.println("<br><br><br>&copy; webAccess");
            printwriter.println("</html>");

            printwriter.flush();
            printwriter.close();
        }
    }

    private static boolean matches(String matchHeader, String toMatch) {
        String[] matchValues = matchHeader.split("\\s*,\\s*");
        Arrays.sort(matchValues);
        return Arrays.binarySearch(matchValues, toMatch) > -1
                || Arrays.binarySearch(matchValues, "*") > -1;
    }

    /**
     * Disposition 지정하기.
     *
     * @param filename
     * @param request
     * @param response
     * @throws Exception
     */
    private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request);

        String dispositionPrefix = "attachment; filename=";
        String encodedFilename   = null;

        switch (browser) {
            case "MSIE":
                encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                break;
            case "TRIDENT":  // IE11 문자열 깨짐 방지
                encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                break;
            case "FIREFOX":
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
                break;
            case "OPERA":
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
                break;
            case "ANDROID":
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
                break;
            case "CHROME":
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < filename.length(); i++) {
                    char c = filename.charAt(i);
                    if (c > '~') {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } else {
                        sb.append(c);
                    }
                }
                encodedFilename = sb.toString();
                break;
            default:
                throw new IOException("Not supported browser");
        }

        response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

        if ("OPERA".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
    }

    /**
     * 브라우저 구분 얻기.
     *
     * @param request
     * @return
     */
    private String getBrowser(HttpServletRequest request) {
        String header = StringUtils.upperCase(request.getHeader("User-Agent"));
        if (header.contains("MSIE")) {
            return "MSIE";
        } else if (header.contains("TRIDENT")) { // IE11 문자열 깨짐 방지
            return "TRIDENT";
        } else if (header.contains("CHROME")) {
            return "CHROME";
        } else if (header.contains("OPERA")) {
            return "OPERA";
        } else if (header.contains("ANDROID")) {
            return "ANDROID";
        }
        return "FIREFOX";
    }

    /**
     * Resource close 처리.
     *
     * @param resources
     */
    private void close(Closeable... resources) {
        for (Closeable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ignore) {
                    //EgovBasicLogger.ignore("Occurred Exception to close resource is ingored!!");
                }
            }
        }
    }
}
