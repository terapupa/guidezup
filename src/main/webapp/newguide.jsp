<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" type="image/x-icon" href="img/favicon.ico"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css"
          href="silviomoreto-bootstrap-select-a8ed49e/dist/css/bootstrap-select.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="font-awesome-4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-social.css">
    <link rel="stylesheet" type="text/css" href="css/local.css"/>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="silviomoreto-bootstrap-select-a8ed49e/js/bootstrap-select.js"></script>

    <title>GuidezUp! Walk Watch Listen</title>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <META name="Description"
          content="Free multi-language travelling audio guides collection. audio, guide, путешествия, путеводитель,
        travel, podcast, mobile, journey, guidezup, popup">
    <META name="Keywords"
          content="audio, guide, путешествия, путеводитель, travel, podcast, mobile, journey, guidezup, popup, mp3
        download,free audio downloads">
    <meta name="robots" content="index, follow">
    <META name="author" content="GuidezUp">
</head>
<body>
<script>
    var baseUrl = "http://localhost:8080/";
    $(document).ready(function () {
        var url = window.location.toString();
        console.log("url = " + url);
        if (url.match('//guidezup.com') != undefined)
        {
            baseUrl = "http://guidezup.com/";
        }
        else if (url.match('//www.guidezup.com') != undefined)
        {
            baseUrl = "http://www.guidezup.com/";
        }
        console.log("baseUrl = " + baseUrl);
        $("#formId").attr("action", baseUrl + "rest/guideservices/addGuide");
    });

</script>

<div class="container">
    <h2>Add new guide</h2>

    <form id="formId" action="#" class="form-horizontal"
          role="form" method="post" enctype="multipart/form-data" target="_blank">
        <div class="form-group">
            <label class="col-sm-2 control-label">Guide name</label>

            <div class="col-sm-10">
                <input name="guideName" class="form-control" id="guideNameId" type="text" value="Enter guide name...">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Country</label>

            <div class="col-sm-5">
                <input name="country" class="form-control" id="countryId" type="text" value="Enter Country...">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Latitude</label>

            <div class="col-sm-5">
                <input name="latitude" type="text" class="form-control" id="latitudeId" type="text"
                       value="Enter Latitude...">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Longitude</label>

            <div class="col-sm-5">
                <input name="longitude" type="text" class="form-control" id="longitudeId" type="text"
                       value="Enter Longitude...">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Language</label>
            <label class="radio-inline">
                <input type="radio" name="optEnglish">English
            </label>
            <label class="radio-inline">
                <input type="radio" name="optRussian">Russian
            </label>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Audio file</label>

            <div class="col-sm-5">
                <input name="audioFile" type="file" class="form-control" id="fileId">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Buy Link</label>

            <div class="col-sm-5">
                <input name="buyLink" type="url" class="form-control" id="buyLinkId">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Password</label>

            <div class="col-sm-5">
                <input name="password" type="password" class="form-control" id="passwordId" type="text"
                       value="Enter Password...">
            </div>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-default">Submit</button>
        </div>


    </form>
</div>
</body>
</html>