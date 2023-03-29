package tbs.api_server.publicAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tbs.api_server.backend.mappers.UserMapper;
import tbs.api_server.config.AccessEnum;
import tbs.api_server.config.AccessLimit;
import tbs.api_server.config.ApplicationConfig;
import tbs.api_server.config.constant.const_Resource_Type;
import tbs.api_server.objects.NetResult;
import tbs.api_server.objects.ServiceResult;
import tbs.api_server.objects.simple.QuestionResource;
import tbs.api_server.objects.simple.UserSecurityInfo;
import tbs.api_server.services.ResourceService;
import tbs.api_server.utility.ApiMethod;
import tbs.api_server.utility.FileUtility;
import tbs.api_server.utils.SecurityTools;

import java.io.File;
import java.util.Date;
import java.util.List;

import static tbs.api_server.publicAPI.ResourceController.Help.*;
import static tbs.api_server.utility.Error.*;

@RestController
@RequestMapping(value = "/resource/*")
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
        }).methodWithLogined();
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Transactional
    @AccessLimit(level = AccessEnum.RESOURCE_ACCESS)
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
                    return NetResult.makeResult(SUCCESS, "保存 "+total+" bytes");
                } else {
                    if (file1.exists()) {
                        file1.delete();
                    }
                    return NetResult.makeResult(EC_FILESYSTEM_ERROR, "写入失败");
                }
            }
        }).methodWithLogined();
    }
    @Autowired
    UserMapper userMapper;

    @Transactional
    @RequestMapping("/delete")
    @AccessLimit(level = AccessEnum.RESOURCE_ACCESS)
    public NetResult delete(int userid, int resource_id) {
        final NetResult result = new NetResult(SUCCESS, null, null);
        return ApiMethod.make(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                QuestionResource resource = (QuestionResource) service.getResourceById(resource_id).getObj();
                if(resource==null)
                    throw  _ERROR.throwError(EC_DB_SELECT_NOTHING,String.format("不存在%d资源",resource_id));
                ServiceResult rs = service.DeleteResource(resource_id);
                FileUtility.BaseThen then = new FileUtility.FileDeleteThen();
                FileUtility.existFile(genPath(resource.getResource()), then);
                int deleted =(int) then.result();
                if(deleted== FileUtility.FileDeleteThen.ERROR)
                {
                    throw _ERROR.throwError(EC_FILESYSTEM_ERROR,"删除资源文件失败");
                }
                return result;
            }
        }).methodWithLogined();
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
        }).methodWithLogined();
    }


    @Transactional
    @RequestMapping("setnote")
    @AccessLimit(level = AccessEnum.RESOURCE_ACCESS)
    public NetResult updateResourceNote(int resource,String note)
    {
        return ApiMethod.makeResult(new ApiMethod.IAction() {
            @Override
            public NetResult action(UserSecurityInfo applyUser) throws BackendError, Exception {
                return NetResult.makeResult(service.updateResourceNote(resource, note),null);
            }
        });
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

