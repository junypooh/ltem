window.console = window.console || {log:function(){}};

var ltem = window.ltem || {};
var sessioninterval;
$(document).ready(function(){
    $.ajaxSetup({
        headers: { 'X-Ajax-call': 'true', 'X-CSRF-TOKEN': ltem.csrf },
        traditional : true
    });

    /**
     * 모바일 상단 메뉴명 셋팅
     */
    $('#mobileMenuNm').text($('.cont_top > h2').text());
    if($('#mobileMenuNm').text() == "") {
        $('#mobileMenuNm').text("biz GIGA IoT 홈");
    }

    $('#mobileMenuNm2').text($('.h2_web > h2').text());
    if($('#mobileMenuNm2').text() == "") {
        $('#mobileMenuNm2').text("biz GIGA IoT 홈");
    }

    try {
        sessioninterval = sessionTimer( sessionTimeOut );
    } catch (e) {
        // 무시
    }
});

/**
 * 날짜를 문자열로 변경 ex ) (new Date("2008-12-31")).format("yyyy.MM.dd")
 */
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    pad = function (val, len) {
        val = String(val);
        len = len || 2;
        while (val.length < len) val = "0" + val;
        return val;
    };

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var weekNameShort = ["일", "월", "화", "수", "목", "금", "토"];
    var d = this;
    var h;
    return f.replace(/(yyyy|yy|HF|QQ|MM|dd|ww|E|HH|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "HF": return pad(d.getHalf());
            case "QQ": return pad(d.getQuarter());
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "ww": return pad(d.getWeek());
            case "E": return weekName[d.getDay()];
            case "e": return weekNameShort[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};

//pads left
String.prototype.lpad = function(padString, length) {
    var str = this;
    while (str.length < length)
        str = padString + str;
    return str;
}

//pads right
String.prototype.rpad = function(padString, length) {
    var str = this;
    while (str.length < length)
        str = str + padString;
    return str;
}
//trimming space from both side of the string
String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g,"");
}

//trimming space from left side of the string
String.prototype.ltrim = function() {
    return this.replace(/^\s+/,"");
}

//trimming space from right side of the string
String.prototype.rtrim = function() {
    return this.replace(/\s+$/,"");
}
Number.prototype.zf = function(len){return this.toString().zf(len);};

/**
 * message등을 표시할 때 format을 사용할 수 있도록 지원함.
 */
String.prototype.format = function(){
    var tokenCount = arguments.length;
    var s = this;
    for( var token = 0; token < tokenCount; token++ ){
        s = s.replace( new RegExp( "\\{" + token + "\\}", "gi" ), arguments[ token ] );
    }
    return s;
};

/**
 * 날짜 문자열을 가공
 */
String.prototype.toDay = function(delimiter) {
    if (this.length == 8) {
        if(delimiter !== '-') delimiter = '\.';
        return this.replace(/^(\d{4})(\d{2})(\d{2})$/i, '$1'+delimiter+'$2' + delimiter + '$3');
    }
    return this;
};

/**
 * 시간 문자열을 가공
 */
String.prototype.toTime = function() {
    if (this.length === 4) {
        return this.replace(/^(\d{2})(\d{2})$/i, '$1\:$2');
    }
    return this;
};

/**
 * Array 비교
 */
Array.prototype.contains = function(element) {
    for(var i=0;i<this.length;i++) {
        if(this[i] == element) {
            return true;
        }
    }
    return false;
}

/**
 *  확장자 -> CSS Class 변경
 */
String.prototype.changeExt = function() {
    var textIcon = ["hwp","pdf","txt","pptx", "xlsx", "docx"];
    var imgIcon = ["jpg","png","gif"];
    var movieIcon = ["mp4","mov","avi"];
    var audioIcon = ["mp3","wav","wma"];
    var zipIcon = ["zip","rar"];
    if(textIcon.contains(this.toLowerCase())) {
        return "pptx";
    } else if(imgIcon.contains(this.toLowerCase())) {
        return "jpg";
    } else if(movieIcon.contains(this.toLowerCase())) {
        return "avi";
    } else if(audioIcon.contains(this.toLowerCase())) {
        return "mp4";
    } else if(zipIcon.contains(this.toLowerCase())) {
        return "zip";
    } else {
        return "etc";
    }
}
/**
 *  replaceAll prototype 선언
 */
String.prototype.replaceAll = function(org, dest) {
    if(this.valueOf() == 'null') {
        return '';
    } else {
        return this.split(org).join(dest);
    }
}

/* 숫자값만을 리턴한다. */
ltem.getOnlyNumber = function(str) {
    return str.replace(/[^\d]/g, '');
};

ltem.parseDate = function(str) {
    str = ltem.getOnlyNumber(str.toString()).rpad("0",14);
    var m = str.match(/^(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})$/);
    return (m) ? new Date(m[1], m[2]-1, m[3], m[4], m[5], m[6]) : null;
};

ltem.calDate = function(date, y, m, d){

    var _date = null;
    if(date ==null){
        _date = new Date();
    }else{
        _date = new Date(date);
    }
    if(y != null && y != 0){
        _date.setFullYear(_date.getFullYear() + y);
    }
    if(m != null && m != 0){
        _date.setMonth(_date.getMonth() + m);
    }
    if(d != null && d != 0){
        _date.setDate(_date.getDate() + d);
    }
    return _date;
};

//device detection
ltem.isMobile = (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent)
|| /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4)));

