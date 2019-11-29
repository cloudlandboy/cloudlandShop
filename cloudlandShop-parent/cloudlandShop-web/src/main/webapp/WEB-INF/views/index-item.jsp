<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div>
    <a class="easyui-linkbutton" onclick="importItems()">一键导入商品数据到索引库</a>
</div>
<script type="text/javascript">
    function importItems() {
		ajaxLoading();
        $.post("/item/importIndex", null, function (data) {
				ajaxLoadEnd();
            if (data.status == 200) {
                $.messager.alert('提示', '导入索引库成功！');
            } else {
                $.messager.alert('提示', '导入索引库失败！');
            }
        });
    }

    function ajaxLoading() {
        var id = "body";
        var left = ($(window).outerWidth(true) - 190) / 2;
        var top = ($(window).height() - 35) / 2;
        var height = $(window).height() * 2;
        $("<div class=\"datagrid-mask\"></div>").css({display: "block", width: "100%", height: height}).appendTo(id).css('z-index',9712);
        $("<div class=\"datagrid-mask-msg\"></div>").html("正在导入,请稍候...").appendTo(id).css({
            display: "block",
            left: left,
            top: top,
        });
    }

    function ajaxLoadEnd() {
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }
</script>