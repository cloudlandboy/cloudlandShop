var CART = {
    itemNumChange: function () {
        $(".increment").click(function () {//＋
            var _thisInput = $(this).siblings("input");
            _thisInput.val(eval(_thisInput.val()) + 1);
            $.post("/cart/update/num/" + _thisInput.attr("itemId") + "/" + _thisInput.val() + ".action", function (data) {
                CART.refreshTotalPrice();
                var subtotalprice = _thisInput.parents(".clearit").find(".pSubtotal").children(".totalprice");
                var subtotalprice_num = _thisInput.attr("itemPrice") * eval(_thisInput.val());
                subtotalprice.html((subtotalprice_num / 100).toFixed(2));
            });
        });
        $(".decrement").click(function () {//-
            var _thisInput = $(this).siblings("input");
            if (eval(_thisInput.val()) == 1) {
                return;
            }
            _thisInput.val(eval(_thisInput.val()) - 1);
            $.post("/cart/update/num/" + _thisInput.attr("itemId") + "/" + _thisInput.val() + ".action", function (data) {
                CART.refreshTotalPrice();
                var subtotalprice = _thisInput.parents(".clearit").find(".pSubtotal").children(".totalprice");
                var subtotalprice_num = _thisInput.attr("itemPrice") * eval(_thisInput.val());
                subtotalprice.html((subtotalprice_num / 100).toFixed(2));
            });
        });
        /*$(".itemnum").change(function(){
            var _thisInput = $(this);
            $.post("/service/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
                CART.refreshTotalPrice();
            });
        });*/
    },
    refreshTotalPrice: function () { //重新计算总价
        var total = 0;
        $(".itemnum").each(function (i, e) {
            var _this = $(e);
            total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
        });
        $("#allMoney2").html(new Number(total / 100).toFixed(2)).priceFormat({ //价格格式化插件
            prefix: '¥',
            thousandsSeparator: ',',
            centsLimit: 2
        });
    }
};

$(function () {
    CART.itemNumChange();
});