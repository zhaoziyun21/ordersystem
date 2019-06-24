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
    _html += '<a id="mb_ico">×</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
 
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
 
    $("#mb_con").css({ zIndex: '999999', width: '15.2rem' ,height: '8rem',backgroundColor: '#FFFFFF',position: 'fixed'
    });
 
    $("#mb_tit").css({ display: 'block', fontSize: '0.96rem', color: 'white', padding: '0.133333rem 0.4rem',
      backgroundColor: '#FF8F33', fontWeight: 'bold',height:'1.866666667rem',lineHeight:'1.866666667rem',fontFamily:'PingFang-SC-Medium'
    });
 
    $("#mb_msg").css({ padding: '1.44rem 0rem 1.626666667rem 0.93333333rem', lineHeight: '0.533333rem',
      fontSize: '0.8rem',background:'white',color:'#333333'
    });
 
    $("#mb_ico").css({ display: 'none', position: 'absolute', right: '0.26666667rem', top: '0.6rem',
      fontSize:'1.7rem',textAlign: 'center',color:"white",backgroundColor: '#FF8F33',
      lineHeight: '0.43666667rem', cursor: 'pointer',  fontFamily: 'PingFang-SC-Medium',fontSize:'1.96rem'
    });
 
    $("#mb_btnbox").css({ padding: '0.4rem 0rem', textAlign: 'center',background:'white'});
    $("#mb_btn_ok,#mb_btn_no").css({ width: '6.2666667rem', height: '1.573333333rem', color: 'white', border: 'none',borderRadius: '0.08rem',padding:'0px 0.3rem'});
    $("#mb_btn_ok").css({ 'backgroundColor': '#FF8F33','margin-right':'0.5333333rem'});
    $("#mb_btn_no").css({ backgroundColor: '#EDEDED',color: '#333333'});
 
 
    //右上角关闭按钮hover样式
    $("#mb_ico").hover(function () {
      $(this).css({color: 'red' });
    }, function () {
      $(this).css({ backgroundColor: '#FF8F33', color: 'white' });
    });
 
    var _widht = document.documentElement.clientWidth; //屏幕宽
    var _height = document.documentElement.clientHeight; //屏幕高
 
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