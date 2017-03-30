<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.serializejson.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/parsley.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/parsley.ko.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.form.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.tmpl.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.tmplPlus.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/ltem.common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/d3.v3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/c3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/swiper.min.js"></script>
<!--[if lt IE 9]>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/html5shiv.js"></script>
<![endif]-->
<script type="text/javascript">
    ltem.csrf = "${_csrf.token}";
</script>
<script type="text/javascript">
    if ( $.cookie('modeView') == "PC" ) {
        document.write('<meta name="viewport" content="width=1340, initial-scale=1.0, user-scalable=yes">');
    } else {
        document.write('<meta name="viewport" content="width=device-width, initial-scale=1.0, target-densitydpi=medium-dpi">');
    }
</script>