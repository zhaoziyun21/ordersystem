/**
 * Created by Administrator on 2016/11/8.
 */

var canvas = document.getElementById('canvas');
var ctx = canvas.getContext('2d');
canvas.width = canvas.parentNode.offsetWidth;



//如果浏览器支持requestAnimFrame则使用requestAnimFrame否则使用setTimeout
window.requestAnimFrame = (function(){
    return  window.requestAnimationFrame       ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame    ||
        function( callback ){
            window.setTimeout(callback, 1000 / 10);
        };
})();
//初始角度为0
var step = 0;
//定义三条不同波浪的颜色
var lines = ["rgba(255,255,255, 1)",
    "rgba(183,183,251, 0.8)",
    "rgba(139,139,248, 0.9)"];
function loop(){
    ctx.clearRect(0,0,canvas.width,canvas.height);
    step++;
    //画3个不同颜色的矩形
    for(var j = lines.length - 1; j >= 0; j--) {
        ctx.fillStyle = lines[j];
        //每个矩形的角度都不同，每个之间相差30度
        var angle = (step+j*120)*Math.PI/400;
        var deltaHeight   = Math.sin(angle) * 12;
        var deltaHeightRight   = Math.cos(angle) *15 ;
        ctx.beginPath();
        ctx.moveTo(0, canvas.height/2+deltaHeight);
        ctx.bezierCurveTo(canvas.width, canvas.height/2+deltaHeight-20, canvas.width, canvas.height+deltaHeightRight-50, canvas.width, canvas.height/2+deltaHeightRight);
        ctx.lineTo(canvas.width, canvas.height);
        ctx.lineTo(0, canvas.height);
        ctx.lineTo(0, canvas.height/2+deltaHeight);
        ctx.closePath();
        ctx.fill();
    }
    requestAnimFrame(loop);
}
loop();