package tbs.api_server.publicAPI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tbs.api_server.config.constant.const_Resource_Type;
import tbs.api_server.services.ResourceService;

import static tbs.api_server.publicAPI.ResourceController.Help.getFile;

@RestController
@RequestMapping("/file/*")
public class FileController {

    @Autowired
    ResourceService service;
    @RequestMapping(value = "/res/image",produces = "image/*")
    public byte[] downloadImg(int id)
    {
        return getFile(service,id, const_Resource_Type.Image);
    }
    @RequestMapping(value = "/res/text",produces = "text/*")
    public byte[] downloadText(int id)
    {
        return getFile(service,id,const_Resource_Type.Text);
    }
    @RequestMapping(value = "/res/video",produces = "video/*")
    public byte[] downloadvideo(int id)
    {
        return getFile(service,id,const_Resource_Type.Video);
    }
    @RequestMapping(value = "/res/audio",produces = "audio/*")
    public byte[] downloadAuio(int id)
    {
        return getFile(service,id,const_Resource_Type.Audio);
    }

}
