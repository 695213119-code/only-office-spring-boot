package com.onlyoffice.demo.web;

import com.onlyoffice.demo.entity.Document;
import com.onlyoffice.demo.entity.DocumentEditCallback;
import com.onlyoffice.demo.entity.DocumentEditCallbackResponse;
import com.onlyoffice.demo.entity.DocumentResponse;
import com.onlyoffice.demo.service.IDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文档接口Controller
 *
 * @author zhanglei
 * @serial 2020-04-08
 */
@Slf4j
@RestController
@RequestMapping("/document/api")
@Api(tags = "文档相关接口")
public class DocumentApiController {

    private final IDocumentService documentService;

    @Autowired
    public DocumentApiController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @ApiOperation(value = "文件预览")
    @PostMapping("/before/view")
    public DocumentResponse beforeView(@ApiParam(value = "文档文件路径（必需）", required = true) @RequestParam String documentPath,
                                       @ApiParam(value = "文档标题(非必需，默认为实体对应的文件名)") @RequestParam(required = false) String documentTitle) {
        return documentService.beforeView(documentPath, documentTitle);
    }

    @ApiOperation(value = "文档编辑")
    @PostMapping("/before/edit")
    public DocumentResponse beforeEdit(@ApiParam(value = "文档文件路径", required = true) @RequestParam String documentPath,
                                       @ApiParam(value = "文档标题") @RequestParam(required = false) String documentTitle) {
        return documentService.beforeEdit(documentPath, documentTitle);
    }

    @ApiOperation("获取文件实体（下载")
    @ApiIgnore
    @GetMapping("/file/{documentKey}")
    public void getDocFile(@ApiParam("文档的加密Key") @PathVariable String documentKey, HttpServletRequest request, HttpServletResponse response) {
        documentService.downloadDocumentFile(documentKey, request, response);
    }

    @ApiOperation("编辑后回调--保存文件实体(onlyOffice在编辑后关闭文件的时候，会回调该接口)")
    @ApiIgnore
    @PostMapping("/callback")
    public DocumentEditCallbackResponse saveDocumentFile(@RequestBody DocumentEditCallback callback) {
        return documentService.callback(callback);
    }

    @ApiOperation(value = "获取文档信息")
    @GetMapping("/doc/{documentKey}")
    @ApiIgnore
    public Document docData(@ApiParam(value = "文档加密Key", required = true) @PathVariable String documentKey) {
        return documentService.getDocument(documentKey);
    }


}