(function($) {
    //select의 options을 삭제
    $.fn.emptySelect = function(index) {
        return this.each(function(){
            if (this.tagName=='SELECT') this.options.length = index;
        });
    };
    //select의 options을 추가
    $.fn.loadSelectOptions = function(optionsDataArray, selectCode, code, name, remainOption) {
        if(typeof(remainOption) === "undefined"){
            remainOption = 0;
        }
        return this.emptySelect(remainOption).each(function(){
            if (this.tagName=='SELECT') {
                var selectElement = this;
                $.each(optionsDataArray,function(index,optionData){
                    var selected = optionData[code]==selectCode?' selected':'';
                    var option = '<option value="'+optionData[code]+'"'+selected+'>'+optionData[name]+'</option>';
                    $(selectElement).append(option);
                });
            }
        });
    };
})(jQuery);

function toCurrency(n) {
    var reg = /(^[+-]?\d+)(\d{3})/; // 정규식
    n += ''; // 숫자를 문자열로 변환

    while (reg.test(n)){
        n = n.replace(reg, '$1' + ',' + '$2');
    }

    return n;
}

/*
 * yyyyMMdd 날짜문자열을 gubun으로 포맷을 변경
 */
function to_date_format(date_str, gubun) {
    if(date_str == null){
        return null;
    }
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);

    return sYear + gubun + sMonth + gubun + sDate;
}

/*
 * hh24mi 시간문자열을 gubun으로 포맷을 변경
 */
function to_time_format(time_str, gubun) {
    if(time_str == null){
        return null;
    }
    var time = String(time_str);
    var hh = time.substring(0,2);
    var mm = time.substring(2,4);
    return hh + gubun + mm;
}


/**
 * Time 스트링을 자바스크립트 Date 객체로 변환
 * parameter time: Time 형식의 String
 */
function toTimeObject(time) { //parseTime(time)
    var year  = time.substr(0,4);
    var month = time.substr(4,2) - 1; // 1월=0,12월=11
    var day   = time.substr(6,2);
    var hour  = time.substr(8,2);
    var min   = time.substr(10,2);

    return new Date(year,month,day,hour,min);
}

function enterFunc(obj, func){
    obj.keypress(function(e){
        if(e.keyCode==13){
            func();
        }
    });
}

/**
 * Date 타입 형식을 문자열 날짜로 변환
 */
function fn_date_to_string(value) {
    return new Date(value).format('yyyy.MM.dd');
}

/**
 * 오늘 날짜 return
 */
function getToday(){
    var now = new Date();
    var year= now.getFullYear();
    var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();

    return year + mon + day;
}

/*
 * yyyyMMdd 날짜문자열을 년월일로 포맷을 변경
 */
function to_date_format_ymd(date_str) {
    if(date_str == null){
        return null;
    }
    var yyyyMMdd = String(date_str);
    var sYear = yyyyMMdd.substring(0,4);
    var sMonth = yyyyMMdd.substring(4,6);
    var sDate = yyyyMMdd.substring(6,8);

    return sYear + '년 ' + sMonth + '월 ' + sDate + '일';
}


/**
 * 초를 시간으로 변경.
 */
function transSecToTime(sec, seprator) {
    var hh = 0;
    var mm = 0;
    var ss = 0;
    var sec = Math.round(sec);
    if(sec > 3600) {
        hh = Math.floor(sec / 3600);
        mm = Math.floor(hh / 60);
        ss = hh % 60;
    } else if(sec < 3600 && sec > 60) {
        mm = Math.floor(sec / 60);
        ss = mm % 60;
    } else {
        ss = Math.ceil(sec);
    }
    if(seprator != "undefined") {
        seprator = "";
    }
    if(hh<10) {
        hh = "0"+hh;
    }
    if(mm<10) {
        mm = "0"+mm;
    }
    if(ss<10) {
        ss = "0"+ss;
    }
    return (hh+seprator+mm+seprator+ss);
}

/**
 * 휴대폰번호 자동 hypen('-') 추가
 * @param obj
 */
function autoHypenPhone(obj){
    var str = $(obj).val();
    str = str.replace(/[^0-9]/g, '');
    var tmp = '';
    if( str.length < 4){
        $(obj).val(str);
    }else if(str.length < 7){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3);
        $(obj).val(tmp);
    }else if(str.length < 11){
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 3);
        tmp += '-';
        tmp += str.substr(6);
        $(obj).val(tmp);
    }else{
        tmp += str.substr(0, 3);
        tmp += '-';
        tmp += str.substr(3, 4);
        tmp += '-';
        tmp += str.substr(7);
        $(obj).val(tmp);
    }
}

/**
 * 카운트 다운
 * @param elementName
 * @param seconds
 * @param interval
 */
function countdown( elementName, timer, intervalId ) {

    if(typeof intervalId != 'undefined') {
        clearInterval(intervalId);
    }

    var minutes, seconds;

    var interval = setInterval(function(){
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        $('#' + elementName).text(minutes + ':' + seconds);

        if (--timer < 0) {
            timer = 0;
            clearInterval(interval);
        }
    }, 1000);

    return interval;
}

/**
 * 세션 타임 아웃 script
 * @param timer
 */
function sessionTimer( timer ) {

    var interval = setInterval(function(){
        if (--timer < 60) {
            $('#autoLogoutConfirm').show();
        }
        if (timer < 0) {
            timer = 0;
            clearInterval(interval);
            location.href = contextPath + "/login/autoLogout";
        }
    }, 1000);

    return interval;

}

/**
 * PC 버전 보기
 * jsHeader.jsp 에서 구현 됨.
 */
function viewPcVersion() {
    $.cookie('modeView', 'PC', {path: '/'});
    location.reload();
}