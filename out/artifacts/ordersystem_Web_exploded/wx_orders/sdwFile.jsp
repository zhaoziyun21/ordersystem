<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>
<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/??sm.min.css,sm-extend.min.css">
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/??sm.min.js,sm-extend.min.js' charset='utf-8'></script>
<title>Insert title here</title>
</head>
<body class="demo-1">
        <div class="container">
            <header class="codrops-header">
              
             
            </header>
            <section>
                <select class="cs-select cs-skin-border">
                    <option value="" disabled selected>选择店铺</option>
                    <option value="email">E-Mail</option>
                    <option value="twitter">Twitter</option>
                    <option value="linkedin">LinkedIn</option>
                </select>
            </section>
 
        </div><!-- /container -->
        <script src="js/classie.js"></script>
        <script src="js/selectFx.js"></script>
        <script>
            (function() {
                [].slice.call(document.querySelectorAll('select.cs-select')).forEach(function(el) {
                    new SelectFx(el);
                });
            })();
        </script>
    </body>

</html>