<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="font-awesome-4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-social.css">
    <link rel="stylesheet" type="text/css" href="css/local.css"/>
    <script src="js/jquery-1.11.3.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAQuDglnklQo2fARuY8FHeu5PDdypvh0is&v=3.exp&signed_in=true"></script>
    <script type="text/javascript" src="http://www.panoramio.com/wapi/wapi.js?v=1"></script>

    <title>GuidezUp! Walk Watch Listen - Free audio guides. Бесплатные туристические аудиогиды (аудиогайды). Бесплатные
        аудиогиды. audio, guide, путешествия, путеводитель, travel, podcast, mobile, journey, аудиогид</title>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <META name="Description"
          content="Free multi-language travelling audio guides collection. audio, guide, путешествия, путеводитель,
        travel, podcast, mobile, journey, guidezup, popup">
    <META name="Keywords"
          content="audio, guide, путешествия, путеводитель, travel, podcast, mobile, journey, guidezup, popup, mp3
        download,free audio downloads">
    <meta name="robots" content="index, follow">
    <META name="author" content="GuidezUp">

    <script>
        var const100meter = 0.00089982311916;

        var gmarkers = [];
        var guidez = [];
        var autocomplete = [];
        var curLanguage = "English";
        var currentIndex = 0;
        var paidBtnClass = "btn btn-primary btn-md";
        var paidBtnClassActive = "btn btn-primary btn-md active";
        var baseUrl = "http://10.0.0.24:8080/";

        var shown = false;
        var playerStopped = true;

        $(document).ready(function () {
            $.get("http://ipinfo.io", function (response) {
                var country = response.country;
                if (country == "RU" || country == "IL" || country == "BY" || country == "UA" || country == "KZ") {
                    curLanguage = "Russian";
                    $("#btnRuId").addClass("active");
                    $("#btnEnId").removeClass("active");
                }
                else {
                    $("#btnEnId").addClass("active");
                    $("#btnRuId").removeClass("active");
                }
            }, "jsonp");
            $("#pano").hide();
        });

        function initialize() {
            var url = window.location.toString();
            console.log("url = " + url);
            if (url.match('//guidezup.com') != undefined) {
                baseUrl = "http://guidezup.com/";
            }
            else if (url.match('//www.guidezup.com') != undefined) {
                baseUrl = "http://www.guidezup.com/";
            }
            console.log("baseUrl = " + baseUrl);
            var mapProp = {
                center: new google.maps.LatLng(51.508742, -0.120850),
                zoom: 8,
                streetViewControl: false,
                mapTypeControlOptions: {
                    mapTypeIds: [
                        google.maps.MapTypeId.ROADMAP,
                        google.maps.MapTypeId.SATELLITE
                    ],
                    position: google.maps.ControlPosition.BOTTOM_LEFT
                }
            };
            map = new google.maps.Map(document.getElementById("googleMap"), mapProp);


            var widgetDiv = document.getElementById("direction-widget");
            map.controls[google.maps.ControlPosition.TOP_LEFT].push(widgetDiv);


            $(document).ready(function () {
                $.get(getServiceUrl() + "init", function () {
                fillData(curLanguage);
                }, "jsonp");
            });
        }
        google.maps.event.addDomListener(window, 'load', initialize);

        function removeMarkers() {
            for (var i = 0; i < gmarkers.length; i++) {
                google.maps.event.clearInstanceListeners(gmarkers[i]);
                gmarkers[i].setMap(null);
            }
        }

        function getServiceUrl() {
            return baseUrl + "rest/guideservices/";
        }

        function getAudioUrl() {
            return baseUrl + "audio/";
        }

        function fillData(language) {
            var startParams = parseStartParams();
            curLanguage = language;
            if (startParams[0] == undefined || startParams[1] == undefined || shown == true) {
                getAndFillPublishedGuides(curLanguage);
            }
            else {
                if (startParams[1] == "rus") {
                    curLanguage = "Russian";
                }
                else {
                    curLanguage = "English";
                }
                shown = true;
                searchAndFillPublishedGuides(startParams[0], curLanguage);
            }

            $.getJSON(getServiceUrl() + "getLables?language=" + curLanguage, function (data) {
                $("#mapId").text(data.mapLbl);
                $("#streetViewId").text(data.streetViewLbl);
                $("#enterGuideNameId").attr("placeholder", data.enterGuideNameLbl);
                $("#searchId").html("<span class=\"glyphicon glyphicon-search\"></span> " + data.searchLbl);
                $("#locateId").html("<span class=\"glyphicon glyphicon-map-marker\"></span> " + data.locateLbl);
                $("#paidId").html("<span class=\"glyphicon glyphicon-credit-card\"></span> " + data.paidLbl);
                $("#directionId").html("<span class=\"glyphicon glyphicon-road\"></span> " + data.directionsLbl);

                $("#buyTheGuideId").html("<span class=\"glyphicon glyphicon-credit-card\"></span> " + data.buyTheGuideLbl);
                $("#selectTheGuideId").text(data.selectTheGuideLbl);
            });
        }

        function initAutoComplete(data) {
            if (autocomplete.length == 0) {
                for (var i = 0; i < data.length; i++) {
                    autocomplete.push(data[i].guideName + ", " + data[i].country);
                }
            }
        }

        function getAndFillPublishedGuides(language) {
            $(document).ready(function () {
                $.getJSON(getServiceUrl() + "getPublishGuides?language=" + language, function (data) {
                    fillGuidez(data);
                    currentIndex = 0;
                    initAutoComplete(data);
                    setActiveMarker($("#guideSelectionId").find("option:selected").index(), true);
                });
            });
        }

        function addToList(array, dataItem) {
            var found = true;
            for (var j = 0; j < array.length; j++) {
                var regex = new RegExp(array[j], "i");
                if (dataItem.search(regex) < 0) {
                    found = false;
                }
            }
            if (found) {
                return "<li><a href=\"#\">" + dataItem + "</a></li>";
            }
            return "";
        }

        function showAutoComplete(pattern, data, dropdownMenuId) {
            if (pattern.length < 1) {
                $(dropdownMenuId).hide();
            }
            else {
                var array = pattern.split(" ");
                var resultList = '';
                var maxSizeIndex = 0;
                for (var i = 0; i < data.length; i++) {
                    var add = addToList(array, data[i]);
                    if (add.length > 0) {
                        resultList += add;
                        maxSizeIndex++;
                    }
                    if (maxSizeIndex >= 4) {
                        break;
                    }
                }
                if (resultList.length > 0) {
                    $(dropdownMenuId).html(resultList).val(0);
                    $(dropdownMenuId).show();
                }
                else {
                    $(dropdownMenuId).hide();
                }
            }
        }

        function searchAndFillPublishedGuides(pattern, language) {
            $(document).ready(function () {
                $.getJSON(getServiceUrl() + "searchPublishGuides?language=" + language + "&pattern=" + pattern, function
                        (data) {
                    fillGuidez(data);
                    $("#paidId").attr("class", paidBtnClass);
                    currentIndex = 0;
                    setActiveMarker($("#guideSelectionId").find("option:selected").index(), true);
                });
            });
        }

        function getAndFillPaidGuides() {
            $(document).ready(function () {
                $.getJSON(getServiceUrl() + "getPaidGuides?language=" + curLanguage, function (data) {
                    fillGuidez(data);
                    currentIndex = 0;
                    setActiveMarker($("#guideSelectionId").find("option:selected").index(), true);
                });
            });
        }

        function getMarkerIndex(latLng) {
            var len = gmarkers.length;
            for (var i = 0; i < len; i++) {
                var gLatLng = gmarkers[i].getPosition();
                if ((gLatLng.lat() == latLng.lat()) && (gLatLng.lng() == latLng.lng())) {
                    return i;
                }
            }
            return 0;
        }

        function fillGuidez(data) {
            removeMarkers();
            gmarkers = [];
            var len = data.length;
            var resultList = '';
            for (var i = 0; i < len; i++) {
                resultList += "<option value=" + i + ">" + data[i].guideName + ", " + data[i].country + "</option>";
                var center = new google.maps.LatLng(Number(data[i].latitude), Number(data[i].longitude));
                var iconLang = "img/guideEn.png";
                if (data[i].language == "ru") {
                    iconLang = "img/guideRu.png";
                }
                (function () {
                    var marker = new google.maps.Marker({
                        position: center,
                        icon: iconLang
                    });
                    marker.setMap(map);
                    gmarkers.push(marker);
                    google.maps.event.addListener(marker, 'click', function () {
                        var selected = getMarkerIndex(marker.getPosition());
                        $('#guideSelectionId').val(selected);
                        setActiveMarker(selected, true);
                    });
                })();
            }
            $("#guideSelectionId").html(resultList).val(0);
            guidez = data;
            enableDisablePlayer(guidez.length > 0);
        }

        /*
         Set active marker on the map by guide index
         */
        function setActiveMarker(newIndex, center) {
            if (gmarkers.length > 0) {
                var paid = guidez[newIndex].buyLink;
                if (paid.length == 0 || paid == "FREE") {
                    $("#buyTheGuideId").attr("class", "btn btn-success btn-block btn-lg disabled");
                    $("#paidLinkId").attr("href", 'javascript:void(0);');
                }
                else {
                    $("#buyTheGuideId").attr("class", "btn btn-success btn-block btn-lg");
                    $("#paidLinkId").attr("href", paid);
                }
                var iconLang = "img/guideEn.png";
                if (guidez[currentIndex].language == "ru") {
                    iconLang = "img/guideRu.png";
                }
                gmarkers[currentIndex].setIcon(iconLang);

                currentIndex = newIndex;
                var iconActiveLang = "img/activeGuideEn.png";
                if (guidez[currentIndex].language == "ru") {
                    iconActiveLang = "img/activeGuideRu.png";
                }
                gmarkers[currentIndex].setIcon(iconActiveLang);

                if (center == true) {
                    map.panTo(gmarkers[currentIndex].getPosition());
                }
                loadAudioFile();
                drawStreetView();
            }
        }

        function loadAudioFile() {
            if (playerStopped) {
                var audio = $("#player")[0];
                if (guidez.length) {
                    var audioOld = $("#playSrc").attr("src");
                    $.getJSON(getServiceUrl() + "getFileToPlay?name=" + guidez[currentIndex].audioFile, function (data) {
                        $("#playSrc").attr("src", getAudioUrl() + data.fileName);
                        audio.load();
                    });
                }
            }
        }

        function getPanoramio(latLng) {
            var latSW = latLng.lat() - const100meter;
            var lngSW = latLng.lng() - const100meter;
            var latNE = latLng.lat() + const100meter;
            var lngNE = latLng.lng() + const100meter;

            var myRequest = {
                'rect': {'sw': {'lat': latSW, 'lng': lngSW}, 'ne': {'lat': latNE, 'lng': lngNE}}
            };

            var photo_ex_widget = new panoramio.PhotoWidget(
                    'pano', myRequest, {
                        'width': 1110,
                        'height': 580,
                        'disableDefaultEvents': [panoramio.events.EventType.PHOTO_CLICKED]
                    });
            photo_ex_widget.setPosition(0);
        }

        function deg2rad(deg) {
            return (deg * Math.PI / 180.0);
        }

        function rad2deg(rad) {
            return (rad * 180.0 / Math.PI);
        }

        function drawStreetView() {
            var streetViewService = new google.maps.StreetViewService();
            var STREETVIEW_MAX_DISTANCE = 100;
            var latLng = gmarkers[currentIndex].getPosition();
            var marker = {lat: latLng.lat(), lng: latLng.lng()};

            streetViewService.getPanorama({
                location: marker,
                radius: STREETVIEW_MAX_DISTANCE
            }, function (streetViewPanoramaData, status) {
                if (status === google.maps.StreetViewStatus.OK) {
                    var panorama = new google.maps.StreetViewPanorama(document.getElementById("pano"));
                    panorama.setPano(streetViewPanoramaData.location.pano);
                    panorama.setPov({
                        heading: 270,
                        pitch: 0
                    });
                    panorama.setVisible(true);
                    console.log("OK - pos = " + gmarkers[currentIndex].getPosition())
                } else {
                    getPanoramio(latLng);
                    console.log("ERROR - pos = " + gmarkers[currentIndex].getPosition())
                }
            });
        }

        function countDistance(lat1, lng1, lat2, lng2, unit) {
            var theta = lng1 - lng2;
            var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == 'K') {
                dist = dist * 1.609344;
            }
            else if (unit == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        function isCurrentLanguage(shortLanguage) {
            return (shortLanguage == "ru" && curLanguage == "Russian") || (shortLanguage == "en" && curLanguage == "English");
        }

        function locateToNearestGuide() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    var curPos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    var index;
                    var distance = Number.MAX_VALUE;
                    for (var i = 0; i < gmarkers.length; i++) {
                        if (isCurrentLanguage(guidez[i].language)) {
                            var mLat = gmarkers[i].getPosition().lat();
                            var mLng = gmarkers[i].getPosition().lng();
                            var d = countDistance(mLat, mLng, curPos.lat(), curPos.lng(), 'K');
                            if (d < distance) {
                                index = i;
                                distance = d;
                            }
                        }
                    }
                    if (index != undefined) {
                        $("#guideSelectionId").val(index);
                        setActiveMarker(index, true);
                    }
                }, function () {
                    handleNoGeolocation(true);
                });
            } else {
                handleNoGeolocation(false);
            }
        }

        function handleNoGeolocation(errorFlag) {
            if (errorFlag) {
                alert('The Geolocation service failed.');
            } else {
                alert('Your browser doesn\'t support geolocation.');
            }
        }

        function parseStartParams() {
            var result = [];
            var url = window.location.toString();
            url = url.split("?");
            if (url[1] != undefined) {
                url = url[1].split("&");
                if (url[1] != undefined && url[0] != undefined) {
                    var langKeyValue = url[0].split("=");
                    var guideKeyValue = url[1].split("=");
                    if (langKeyValue[0] == "lang" && guideKeyValue[0] == "guide") {
                        result = [guideKeyValue[1], langKeyValue[1], url[0]];
                    }
                }
            }
            return result;
        }

        $(document).ready(function () {
            var audioElement = $("#player")[0];
            $("#btnEnId").click(function () {
                $("#enterGuideNameId").val("");
                $("#btnEnId").addClass("active");
                $("#btnRuId").removeClass("active");
                $("#paidId").attr("class", paidBtnClass);
                fillData("English");
            });
            $("#btnRuId").click(function () {
                $("#enterGuideNameId").val("");
                $("#btnRuId").addClass("active");
                $("#btnEnId").removeClass("active");
                $("#paidId").attr("class", paidBtnClass);
                fillData("Russian");
            });
            $("#guideSelectionId").on('change', function () {
                var index = $("#guideSelectionId").find("option:selected").index();
                setActiveMarker(index, true);
            });

            $("#searchId").click(function () {
                searchAndFillPublishedGuides($("#enterGuideNameId").val(), curLanguage);
            });
            $("#locateId").click(function () {
                locateToNearestGuide();
            });
            $("#paidId").click(function () {
                if ($("#paidId").attr("class") == paidBtnClass) {
                    $("#paidId").attr("class", paidBtnClassActive);
                    getAndFillPaidGuides();
                }
                else {
                    $("#paidId").attr("class", paidBtnClass);
                    fillData(curLanguage);
                }
            });
            $("#enterGuideNameId")
                    .keypress(function (e) {
                        if (e.which == 13) {
                            searchAndFillPublishedGuides($("#enterGuideNameId").val(), curLanguage);
                        }
                    })
                    .keyup(function () {
                        showAutoComplete($(this).val().trim(), autocomplete, "#dropdownMenuId");
                    });
            $("#liMapId").click(function (e) {
                e.preventDefault();
                e.stopImmediatePropagation();
                $("#liMapId").addClass("active");
                $("#liStreetViewId").removeClass("active");
                $("#googleMap").show();
                $("#pano").hide();
                google.maps.event.trigger(map, 'resize');
                map.panTo(gmarkers[currentIndex].getPosition());
            });
            $("#liStreetViewId").click(function (e) {
                e.preventDefault();
                e.stopImmediatePropagation();
                $("#liStreetViewId").addClass("active");
                $("#liMapId").removeClass("active");
                $("#pano").show();
                $("#googleMap").hide();
                drawStreetView();
            });
            $("#emailId").click(function () {
                var email = 'guidezupads@gmail.com';
                var subject = 'Question about Guidezup';
                window.location = 'mailto:' + email + '?subject=' + subject;
            });
            $("#fbId").click(function () {
                window.open('http://www.facebook.com/Guidezup', '_blank');
            });
            $("#twitterId").click(function () {
                window.open('http://www.twitter.com/Guidezup', '_blank');
            });
            $("#googleId").click(function () {
                window.open('https://plus.google.com/u/1/112078580883680850683/posts', '_blank');
            });
            $("#directionId").click(function () {
                $("#directionId").removeClass("active");
                window.open('https://maps.google.com?saddr=Current+Location&daddr=' +
                        guidez[currentIndex].latitude + ',' + guidez[currentIndex].longitude, '_blank');
            });
            $(".button-pause").on("click", function () {
                $(".button-pause").blur().addClass("active");
                $(".button-play").removeClass("active");
                audioElement.pause();
            });
            $(".button-play").on("click", function () {
                playerStopped = false;
                $(".button-play").blur().addClass("active");
                $(".button-pause").removeClass("active");
                audioElement.play();
            });
            $(".button-stop").on("click", function () {
                playerStopped = true;
                $(".button-stop").blur();
                $(".button-play").removeClass("active");
                $(".button-pause").removeClass("active");
                audioElement.pause();
                audioElement.currentTime = 0;
                loadAudioFile();

            });
            $(".button-skip-forward").on("click", function () {
                $(".button-skip-forward").blur();
                if (!playerStopped) {
                    audioElement.currentTime += 5;
                }
            });

            $(".button-skip-backward").on("click", function () {
                $(".button-skip-backward").blur();
                if (!playerStopped) {
                    audioElement.currentTime -= 5;
                }
            });
            $("#dropdownMenuId").on('click', 'li', function () {
                $("#enterGuideNameId").val($(this).children().text());
                $("#dropdownMenuId").hide();
            });
            $('body').click(function () {
                $("#dropdownMenuId").hide();
            });

        });

        function enableDisablePlayer(enable) {
            if (enable) {
                $(".button-stop").removeClass("disabled");
                $(".button-play").removeClass("disabled");
                $(".button-pause").removeClass("disabled");
                $(".button-skip-backward").removeClass("disabled");
                $(".button-skip-forward").removeClass("disabled");
            }
            else {
                $(".button-stop").addClass("disabled");
                $(".button-play").addClass("disabled");
                $(".button-pause").addClass("disabled");
                $(".button-skip-backward").addClass("disabled");
                $(".button-skip-forward").addClass("disabled");
            }
        }

    </script>

