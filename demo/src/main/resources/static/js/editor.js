var Editor = function () {
    var docEditor;

    var innerAlert = function (message) {
        if (console && console.log)
            console.log(message);
    };

    var onReady = function () {
        innerAlert("Document editing ready");
    };

    var onDocumentStateChange = function (event) {
        var title = document.title.replace(/\*$/g, "");
        document.title = title + (event.data ? "*" : "");
    };

    var onError = function (event) {
        if (event) {
            innerAlert(event.data);
        }
    };

    var onOutdatedVersion = function () {
        location.reload(true);
    };

    var getDocumentConfig = function (document) {
        if (document) {
            return {
                "document": document
            };
        }
        innerAlert("文档未指定！");
        return null;
    };

    var onRequestEditRights = function () {
        location.href = location.href.replace(RegExp("action=view\&?", "i"), "");
    };

    var connectEditor = function (document, documentEditParam) {
        var config = getDocumentConfig(document);
        config.width = "100%";
        config.height = "100%";
        config.events = {
            "onAppReady": onReady,
            "onDocumentStateChange": onDocumentStateChange,
            'onRequestEditRights': onRequestEditRights,
            "onError": onError,
            "onOutdatedVersion": onOutdatedVersion
        };
        config.editorConfig = {
            "lang": "zh-CN",
            "mode": "edit",
            "recent": [],
            // 自定义一些配置
            "customization": {
                "chat": false, // 禁用聊天菜单按钮
                "commentAuthorOnly": true, // 仅能编辑和删除其注释
                "comments": false, // 隐藏文档注释菜单按钮
                "compactHeader": false, // 隐藏附加操作按钮
                "compactToolbar": false, // 完整工具栏(true代表紧凑工具栏)
                "feedback": {
                    "visible": false // 隐藏反馈按钮
                },
                "forcesave": false, // true 表示强制文件保存请求添加到回调处理程序
                "goback": false, /*{
                            "blank": true, // 转到文档时，在新窗口打开网站(false表示当前窗口打开)
                            "text": "转到文档位置（可以考虑放文档打开源页面）",
                            // 文档打开失败时的跳转也是该地址
                            "url": "http://www.lezhixing.com.cn"
                        },*/
                "help": false, // 隐藏帮助按钮
                "hideRightMenu": false, // 首次加载时隐藏右侧菜单(true 为显示)
                "showReviewChanges": false, // 加载编辑器时自动显示/隐藏审阅更改面板(true显示 false隐藏)
                "toolbarNoTabs": false, // 清楚地显示顶部工具栏选项卡(true 代表仅突出显示以查看选择了哪一个)
                "zoom": 100 // 定义文档显示缩放百分比值
            }
        };
        $.extend(config.editorConfig, documentEditParam);
        console.log(config);
        docEditor = new DocsAPI.DocEditor("iframeEditor", config);
    };

    return {
        init: function (document, documentEditParam) {
            connectEditor(document, documentEditParam);
        }
    }
}();