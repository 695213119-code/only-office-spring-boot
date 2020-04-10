package com.onlyoffice.demo.service.impl;

import com.onlyoffice.demo.entity.Document;
import com.onlyoffice.demo.service.IDocumentModelViewService;
import com.onlyoffice.demo.service.IDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * 文档操作业务类
 *
 * @author : zhangLei
 * @serial : 2020-04-09 0009
 */
@Service
@Slf4j
public class DocumentModelViewServiceImpl implements IDocumentModelViewService {

    private final IDocumentService documentService;

    @Autowired
    public DocumentModelViewServiceImpl(IDocumentService documentService) {
        this.documentService = documentService;
    }


    @Override
    public String view(String documentKey, Model model) {
        Document document = documentService.getDocument(documentKey);
        log.info("============> 预览页面时获取到的document数据:" + document);
        model.addAttribute("document", document);
        return "/viewer";
    }

    @Override
    public String edit(String documentKey, String userId, String userName, Model model) {
        Document document = documentService.getDocument(documentKey);
        model.addAttribute("document", document);
        // 如果该格式不支持编辑，则返回预览页面
        if (!documentService.canEdit(document)) {
            log.error("============> 由于不支持PDF格式的文档编辑,所以返回至文档预览页面!");
            return "/viewer";
        }
        model.addAttribute("documentEditParam", documentService.buildDocumentEditParam(userId, userName));
        return "/editor";
    }
}