</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading" style="background-color: #AAD7EB">
            <div class="row">
                <div class="col-lg-8">
                    <img src="img/logo1.gif">
                </div>
                <div class="col-lg-4" style="text-align: right">
                    <div class="panel-body">
                        <button type="button" id="btnEnId" class="btn btn-default btn-md">English</button>
                        <button type="button" id="btnRuId" class="btn btn-default btn-md">Русский</button>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <div class="input-group dropdown">
                    <input type="text" class="form-control input-lg" id="enterGuideNameId"
                           placeholder="Enter guide name...">
                    <ul class="dropdown-menu" id="dropdownMenuId"></ul>
                    <span class="input-group-btn">
                        <button type="button" id="searchId" class="btn btn-primary btn-lg">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
                </div>
            </div>

            <button type="button" id="locateId" class="btn btn-primary btn-md"><span
                    class="glyphicon glyphicon-map-marker"></span> Locate
            </button>
            <button type="button" id="paidId" class="btn btn-primary btn-md"><span
                    class="glyphicon glyphicon-credit-card"></span> The best
            </button>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <form role="form">
                <div class="form-group">
                    <label id="selectTheGuideId" for="guideSelectionId">Select the guide:</label>
                    <select name="selValue" class="form-control input-lg" data-style="btn-default btn-lg"
                            id="guideSelectionId"></select>
                </div>
            </form>
            <a href='javascript:void(0);' target="_blank" id="paidLinkId"
               class="sellfy-buy-button in-new-page sellfy-buy-button-custom"
               style="width: 100%;text-decoration:none;">
                <button type="button" id="buyTheGuideId" class="btn btn-success btn-block btn-lg disabled">
                    <span class="glyphicon glyphicon-credit-card"></span> Buy the guide
                </button>
            </a>
        </div>
        <div class="panel-heading" style="text-align: center">
            <audio id="player" class="btn-block">
                <source id="playSrc" src="audio/sample.mp3" type="audio/mpeg"/>
                Your browser does not support the audio element.
            </audio>
            <div class="buttons">
                <button type="button" class="btn btn-default btn-lg button-skip-backward">
                    <span class="glyphicon glyphicon-fast-backward"></span>
                </button>
                <button type="button" class="btn btn-default btn-lg button-pause">
                    <span class="glyphicon glyphicon-pause"></span>
                </button>
                <button type="button" class="btn btn-default btn-lg button-stop">
                    <span class="glyphicon glyphicon-stop"></span>
                </button>
                <button type="button" class="btn btn-default btn-lg button-play">
                    <span class="glyphicon glyphicon-play"></span>
                </button>
                <button type="button" class="btn btn-default btn-lg button-skip-forward">
                    <span class="glyphicon glyphicon-fast-forward"></span>
                </button>

            </div>

        </div>

        <div class="panel-body">
            <ul class="nav nav-tabs">
                <li class="active" id="liMapId"><a id="mapId" href="#">Map</a></li>
                <li id="liStreetViewId"><a id="streetViewId" href="#">Street View</a></li>
            </ul>
            <div id="googleMap" style="height:37em;"></div>
            <div id="pano" style="height:37em;"></div>
            <div id="direction-widget">
                <button type="button" id="directionId" class="btn btn-primary btn-md">
                    <span class="glyphicon glyphicon-road"></span> Directions
                </button>
            </div>

        </div>
        <div class="panel-footer" style="text-align: center">
            <a id="fbId" class="btn btn-social-icon" style="padding:0;color: #163758">
                <i class="fa fa-facebook-square fa-3x"></i>
            </a>
            <a id="googleId" class="btn btn-social-icon" style="padding:0;color: #9f191f">
                <i class="fa fa-google-plus-square fa-3x"></i>
            </a>
            <a id="twitterId" class="btn btn-social-icon" style="padding:0;color: #1087dd">
                <i class="fa fa-twitter-square fa-3x"></i>
            </a>
            <a class="btn btn-social-icon" style="padding:0;color: #1087dd">
                <i class="fa fa-em fa-3x"></i>
            </a>
            <button id="emailId" type="button" class="btn btn-primary">
                <span class="glyphicon glyphicon-envelope"></span> E-mail
            </button>

        </div>
    </div>

</div>

</body>
<script type="text/javascript" src="https://sellfy.com/js/api_buttons.js"></script>
<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
        a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-67045406-1', 'auto');
    ga('send', 'pageview');
</script>

</html>
