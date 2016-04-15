hello.init({
    google: '36542558745-1l96ap825s047nutvan7j1jb6l29eiva.apps.googleusercontent.com'
}, 
{
    redirect_uri: 'login.html',
    scope: 'email'
});
hello.on('auth.login', function(auth) {
    // Call user information, for the given network
    hello(auth.network).api('/me').then(function(r) {
        user=r;
        //  start spinning
        blockPage();
        getUserInfo().then(createAudioRecordModal).then(getMyPreviousWork).then(getOthersWork);
    });
});

//hello.logout() doesnt work for google plus!!
function logout(){
	hello.logout();
	window.location = "login.html";
}

function getUserInfo(){
    userJson = {};
    userJson.userId = user.id;
    userJson.firstName = user.first_name;
    userJson.lastName = user.last_name;
    userJson.displayName = user.displayName;
    userJson.emailId = user.email;
    return $.ajax({
        url: "rest/users", 
        method: "POST",
        contentType: "application/json",
        data : JSON.stringify(userJson),
        success: function(data){
            $("#userName").html(user.first_name+" "+user.last_name);  
            userId = data;
        }
    });
}

function createAudioRecordModal(){
    blob_upload_url="/rest/users/"+userId+"/audioClips/blob";
    return $.ajax({
        url: blob_upload_url, 
        method: "GET",
        contentType: "application/json",
        success: function(data){
            $("#audioRecordModalDetails").append('<form action="'+data+'"  method="POST" enctype="multipart/form-data"><input type="hidden" id="audioSubmit_userId" name="userId" value="'+userId+'"/><div><label> Title:</label>&nbsp; &nbsp; <input type="text" name="title"></div><br/><div class="wrapper"><label> Image:</label><input id="myImage" type="file" class="file" name="myImage" data-preview-file-type="text"/></div><br/><div class="wrapper"><label> Audio:</label><input type="file" class="file" id="myAudio" name="myAudio" data-preview-file-type="text"/></div><br/><button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> Save Audio</button>&nbsp; &nbsp; &nbsp; <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times"></i> Cancel</button></form>');
            $("#myImage").fileinput({'showUpload':false, 'showPreview':false});
            $("#myAudio").fileinput({'showUpload':false, 'showPreview':false});
        }
    });
}

function getMyPreviousWork(){
    basic_url="/rest/users/"+userId+"/audioClips";
        $.ajax({
                url: basic_url,
                type: "GET",
                crossDomain: true,
                dataType: "json",
                success: function (data) {
                    $.each(data, function(i, item) {
                        image_url = basic_url+"/image?blobkey="+item.imageId;
                        audio_url = basic_url+"/audio?blobkey="+item.audioId;
                        delete_button = '<button class="btn btn-danger" value="'+item.keyname+'" onclick="deleteAudioClip(this)"><i class="fa fa-times"></i>Delete Audio</button>';
                        $("#myPrevWork").before("<li> <div class=\"timeline-image\"><img class=\"timeline_audio_image img-circle img-responsive\" src=\""+image_url+"\" alt=\"Audio Clip\"></div><div class=\"timeline-panel\"><div class=\"timeline-heading\"><h4>"+item.title+"</h4><h4 class=\"subheading\">"+item.date+"</h4></div><div class=\"timeline-body\"><p class=\"text-muted\"><audio controls> <source src=\""+audio_url+"\" type=\"audio/mpeg\" /></audio></p><div><br/><br/>"+delete_button+"</div></div></div></li>");
                    });
                },
                error: function (xhr, status) {
                    alert("error");
                }
        });
}

function getOthersWork(){
    othersWorkModalId=0;
    basic_url="/rest/users/"+userId+"/audioClips";
    $.ajax({
            url: basic_url+"/others",
            type: "GET",
            crossDomain: true,
            dataType: "json",
            success: function (data) {
                $.each(data, function(i, item) {
                    currModalId=othersWorkModalId++;
                    image_url = basic_url+"/image?blobkey="+item.imageId;
                    audio_url = basic_url+"/audio?blobkey="+item.audioId;
                    $("#othersWork").append('<div class="col-md-4 col-sm-6 portfolio-item"><a href="#othersWorkModal'+currModalId+'" class="portfolio-link" data-toggle="modal" data-target="#othersWorkModal'+currModalId+'"><div class="portfolio-hover"><div class="portfolio-hover-content"><i class="fa fa-youtube-play fa-3x"></i></div></div><img src="'+image_url+'" class="audio_image" alt=""></a><div class="portfolio-caption"><h4>'+item.title+'</h4><p class="text-muted">'+item.owner.displayName+'</p></div>');
                    $("#audioRecordModal").after('<div class="audio-modal modal fade" id="othersWorkModal'+currModalId+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-body"><a class="audio-modal close-modal" data-dismiss="modal"><i class="fa fa-3x fa-times"></i></a><h2>'+item.title+'</h2><p class="item-intro text-muted">By '+item.owner.displayName+'</p><img class="portfolio-img img-responsive img-centered" src="'+image_url+'" alt=""><br/><audio controls><source src="'+audio_url+'" type="audio/mpeg" /></audio><br/><br/><ul class="list-inline"><li>Date: '+item.date+'</li></ul><br/></div><div class="clearfix"></div></div></div></div></div>');
                
               });
                unblockPage();  
            },
            error: function (xhr, status) {
                alert("error");
            }
    });
}

function deleteAudioClip(btn){
	$.ajax({
		url: "/rest/users/"+userId+"/audioClips/"+btn.value,
		type: "DELETE",
        crossDomain: true,
        success: function (data) {
        	alert("delete successful");
        	location.reload();
        },
        error: function (xhr, status) {
            alert("error");
        }
	});
}

function blockPage(){
	$.blockUI({ css: { 
        border: 'none', 
        padding: '15px', 
        backgroundColor: '#fff', 
        '-webkit-border-radius': '10px', 
        '-moz-border-radius': '10px', 
        opacity: .9, 
        color: '#ff8000' 
    } }); 
}

function unblockPage(){
	$.unblockUI();
}

