package com.onlyoffice.demo.web;

import com.onlyoffice.demo.service.IDocumentModelViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * ModelAndView 相关控制器
 * 注意:只能使用@Controller,不然会出现只能返回字符串,并不能返回页面
 * 此Controller不对外暴露
 *
 * @author : zhangLei
 * @serial : 2020-04-09
 */
@Slf4j
@Controller
@RequestMapping("/document/model")
@ApiIgnore
@Api(tags = "操作文档相关接口,主要用来确认访问路径")
public class DocumentModelViewController {

    private final IDocumentModelViewService documentModelViewService;

    @Autowired
    public DocumentModelViewController(IDocumentModelViewService documentModelViewService) {
        this.documentModelViewService = documentModelViewService;
    }

    @GetMapping("/viewer/{documentKey}")
    @ApiOperation("返回预览页面")
    public String view(@ApiParam(value = "文档的加密Key", required = true) @PathVariable String documentKey, Model model) {
        return documentModelViewService.view(documentKey, model);
    }

    @GetMapping("/editor/{documentKey}")
    @ApiOperation("返回编辑页面")
    public String edit(@ApiParam(value = "文档的加密Key", required = true) @PathVariable String documentKey,
                       @ApiParam(value = "用户id", required = true) @RequestParam String userId,
                       @ApiParam(value = "用户名", required = true) @RequestParam String userName, Model model) {
        return documentModelViewService.edit(documentKey, userId, userName, model);
    }

}
