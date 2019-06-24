(function () {
  $.MsgBox = {
    Alert: function (title, msg,callback) {
      GenerateHtml("alert", title, msg);
      btnOk(callback); 
      btnNo();
    },
    Confirm: function (title, msg, callback) {
      GenerateHtml("confirm", title, msg);
      btnOk(callback);
      btnNo();
    }
  }
 
  //生成Html
  var GenerateHtml = function (type, title, msg) {
 
    var _html = "";
 
    _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
    _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
 
    if (type == "alert") {
      _html += '<input id="mb_btn_ok" type="button" value="确定" />';
    }
    if (type == "confirm") {
      _html += '<input id="mb_btn_ok" type="button" value="确定" />';
      _html += '<input id="mb_btn_no" type="button" value="取消" />';
    }
    _html += '</div></div>';
 
    //必须先将_html添加到body，再设置Css样式
    $("body").append(_html); GenerateCss();
  }
 
  //生成Css
  var GenerateCss = function () {
    $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
      filter: 'alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
    });
 
    $("#mb_con").css({ zIndex: '999999', width: '400px', position: 'fixed',
      backgroundColor: '#FF8F33'
    });
 
    $("#mb_tit").css({ display: 'block', fontSize: '14px', color: 'white', padding: '5px 15px',
      backgroundColor: '#FF8F33', fontWeight: 'bold'
    });
 
    $("#mb_msg").css({ padding: '20px', lineHeight: '20px',
      fontSize: '13px',background:'white',margin:'0px 6px'
    });
 
    $("#mb_ico").css({ display: 'none', position: 'absolute', right: '10px', top: '5px',
      /* width: '35px', height: '18px',*/ textAlign: 'center',color:"white",backgroundColor: '#FF8F33',
      lineHeight: '16px', cursor: 'pointer',  fontFamily: '微软雅黑'
    });
 
    $("#mb_btnbox").css({ padding: '15px 0 10px 0', textAlign: 'center',background:'white',margin:'0px 6px 6px 6px' });
    $("#mb_btn_ok,#mb_btn_no").css({ width: '46px', height: '30px', color: 'white', border: 'none',borderRadius: '3px'});
    $("#mb_btn_ok").css({ 'backgroundColor': '#FF8F33','margin-right':'20px','margin-left':'15px' });
    $("#mb_btn_no").css({ backgroundColor: '#EDEDED',color: '#333333'});
 
 
    //右上角关闭按钮hover样式
    $("#mb_ico").hover(function () {
      $(this).css({color: 'red' });
    }, function () {
      $(this).css({ backgroundColor: '#FF8F33', color: 'white' });
    });
 
    var _widht = document.documentElement.clientWidth; //屏幕宽
    var _height = window.screen.height; //屏幕高
 
    var boxWidth = $("#mb_con").width();
    var boxHeight = $("#mb_con").height();
 
    //让提示框居中
    $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
  }
 
 
  //确定按钮事件
  var btnOk = function (callback) {
    $("#mb_btn_ok").click(function () {
      $("#mb_box,#mb_con").remove();
      if (typeof (callback) == 'function') {
        callback();
      }
    });
  }
 
  //取消按钮事件
  var btnNo = function () {
    $("#mb_btn_no,#mb_ico").click(function () {
      $("#mb_box,#mb_con").remove();
      
      $("body").mLoading("hide");//隐藏loading组件
    });
  }
})();