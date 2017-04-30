$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover();
    $('#data-table input').attr('readonly','readonly');
    loadData();
    testHtml= $("#testData").html();
    });



function loadData()
{
  $('#data-table td').click( function() {
           setval($(this).find('input').attr('id').substring(0,($(this).find('input').attr('id')).length-3),$(this).find('input').attr('id'));
           $('#addValue').modal();
      });


      $('path').click( function() {
            setval($(this).attr('data-name')+"_lt",$(this).attr('data-name')+"_pp");
           $('#addValue').modal();
      });

  $('label').click( function() {
      value=$(this).attr("value");
      });

}




function deleteCase(caseId,link){
 $.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
         $("#"+caseId).remove();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            show('Something Went Wrong .. In Deleting Case');
        }
    });

}


function editTest(link,editLink)
{

        var testHtml= $("#testData").html();
$.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
        $("#testData").html($.parseHTML(json.testData));
        $("#testName").val(json.testName);
        $("#testInfo").val(json.testInfo);
        $("#saveTest").text("Edit And Calculate Test");
        $("#testForm").attr("action",editLink);
        $( "#s2" ).click();
        loadData();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something Went Wrong .. In Editing Test');
        }
    });
}

function updateHuman()
{
        $("#testData").html(testHtml);
        loadData();
}


function deleteTest(testId,link){
 $.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
         $('div[id=\"'+testId+'\"]').remove();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something Went Wrong .. In Deleting Test');
        }
    });

}



$("#testForm").submit(function(e){
        $("#testContent").attr("value",$("#testData").html());
});





$("#caseForm").submit(function(e){
    var postData = $(this).serializeArray();
    var formURL = $(this).attr("action");
    var userName =document.getElementById('userName').innerHTML;
    $.ajax(
    {
        url : "/users/"+userName+"/createCase",
        type: "POST",
        crossDomain: true,
        data : postData,
        success:function(json, textStatus, jqXHR)
        {
            getCases(userName);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something went wrong');
        }
    });
    e.preventDefault(); //STOP default action

  $("#newCase").modal("hide");
});


// create input modal
var manName="";
var tdName="";


    function setval(manName,tdName)
    {
        $("#modalText").text("please Enter "+manName+" Value :");
        this.manName=manName;
        this.tdName=tdName;
    }

function getValue()
{
    $("#addValue").modal("hide");
        index = $('#'+tdName).parent().parent().index();
        $('#data-table > tbody  > tr').each(function(i) {
        if(i>=index){
         if(tdName.includes("Right_lt")){
                     td=$(this).find("input:eq(0)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Left_lt")){
                     td=$(this).find("input:eq(1)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Right_pp")){
                     td=$(this).find("input:eq(2)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Left_pp")){
                  td=$(this).find("input:eq(3)");
                  man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-4);
         }
    if(value==0){
        $( "path[data-name='"+man+"']" ).attr("fill","rgb(249, 117, 117)");
        td.attr("value",value);
        td.css("background","rgba(255,73,73,.5)");
    }
    if(value==1){
        $( "path[data-name='"+man+"']" ).attr("fill","rgb(244, 242, 81)");
        td.css("background","rgba(230,255,98,.5)");
        td.attr("value",value);
        }
        if(value==2){
            $( "path[data-name='"+man+"']" ).attr("fill","rgb(113, 253, 136)");
            td.css("background","rgba(65,233,53,.5)");
            td.attr("value",value);
                    }
            if(value=="NT"){
                $( "path[data-name='"+man+"']" ).attr("fill","rgb(191, 191, 191)");
                td.css("background","rgba(97,97,97,.5)");
                td.attr("value",value);
                                       }

  if( $('#downValues').is(':checked') ==false)
        return false;

                                                }


});

}




function save()
{

var svgElements= $('.row').find('svg');

//replace all svgs with a temp canvas
svgElements.each(function () {
    var canvas, xml;

    canvas = document.createElement("canvas");
    canvas.className = "screenShotTempCanvas";
    //convert SVG into a XML string
    xml = (new XMLSerializer()).serializeToString(this);


canvas.width  = $(this).width();
canvas.height = $(this).height();
    //draw the SVG onto a canvas
    canvg(canvas, xml);
    $(canvas).insertAfter(this);
    //hide the SVG element
    this.className = "tempHide";
    $(this).hide();


 html2canvas($('.col-md-9'), {
           background :'#F8FBFB',
           onrendered: function (canvas) {
            var img = canvas.toDataURL("image/png");
                 var link = document.createElement("a");
                link.download = "test.png";
                link.href = img;
                link.click();

                var doc = new jsPDF("p", "mm", "a4");
                    var width = doc.internal.pageSize.width;
                    var height = doc.internal.pageSize.height;
                    doc.addImage(img, 'PNG',0,0,width, height);
                    doc.save('test');


var blob = dataURItoBlob(img);
var fd = new FormData();
fd.append("file", blob);

 $.ajax({
                type: "POST",
                url: "/showTest",
                data: fd,
                contentType: false,
                processData: false,
                cache: false,
                /*beforeSend: function(xhr, settings) {
                    xhr.setRequestHeader("Content-Type", "multipart/form-data;boundary=gc0p4Jq0M2Yt08jU534c0p");
                    settings.data = {name: "file", file: inputElement.files[0]};
                },*/
                success: function (result) {
                    if ( result.reseponseInfo == "SUCCESS" ) {
                        show('mbrook');
                    } else {

                    }
                },
                error: function (result) {
                    console.log(result.responseText);
                }
            });



            }

         });

  $(canvas).remove();
  $(this).show();

});

}



function dataURItoBlob(dataURI) {
    // convert base64/URLEncoded data component to raw binary data held in a string
    var byteString;
    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to a typed array
    var ia = new Uint8Array(byteString.length);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ia], {type:mimeString});
}



function show(message)
{

        $("#modal").remove();
        modal="<div class=\"modal fade\" id=\"modal\" role=\"dialog\">"+
            "        <div class= \"modal-dialog modal-md\">"+
            "            <div class=\"modal-content\">"+
            "                <div class=\"modal-header\">"+
            "                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>"+
            "                    <h4 class=\"modal-title\">Info</h4>"+
            "                </div>"+
            "                <div class=\"modal-body\">"+
            "                    <h5 class=\"text-danger\">"+message+
            "                    </h5><div class=\"modal-footer\">"+
            "                        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
            "                    </div>"+
            "                </div>"+
            "            </div>"+
            "        </div>"+
            "    </div>"
        $("body").append(modal);
        $("#modal").modal();
}


function sendTo(link)
{

 $.ajax(
    {
        url : link,
        type: "POST",
        crossDomain: true,
        beforeSend: function() {
            show("Sending...");
        },
        success:function(json, textStatus, jqXHR)
        {
            show("Email Has Been Sent Successfully");
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            show('Something went wrong at sending Email .. please try again later :)');
        }
    });
}