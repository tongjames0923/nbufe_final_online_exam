package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Resource_Type;
import tbs.api_server.config.constant.const_User;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.QuestionResource;
import tbs.api_server.objects.simple.UserDetailInfo;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ResourceService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.Error;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utils.SecurityTools;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static tbs.api_server.publicAPI.ResourceController.Help.*;
import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping(value = "/resource/*")
@Scope("prototype")
public class ResourceController {
    @Autowired
    ResourceService service;

    @Transactional
    @RequestMapping("/getByType")
    public NetResult getResourcesByType(int type, int from, int num) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult result = service.getResourcesByType(type, from, num);
                resourcesLinkApply((List<QuestionResource>) result.getObj());
                return NetResult.makeResult(result, null);
            }
        }).method();
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Transactional
    public NetResult upload(@RequestParam MultipartFile file, @RequestParam int type, @RequestParam String note) {

        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                MultipartFile bytes = file;
                int tp = type;
                String name = file.getOriginalFilename();
                name = name.substring(name.lastIndexOf("."));
                String filepath = makePath(name, note);
                File file1 = new File(genPath(filepath));
                int total = 0;
                ServiceResult result = service.UploadResource(tp, filepath, note);
                if (!bytes.isEmpty()) {
                    FileUtility.BaseThen then = new FileUtility.FileWriteThen(bytes.getInputStream(), true);
                    FileUtility.existFile(file1.getAbsolutePath(), then);
                    total = (int) then.result();
                }
                if (total == bytes.getSize()) {
                    return NetResult.makeResult(result, null);
                } else {
                    Error._ERROR.rollback();
                    if (file1.exists()) {
                        file1.delete();
                    }
                    return NetResult.makeResult(EC_FILESYSTEM_ERROR, "写入失败");
                }
            }
        }).method();
    }
    @Autowired
    UserMapper userMapper;

    @Transactional
    @RequestMapping("/delete")
    public NetResult delete(int userid, int resource_id) {
        final NetResult result = new NetResult(SUCCESS, null, null);
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                QuestionResource resource = (QuestionResource) service.getResourceById(resource_id).getObj();
                Optional.ofNullable(resource).ifPresent(new Consumer<QuestionResource>() {
                    @Override
                    public void accept(QuestionResource questionResource) {
                        UserDetailInfo info =userMapper.getUserDetailInfoByID(userid);
                        if (info.getLevel() == const_User.LEVEL_EXAM_STAFF) {

                            try {

                                ServiceResult rs = service.DeleteResource(resource_id);
                                FileUtility.BaseThen then = new FileUtility.FileDeleteThen();
                                FileUtility.existFile(genPath(questionResource.getResource()), then);
                                boolean deleted = (boolean) then.result();
                                if (!deleted) {
                                    result.setCode(EC_FILESYSTEM_ERROR);
                                    result.setMessage("删除资源文件失败");
                                }
                            } catch (BackendError backendError) {
                                result.setCode(backendError.getCode());
                                result.setMessage(backendError.getMessage());
                                result.setData(backendError.getData());
                            } catch (Exception e) {
                                result.setCode(EC_UNKNOWN);
                                result.setMessage(e.getMessage());
                            }
                        } else {
                            result.setCode(EC_LOW_PERMISSIONS);
                            result.setMessage("权限不足,无法删除资源");
                        }
                    }
                });
                return result;
            }
        }).method();
    }
    @Transactional
    @RequestMapping("/getByNote")
    public NetResult getResourcesByNote(String note, int from, int num) {
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                ServiceResult rs = service.getResourceByNote(note, from, num);
                resourcesLinkApply((List<QuestionResource>) rs.getObj());
                return NetResult.makeResult(SUCCESS, null, rs.getObj());
            }
        }).method();
    }


    public static class Help {

        public static void resourcesLinkApply(List<QuestionResource> resources) {
            for (QuestionResource r : resources) {
                r.setResource(gen_file_link(r));
            }

        }
        public static String gen_file_link(QuestionResource resource) {
            String res = "/file/res/";
            switch (resource.getResource_type()) {
                case const_Resource_Type.Audio:
                    res += "audio?";
                    break;
                case const_Resource_Type.Video:
                    res += "video?";
                    break;
                case const_Resource_Type.Text:
                    res += "text?";
                    break;
                case const_Resource_Type.Image:
                    res += "image?";
                    break;
            }
            res += "id=" + resource.getId();
            return res;
        }


        public static byte[] getFile(ResourceService service, int id, int type) {
            try {
                ServiceResult result = service.getResourceById(id);
                QuestionResource questionResource = (QuestionResource) result.getObj();
                if (questionResource.getResource_type() != type)
                    return null;
                if (result.getCode() > 0) {
                    FileUtility.FileReadThen readThen = new FileUtility.FileReadThen();
                    FileUtility.existFile(genPath(questionResource.getResource()), readThen);
                    return (byte[]) (readThen.result());
                } else {
                    return null;
                }

            } catch (Throwable ex) {
                return null;
            }
        }

        public static String makePath(String type, String note) throws Exception {
            Date date = new Date();
            String d = Integer.toHexString((int) (date.getTime() % Integer.MAX_VALUE)) + note;

            d = SecurityTools.Encrypt_str(d);

            d = d.substring(32, 64);
            d += type;
            return d;
        }

        public static String genPath(String filename) {
            return ApplicationConfig.resourceDir + "/" + filename;
        }
    }
}

