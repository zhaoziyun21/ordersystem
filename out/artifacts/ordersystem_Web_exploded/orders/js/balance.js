/*账户余额*/
$(function(){
	var bg = null;//用来记录颜色的变量
    var tb = document.getElementById("j_tb");
    var trs = tb.getElementsByTagName("tr");
    for (var i = 0; i < trs.length; i++) {
        trs[i].onmouseover = function () {
            bg = this.style.backgroundColor;//保存当前的颜色
            this.style.backgroundColor = "#EFEFEF";
        }
        //鼠标离开当前行 要回复原来的原色
        trs[i].onmouseout = function () {
            this.style.backgroundColor = bg;
        }
    }
})