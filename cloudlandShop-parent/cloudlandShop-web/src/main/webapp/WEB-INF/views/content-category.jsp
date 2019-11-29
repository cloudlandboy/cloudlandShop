<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/content/category/list', //请求的url
		animate: true,
		method : "GET",
		onContextMenu: function(e,node){    //在节点上右键触发的事件（显示菜单）
		    //阻止浏览器默认行为，不让显示浏览器菜单
            e.preventDefault();
            $(this).tree('select',node.target);
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        //结束编辑时调用
        onAfterEdit : function(node){
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/content/category/create",{parentId:node.parentId,name:node.text},function(data){
        			if(data.status == 200){
        				_tree.tree("update",{
            				target : node.target,
            				id : data.data.id
            			});
        			}else{
        				$.messager.alert('提示','创建'+node.text+' 分类失败!');
        			}
        		});
        	}else{
        		$.post("/content/category/update",{id:node.id,name:node.text});
        	}
        }
	});
});
function menuHandler(item){
    //item就是点击的菜单项
	var tree = $("#contentCategory");
	var node = tree.tree("getSelected");
	if(item.name === "add"){
		tree.tree('append', {
            parent: (node?node.target:null),
            //新建的节点的数据，id为0
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        });
		//获取第一个id为0的新增节点
		var _node = tree.tree('find',0);
		//把新增节点变为可编辑状态,再次点击编辑区以外的地方就会结束编辑，调用结束编辑方法
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.post("/content/category/delete/",{id:node.id},function(data){
				    if(data.status==200){
                        tree.tree("remove",node.target);
                    }else{
				        $.messager.alert('删除失败',data.msg,'error');
                    }

				});	
			}
		});
	}
}
</script>